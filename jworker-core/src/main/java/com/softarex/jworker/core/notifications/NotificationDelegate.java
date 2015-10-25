package com.softarex.jworker.core.notifications;

import com.softarex.jworker.core.task.BaseTask;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.greghaines.jesque.utils.ConcurrentHashSet;
import net.greghaines.jesque.utils.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class NotificationDelegate {
    private static final Logger logger = LoggerFactory.getLogger(NotificationDelegate.class);

    private final Map<NotificationType, ConcurrentSet<NotificationListener>> eventListenerMap;
    
    public NotificationDelegate() {
        final Map<NotificationType, ConcurrentSet<NotificationListener>> elp = 
                new EnumMap<>(NotificationType.class);
        
        for (final NotificationType nt : NotificationType.values()) {
            elp.put(nt, new ConcurrentHashSet<>());
        }
        this.eventListenerMap = Collections.unmodifiableMap(elp);
    }
    
    public void addListener(final NotificationListener listener, final NotificationType... nts) {
        if (listener != null) {
            for (final NotificationType nt : nts) {
                final ConcurrentSet<NotificationListener> listeners = this.eventListenerMap.get(nt);
                if (listeners != null) {
                    listeners.add(listener);
                }
            }
        }
    }
    
    public void removeListener(final NotificationListener listener) {
        removeListener(listener, NotificationType.values());
    }
    
    public void removeListener(final NotificationListener listener, final NotificationType... nts) {
        if (listener != null) {
            for (final NotificationType nt : nts) {
                final ConcurrentSet<NotificationListener> listeners = this.eventListenerMap.get(nt);
                if (listeners != null) {
                    listeners.remove(listener);
                }
            }
        }
    }
    
    public void removeAllListeners() {
        removeAllListeners(NotificationType.values());
    }

    public void removeAllListeners(final NotificationType... nts) {
        for (final NotificationType nt : nts) {
            final ConcurrentSet<NotificationListener> listeners = this.eventListenerMap.get(nt);
            if (listeners != null) {
                listeners.clear();
            }
        }
    }
    
    public void fireNotification(final Notification notification) {
        final ConcurrentSet<NotificationListener> listeners = this.eventListenerMap.get(notification.getNotificationType());
        if (listeners != null) {
            listeners.stream().filter((listener) -> (listener != null)).forEach((listener) -> {
                try {
                    listener.onNotification(notification);
                } catch (Exception e) {
                    logger.error("Can't delegate notification " + notification, e);
                }
            });
        }
    }
}
