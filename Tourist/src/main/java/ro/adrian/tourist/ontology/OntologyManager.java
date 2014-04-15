package ro.adrian.tourist.ontology;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by Adrian Olar on 4/5/14.
 * License Thesis Project
 */
public class OntologyManager {
    private static final String TAG = OntologyManager.class.getCanonicalName();
    private FileWriter fileWriter;
    private static OntologyManager INSTANCE;

    private static final String instanceKeyString = "instance";

    private OntologyManager() {
    }

    public static OntologyManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OntologyManager();
        }
        return INSTANCE;
    }

    public void writeToFile(String filePath, String message, boolean append) {
        File sdCardFile = new File(Environment.getExternalStorageDirectory() + filePath);
        Log.d(TAG, sdCardFile.getPath());
        try {
            fileWriter = new FileWriter(sdCardFile, append);
            fileWriter.write(message);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "**Exception when writing to file**");
        }
    }

    private String createInstance(String placeName, String instanceType) {
        return "(" + instanceKeyString + " " + placeName + " " + instanceType + ")";
    }
}
