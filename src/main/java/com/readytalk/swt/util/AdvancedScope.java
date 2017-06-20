package com.readytalk.swt.util;

/*
 * The Advanced Scope allows setting a gc to advanced for a certain time (scope)
 * Calling complete will reset the gc to the original advanced value.
 */
public interface AdvancedScope {
    void complete();
}
