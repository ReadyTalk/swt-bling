package com.readytalk.swt.util;

import org.eclipse.swt.SWT;

public class ClientOS {
    public static boolean isWindows7() {
        boolean windows7 = false;
        String swtPlatform = SWT.getPlatform();
        if(swtPlatform.equals("win32")) {
            double version;
            try {
                version = Float.parseFloat(System.getProperty("os.version"));
                windows7 = version <= 7.0;
            } catch (NumberFormatException ex) {
                windows7 = true;
            }
        }
        return windows7;
    }

    public static boolean isWindows() {
        boolean windows = false;
        String swtPlatform = SWT.getPlatform();
        if(swtPlatform.equals("win32")) {
           windows = true;
        }
        return windows;
    }
}
