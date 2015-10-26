package com.softarex.jworker.core.notifications;

import com.softarex.jworker.core.task.BaseTask;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class Notification {
    private NotificationType notificationType;
    private BaseTask task;

    public Notification() {
    }

    public Notification(NotificationType notificationType, BaseTask task) {
        this.notificationType = notificationType;
        this.task = task;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public BaseTask getTask() {
        return task;
    }

    public void setTask(BaseTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "Notification{" + "notificationType=" + notificationType + ", task=" + task + '}';
    }
}
