package ru.netology.moneytransferservice.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static Log INSTANCE = null;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd - HH:mm:ss");
    private final SaveLog saveLog = new LogFile();
    private final PrintLog printLog = new Printer();

    public static Log getInstance() {
        if (INSTANCE == null) {
            synchronized (Log.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Log();
                }
            }
        }
        return INSTANCE;
    }

    public void log(String typelevel, String logMessage) {
        String logLine = LocalDateTime.now().format(timeFormatter) + " : [" + typelevel + "]" + " : " + logMessage + "\n";
        this.printLog.logShow(logLine);
        this.saveLog.saveLog(logLine);
    }
}
