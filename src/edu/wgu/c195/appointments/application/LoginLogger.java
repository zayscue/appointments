package edu.wgu.c195.appointments.application;

import edu.wgu.c195.appointments.domain.entities.User;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SignInLogger implements Runnable {

    private static Logger LOGGER;

    static {
        try {
            FileHandler fileHandler = new FileHandler("appointments_sign_in.log", true);
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    StringBuffer buffer = new StringBuffer(1000);
                    buffer.append(new java.util.Date());
                    buffer.append(' ');
                    buffer.append(record.getLevel());
                    buffer.append(' ');
                    buffer.append(formatMessage(record));
                    buffer.append('\n');
                    return buffer.toString();
                }
            });
            LOGGER = Logger.getLogger("appointments_sign_in");
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final User user;

    public SignInLogger(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        LOGGER.info(this.user.getUserName() + " signed in.");
    }

    public static Runnable createLogger(final User user) {
        return new SignInLogger(user);
    }
}
