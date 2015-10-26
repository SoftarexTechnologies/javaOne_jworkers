package com.softarex.jworker.demo.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softarex.jworker.core.RedisConfig;
import com.softarex.jworker.core.notifications.Notification;
import com.softarex.jworker.core.notifications.NotificationSender;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class PubSubNotificationSender implements NotificationSender {
    public static final String CHANNEL_NAME = "task-execution";
    
    private final RedisConfig config;
    private Jedis jedis;
    private final ObjectMapper objectMapper;

    public PubSubNotificationSender(RedisConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enableDefaultTyping();
        this.objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @Override
    public synchronized void sendNotification(Notification notification) throws Exception {
        this.checkState();
        String message = this.objectMapper.writeValueAsString(notification);
        this.jedis.publish(CHANNEL_NAME, message);
    }

    @Override
    public void init() throws Exception {
        Jedis jedisTmp = new Jedis(this.config.getHost(), this.config.getPort());
        if (this.config.hasPassword()) {
            jedisTmp.auth(this.config.getPassword());
        }
        jedisTmp.select(this.config.getDatabase());
        this.jedis = jedisTmp;
    }
    
    private void checkState() {
        if (this.jedis == null) {
            throw new IllegalStateException("is not initializeId yet");
        }
    }
}
