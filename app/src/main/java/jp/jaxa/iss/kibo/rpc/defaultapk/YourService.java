package jp.jaxa.iss.kibo.rpc.defaultapk;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;


public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        api.startMission();
       api.moveTo(new Point(10.71 - 0.1302, -7.7 - 0.0572 + 0.1111, 4.48), new Quaternion(0, 0.707f, 0, 0.707f), true);
       api.laserControl(true);
       api.takeTargetSnapshot(3);
    }

    @Override
    protected void runPlan2(){
        // write your plan 2 here
    }

    @Override
    protected void runPlan3(){
        // write your plan 3 here
    }

}

