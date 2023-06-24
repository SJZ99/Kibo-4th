package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//        message = QrCodeHelper.scan();
        Bitmap bitmap = api.getBitmapNavCam();
        try {

            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, -1.0f);

            bitmap = Bitmap.createBitmap(bitmap, 361, 293, 548, 418, matrix, true);
            api.saveBitmapImage(bitmap, "crop.jpg");
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixel = new int[width * height];
            bitmap.getPixels(pixel,0, width, 0, 0, width, height);

            RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width / 2,height / 2, pixel);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(rgbLuminanceSource));
            Map<DecodeHintType, Object> hint = new HashMap<>();
//            hint.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
//            hint.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            com.google.zxing.Result ans = new QRCodeReader().decode(binaryBitmap);

            message = ans.getText();

        } catch (Exception e) {
            message = "";
        }
        hasScanned = !message.equals("");
    }

    @Override
    protected void runPlan1(){
        api.startMission();

        qrCodeMission();

        int a = 5, b = 6, c = 1, d = 4;
        move(7, a);
        api.laserControl(true);
        api.takeTargetSnapshot(a);

//        move(a, b);
//        api.laserControl(true);
//        api.takeTargetSnapshot(b);
//
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
//        while(api.getTimeRemaining().get(1) > 90000) {
//            List<Integer> activatedTargets = api.getActiveTargets();
//
//            // sort, at most two point, greedy
//            List<WayPointsHelper.MyPoint> points = sort(activatedTargets);
//
//
//            for(WayPointsHelper.MyPoint target : points) {
//                if(api.getTimeRemaining().get(0) < 41000 || api.getTimeRemaining().get(1) < 85000) {
//                    break;
//                }
//                move(currPoint, target.id);
//                api.laserControl(true);
//                api.takeTargetSnapshot(target.id);
//            }
//        }

        api.notifyGoingToGoal();
        move(currPoint, 8);
        api.reportMissionCompletion(message);


//        Integer temp =0;
//
//        while(true){
//
//            List< Integer> list = api.getActiveTargets();
//
//            for (int i=0; i< list.size(); i++){
//                move(temp, list.get(i));
//                api.laserControl(true);
//                api.takeTargetSnapshot(list.get(i));
//                temp=list.get(i);
//            }
//
//
//
//            if (api.getTimeRemaining().get(1) < 60000){
//                break;
//            }
//
//        }
//        api.notifyGoingToGoal();
//        move(temp, 8);
//        api.reportMissionCompletion(" ");
    }


}

