package com.softarex.jworker.demo.workers;

import com.softarex.jworker.core.annotations.JWorkerTask;
import com.softarex.jworker.core.worker.BaseWorker;
import com.softarex.jworker.demo.tasks.DemoTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demo worker object. 
 * This worker object prints to the console some text provided by the task
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
@JWorkerTask(taskClass = DemoTask.class)
public class DemoWorker extends BaseWorker<DemoTask> {
    public DemoWorker(DemoTask task) {
        super(task);
    }

    @Override
    protected void run(DemoTask task) {
        Logger.getLogger(DemoWorker.class.getName()).log(
            Level.INFO, "DemoWorker: {0}", task.getMessage());
    }  
}
