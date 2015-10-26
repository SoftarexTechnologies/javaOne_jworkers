package com.softarex.jworker.core.notifications;

import com.softarex.jworker.core.task.BaseTask;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public abstract class NotificationReceiver {
    protected NotificationDelegate notificationDelegate;

    public NotificationReceiver() {
        this.notificationDelegate = new NotificationDelegate();
    }
    
    public void addListener(NotificationListener listener) {
        this.notificationDelegate.addListener(listener, NotificationType.values());
    }
    
    public void addListener(NotificationListener listener, NotificationType...nts) {
        this.notificationDelegate.addListener(listener, nts);
    }
    
    /**
     * Notifications broker calls this method during initialization.
     * This method should contain receiver initialization logic.
     * 
     * @throws Exception 
     */
    public abstract void init() throws Exception;
}
