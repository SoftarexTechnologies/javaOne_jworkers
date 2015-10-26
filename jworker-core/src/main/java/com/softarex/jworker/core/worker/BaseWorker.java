package com.softarex.jworker.core.worker;

import com.softarex.jworker.core.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ivan Dubynets
 * @param <TTask> task type
 * @email ivan@softarex.com
 */
public abstract class BaseWorker<TTask extends BaseTask> implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(BaseWorker.class);
    
    protected TTask task;
    
    public BaseWorker(TTask task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        logger.debug("Executing task: " + this.task.getId());
        try {
            this.run(this.task);
        } finally {
            logger.debug("Task executed: " + this.task.getId());
        }
    }
    
    protected abstract void run(TTask task);

    public BaseTask getTask() {
        return this.task;
    }
}
