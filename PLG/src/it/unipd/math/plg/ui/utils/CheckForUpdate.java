package it.unipd.math.plg.ui.utils;

import it.unipd.math.plg.ui.PLGNewRelease;
import it.unipd.math.plg.ui.ProcessLogGeneratorApp;
import java.io.InputStream;
import java.net.URL;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * This class implements the system to check for software updates
 *
 * @author Andrea Burattin
 */
public class CheckForUpdate implements Runnable {

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			System.out.print("Checking for update... ");
			if (!Configurations.CHECK_FOR_UPDATES) {
				System.out.println("skipped!");
				return;
			}
			
			Document doc = getUpdateInformation();
			String version = doc.getDocumentElement().getElementsByTagName("version").item(0).getTextContent();
			String releaseDate = doc.getDocumentElement().getElementsByTagName("releaseDate").item(0).getTextContent();
			String message = doc.getDocumentElement().getElementsByTagName("message").item(0).getTextContent();
			String dowloadUrl = doc.getDocumentElement().getElementsByTagName("dowloadUrl").item(0).getTextContent();

			if (!version.equals(Configurations.SW_VERSION)) {
				System.out.println("Updates found!");
				JFrame mainFrame = ProcessLogGeneratorApp.getApplication().getMainFrame();
				PLGNewRelease dialog = new PLGNewRelease(mainFrame, true);
				dialog.setLabels(version, releaseDate, message, dowloadUrl);
				dialog.setLocationRelativeTo(mainFrame);
				dialog.setVisible(true);
			} else {
				System.out.println("No updates available");
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
