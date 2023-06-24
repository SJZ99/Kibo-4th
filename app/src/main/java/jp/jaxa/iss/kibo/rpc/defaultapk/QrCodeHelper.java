package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class QrCodeHelper {

    public static String scan(Bitmap bitmap) {
        try {


            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, 1.0f);

            bitmap = Bitmap.createBitmap(bitmap, 361, 293, 548, 418, matrix, true);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixel = new int[width * height];
            bitmap.getPixels(pixel,0, width, 0, 0, width, height);

            RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width / 2,height / 2, pixel);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
            Map<DecodeHintType, Object> hint = new HashMap<>();
            hint.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
            hint.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            Result ans = new QRCodeReader().decode(binaryBitmap);

            return(ans.getText());

        } catch (Exception e) {
            return "";
        }
    }
}
