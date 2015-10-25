package com.softarex.jworker.core.notifications;

import com.softarex.jworker.core.task.BaseTask;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class NotificationsBroker {
    private static final Logger logger = LoggerFactory.getLogger(NotificationsBroker.class);
    
    private final NotificationReceiver receiver;
    private final NotificationSender sender;

    public NotificationsBroker(NotificationReceiver receiver) {
        this(receiver, null);
    }
    
    public NotificationsBroker(NotificationSender sender) {
        this(null, sender);
    }
    
    public NotificationsBroker(NotificationReceiver receiver, 
                               NotificationSender sender) {
        this.receiver = receiver;
        this.sender   = sender;
    }
    
    public void init() throws Exception {
        if (this.receiver != null) {
            this.receiver.init();
            logger.debug("Receiver initialized");
        }
        
        if (this.sender != null) {
            this.sender.init();
            logger.debug("Sender initialized");
        }
    }
    
    public void fireNotification(NotificationType tn, BaseTask task) {
        this.sendNotification(new Notification(tn, task));
    }
    
    public void sendNotification(Notification notification) {
        this.checkSender();
        try {
            this.sender.sendNotification(notification);
        } catch (Exception ex) {
            logger.error("Can't send notification: " + notification, ex);
        }
    }
    
        public void addNotificationListener(NotificationListener listener, NotificationType...nts) {
        this.checkReceiver();
        if (nts.length == 0) {
            this.receiver.addListener(listener);
        } else {
            this.receiver.addListener(listener, nts);
        }
    }
    
    protected void checkSender() {
        if (this.sender == null) {
            throw new IllegalStateException("Sender is not initialized");
        }
    }
    
    protected void checkReceiver() {
        if (this.receiver == null) {
            throw new IllegalStateException("Receiver is not initialized");
        }
    }
}
