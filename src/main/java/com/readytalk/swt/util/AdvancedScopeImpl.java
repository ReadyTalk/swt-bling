package com.readytalk.swt.util;

import org.eclipse.swt.graphics.GC;

import static com.readytalk.swt.util.AdvancedGC.setAdvanced;

class AdvancedScopeImpl implements AdvancedScope {
    private boolean changed = false;
    private boolean targetSetting;
    private boolean initialSetting;
    private GC gc;

    public AdvancedScopeImpl(GC gc, boolean targetSetting) {
        this.targetSetting = targetSetting;
        this.gc = gc;
        this.initialSetting = gc.getAdvanced();
        if (initialSetting != targetSetting) {
            changed = true;
            setAdvanced(gc, targetSetting);
        }

    }

    public void complete() {
        if (changed) {
            setAdvanced(gc, initialSetting);
        }
    }
}
