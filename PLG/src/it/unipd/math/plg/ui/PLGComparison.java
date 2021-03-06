/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PLGComparison.java
 *
 * Created on May 4, 2011, 7:17:48 PM
 */
package it.unipd.math.plg.ui;

import it.processmining.clustering.exceptions.ClusteringException;
import it.processmining.clustering.hierarchical.Cluster;
import it.processmining.clustering.hierarchical.DistanceMatrix;
import it.processmining.clustering.hierarchical.HierarchicalClustering;
import it.processmining.clustering.model.BinaryConstraint;
import it.processmining.clustering.model.process.HeuristicsNetSetRepresentation;
import it.processmining.clustering.ui.DendrogramWidget;
import it.unipd.math.plg.models.PlgProcess;
import it.unipd.math.plg.ui.utils.PLGLogger;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author delas
 */
public class PLGComparison extends javax.swing.JInternalFrame {
	
	PLGMainUI mainUI = null;
	DendrogramWidget dw = null;

    /** Creates new form PLGComparison */
    public PLGComparison(PLGMainUI mainUI) {
		this.mainUI = mainUI;
        initComponents();
		updateAll();
    }
	
	
	private void updateDendrogram(HashSet<HeuristicsNetSetRepresentation> elementsSet) {
		
		PLGLogger.log("Updating dendrogram");
		
		double alpha = (double)jSlider1.getValue() / 100.0;
		
		try {
			DistanceMatrix dm = new DistanceMatrix(elementsSet, alpha);
			Cluster root = HierarchicalClustering.cluster(elementsSet, alpha);
			dw = new DendrogramWidget(dm, root);
			jPanelDendrogram.removeAll();
			jPanelDendrogram.add(dw, BorderLayout.CENTER);
			
		} catch (ClusteringException e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
		}
	}
	
	
	private void updateLists(HashSet<HeuristicsNetSetRepresentation> elementsSet) {
		
		PLGLogger.log("Updating lists");
		
		DefaultListModel listModel = new DefaultListModel();
		for(HeuristicsNetSetRepresentation hnr : elementsSet) {
			listModel.addElement(hnr.getName());
		}

		jList1.setModel(listModel);
		jList2.setModel(listModel);
	}
	
	
	private PlgProcess getProcess(String processName) {
		for (JInternalFrame jif : mainUI.getAllWindow()) {
			if (jif instanceof PLGProcessWindow) {
				PLGProcessWindow p = (PLGProcessWindow)jif;
				if (p.getProcess().getName().equals(processName)) {
					return p.getProcess();
				}
			}
		}
		return null;
	}
	
	
	private void updatePositiveRelations(PlgProcess p1, String p1n, PlgProcess p2, String p2n) {
		
		PLGLogger.log("Updating positive raltions");
		
		jLabel3.setText("<html>Positive relations in <b>" + p1n + "</b> but not in <b>" + p2n + "</b></html>");
		jLabel4.setText("<html>Positive relations in <b>" + p2n + "</b> but not in <b>" + p1n + "</b></html>");
		
		HeuristicsNetSetRepresentation hnsr1 = new HeuristicsNetSetRepresentation(p1n, p1.getHeuristicsNet());
		HeuristicsNetSetRepresentation hnsr2 = new HeuristicsNetSetRepresentation(p2n, p2.getHeuristicsNet());
		
		// relations in p1 not in p2
		jTextArea3.setText("");
		for (BinaryConstraint bc : hnsr1.getFollowedConstraints()) {
			if (!hnsr2.getFollowedConstraints().contains(bc)) {
				jTextArea3.append(bc.toString() + "\n");
			}
		}
		
		// relations in p2 not in p1
		jTextArea4.setText("");
		for (BinaryConstraint bc : hnsr2.getFollowedConstraints()) {
			if (!hnsr1.getFollowedConstraints().contains(bc)) {
				jTextArea4.append(bc.toString() + "\n");
			}
		}
	}
	
	
	private void updateInconsistencies(PlgProcess p1, String p1n, PlgProcess p2, String p2n) {
		
		PLGLogger.log("Updating inconsistencies");
		
		jLabel1.setText("<html>Inconsistencies within " + p1n + "</html>");
		jLabel2.setText("<html>Inconsistencies within " + p2n + "</html>");
		
		// inconsistencies of p1
		jTextArea1.setText("");
		HeuristicsNetSetRepresentation hnsr1 = new HeuristicsNetSetRepresentation(p1n, p1.getHeuristicsNet());
		for (BinaryConstraint bc : hnsr1.getFollowedConstraints()) {
			if (hnsr1.getNotFollowedConstraints().contains(bc)) {
				jTextArea1.setText(bc.toString());
			}
		}
		
		// inconsistencies of p2
		jTextArea2.setText("");
		HeuristicsNetSetRepresentation hnsr2 = new HeuristicsNetSetRepresentation(p2n, p2.getHeuristicsNet());
		for (BinaryConstraint bc : hnsr2.getFollowedConstraints()) {
			if (hnsr2.getNotFollowedConstraints().contains(bc)) {
				jTextArea2.setText(bc.toString());
			}
		}
	}
	
	
	private void updateNegativeRelations(PlgProcess p1, String p1n, PlgProcess p2, String p2n) {
		
		PLGLogger.log("Updating negative raltions");
		
		jLabel5.setText("<html>Negative relations in <b>" + p1n + "</b> but not in <b>" + p2n + "</b></html>");
		jLabel6.setText("<html>Negative relations in <b>" + p2n + "</b> but not in <b>" + p1n + "</b></html>");
		
		HeuristicsNetSetRepresentation hnsr1 = new HeuristicsNetSetRepresentation(p1n, p1.getHeuristicsNet());
		HeuristicsNetSetRepresentation hnsr2 = new HeuristicsNetSetRepresentation(p2n, p2.getHeuristicsNet());
		
		// relations in p1 not in p2
		jTextArea5.setText("");
		for (BinaryConstraint bc : hnsr1.getNotFollowedConstraints()) {
			if (!hnsr2.getNotFollowedConstraints().contains(bc)) {
				jTextArea5.append(bc.toString() + "\n");
			}
		}
		
		// relations in p2 not in p1
		jTextArea6.setText("");
		for (BinaryConstraint bc : hnsr2.getNotFollowedConstraints()) {
			if (!hnsr1.getNotFollowedConstraints().contains(bc)) {
				jTextArea6.append(bc.toString() + "\n");
			}
		}
	}
	
	
	private void updateComparison(String process1, String process2) {
		PlgProcess p1 = getProcess(process1);
		PlgProcess p2 = getProcess(process2);
		
		if (p1 == null || p2 == null) {
			JOptionPane.showMessageDialog(this, "One process is no more available", "Error", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		
		
		// graphs
		jPanel6.removeAll();
		jPanel6.add(p1.getDependencyGraph().getGrappaVisualization(), BorderLayout.CENTER);
		jPanel7.removeAll();
		jPanel7.add(p2.getDependencyGraph().getGrappaVisualization(), BorderLayout.CENTER);
		try {
			jPanel8.removeAll();
			jPanel8.add(p1.getPetriNet().getGrappaVisualization(), BorderLayout.CENTER);
			jPanel9.removeAll();
			jPanel9.add(p2.getPetriNet().getGrappaVisualization(), BorderLayout.CENTER);
		} catch (IOException ex) {
			System.err.println(ex);
		}
		
		// inconsistencies
		updateInconsistencies(p1, process1, p2, process2);
		updatePositiveRelations(p1, process1, p2, process2);
		updateNegativeRelations(p1, process1, p2, process2);
		
	}
	
	
	private void updateAll() {
		HashSet<HeuristicsNetSetRepresentation> elementsSet = new HashSet<HeuristicsNetSetRepresentation>();
		
		for(JInternalFrame jif : mainUI.getAllWindow()) {
			if (jif instanceof PLGProcessWindow) {
				PLGProcessWindow p = (PLGProcessWindow)jif;
				elementsSet.add(new HeuristicsNetSetRepresentation(p.getProcess().getName(), p.getProcess().getHeuristicsNet()));
			}
		}
		
		// now the dendrogram
		updateDendrogram(elementsSet);
		updateLists(elementsSet);
	}
	

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelDendrogram = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel8 = new javax.swing.JLabel();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(it.unipd.math.plg.ui.ProcessLogGeneratorApp.class).getContext().getResourceMap(PLGComparison.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanelDendrogram.setName("jPanelDendrogram"); // NOI18N
        jPanelDendrogram.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab(resourceMap.getString("jPanelDendrogram.TabConstraints.tabTitle"), jPanelDendrogram); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jTabbedPane4.setName("jTabbedPane4"); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane2.setName("jTabbedPane2"); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setLayout(new java.awt.BorderLayout());
        jTabbedPane2.addTab(resourceMap.getString("jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        jPanel8.setName("jPanel8"); // NOI18N
        jPanel8.setLayout(new java.awt.BorderLayout());
        jTabbedPane2.addTab(resourceMap.getString("jPanel8.TabConstraints.tabTitle"), jPanel8); // NOI18N

        jPanel5.add(jTabbedPane2);

        jTabbedPane3.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane3.setName("jTabbedPane3"); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N
        jPanel7.setLayout(new java.awt.BorderLayout());
        jTabbedPane3.addTab(resourceMap.getString("jPanel7.TabConstraints.tabTitle"), jPanel7); // NOI18N

        jPanel9.setName("jPanel9"); // NOI18N
        jPanel9.setLayout(new java.awt.BorderLayout());
        jTabbedPane3.addTab(resourceMap.getString("jPanel9.TabConstraints.tabTitle"), jPanel9); // NOI18N

        jPanel5.add(jTabbedPane3);

        jTabbedPane4.addTab(resourceMap.getString("jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 1, 1, 1));
        jPanel10.setName("jPanel10"); // NOI18N
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jLabel3, gridBagConstraints);

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jLabel4, gridBagConstraints);

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jTextArea3.setColumns(20);
        jTextArea3.setEditable(false);
        jTextArea3.setRows(5);
        jTextArea3.setName("jTextArea3"); // NOI18N
        jScrollPane5.setViewportView(jTextArea3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel10.add(jScrollPane5, gridBagConstraints);

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        jTextArea4.setColumns(20);
        jTextArea4.setEditable(false);
        jTextArea4.setRows(5);
        jTextArea4.setName("jTextArea4"); // NOI18N
        jScrollPane6.setViewportView(jTextArea4);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel10.add(jScrollPane6, gridBagConstraints);

        jTabbedPane4.addTab(resourceMap.getString("jPanel10.TabConstraints.tabTitle"), jPanel10); // NOI18N

        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 1, 1, 1));
        jPanel11.setName("jPanel11"); // NOI18N
        jPanel11.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel11.add(jLabel5, gridBagConstraints);

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel11.add(jLabel6, gridBagConstraints);

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        jTextArea5.setColumns(20);
        jTextArea5.setEditable(false);
        jTextArea5.setRows(5);
        jTextArea5.setName("jTextArea5"); // NOI18N
        jScrollPane7.setViewportView(jTextArea5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel11.add(jScrollPane7, gridBagConstraints);

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        jTextArea6.setColumns(20);
        jTextArea6.setEditable(false);
        jTextArea6.setRows(5);
        jTextArea6.setName("jTextArea6"); // NOI18N
        jScrollPane8.setViewportView(jTextArea6);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel11.add(jScrollPane8, gridBagConstraints);

        jTabbedPane4.addTab(resourceMap.getString("jPanel11.TabConstraints.tabTitle"), jPanel11); // NOI18N

        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 1, 1, 1));
        jPanel12.setName("jPanel12"); // NOI18N
        jPanel12.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel12.add(jLabel1, gridBagConstraints);

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel12.add(jLabel2, gridBagConstraints);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane3.setViewportView(jTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel12.add(jScrollPane3, gridBagConstraints);

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setRows(5);
        jTextArea2.setName("jTextArea2"); // NOI18N
        jScrollPane4.setViewportView(jTextArea2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel12.add(jScrollPane4, gridBagConstraints);

        jTabbedPane4.addTab(resourceMap.getString("jPanel12.TabConstraints.tabTitle"), jPanel12); // NOI18N

        jSplitPane1.setRightComponent(jTabbedPane4);

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setName("jList1"); // NOI18N
        jScrollPane1.setViewportView(jList1);

        jPanel4.add(jScrollPane1);

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList2.setName("jList2"); // NOI18N
        jScrollPane2.setViewportView(jList2);

        jPanel4.add(jScrollPane2);

        jSplitPane1.setTopComponent(jPanel4);

        jPanel3.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jPanel1.add(jTabbedPane1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N
        jPanel2.add(jLabel7);

        jSlider1.setMinorTickSpacing(10);
        jSlider1.setName("jSlider1"); // NOI18N
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jPanel2.add(jSlider1);

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N
        jPanel2.add(jLabel8);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		updateAll();
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		if (jList1.getSelectedIndex() >= 0 && jList2.getSelectedIndex() >= 0) {
			updateComparison((String)jList1.getSelectedValue(), (String)jList2.getSelectedValue());
		} else {
			JOptionPane.showMessageDialog(this, "Please select two processes", "Error", JOptionPane.ERROR_MESSAGE); 
		}
	}//GEN-LAST:event_jButton2ActionPerformed

	private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
		double v = (double)jSlider1.getValue() / 100.0;
		jLabel8.setText(" " + v);
	}//GEN-LAST:event_jSlider1StateChanged

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		
		if (dw != null) {
			String filename = File.separator+"svg";
			JFileChooser fc = new JFileChooser(new File(filename));
			FileFilter ff = new FileNameExtensionFilter("SVG file", "svg");
			fc.setFileFilter(ff);
			fc.showSaveDialog(this);
			File selFile = fc.getSelectedFile();
			if (selFile != null) {
				String saveFilename = selFile.toString();
				String ext = saveFilename.substring(saveFilename.lastIndexOf(".") + 1, saveFilename.length());
				if (!ext.equals("svg")) {
					saveFilename = saveFilename + ".svg";
				}
				dw.getSVG(saveFilename);
				javax.swing.JOptionPane.showMessageDialog(this, "File saved in "+ saveFilename);
			}
		}
	}//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelDendrogram;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    // End of variables declaration//GEN-END:variables
}
