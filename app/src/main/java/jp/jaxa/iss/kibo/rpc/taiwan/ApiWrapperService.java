package jp.jaxa.iss.kibo.rpc.taiwan;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;
import jp.jaxa.iss.kibo.rpc.taiwan.helper.PathLengthHelper;
import jp.jaxa.iss.kibo.rpc.taiwan.helper.WayPointsHelper;
import jp.jaxa.iss.kibo.rpc.taiwan.obj.WayPoint;

/**
 * Created by Jian-Zhe, Su on 7.11.2023.
 * Consist of moveTo wrapper, move function, string transformation
 */

public class ApiWrapperService extends KiboRpcService {
    // api.moveTo message
    protected static final boolean isDebug = true;

    // maximum times of single movement
    static final int LOOP_MAX = 5;

    // current point, 0 for start point, [0, 8]
    protected static int currPoint = 0;

    // qr code message
    protected static String message = "";

    protected static WayPoint[] wayPoints = new WayPoint[6];
    protected static WayPoint[] bestRoute = new WayPoint[6];
    protected static int bestRouteSize = 0;

    protected final void decideRoute(List<Integer> targets) {
        resetRoute();

        wayPoints[0] = new WayPoint(currPoint, 0, 0, (short) 0);

        _decideRoute(1, targets);

        String res = "";
        for(int i = 1; i < bestRouteSize; ++i) {
            res += bestRoute[i].getId() + " ";
        }
        log("Decision: " + res);
        if(bestRouteSize > 0) log("need time: " + bestRoute[bestRouteSize - 1].getTime());
        log("phrase: " + api.getTimeRemaining().get(0));
        log("mission: " + api.getTimeRemaining().get(1));
    }

    private void _decideRoute(int index, List<Integer> targets) {
        // index must greater than 0
        if(index < 0) return;

        // used all targets, or has used point 8 (goal)
        if(wayPoints[index - 1].numberOfVisitedPoints() - 1 == targets.size() || wayPoints[index - 1].getId() == 8) {

            // no route has been saved
            if(bestRouteSize <= 0 || bestRoute[0] == null) {
                // copy to bestRoute
                resetRoute();
                for(int i = 0; i < index; ++i) {
                    bestRoute[i] = wayPoints[i];
                }
                bestRouteSize = index;

            } else { // compare to saved route

                WayPoint best = bestRoute[bestRouteSize - 1], curr = wayPoints[index - 1];
                if(best.compareTo(curr) < 0) {
                    // copy to bestRoute
                    resetRoute();
                    for(int i = 0; i < index; ++i) {
                        if(wayPoints[i] != null) {
                            log("id: " + wayPoints[i].getId());
                        }
                        bestRoute[i] = wayPoints[i];
                    }
                    bestRouteSize = index;
                }
            }
            return;
        }

        // every targets can be put into route or not
        for(int i : targets) {

            // if this path has visited this target, skip it
            if(wayPoints[index - 1].isVisited(i)) {
                continue;
            }

            float pathTime = wayPoints[index - 1].getTime() + PathLengthHelper.getTime(wayPoints[index - 1].getId(), i);
            long missionTime = api.getTimeRemaining().get(1);
            long phraseTime = api.getTimeRemaining().get(0);

            // time check
            if(pathTime + 6000 > phraseTime || pathTime + 7500 > missionTime) {
                log("Time check failed: id=" + i);
                continue;
            }

            // add way point to route
            wayPoints[index] = new WayPoint(i, pathTime, wayPoints[index - 1].getPoints(), wayPoints[index - 1].getVisited());

            // after put a way point at current index, try to put into next index
            _decideRoute(index + 1, targets);

            // or, construct a route without this target (but qr code and goal can't be eliminated
            if(i != 7 && i != 8) {
                wayPoints[index - 1].setVisited(i);
                _decideRoute(index, targets);
                wayPoints[index - 1].setUnvisited(i);
            }
        }
    }

    private void resetRoute() {
        bestRoute = new WayPoint[6];
        bestRouteSize = 0;
    }

    public void log(String message) {
        Log.i("Mainnn", message);
    }

    /**
         *  Move five times at most
         * @param p point
         * @param q quaternion
         * @return success or not
         */
    protected boolean moveTo(Point p, Quaternion q) {
        // if mission time less than 10 sec, perform less times.
        long missionTime = api.getTimeRemaining().get(1);
        int loopCounter = 0;
        Result result;
        do {
            result = api.moveTo(p, q, isDebug);
            ++loopCounter;
        } while(result != null && !result.hasSucceeded() && loopCounter < LOOP_MAX && missionTime > 10000);
        return result != null && result.hasSucceeded();
    }

    /**
         *  Move from point to point
         * @param from current point
         * @param to target point
         */
    protected void move(int from, int to) {
        // same point
        if(from == to) return;

        // reverse or not
        boolean isReversed = false;
        Quaternion rotation = WayPointsHelper.getTargetRotation(to);
        if(from > to) {
            int temp = from;
            from = to;
            to = temp;
            isReversed = true;
        }

        ArrayList<Point> points = WayPointsHelper.getWayPoint(from, to);
        boolean isSuccess = false;
        if(isReversed) {
            for(int i = points.size() - 2; i >= 0; --i) {
                isSuccess = moveTo(points.get(i), rotation);
            }
        } else {
            for(int i = 1; i < points.size(); ++i) {
                isSuccess = moveTo(points.get(i), rotation);
            }
        }

        // check movement result
        if(isSuccess) {
            currPoint = isReversed ? from : to;

            // qr code and goal
            if(currPoint != 7 && currPoint != 8) {
                api.laserControl(true);
                api.takeTargetSnapshot(currPoint);
                api.laserControl(false);
            }
        }
    }

    protected void processString() {
        if(message.equals("JEM")) {
            message = "STAY_AT_JEM";
        } else if(message.equals("COLUMBUS")) {
            message ="GO_TO_COLUMBUS";
        } else if(message.equals("RACK1")) {
            message = "CHECK_RACK_1";
        } else if(message.equals("ASTROBEE")) {
            message = "I_AM_HERE";
        } else if(message.equals("INTBALL")) {
            message = "LOOKING_FORWARD_TO_SEE_YOU";
        } else if(message.equals("BLANK")){
            message = "NO_PROBLEM";
        }
    }
}
