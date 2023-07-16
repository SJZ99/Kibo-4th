package jp.jaxa.iss.kibo.rpc.taiwan;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.util.List;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.taiwan.helper.PathLengthHelper;
import jp.jaxa.iss.kibo.rpc.taiwan.helper.QrCodeHelper;

public class YourService extends ApiWrapperService {

    private boolean isQrCodeFinished() {
        return !message.equals("");
    }

    private void qrCodeMission() {

//        api.flashlightControlFront(0.5f);
        try {
            Thread.sleep(4800l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mat mat = api.getMatNavCam();

        long t = api.getTimeRemaining().get(1);
//        message = QrCodeHelper.scan(mat, api.getNavCamIntrinsics());

        Rect rect = new Rect(0, 0, 1280, 960);
        Mat newMat1 = new Mat(mat, rect);
        Mat newMat2 = new Mat(newMat1.size(), newMat1.type());

        api.saveMatImage(mat, "raw.jpg");
        newMat2 = QrCodeHelper.undistortImg(mat, api.getNavCamIntrinsics());
        api.saveMatImage(newMat2, "undistort.jpg");

        // adaptive threshold
        Mat gray = new Mat(newMat2, new Rect(640, 480, 640, 480));

        if (gray.type() != CvType.CV_8UC1) {
            gray.convertTo(gray, CvType.CV_8UC1);
        }

        Imgproc.adaptiveThreshold(gray, gray, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 9, 3);
        api.saveMatImage(gray, "threshold.jpg");

        

        log("Process Time: " + (t - api.getTimeRemaining().get(1)));

        // if failed, try again with consuming more time
//        if(message.equals("")) {
//            moveTo(WayPointsHelper.getPoint(7), WayPointsHelper.getTargetRotation(7));
//            mat = api.getMatNavCam();
//            message = QrCodeHelper.deepScan(mat, api.getNavCamIntrinsics());
//        }

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

        //new Point(10.66, -8.85, 4.48)
        // p2 rotate: new Quaternion(0.787f, 0.447f, -0.21f, 0.37f)
        move(currPoint, 2);


//        moveTo(new Point(11.369 , -8.55, 4.9), new Quaternion(0, 0.707f, 0, 0.707f));
        moveTo(new Point(11.369 - 0.6, -8.55 - 0.75, 4.9), new Quaternion(0, 0.707f, 0, 0.707f));


//        move(currPoint, 1);
//        moveTo(WayPointsHelper.getPoint(1), new Quaternion(0.706f, 0.581f, -0.258f, 0.313f));

//        move(0, 3);
//        move(3, 2);
        qrCodeMission();

        api.notifyGoingToGoal();
        processString();
        api.reportMissionCompletion(message);
    }
}

