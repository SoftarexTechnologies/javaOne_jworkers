package com.softarex.jworker.demo.tasks;

import com.softarex.jworker.core.annotations.JWorkerQueue;
import com.softarex.jworker.core.task.BaseTask;
import java.util.UUID;

/**
 * Demo task for the DemoWorker.
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
@JWorkerQueue("demo")
public class DemoTask extends BaseTask {
    private String message;

    public DemoTask(String message) {
        this.message = message;
    }

    public DemoTask() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
