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


        move(1, 3);
        api.laserControl(true);
        api.takeTargetSnapshot(3);

        move(3,4);
        api.laserControl(true);
        api.takeTargetSnapshot(4);

        api.reportMissionCompletion("");
    }
}

