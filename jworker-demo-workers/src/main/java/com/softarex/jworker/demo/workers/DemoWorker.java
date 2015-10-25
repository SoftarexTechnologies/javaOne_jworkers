package com.softarex.jworker.demo.workers;

import com.softarex.jworker.core.annotations.JWorkerTask;
import com.softarex.jworker.core.worker.BaseWorker;
import com.softarex.jworker.demo.tasks.DemoTask;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demo worker object. 
 * This worker object prints to the console some text provided by the task
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
@JWorkerTask(taskClass = DemoTask.class)
public class DemoWorker extends BaseWorker<DemoTask> {
    private static final Logger logger = LoggerFactory.getLogger(DemoWorker.class);
    
    public DemoWorker(DemoTask task) {
        super(task);
    }

    @Override
    protected void run(DemoTask task) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.error("Sleep failed", ex);
        }
        
        logger.info("DemoWorker: " + task.getMessage());
    }  
}
