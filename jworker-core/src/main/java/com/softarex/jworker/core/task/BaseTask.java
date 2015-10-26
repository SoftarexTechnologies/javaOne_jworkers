package com.softarex.jworker.core.task;

import java.io.Serializable;
import java.util.Objects;
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

    @Override
    public String toString() {
        return "BaseTask{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BaseTask other = (BaseTask) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
