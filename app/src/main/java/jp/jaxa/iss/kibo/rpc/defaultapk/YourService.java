package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;


public class YourService extends KiboRpcService {
    static final boolean isDebug = true;

    /*************** move ***************/
    private void move(int from, int to) {
        // from point number map to index
        --from; --to;

        // same point
        if(from == to) return;

        // reverse or not
        boolean isReversed = false;
        Quaternion rotation = WayPointsHelper.getTargetRotation(to);
        if(from > to) {
            from ^= to ^= from;
            isReversed = true;
            rotation = WayPointsHelper.getTargetRotation(to);
        }

        ArrayList<Point> points = WayPointsHelper.getWayPoint(from, to);
        if(isReversed) {
            for(int i = points.size() - 1; i >= 0; --i) {
                api.moveTo(points.get(i), rotation, isDebug);
            }
        } else {
            for(int i = 0; i < points.size(); ++i) {
                api.moveTo(points.get(i), rotation, isDebug);
            }
        }
    }
    /*************** move end ***************/

    @Override
    protected void runPlan1(){
        api.startMission();
        start2p1();
        api.laserControl(true);
        api.takeTargetSnapshot(1);
        move(1, 2);
        //move(1, 3);
        //move(1, 4);
        //move(1, 5);
        api.laserControl(true);
        api.takeTargetSnapshot(2);
        api.reportMissionCompletion("");
    }

    private void start2p1() {
        api.moveTo(new Point(10.6, -9.9, 4.9), new Quaternion(0, 0, -0.707f, 0.707f), isDebug);
        api.moveTo(new Point(11.27 - 0.06, -9.92 - 0.05, 5.29 + 0.185), new Quaternion(0, 0, -0.707f, 0.707f), isDebug);
    }
}

