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

        move(0, 1);
        api.laserControl(true);
        api.takeTargetSnapshot(1);
<<<<<<< HEAD
        move(1, 3);
=======

        //move(1, 2);
>>>>>>> cc9b692ba0f7051255ffb4b1b59cda2059b12f01
        //move(1, 3);
        //move(1, 4);
        move(1, 5);
        //move(1, 6);

        api.laserControl(true);
<<<<<<< HEAD
        api.takeTargetSnapshot(3);
        move(3,4);
        api.laserControl(true);
        api.takeTargetSnapshot(4);
=======
        api.takeTargetSnapshot(5);
>>>>>>> cc9b692ba0f7051255ffb4b1b59cda2059b12f01
        api.reportMissionCompletion("");
    }
}

