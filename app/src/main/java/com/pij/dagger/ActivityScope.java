package com.pij.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * <p>Created on 16/04/2017.</p>
 * @author Pierrejean
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope { }
