package com.softarex.jworker.demo.server;

import com.softarex.jworker.core.worker.JWorkerFactory;
import com.softarex.jworker.demo.workers.DemoWorker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerImpl;

/**
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class Program {
    /**
     * Entry point.
     * @param args 
     */
    public static void main(String[] args) {
        // TODO: load redis parameters from properties file
        final Config config = new ConfigBuilder().withDatabase(5).withHost("192.168.56.101").build();
        
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
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
