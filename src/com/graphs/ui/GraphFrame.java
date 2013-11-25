package com.graphs.ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.graphs.ui.PanneauUI.DrawingState;

public class GraphFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static ImageIcon NODE_BUTTON_ICON = new ImageIcon(new ImageIcon("image/original/noeud_vert.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	private final static ImageIcon LINK_BUTTON_ICON = new ImageIcon(new ImageIcon("image/link.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	private final static ImageIcon DELETE_BUTTON_ICON = new ImageIcon(new ImageIcon("image/supprimer.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	private final static ImageIcon NORMAL_BUTTON_ICON = new ImageIcon(new ImageIcon("image/default.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	
	private PanneauUI workspace;
	private JPanel contentPane;
	private JToolBar toolbar;
	
	public GraphFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		initComponents();
		initToolbar();
		pack();
	}

	private void initComponents() {
		workspace = new PanneauUI();
		contentPane = new JPanel(new BorderLayout());
		contentPane.add(workspace, "Center");
		setContentPane(contentPane);
	}
	
	private void initToolbar() {
		toolbar = new JToolBar("Palette", JToolBar.VERTICAL);
		JButton nodeButton = new JButton(NODE_BUTTON_ICON);
		JButton linkButton = new JButton(LINK_BUTTON_ICON);
		JButton deleteButton = new JButton(DELETE_BUTTON_ICON);
		JButton normalButton = new JButton(NORMAL_BUTTON_ICON);
		nodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workspace.setState(DrawingState.INSERT_NODE);
			}
		});
		linkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workspace.setState(DrawingState.INSERT_LINK);
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workspace.setState(DrawingState.DELETE_ELEMENT);
			}
		});
		normalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workspace.setState(DrawingState.NORMAL);
			}
		});
		toolbar.add(nodeButton);
		toolbar.add(linkButton);
		toolbar.add(deleteButton);
		toolbar.add(normalButton);
		contentPane.add(toolbar, "West");
		toolbar.setCursor(PanneauUI.NORMAL_CURSOR);
	}
	
	public static void main(String[] args) {
		new GraphFrame().setVisible(true);
	}
	
}
