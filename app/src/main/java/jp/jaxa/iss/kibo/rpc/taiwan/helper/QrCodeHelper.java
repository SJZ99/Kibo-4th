package jp.jaxa.iss.kibo.rpc.taiwan.helper;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;

import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class QrCodeHelper {

    public static Mat undistortImg(Mat src, double[][] navIntrinsics, boolean black) {
        Mat cam_Matrix = new Mat(3, 3, CvType.CV_32FC1);
        Mat distCoeff = new Mat(1, 5, CvType.CV_32FC1);

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

        Mat output = new Mat(src.size(), src.type());

        if(black) {
            // undistort with keeping all pixel
            Mat optimal = Calib3d.getOptimalNewCameraMatrix(cam_Matrix, distCoeff, new Size(1280, 960), 1);
            Mat map1 = new Mat(), map2 = new Mat();
            Imgproc.initUndistortRectifyMap(cam_Matrix, distCoeff, new Mat(), optimal, new Size(1280, 960), CvType.CV_8UC1, map1, map2);

            Imgproc.remap(src, output, map1, map2, Imgproc.INTER_LINEAR);
        } else {
            Imgproc.undistort(src, output, cam_Matrix, distCoeff);
        }

        return output;
    }

    public static Mat preprocess(Mat raw) {
        // sharpness
        Mat minus = new Mat();
        Imgproc.GaussianBlur(raw, minus, new Size(5, 5), 0);
        Core.addWeighted(raw, 2.1, minus, -1.1, 0, raw);

        // threshold
        Imgproc.threshold(raw, raw, 100, 255, Imgproc.THRESH_BINARY);

        minus.release();
        return raw;
    }

    private static String scanCore(Bitmap bitmap, Map<DecodeHintType, Object> hint) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixel = new int[width * height];
            bitmap.getPixels(pixel,0, width, 0, 0, width, height);

            RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width, height, pixel);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));

            Result ans = new QRCodeReader().decode(binaryBitmap, hint);
            return ans == null ? "" : ans.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public static String deepScan(Bitmap bitmap) {
        Map<DecodeHintType, Object> hint = new HashMap<>();
        hint.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hint.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hint.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.QR_CODE));

        return scanCore(bitmap, hint);
    }

    public static String deepScan(Mat mat) {
        Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);

        return deepScan(bitmap);
    }
}
