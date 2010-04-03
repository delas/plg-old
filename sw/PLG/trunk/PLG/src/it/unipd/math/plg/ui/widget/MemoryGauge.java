package it.unipd.math.plg.ui.widget;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JProgressBar;


/**
 * @author Andrea Burattin
 */
public class MemoryGauge implements Runnable {

	protected long memoryAllocated = 0;
	protected long memoryUsed = 0;

	protected JProgressBar progress;
	protected JLabel label;

	protected DecimalFormat decFormat = new DecimalFormat("###0.#");

	protected boolean isRunning = false;
	protected long pollInterval = 1500;


	public MemoryGauge(JProgressBar progress, JLabel label) {
		this.progress = progress;
		this.label = label;
		isRunning = false;
	}


	public synchronized void setCompleteMemory(long completeMemory) {
		memoryAllocated = completeMemory;
	}


	public synchronized void setUsedMemory(long usedMemory) {
		memoryUsed = usedMemory;
	}


	public synchronized void setFreeMemory(long freeMemory) {
		memoryUsed = memoryAllocated - freeMemory;
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		isRunning = true;
		while(isRunning==true) {
			// periodically update memory information
			memoryAllocated = Runtime.getRuntime().totalMemory();
			memoryUsed = memoryAllocated - Runtime.getRuntime().freeMemory();

			progress.setMaximum((int) memoryAllocated);
			progress.setValue((int) memoryUsed);

			String memoryString = convertBytesToText(memoryUsed) + "/" + convertBytesToText(memoryAllocated) + " MB";
			label.setText(memoryString);

			try {
				Thread.sleep(pollInterval);	// sleep for some time
			} catch (InterruptedException e) {
				System.err.println("Memory usage polling thread interrupted on wait state:");
				e.printStackTrace();
			}
		}
	}


	public synchronized void stop() {
		isRunning = false;
	}


	protected String convertBytesToText(long bytes) {
		double mb = (double)bytes / 1048576.0;
		return decFormat.format(mb);
	}

}
