package com.softarex.jworker.core.task;

import java.io.Serializable;
import java.util.UUID;

/**
 * Used as base class for the worker tasks
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public abstract class BaseTask implements Serializable {  
    private String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Task validation function. Can be overridden in derived class for adding custom validation rules
     * @throws InvalidTaskException  in case if some task configuration is not valid
     */
    public void validate() throws InvalidTaskException {
    }
}
