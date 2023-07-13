package jp.jaxa.iss.kibo.rpc.taiwan.helper;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;

import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class QrCodeHelper {
    private static String scanCore(Bitmap bitmap, Map<DecodeHintType, Object> hint) {
        try {
            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, -1.0f);

            bitmap = Bitmap.createBitmap(bitmap, 361, 293, 548, 418, matrix, true);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixel = new int[width * height];
            bitmap.getPixels(pixel,0, width, 0, 0, width, height);

            RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width, height, pixel);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));

            com.google.zxing.Result ans = new QRCodeReader().decode(binaryBitmap, hint);

            if(ans == null || ans.getText() == null) {
                return "";
            }
            return ans.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public static String scan(Bitmap bitmap) {
        Map<DecodeHintType, Object> hint = new HashMap<>();
        hint.put(DecodeHintType.CHARACTER_SET, "UTF-8");

        return scanCore(bitmap, hint);
    }

    public static String deepScan(Bitmap bitmap) {
        Map<DecodeHintType, Object> hint = new HashMap<>();
        hint.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hint.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        return scanCore(bitmap, hint);
    }
}
