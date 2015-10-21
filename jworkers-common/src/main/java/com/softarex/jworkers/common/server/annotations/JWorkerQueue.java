/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softarex.jworkers.common.server.annotations;

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
