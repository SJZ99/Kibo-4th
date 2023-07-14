package jp.jaxa.iss.kibo.rpc.taiwan;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;

import java.util.List;

import jp.jaxa.iss.kibo.rpc.taiwan.helper.PathLengthHelper;
import jp.jaxa.iss.kibo.rpc.taiwan.helper.QrCodeHelper;
import jp.jaxa.iss.kibo.rpc.taiwan.helper.WayPointsHelper;

public class YourService extends ApiWrapperService {

    private boolean isQrCodeFinished() {
        return !message.equals("");
    }

    private void qrCodeMission() {

        Bitmap bitmap = api.getBitmapNavCam();
        message = QrCodeHelper.scan(bitmap);

        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, -1.0f);
        api.saveBitmapImage(Bitmap.createBitmap(bitmap, 361, 293, 548, 418, matrix, true), "qrcode.jpg");

        // if failed, try again with consuming more time
        if(message.equals("")) {
            moveTo(WayPointsHelper.getPoint(7), WayPointsHelper.getTargetRotation(7));
            message = QrCodeHelper.deepScan(bitmap);
        }

        log("Qr code: " + message);
    }

    private boolean deactivation(List<Integer> activatedTargets) {

        // if find point 6, add qr code point
        for(int i = 0; i < activatedTargets.size(); ++i) {
            if(activatedTargets.get(i) == 6 && !isQrCodeFinished()) {
                activatedTargets.add(7);
//                break;
            }

            if(activatedTargets.get(i) == 5 || activatedTargets.get(i) == 6) {
                log("FUCK");
            }
        }

        // enumerate all possible route, save best route in bestRoute, which is a WayPoint array
        decideRoute(activatedTargets);

        if(bestRoute[bestRouteSize - 1] != null && bestRoute[bestRouteSize - 1].getId() != 8) {
            int lastTarget = bestRoute[bestRouteSize - 1].getId();
            float pathTime = bestRoute[bestRouteSize - 1].getTime();
            pathTime += isQrCodeFinished() ? PathLengthHelper.getTime(lastTarget, 8) : PathLengthHelper.getTime(lastTarget, 7) + PathLengthHelper.getTime(7, 8);
            if(pathTime + 7500 > api.getTimeRemaining().get(1)) {
                return false;
            }
        }

        // movement follow the result of enumeration, if id equals 7, then perform qr code mission
        for(int i = 1; i < bestRouteSize; ++i) {
            if(bestRoute[i] != null && bestRoute[i].getId() != 0) {

                log("phrase time: " + api.getTimeRemaining().get(0));
                log("mission time: " + api.getTimeRemaining().get(1));
                log("Target id: " + bestRoute[i].getId());
                // go to goal
                if(bestRoute[i].getId() == 8) {
                    goingToGoalMission();
                    break;
                }

                // normal movement
                move(currPoint, bestRoute[i].getId());

                // at point 7, scan qr code
                if(bestRoute[i].getId() == 7) {
                    qrCodeMission();
                }
            }
        }

        return true;
    }

    private void goingToGoalMission() {
        // start to go to goal
        api.notifyGoingToGoal();
        processString();

        // if there are running out of time, report immediately
        if(api.getTimeRemaining().get(1) < PathLengthHelper.getTime(currPoint, 8)) {
            api.reportMissionCompletion(message);
            return;
        }

        move(currPoint, 8);

        if(message.equals("")) {
            message = "I_AM_HERE";
        }
        api.reportMissionCompletion(message);
    }

    @Override
    protected void runPlan1(){
        api.startMission();

        // make decision
        List<Integer> activatedTargets;
        long missionTime = api.getTimeRemaining().get(1);

        // if qr code haven't been scanned, reserve 122 sec for qr code and going to goal
        // otherwise, keep deactivating until need to go to goal (18 sec for safety)
        while(
            PathLengthHelper.getTime(currPoint, 8) + 123000 <= missionTime
            || (isQrCodeFinished() && PathLengthHelper.getTime(currPoint, 8) + 18000 <= missionTime)
        ) {
            activatedTargets = api.getActiveTargets();
            missionTime = api.getTimeRemaining().get(1);

            // find best route and go to deactivate
            if(!deactivation(activatedTargets)) {
                break;
            }

        }

        log("end of while loop");

        if(!isQrCodeFinished()) {
            activatedTargets = api.getActiveTargets();

            // put qr code and goal into possible choice (qr code and goal must be selected)
            activatedTargets.add(7);
            activatedTargets.add(8);

            deactivation(activatedTargets);

            if(currPoint != 8) {
                goingToGoalMission();
            }
            log("end of not found qrcode end game");
        } else {
            goingToGoalMission();
            log("end of normal end game");
        }
    }
}

