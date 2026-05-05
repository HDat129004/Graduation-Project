package com.doantn.app;

/**
 * MainLauncher: Launcher to choose between console and GUI mode.
 */
public class MainLauncher {
    public static void main(String[] args) {
        if (args.length > 0 && "--gui".equals(args[0])) {
            // Launch GUI, remove --gui from args
            String[] guiArgs = new String[args.length - 1];
            System.arraycopy(args, 1, guiArgs, 0, guiArgs.length);
            MainGUI.main(guiArgs);
        } else {
            // Launch console
            App.main(args);
        }
    }
}