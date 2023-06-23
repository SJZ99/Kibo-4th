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

    private void decide(List<Point> points) {
        Point p = WayPointsHelper.getPoint(currPoint);
        Collections.sort(points, new WayPointsHelper.PointComparator(p));
    }

    @Override
    protected void runPlan1(){
        api.startMission();


        int a = 4, b = 8, c = 5, d=4;
        move(0, a);
        api.laserControl(true);
        api.takeTargetSnapshot(a);


        move(a, b);
//        api.saveMatImage(api.getMatNavCam(), "qr.jpg");
        api.laserControl(true);
        api.takeTargetSnapshot(b);

//        move(b, c);
//        api.laserControl(true);
//        api.takeTargetSnapshot(c);
//
//        move(c, d);
//        api.laserControl(true);
//        api.takeTargetSnapshot(d);

        api.reportMissionCompletion("");
    }
}

