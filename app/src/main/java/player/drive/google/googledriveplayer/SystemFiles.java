package player.drive.google.googledriveplayer;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by 174pr on 26.03.2018.
 */

public final class SystemFiles {

    public static boolean SaveFile(Context context, String fileName, String fileData) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.write(fileData);
            writer.flush();
            writer.close();

            return  true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String ReadFile(Context context, String FileName) {
        try {
            FileInputStream fIn = context.openFileInput(FileName);
            InputStreamReader isr = new InputStreamReader(fIn);
            StringBuilder stringBuilder = new StringBuilder();

            int temp;
            while((temp = isr.read()) != -1) {
                char symbol = (char)temp;
                stringBuilder.append((symbol));
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
