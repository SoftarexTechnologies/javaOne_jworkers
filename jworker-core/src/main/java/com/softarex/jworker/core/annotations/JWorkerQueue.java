package com.softarex.jworker.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JWorkerQueue {
    String value();
}
