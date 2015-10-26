package com.softarex.jworker.demo.client;

import com.softarex.jworker.core.RedisConfig;
import com.softarex.jworker.core.client.JWorkerClient;
import com.softarex.jworker.core.notifications.Notification;
import com.softarex.jworker.core.notifications.NotificationsBroker;
import com.softarex.jworker.core.task.BaseTask;
import com.softarex.jworker.demo.tasks.DemoTask;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import net.greghaines.jesque.Config;

/**
 * This class contains examples how to schedule tasks and get tasks execution results
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class Program {
    public static void main(String[] args) throws Exception {
        RedisConfig redisConfig = new RedisConfig();
        final Config config = redisConfig.buildJesqueConfig();
        
        NotificationsBroker notificationsBroker = new NotificationsBroker(
            new PubSubNotificationReceiver(redisConfig)
        );
        notificationsBroker.init();
        
        final MainFrame frame = new MainFrame();
        
        frame.rise((ActionEvent e) -> {
            final JWorkerClient client = new JWorkerClient(config);
            scheduleTasks(client, frame);
            client.end();
        });
        
        notificationsBroker.addNotificationListener((Notification notification) -> {
            SwingUtilities.invokeLater(() -> {
                frame.updateTask(notification.getTask(), notification.getNotificationType());
            });
        });
    }
    
    private static void scheduleTask(JWorkerClient client, MainFrame frame, BaseTask task) {
        frame.addTask(task);
        client.enqueue(task);
    }
    
    private static void scheduleTasks(JWorkerClient client, MainFrame frame) {
        scheduleTask(client, frame, new DemoTask("Hello Java One 2015!!!"));
        // TODO: schedule your tasks here
        
    }
}
