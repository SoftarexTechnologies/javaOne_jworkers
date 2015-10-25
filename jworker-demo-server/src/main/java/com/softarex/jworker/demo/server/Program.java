package com.softarex.jworker.demo.server;

import com.softarex.jworker.core.RedisConfig;
import com.softarex.jworker.core.notifications.NotificationType;
import com.softarex.jworker.core.notifications.NotificationsBroker;
import com.softarex.jworker.core.worker.BaseWorker;
import com.softarex.jworker.core.worker.JWorkerFactory;
import com.softarex.jworker.demo.workers.DemoWorker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerEvent;
import net.greghaines.jesque.worker.WorkerImpl;
import net.greghaines.jesque.worker.WorkerListener;
import net.greghaines.jesque.worker.WorkerPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class Program {
    private static final Logger logger = LoggerFactory.getLogger(Program.class);
    
    /**
     * Entry point.
     * @param args 
     */
    public static void main(String[] args) throws Exception {
        RedisConfig redisConf = new RedisConfig(); // loads redis.propeties file by default
        final Config config = redisConf.buildJesqueConfig();
        final NotificationsBroker notificationsBroker = 
                new NotificationsBroker(new PubSubNotificationSender(redisConf));
        notificationsBroker.init();
        
        List<Class<?>> workers = new ArrayList<>();
        workers.add(DemoWorker.class);
        // TODO: add your workers here
        
        final JWorkerFactory workersFactory = new JWorkerFactory(workers);
        final WorkerPool workerPool = new WorkerPool(new Callable<Worker>() {

            @Override
            public Worker call() throws Exception {
                return new WorkerImpl(
                    config,
                    Arrays.asList(workersFactory.getQueues()),
                    workersFactory
                );
            }
        }, redisConf.getPoolSize());
        
        workerPool.getWorkerEventEmitter().addListener((event, worker, queue, job, runnerWorker, result, t) -> {
            if (runnerWorker instanceof BaseWorker) {
                notificationsBroker.fireNotification(NotificationType.TASK_IN_PROGRESS, ((BaseWorker)runnerWorker).getTask());
            }
        }, WorkerEvent.JOB_EXECUTE);

        workerPool.getWorkerEventEmitter().addListener((event, worker, queue, job, runnerWorker, result, t) -> {
            if (runnerWorker instanceof BaseWorker) {
                notificationsBroker.fireNotification(NotificationType.TASK_FAILED, ((BaseWorker)runnerWorker).getTask());
            }
        }, WorkerEvent.JOB_FAILURE);
        
        workerPool.getWorkerEventEmitter().addListener((event, worker, queue, job, runnerWorker, result, t) -> {
            if (runnerWorker instanceof BaseWorker) {
                notificationsBroker.fireNotification(NotificationType.TASK_DONE, ((BaseWorker)runnerWorker).getTask());
            }
        }, WorkerEvent.JOB_SUCCESS);
        
        workerPool.run();
    }
}
