package com.softarex.jworker.demo.client;

import com.softarex.jworker.core.RedisConfigBuilder;
import com.softarex.jworker.core.client.JWorkerClient;
import com.softarex.jworker.demo.tasks.DemoTask;
import net.greghaines.jesque.Config;

/**
 * This class contains examples how to schedule tasks and get tasks execution results
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class Program {

    public static void main(String[] args) {
        final Config config = new RedisConfigBuilder().buildJesqueConfig();
        
        final JWorkerClient client = new JWorkerClient(config);
        
        client.enqueue(new DemoTask("Hello Java One 2015!!!"));
        // TODO: schedule your tasks here
        
        client.end();
    }
}
