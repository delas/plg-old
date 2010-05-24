/*
 * ProcessLogGeneratorView.java
 */

package it.unipd.math.plg.ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.beans.PropertyVetoException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import it.unipd.math.plg.models.PlgProcess;
import it.unipd.math.plg.ui.utils.CheckForUpdate;
import it.unipd.math.plg.ui.utils.Configurations;
import it.unipd.math.plg.ui.widget.MemoryGauge;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The application's main frame.
 */
public class PLGMainUI extends FrameView {

    public PLGMainUI(SingleFrameApplication app) {
        super(app);

        initComponents();

		MemoryGauge memGaugeThread = new MemoryGauge(jProgressBar1, jLabel2);
		Thread mt = new Thread(memGaugeThread);
		mt.start();

		CheckForUpdate check = new CheckForUpdate();
		Thread up = new Thread(check);
		up.start();
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ProcessLogGeneratorApp.getApplication().getMainFrame();
            aboutBox = new PLGAboutUI(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ProcessLogGeneratorApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.BorderLayout());

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(it.unipd.math.plg.ui.ProcessLogGeneratorApp.class).getContext().getResourceMap(PLGMainUI.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel1.add(jLabel1);

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel1.add(jLabel2);

        jProgressBar1.setName("jProgressBar1"); // NOI18N
        jPanel1.add(jProgressBar1);

        mainPanel.add(jPanel1, java.awt.BorderLayout.SOUTH);

        jDesktopPane1.setAutoscrolls(true);
        jDesktopPane1.setName("jDesktopPane1"); // NOI18N
        mainPanel.add(jDesktopPane1, java.awt.BorderLayout.CENTER);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(it.unipd.math.plg.ui.ProcessLogGeneratorApp.class).getContext().getActionMap(PLGMainUI.class, this);
        jMenuItem1.setAction(actionMap.get("actionCreateNetProcess")); // NOI18N
        jMenuItem1.setIcon(resourceMap.getIcon("jMenuItem1.icon")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        fileMenu.add(jMenuItem1);

        jMenuItem4.setAction(actionMap.get("actionOpenExistingProcess")); // NOI18N
        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(resourceMap.getIcon("jMenuItem4.icon")); // NOI18N
        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        fileMenu.add(jMenuItem4);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N

        jMenuItem2.setAction(actionMap.get("windowTileAction")); // NOI18N
        jMenuItem2.setIcon(resourceMap.getIcon("jMenuItem2.icon")); // NOI18N
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenu2.add(jMenuItem2);

        jMenuItem3.setAction(actionMap.get("windowCascadeAction")); // NOI18N
        jMenuItem3.setIcon(resourceMap.getIcon("jMenuItem3.icon")); // NOI18N
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenu2.add(jMenuItem3);

        menuBar.add(jMenu2);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setIcon(resourceMap.getIcon("aboutMenuItem.icon")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void actionCreateNetProcess() {
		JFrame mainFrame = ProcessLogGeneratorApp.getApplication().getMainFrame();
		if (newProcessSetup == null) {
			newProcessSetup = new PLGNewProcessSetup(mainFrame, true);
		}
		newProcessSetup.isCanceled = true;
		newProcessSetup.setLocationRelativeTo(mainFrame);
		newProcessSetup.setVisible(true);

		if (! newProcessSetup.isCanceled) {
			newProcess(newProcessSetup);
		}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

    private JDialog aboutBox;
	private PLGNewProcessSetup newProcessSetup = null;
	private int progress = 1;

	private void newProcess(PLGNewProcessSetup setup) {
		JFrame mainFrame = ProcessLogGeneratorApp.getApplication().getMainFrame();
		PLGWaiting wait = new PLGWaiting(mainFrame, true);
		wait.setLocationRelativeTo(mainPanel);
		wait.setLabel("Generating process...");
		wait.setVisible(true);

		/*int[] parameters = new int[] {
			setup.getAndBranches(),
			setup.getXorBranches(),
			setup.getLoopProbability(),
			setup.getSingleActivityProbability(),
			setup.getSequenceActivitiesProbability(),
			setup.getAndProbability(),
			setup.getXorProbability()
		};
		int deep = setup.getNetworkDeep();*/

		PLGProcessWindow pUi = new PLGProcessWindow();
		PlgProcess p = new PlgProcess("TestProcess");

		p.randomize(setup.getAndBranches(),
			setup.getXorBranches(),
			setup.getLoopProbability(),
			setup.getSingleActivityProbability(),
			setup.getSequenceActivitiesProbability(),
			setup.getAndProbability(),
			setup.getXorProbability(),
			setup.getNetworkDeep());

		pUi.setProcess(p);
		pUi.setTitle("Process " + progress++ + " [max depth: "+ setup.getNetworkDeep() +"]");
		jDesktopPane1.add(pUi);
		pUi.setVisible(true);
		try {
			pUi.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		wait.setVisible(false);
	}

	@Action
	public void windowTileAction() {
		JInternalFrame sub[];
		int n=0, i=0;
		Dimension dSize = new Dimension();

		jDesktopPane1.getSize(dSize);
		sub = jDesktopPane1.getAllFrames();
		n = (int) dSize.height / sub.length;

		for(i=0; i < sub.length; i++)
		{
			sub[i].setSize(dSize.width, n);
			sub[i].setLocation(0, i*n);
		}
	}

	@Action
	public void windowCascadeAction() {
		JInternalFrame sub[];
		int n=0, i=0;
		Dimension dSize=new Dimension();

		jDesktopPane1.getSize(dSize);
		sub = jDesktopPane1.getAllFrames();
		n = (int)(dSize.height/10);

		for(i=0; i < sub.length; i++)
		{
			sub[i].setSize(dSize.width-(int)(dSize.width/10), dSize.height-n);
			sub[i].setLocation(i*n, i*n);
			try {
				sub[i].setSelected(true);
			} catch (java.beans.PropertyVetoException ev) {
				ev.printStackTrace();
			}
		}
	}

	@Action
	public void actionOpenExistingProcess() {
		String filename = File.separator+"dot";
	    JFileChooser fc = new JFileChooser(new File(filename));
		FileFilter ff = new FileNameExtensionFilter("ProcessLogGenerator file", "plg");
		fc.setFileFilter(ff);
	    fc.showOpenDialog(mainPanel);
	    File selFile = fc.getSelectedFile();
	    if (selFile != null) {
			try {
				PLGProcessWindow pUi = new PLGProcessWindow();
				PlgProcess p = PlgProcess.loadProcessFrom(selFile.getAbsolutePath());
				pUi.setProcess(p);
				pUi.setTitle("Process \"" + selFile.getName() +"\"");
				jDesktopPane1.add(pUi);
				pUi.setVisible(true);
				pUi.setSelected(true);
			} catch (IOException ex) {
				Logger.getLogger(PLGMainUI.class.getName()).log(Level.SEVERE, null, ex);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
	}

}
