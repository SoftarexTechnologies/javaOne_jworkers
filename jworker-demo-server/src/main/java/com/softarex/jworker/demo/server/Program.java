package com.softarex.jworker.demo.server;

import com.softarex.jworker.core.RedisConfig;
import com.softarex.jworker.core.worker.JWorkerFactory;
import com.softarex.jworker.demo.workers.DemoWorker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerImpl;
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
    public static void main(String[] args) {
        final Config config = new RedisConfig().buildJesqueConfig();
        
        List<Class<?>> workers = new ArrayList<>();
        
        
        workers.add(DemoWorker.class);
        // TODO: add your workers here
        
        JWorkerFactory workersFactory = new JWorkerFactory(workers);
        
        final Worker worker = new WorkerImpl(
            config,
            Arrays.asList(workersFactory.getQueues()),
            workersFactory
        );
                                                                                                                                                                                                 
        final Thread workerThread = new Thread(worker);
        workerThread.start();

        try {
            workerThread.join();
        } catch (Exception e) {
            logger.error("Can't join thread", e);
        }
    }
}
