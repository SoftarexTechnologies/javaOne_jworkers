
package com.softarex.jworker.core.notifications;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public interface NotificationSender {
    
    public void sendNotification(Notification notification) throws Exception;
    
    /**
     * Notifications broker calls this method during initialization.
     * This method should contain sender initialization logic.
     * 
     * @throws Exception 
     */
    public void init() throws Exception;
}
