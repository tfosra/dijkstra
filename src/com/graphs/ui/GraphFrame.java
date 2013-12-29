package com.graphs.ui;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.arsoft.swing.menuCreator.ZMenuBar;
import com.arsoft.swing.menuCreator.ZMenuCreator;
import com.graphs.model.solver.GraphSolver.SolverMethod;
import com.graphs.ui.PanneauUI.DrawingState;

public class GraphFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static ImageIcon NODE_BUTTON_ICON = new ImageIcon(ImageIcon.class.getResource("/image/new/noeud_vert32.png"));
	private final static ImageIcon LINK_BUTTON_ICON = new ImageIcon(ImageIcon.class.getResource("/image/new/link32.png"));
	private final static ImageIcon DELETE_BUTTON_ICON = new ImageIcon(ImageIcon.class.getResource("/image/new/erase32.png"));
	private final static ImageIcon NORMAL_BUTTON_ICON = new ImageIcon(ImageIcon.class.getResource("/image/new/default32.png"));
	private final static ImageIcon CLEAR_BUTTON_ICON = new ImageIcon(ImageIcon.class.getResource("/image/new/clear32.png"));
	
	private PanneauUI workspace;
	private JPanel contentPane;
	private JToolBar paletteToolBar;
	private JToolBar actionToolBar;
	protected JTextField stateField;
	private JScrollPane scroll;
	
	private ZMenuBar menubar;
	
	public GraphFrame() {
		setTitle("Path Solver v1.00");
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
		initComponents();
		initToolbars();
		initMenus();
//		pack();
	}

	private void initComponents() {
		stateField = new JTextField();
		stateField.setEditable(false);
		workspace = new PanneauUI(this, stateField);
		contentPane = new JPanel(new BorderLayout());
		scroll = new JScrollPane(workspace);
		changeSize();
		contentPane.add(scroll, "Center");
		contentPane.add(stateField, "South");
		setContentPane(contentPane);
	}
	
	private void initToolbars() {
		paletteToolBar = new JToolBar("Palette", JToolBar.VERTICAL);
		JButton nodeButton = new JButton(NODE_BUTTON_ICON);
		JButton linkButton = new JButton(LINK_BUTTON_ICON);
		JButton deleteButton = new JButton(DELETE_BUTTON_ICON);
		JButton normalButton = new JButton(NORMAL_BUTTON_ICON);
		JButton clearButton = new JButton(CLEAR_BUTTON_ICON);
		
		nodeButton.setBorder(BorderFactory.createEtchedBorder());
		nodeButton.setBorderPainted(false);
		nodeButton.setToolTipText("Add a node");
		nodeButton.setFocusable(false);
		
		normalButton.setBorder(BorderFactory.createEtchedBorder());
		normalButton.setBorderPainted(false);
		normalButton.setToolTipText("Normal mode");
		normalButton.setFocusable(false);
		
		linkButton.setBorder(BorderFactory.createEtchedBorder());
		linkButton.setBorderPainted(false);
		linkButton.setToolTipText("Add a link");
		linkButton.setFocusable(false);

		deleteButton.setBorder(BorderFactory.createEtchedBorder());
		deleteButton.setBorderPainted(false);
		deleteButton.setToolTipText("Delete a form");
		deleteButton.setFocusable(false);
		
		clearButton.setBorder(BorderFactory.createEtchedBorder());
		clearButton.setBorderPainted(false);
		clearButton.setToolTipText("Clear workspace");
		clearButton.setFocusable(false);
		
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
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Cette opération est irréversible. Continuer ?", "Suppression du graphe", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					workspace.clearWorkspace();
				}
			}
		});
		paletteToolBar.add(normalButton);
		paletteToolBar.add(nodeButton);
		paletteToolBar.add(linkButton);
		paletteToolBar.add(deleteButton);
		paletteToolBar.add(clearButton);
		paletteToolBar.setCursor(PanneauUI.NORMAL_CURSOR);
		
		JButton dijkstraButton = new JButton("Dijkstra");
		JButton kruskalButton = new JButton("Kruskal");
		
		dijkstraButton.setBorder(BorderFactory.createEtchedBorder());
		dijkstraButton.setBorderPainted(false);
		dijkstraButton.setToolTipText("Shortest Path");
		dijkstraButton.setFocusable(false);
		
		kruskalButton.setBorder(BorderFactory.createEtchedBorder());
		kruskalButton.setBorderPainted(false);
		kruskalButton.setToolTipText("Minimum Spanning Tree");
		kruskalButton.setFocusable(false);
		
		dijkstraButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workspace.setState(DrawingState.SOLVER, SolverMethod.A_STAR_METHOD);
			}
		});
		kruskalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workspace.setState(DrawingState.SOLVER, SolverMethod.KRUSKAL_METHOD);
			}
		});
		
		actionToolBar = new JToolBar("Actions", JToolBar.HORIZONTAL);
		actionToolBar.add(workspace.annulerButton);
		actionToolBar.add(workspace.repeterButton);
		
		actionToolBar.addSeparator();
		actionToolBar.add(workspace.linkLabelCheckbox);
		actionToolBar.add(workspace.nodeLabelCheckbox);
		
		actionToolBar.addSeparator();
		actionToolBar.add(dijkstraButton);
		actionToolBar.add(kruskalButton);
		actionToolBar.setCursor(PanneauUI.NORMAL_CURSOR);
		
		contentPane.add(paletteToolBar, "West");
		contentPane.add(actionToolBar, "North");
	}
	
	private void initMenus() {
		ZMenuCreator creator = null;
		try {
			creator = new ZMenuCreator(getClass().getResourceAsStream("/config/menu.txt"));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e);
		}
		menubar = creator.constructMenuBar();
		setJMenuBar(menubar);
	}
	
	public void changeSize() {
		scroll.setSize(workspace.getSize());
		setMaximumSize(workspace.getPreferredSize());
		setSize(workspace.getSize());
		Point centerPoint = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		centerPoint.translate(-getSize().width / 2, -getSize().height / 2);
		setLocation(centerPoint);
	}
	
	public static void main(String[] args) {
		new GraphFrame().setVisible(true);
	}
	
}
