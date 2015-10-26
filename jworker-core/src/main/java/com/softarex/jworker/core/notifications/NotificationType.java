package com.softarex.jworker.core.notifications;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public enum NotificationType {
    TASK_IN_PROGRESS("In progress"),
    TASK_DONE("Done"),
    TASK_FAILED("Error");
    
    private final String name;

    private NotificationType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
