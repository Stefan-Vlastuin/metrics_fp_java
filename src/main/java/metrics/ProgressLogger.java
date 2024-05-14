package metrics;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProgressLogger {

    private final static String LOG_PATH = "logs/metrics.log";
    private final Logger logger = Logger.getLogger("metrics");
    FileHandler fh;
    private static ProgressLogger instance;

    private ProgressLogger(){
        try {
            fh = new FileHandler(LOG_PATH, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            log(e);
        }
    }

    public static ProgressLogger getInstance() {
        if (instance == null) {
            instance = new ProgressLogger();
        }
        return instance;
    }

    public void log(Level level, String message) {
        this.logger.log(level, message);
    }

    public void log(String message) {
        log(Level.INFO, message);
    }

    public void log(Throwable throwable){
        logger.log(Level.SEVERE, throwable.getMessage(), throwable);
    }

    public void log(Throwable throwable, String message){
        logger.log(Level.SEVERE, message + ": " + throwable.getMessage(), throwable);
    }

    public void close(){
        if (fh != null) {
            fh.close();
        }
    }

}
