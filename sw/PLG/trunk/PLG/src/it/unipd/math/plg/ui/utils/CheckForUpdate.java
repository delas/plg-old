package it.unipd.math.plg.ui.utils;

import it.unipd.math.plg.ui.PLGAboutUI;
import it.unipd.math.plg.ui.PLGMainUI;
import it.unipd.math.plg.ui.PLGNewRelease;
import it.unipd.math.plg.ui.ProcessLogGeneratorApp;
import java.io.InputStream;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * This class implements the system to check for software updates
 *
 * @author Andrea Burattin
 */
public class CheckForUpdate implements Runnable {

	private boolean forced = false;
	private JLabel updateLabel = null;


	public CheckForUpdate() {
	}

	public CheckForUpdate(boolean forced, JLabel updateLabel) {
		this.forced = forced;
		this.updateLabel = updateLabel;
	}

	@Override
	public void run() {
		boolean toCheck = Configurations.CHECK_FOR_UPDATES || forced;
		try {
			Thread.sleep(1000);
			PLGLogger.log("Checking for update...");
			if (updateLabel == null) {
				System.out.print("Checking for update... ");
			} else {
				updateLabel.setText("Checking for update...");
			}
			
			if (!toCheck) {
				PLGLogger.log("Checking for update skipped");
				System.out.println("skipped!");
				return;
			}
			
			Document doc = getUpdateInformation();
			String version = doc.getDocumentElement().getElementsByTagName("version").item(0).getTextContent();
			Double release = Double.parseDouble(doc.getDocumentElement().getElementsByTagName("release").item(0).getTextContent());
			String releaseDate = doc.getDocumentElement().getElementsByTagName("releaseDate").item(0).getTextContent();
			String message = doc.getDocumentElement().getElementsByTagName("message").item(0).getTextContent();
			String dowloadUrl = doc.getDocumentElement().getElementsByTagName("dowloadUrl").item(0).getTextContent();
			
			if (release > Configurations.SW_RELEASE) {
				PLGLogger.log("Updates found!");
				if (updateLabel == null) {
					System.out.println("Updates found!");
				} else {
					updateLabel.setText("Updates found!");
				}

				JFrame mainFrame = ProcessLogGeneratorApp.getApplication().getMainFrame();
				PLGNewRelease dialog = new PLGNewRelease(mainFrame, true);
				dialog.setLabels(version, releaseDate, message, dowloadUrl);
				dialog.setLocationRelativeTo(mainFrame);
				dialog.setVisible(true);
				Configurations.UPDATES_FOUND = true;
			} else {
				PLGLogger.log("No updates available");
				if (updateLabel == null) {
					System.out.println("No updates available");
				} else {
					updateLabel.setText("You are up to date");
				}
				Configurations.UPDATES_FOUND = false;
			}
		} catch (Exception e) {
			System.out.println("FAILED:");
			e.printStackTrace();
		}
	}

	/**
	 *This method to get the update informations
	 *
	 * @return the document with the updated information
	 * @throws Exception
	 */
	public Document getUpdateInformation() throws Exception {
		String updateString = Configurations.UPDATES_URL;
		updateString += "?SW=" + Configurations.SW_CODE;
		updateString += "&VERSION=" + Configurations.SW_VERSION;
		updateString += "&OS=" + System.getProperty("os.name");

		URL url = new URL(updateString);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		InputStream stream = url.openStream();
		Document doc = db.parse(stream);
		doc.getDocumentElement().normalize();

		return doc;
	}

}
