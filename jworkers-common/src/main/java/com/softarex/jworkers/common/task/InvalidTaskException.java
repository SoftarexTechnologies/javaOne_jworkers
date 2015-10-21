package com.softarex.jworkers.common.task;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class InvalidTaskException extends Exception {

    public InvalidTaskException() {
    }

    public InvalidTaskException(String message) {
        super(message);
    }

    public InvalidTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTaskException(Throwable cause) {
        super(cause);
    }
}
