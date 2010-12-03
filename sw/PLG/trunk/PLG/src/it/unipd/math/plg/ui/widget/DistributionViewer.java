package it.unipd.math.plg.ui.widget;

import it.unipd.math.plg.models.distributions.PlgProbabilityDistribution;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 * This widget is used for showing the current distribution behaviour
 *
 * @author Andrea Burattin
 */
public class DistributionViewer extends JComponent {

	private int minValue = 2;
	private int maxValue = 10;
	protected PlgProbabilityDistribution distribution = PlgProbabilityDistribution.normalDistributionFactory();

	
	/**
	 * Get the value of distribution
	 *
	 * @return the value of distribution
	 */
	public PlgProbabilityDistribution getDistribution() {
		return distribution;
	}


	/**
	 * Set the value of distribution
	 *
	 * @param distribution new value of distribution
	 */
	public void setDistribution(PlgProbabilityDistribution distribution) {
		this.distribution = distribution;
	}


	/**
	 * Get the value of maxValue
	 *
	 * @return the value of maxValue
	 */
	public int getMaxValue() {
		return maxValue;
	}


	/**
	 * Set the value of maxValue
	 *
	 * @param maxValue new value of maxValue
	 */
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}


	/**
	 * Get the value of minValue
	 *
	 * @return the value of minValue
	 */
	public int getMinValue() {
		return minValue;
	}


	/**
	 * Set the value of minValue
	 *
	 * @param minValue new value of minValue
	 */
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		Graphics2D gBuf = (Graphics2D)g;
		gBuf.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// BACKGROUND
//		gBuf.setColor(Color.WHITE);
//		gBuf.fillRect(0, 0, width, height);

		// MAIN AXES
		int realHeight = height-17;
		int pos1 = width/3;
		int pos2 = (width/3)*2;

		gBuf.setColor(Color.WHITE);
		gBuf.fillRect(5, 5, width-10, height-17);
		
		gBuf.setColor(Color.BLACK);
		gBuf.drawRect(5, 5, width-10, height-17);
		gBuf.drawLine(3, 5+realHeight/2, 4, 5+realHeight/2);
		gBuf.drawLine(pos1, 3, pos1, 4);
		gBuf.drawLine(pos2, 3, pos2, 4);

		gBuf.setColor(Color.LIGHT_GRAY);
		for(int i = 6; i < width-10; i+=5) {
			gBuf.drawLine(i, 5+realHeight/2, i+2, 5+realHeight/2);
		}
		for(int i = 7; i < height-17; i+=5) {
			gBuf.drawLine(pos1, i, pos1, i+2);
			gBuf.drawLine(pos2, i, pos2, i+2);
		}


		// INSERT ALL THE TEXTS
		gBuf.setColor(Color.GRAY);
		gBuf.setFont(gBuf.getFont().deriveFont(10.0f));
		gBuf.drawString(new Integer(minValue).toString(), 5, height-2);
		if (maxValue < 10) {
			gBuf.drawString(new Integer(maxValue).toString(), width-10, height-2);
		} else if (maxValue < 100) {
			gBuf.drawString(new Integer(maxValue).toString(), width-15, height-2);
		} else {
			gBuf.drawString(new Integer(maxValue).toString(), width-20, height-2);
		}
		int delta = maxValue - minValue;
		if (maxValue >= 10) {
			gBuf.drawString(new Integer(new Double(Math.ceil(delta/3.)).intValue()).toString(), (width/3)-5, height-2);
			gBuf.drawString(new Integer(new Double(Math.ceil(delta/3.*2)).intValue()).toString(), (width/3*2)-5, height-2);
		}

		// BASIC VALUES
		int newWidth = width - 12;
		double diffHeight = height - 17;
		int prevX = -10;
		int prevY = -10;
		int size = maxValue-minValue+1;
		if (maxValue > 1000) {
			size = 1000-minValue+1;
		}

		Double[] plot = distribution.getValuesForPlotting(size+1);

		// draw the lines
		for (int i = 0; i < size+1; i++) {
			int x = Math.round(5.f + (((float)newWidth / (float)size) * (float)(i)));
			double v = plot[i];
			int y = (int)Math.round(diffHeight * v) - 5;

			if (prevY > -10) {
				gBuf.setColor(Color.GRAY);
				gBuf.drawLine(prevX+1, height-17-prevY, x+1, height-17-y);
			}
			prevX = x;
			prevY = y;
		}
		// draw the points
		for (int i = 0; i < size+1; i++) {
			int x = Math.round(5.f + (((float)newWidth / (float)size) * (float)(i)));
			double v = plot[i];
			int y = (int)Math.round(diffHeight * v) - 5;
			
			gBuf.setColor(Color.RED);
			gBuf.drawOval(x, height-18-y, 2, 2);
			gBuf.drawOval(x, height-18-y, 1, 1);
		}

		gBuf.dispose();
	}
}
