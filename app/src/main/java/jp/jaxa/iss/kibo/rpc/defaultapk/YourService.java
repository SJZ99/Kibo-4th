package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;


public class YourService extends KiboRpcService {
    static final boolean isDebug = true;
    static final int LOOP_MAX = 5;
    static int currPoint = 0;
    static boolean hasScanned = false;
    static String message = "";

    /*************** move ***************/
    private boolean moveTo(Point p, Quaternion q) {
        int loopCounter = 0;
        Result result = null;
        do {
            result = api.moveTo(p, q, isDebug);
            ++loopCounter;
        } while(result != null && !result.hasSucceeded() && loopCounter < LOOP_MAX);
        return result != null && result.hasSucceeded();
    }

    private void move(int from, int to) {
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

        if(isSuccess) {
            currPoint = isReversed ? from : to;
        }
    }
    /*************** move end ***************/

    private List<WayPointsHelper.MyPoint> sort(List<Integer> targets) {
        Point p = api.getRobotKinematics().getPosition();

        // list for id and point
        List<WayPointsHelper.MyPoint> points = new ArrayList<>(targets.size());

        // map
        for(Integer i : targets) {
            points.add(new WayPointsHelper.MyPoint(i, WayPointsHelper.getPoint(i)));
        }

        // sort
        Collections.sort(points, new WayPointsHelper.PointComparator(p));
        return points;
    }

    private void qrCodeMission() {
        move(0, 7);
        message = QrCodeHelper.scan();
        hasScanned = !message.equals("");
    }

    @Override
    protected void runPlan1(){
        api.startMission();

//        int a = 3, b = 4, c = 8, d = 5;
//        move(0, a);
//        api.laserControl(true);
//        api.takeTargetSnapshot(a);
//        move(a, b);
////        api.saveMatImage(api.getMatNavCam(), "qr.jpg");
//        api.laserControl(true);
//        api.takeTargetSnapshot(b);

//        move(b, c);
//        api.laserControl(true);
//        api.takeTargetSnapshot(c);
//
//        move(c, d);
//        api.laserControl(true);
//        api.takeTargetSnapshot(d);

        // 0 -> 7, and scan
//        qrCodeMission();

        // 55 sec for moving to the goal
        while(api.getTimeRemaining().get(1) > 90000) {
            List<Integer> activatedTargets = api.getActiveTargets();

            // sort, at most two point, greedy
            List<WayPointsHelper.MyPoint> points = sort(activatedTargets);


            for(WayPointsHelper.MyPoint target : points) {
                if(api.getTimeRemaining().get(0) < 41000 || api.getTimeRemaining().get(1) < 85000) {
                    break;
                }
                move(currPoint, target.id);
                api.laserControl(true);
                api.takeTargetSnapshot(target.id);
            }
        }

        api.notifyGoingToGoal();
        move(currPoint, 8);
        api.reportMissionCompletion("");
    }
}

