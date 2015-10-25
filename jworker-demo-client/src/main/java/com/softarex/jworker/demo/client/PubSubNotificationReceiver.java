package com.softarex.jworker.demo.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softarex.jworker.core.RedisConfig;
import com.softarex.jworker.core.notifications.Notification;
import com.softarex.jworker.core.notifications.NotificationReceiver;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class PubSubNotificationReceiver extends NotificationReceiver {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PubSubNotificationReceiver.class);
    
    public static final String CHANNEL_NAME = "task-execution";
    
    private final RedisConfig config;
    private Jedis jedis;
    private final ObjectMapper objectMapper;
    private Thread subscriberThread;

    public PubSubNotificationReceiver(RedisConfig config) {
        this.config       = config;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enableDefaultTyping();
        this.objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @Override
    public void init() throws Exception {
        this.jedis = new Jedis(this.config.getHost(), this.config.getPort());
        if (this.config.hasPassword()) {
            this.jedis.auth(this.config.getPassword());
        }
        this.jedis.select(this.config.getDatabase());
        
        this.subscriberThread = new Thread(() -> {
            this.jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    PubSubNotificationReceiver.this.processPubSubMessage(message);
                }
            }, CHANNEL_NAME);
        });
        
        this.subscriberThread.start();
    }
    
    
    
    protected void processPubSubMessage(String message) {
        Notification notification;
        try {
            notification = this.objectMapper.readValue(message, Notification.class);
        } catch (IOException ex) {
            logger.error("Can't load notification", ex);
            return;
        }
        
        this.notificationDelegate.fireNotification(notification);
    }
}
