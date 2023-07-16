package jp.jaxa.iss.kibo.rpc.taiwan.helper;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;

import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

public class QrCodeHelper {
    public static Mat processed;

    public static Mat undistortImg(Mat src, double[][] navIntrinsics) {
        Mat cam_Matrix = new Mat(3, 3, CvType.CV_32FC1);
        Mat distCoeff = new Mat(1, 5, CvType.CV_32FC1);
        Mat output = new Mat(src.size(), src.type());

        // cam_matrix & dat coefficient arr to mat
        for (int i = 0; i <= 8; i++) {
            int row , col ;
            if(i < 3){ row = 0; col = i; }
            else if(i < 6){ row = 1; col = i - 3; }
            else{ row = 2; col = i - 6; }
            cam_Matrix.put(row, col, navIntrinsics[0][i]);
        }

        for(int i = 0; i < 5; i++) {
            distCoeff.put(0, i, navIntrinsics[1][i]);
        }

        Imgproc.undistort(src, output, cam_Matrix, distCoeff);

        return output;
    }

    private static Bitmap preprocess(Mat mat, double[][] navIntrinsics) {
        // crop
        Rect rect = new Rect(0, 0, 1280, 960);
        Mat newMat1 = new Mat(mat, rect);
        Mat newMat2 = new Mat(newMat1.size(), newMat1.type());


        newMat2 = undistortImg(mat, navIntrinsics);

        Imgproc.threshold(newMat2, newMat1, 150, 255, Imgproc.THRESH_BINARY);

        // flip
//        Point center = new Point(newMat2.cols() / 2, newMat2.rows() / 2);
//        Imgproc.warpAffine(newMat2, newMat1, Imgproc.getRotationMatrix2D(center, 88, 1), newMat1.size());

        // convert to bitmap
        Bitmap bitmap = Bitmap.createBitmap(newMat2.cols(), newMat2.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(newMat1, bitmap);

        mat.release();
        newMat1.release();
        newMat2.release();

        return bitmap;
    }

    private static String scanCore(Mat mat, Map<DecodeHintType, Object> hint, double[][] navIntrinsics) {
        try {
            Rect rect = new Rect(0, 0, 1280, 960);
            Mat newMat1 = new Mat(mat, rect);
            Mat newMat2 = new Mat(newMat1.size(), newMat1.type());

            newMat2 = undistortImg(mat, navIntrinsics);
            Imgproc.threshold(newMat2, newMat1, 150, 255, Imgproc.THRESH_BINARY);

            // Detect QR codes
            QRCodeDetector detector = new QRCodeDetector();
            String ans = "";

            ans = detector.detectAndDecode(newMat1);

            return ans == null ? "" : ans;
        } catch (Exception e) {
            return "";
        }
    }

    public static String scan(Mat mat, double[][] navIntrinsics) {
        Map<DecodeHintType, Object> hint = new HashMap<>();
        hint.put(DecodeHintType.CHARACTER_SET, "UTF-8");

        return scanCore(mat, hint, navIntrinsics);
    }

    public static String deepScan(Mat mat, double[][] navIntrinsics) {
        Map<DecodeHintType, Object> hint = new HashMap<>();
        hint.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hint.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        return scanCore(mat, hint, navIntrinsics);
    }
}
