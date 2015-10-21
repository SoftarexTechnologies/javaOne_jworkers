package com.softarex.jworkers.common.task;

import java.io.Serializable;

/**
 * Used as base class for the worker tasks
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public abstract class BaseTask implements Serializable {  
    /**
     * Task validation function. Can be overridden in derived class for adding custom validation rules
     * @throws InvalidTaskException  in case if some task configuration is not valid
     */
    public void validate() throws InvalidTaskException {
    }
}
