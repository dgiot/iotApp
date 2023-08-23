package android.tx.com.dgiot_amis.utils;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * @author jie
 * @date 2022/8/3
 * @time 14:44
 */
public class DgiotUtils {

    public static Uri createImageFile() throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(), "/dgiot/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }
}
