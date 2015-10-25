package com.softarex.jworkers.core.worker;

import com.softarex.jworkers.core.task.BaseTask;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public abstract class BaseWorker<TTask extends BaseTask> implements Runnable {
    protected TTask task;
    
    
    public BaseWorker(TTask task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        this.run(this.task);
    }
    
    protected abstract void run(TTask task);
}
