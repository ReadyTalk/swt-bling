package com.readytalk.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.GC;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvancedGC {
    protected static final Logger log = Logger.getLogger(AdvancedGC.class.getName());
    private static boolean loggedAdvanced = false;

    public static void setAdvanced(GC gc, boolean targetSetting) {
        if (targetSetting) {
            try {
                gc.setAdvanced(true);
                gc.setAntialias(SWT.ON);
                gc.setTextAntialias(SWT.ON);
                gc.setInterpolation(SWT.HIGH);
            } catch (SWTException e) {
                if (!loggedAdvanced) {
                    log.log(Level.WARNING, "Unable to set advanced drawing", e);
                    loggedAdvanced = true;
                }
            }
        } else {
            gc.setAdvanced(false);
        }

    }

    /*
    * Getting an advanced scope allows setting a gc to a different
    * advanced value for a certain scope
    * Calling complete on the returned AdvancedScope
    * will reset the gc to the original advanced value.
    */
    public static AdvancedScope advancedScope(GC gc, boolean targetSetting) {
        return new AdvancedScopeImpl(gc, targetSetting);
    }


}
