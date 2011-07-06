/*
 * ProcessLogGeneratorApp.java
 */

package it.unipd.math.plg.ui;

import it.unipd.math.plg.ui.utils.Configurations;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ProcessLogGeneratorApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new PLGMainUI(this));
		JFrame mainFrame = ProcessLogGeneratorApp.getApplication().getMainFrame();
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ProcessLogGeneratorApp
     */
    public static ProcessLogGeneratorApp getApplication() {
        return Application.getInstance(ProcessLogGeneratorApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {

		// may we have to skip the check for updates? (as debug mode)
		Configurations.CHECK_FOR_UPDATES = !(Arrays.asList(args).contains("--no-check-updates"));
		// do you want to show the log window from the beginning?
		Configurations.INITIALLY_SHOW_LOG_WINDOW = (Arrays.asList(args).contains("--show-log-window"));

		// try setting the correct look and feel :)
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// handle exception
		}


		launch(ProcessLogGeneratorApp.class, args);
    }
}
