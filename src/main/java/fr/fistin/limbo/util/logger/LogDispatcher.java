package fr.fistin.limbo.util.logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 13:30
 */
public class LogDispatcher extends Thread {

    private final LimboLogger logger;
    private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

    public LogDispatcher(LimboLogger logger) {
        super("Limbo Logger Thread");
        this.logger = logger;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            LogRecord record;
            try {
                record = this.queue.take();
            } catch (InterruptedException e) {
                continue;
            }

            this.logger.doLog(record);
        }

        for (LogRecord record : this.queue) {
            this.logger.doLog(record);
        }
    }

    public void queue(LogRecord record) {
        if (!this.isInterrupted()) {
            this.queue.add(record);
        }
    }

    public BlockingQueue<LogRecord> getQueue() {
        return this.queue;
    }

}
