package com.softarex.jworker.demo.client;

import com.softarex.jworker.core.RedisConfig;
import com.softarex.jworker.core.client.JWorkerClient;
import com.softarex.jworker.core.notifications.Notification;
import com.softarex.jworker.core.notifications.NotificationListener;
import com.softarex.jworker.core.notifications.NotificationsBroker;
import com.softarex.jworker.demo.tasks.DemoTask;
import net.greghaines.jesque.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains examples how to schedule tasks and get tasks execution results
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class Program {
    
    private static final Logger logger = LoggerFactory.getLogger(Program.class);

    public static void main(String[] args) throws Exception {
        RedisConfig redisConfig = new RedisConfig();
        final Config config = redisConfig.buildJesqueConfig();
        
        NotificationsBroker notificationsBroker = new NotificationsBroker(new PubSubNotificationReceiver(redisConfig));
        notificationsBroker.init();
        
        notificationsBroker.addNotificationListener((Notification notification) -> {
            logger.info("notification received: " + notification);
        });
        
        final JWorkerClient client = new JWorkerClient(config);
        
        int tasks = 1000;
        while (tasks > 0) {
            client.enqueue(new DemoTask("Hello Java One 2015!!!"));
            Thread.sleep(1000);
            tasks--;
        }
        
        //client.enqueue(new DemoTask("Hello Java One 2015!!!"));
        // TODO: schedule your tasks here
        
        client.end();
    }
}
