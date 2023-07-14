package com.example.testalgorithm;

import java.util.ArrayList;
import java.util.List;
import com.example.testalgorithm.obj.*;
import com.example.testalgorithm.helper.*;

public class PathFinding {
    protected static final boolean isDebug = true;

    // maximum times of single movement
    static final int LOOP_MAX = 5;

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
        log("need time: " + bestRoute[bestRouteSize - 1].getTime());
    }

    private void _decideRoute(int index, List<Integer> targets) {
        // index must greater than 0
        if(index < 0) return;

        // used all targets, or has used point 8 (goal)
        if(wayPoints[index - 1].numberOfVisitedPoints() - 1 == targets.size() || wayPoints[index - 1].getId() == 8) {

            // no route has been saved
            if(bestRouteSize == 0 || bestRoute[0] == null) {
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
            long missionTime = getTimeRemaining().get(1);
            long phraseTime = getTimeRemaining().get(0);

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

    public static void log(String message) {
        System.out.println("I/LOG: " + message);
    }

    private void resetRoute() {
        bestRoute = new WayPoint[6];
        bestRouteSize = 0;
    }

    // current point, 0 for start point, [0, 8]
    protected static int currPoint = 1;

    private List<Long> getTimeRemaining() {
        ArrayList<Long> res = new ArrayList<>();
        res.add(120000l);
        res.add(130000l);
        return res;
    }

    public static void main(String[] args) {
        List<Integer> actTar = new ArrayList<>();
        actTar.add(2);
        actTar.add(4);
        actTar.add(7);
        actTar.add(8);

        PathFinding pf = new PathFinding();

        log("Start");
        pf.decideRoute(actTar);

        log("----------result-----------");
        for(WayPoint wp : pf.bestRoute) {
            if(wp != null) {
                log(wp.getId() + "");
            }
        }
        log("End");
        log("time: " + pf.bestRoute[pf.bestRouteSize - 1].getTime());
    }
}
