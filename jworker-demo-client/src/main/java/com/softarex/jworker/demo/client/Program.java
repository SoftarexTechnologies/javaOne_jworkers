package com.softarex.jworker.demo.client;

import com.softarex.jworker.core.client.JWorkerClient;
import com.softarex.jworker.demo.tasks.DemoTask;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;

/**
 * This class contains examples how to schedule tasks and get tasks execution results
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class Program {

    public static void main(String[] args) {
        // TODO: load redis parameters from properties file
        final Config config = new ConfigBuilder().withDatabase(5).withHost("192.168.56.101").build();
        
        final JWorkerClient client = new JWorkerClient(config);
        
        client.enqueue(new DemoTask("Hello Java One 2015!!!"));
        // TODO: schedule your tasks here
        
        client.end();
    }
}
