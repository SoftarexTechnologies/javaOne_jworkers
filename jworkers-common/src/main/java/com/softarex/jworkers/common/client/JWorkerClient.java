package com.softarex.jworkers.common.client;

import com.softarex.jworkers.common.server.annotations.JWorkerQueue;
import com.softarex.jworkers.common.task.BaseTask;
import java.util.concurrent.TimeUnit;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.client.ClientImpl;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class JWorkerClient {
    protected ClientImpl client;
    
    public JWorkerClient(Config config) {
        this.client = new ClientImpl(config);
    }

    public JWorkerClient(Config config, boolean checkConnectionBeforeUse) {
        this.client = new ClientImpl(config, checkConnectionBeforeUse);
    }

    public JWorkerClient(Config config, long initialDelay, long period, TimeUnit timeUnit) {
        this.client = new ClientImpl(config, initialDelay, period, timeUnit);
    }
    
    public void enqueue(BaseTask task) {
        JWorkerQueue queue = task.getClass().getAnnotation(JWorkerQueue.class);
        String queueName = "general";
        if (queue != null) {
            queueName = queue.value();
        }
        this.client.enqueue(queueName, new Job(task.getClass().getName(), task));
    }
    
    public void end() {
        this.client.end();
    }
}
