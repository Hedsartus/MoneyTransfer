package ru.netology.moneytransferservice.log;

import java.io.FileWriter;
import java.io.IOException;

public class LogFile implements SaveLog {
    private static final String FILE_NAME = "log.txt";
    private FileWriter fileWriter;

    @Override
    public void saveLog(String logLine) {
        try {
            this.fileWriter = new FileWriter(FILE_NAME, true);
            this.fileWriter.write(logLine);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
