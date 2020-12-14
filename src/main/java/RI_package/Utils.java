package RI_package;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    public static void saveFile(String nameFile, String content) {
        File file = new File(nameFile);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(content.replace(",", "."));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
