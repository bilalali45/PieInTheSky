package pk.com.Taj.app.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    static final String filename = "sdcard/pits.log";

    public static void write(String text) {
        File logFile = new File(filename);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void writeError(Exception e) {
        String error = "--------------------------------------------------------------\r\n";
        error += "Datetime: " + getDateTime() + "\r\n";
        error += "--------------------------------------------------------------\r\n";
        error += getStackTrace(e);
        write(error);
    }

    public static void writeLinkageError(LinkageError e) {
        String error = "--------------------------------------------------------------\r\n";
        error += "Datetime: " + getDateTime() + "\r\n";
        error += "--------------------------------------------------------------\r\n";
        error += getStackTrace2(e);
        write(error);
    }


    private static String getStackTrace2(LinkageError e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    private static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    private static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        return sdf.format(new Date());

    }

}

