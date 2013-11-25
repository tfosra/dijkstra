package com.graphs.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.graphs.ui.FormUI.Etats;

public class PanneauUI extends JPanel implements MouseMotionListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum DrawingState {
		INSERT_NODE, INSERT_LINK, DELETE_ELEMENT, NORMAL
	}
	
	private final static Toolkit tk = Toolkit.getDefaultToolkit();
	public final static Cursor DELETE_ELEMENT_CURSOR = tk.createCustomCursor(tk.createImage("image/delete.png"), new Point(16, 16), "Suppression");
	public final static Cursor INSERT_LINK_CURSOR = tk.createCustomCursor(tk.createImage("image/insert-link.png"), new Point(16, 16), "Insertion_lien");
	public final static Cursor INSERT_NODE_CURSOR = tk.createCustomCursor(tk.createImage("image/original/noeud_vert.png"), new Point(16, 16), "Insertion_noeud");
	public final static Cursor NORMAL_CURSOR = tk.createCustomCursor(tk.createImage("image/default.png"), new Point(2, 2), "Normal");
	
	private GraphUI graph;
	private FormUI previousForm;
	private FormUI selectedForm;
	private DrawingState state;
	
	public PanneauUI() {
		setPreferredSize(new Dimension(500, 500));
		initParameters();
		initComponents();
		addMouseListener(this);
		addMouseMotionListener(this);
		setState(DrawingState.NORMAL);
	}
	
	private void initParameters() {
		graph = new GraphUI();
	}

	private void initComponents() {
		this.setLayout(null);
		this.setBackground(Color.white);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		graph.renderGraph(g);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		final FormUI f = graph.getPointedForm(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON3) {
			setState(DrawingState.NORMAL);
			if (f != null) {
				if (previousForm != null) {
					previousForm.setState(Etats.NORMAL);
				}
				previousForm = f;
				JPopupMenu menu = new JPopupMenu();
				JMenuItem deleteItem = new JMenuItem("Delete form");
				JMenuItem editItem = new JMenuItem("Edit form");
				menu.add(deleteItem);
				menu.add(editItem);
				menu.show(this, e.getX(), e.getY());
				menu.setCursor(NORMAL_CURSOR);
				deleteItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						graph.removeForm(f);
					}
				});
				editItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						f.setState(Etats.EDITING);
					}
				});
			}
			else {
				if (previousForm != null) {
					previousForm.setState(Etats.NORMAL);
					previousForm = null;
				}
				JPopupMenu menu = new JPopupMenu();
				JMenuItem saveItem = new JMenuItem("Save graph");
				menu.add(saveItem);
				menu.show(this, e.getX(), e.getY());
				menu.setCursor(NORMAL_CURSOR);
				saveItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveGraph();
					}
				});
			}
		}
		else if (e.getClickCount() == 2) {
			if (previousForm != null) {
				previousForm.setState(Etats.NORMAL);
			}
			previousForm = f;
			if (f != null) f.setState(Etats.EDITING);
		}
		else if (e.getClickCount() == 1) {
			// Retrieve a selected form
			if (f != null) {						// Si une forme est sélectionnée
				if (previousForm != selectedForm) {
					if (previousForm != null) {
						previousForm.setState(Etats.NORMAL);
						previousForm = f;
					}
				}
				if (state == DrawingState.DELETE_ELEMENT) {
					graph.removeForm(f);
					if (previousForm == selectedForm) previousForm = null;
					selectedForm = null;
				}
				else if (state == DrawingState.NORMAL) {
					if (f instanceof NodeUI) { 
						if (selectedForm == null) {
							f.setState(Etats.SELECTED);
						}
						else {
							selectedForm.setState(Etats.NORMAL);
						}
						selectedForm = f;
					}
				}
				else if (state == DrawingState.INSERT_LINK) {
					if (f instanceof NodeUI) {
						if (selectedForm != null && !selectedForm.equals(f)) {
							graph.addLink((NodeUI)selectedForm, (NodeUI)f);
							selectedForm.setState(Etats.NORMAL);
							selectedForm = null;
						} else if (!f.equals(selectedForm)) {
							//selectedForm.setState(Etats.NORMAL);
							selectedForm = f;
							selectedForm.setState(Etats.SELECTED);
						}
					}
					else {
						setState(DrawingState.NORMAL);
					}
				}
			}
			else { // Si on a cliqué dans le vide
				if (previousForm != null) {
					previousForm.setState(Etats.NORMAL);
					previousForm = null;
				}
				if (state == DrawingState.INSERT_NODE) {
					// Ajout d'une nouvelle forme
					NodeUI node = new NodeUI(new Point(e.getX(), e.getY()));
					graph.addNode(node);
					//setState(DrawingState.NORMAL);
				}
				else {
					if (selectedForm != null) selectedForm.setState(Etats.NORMAL);
					selectedForm = null;
					setState(DrawingState.NORMAL);
				}
			}
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		if (previousForm != null) {
			previousForm.setState(Etats.NORMAL);
		}
		previousForm = f;
		if (f != null) {
			f.setState(Etats.CLICKED);
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		if (previousForm != null) {
			previousForm.setState(Etats.NORMAL);
		}
		previousForm = f;
		if (f != null) {
			f.setState(Etats.OVER);
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		if (previousForm != null) {
			previousForm.setState(Etats.NORMAL);
			previousForm = f;
		}
		if (f != null) {
			f.setState(Etats.OVER);
			f.drag(e.getPoint());
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		if (previousForm != null && previousForm != selectedForm) {
			previousForm.setState(Etats.NORMAL);
		}
		previousForm = f;
		if (f != null && f != selectedForm) {
			f.setState(Etats.OVER);
		}
		repaint();
	}
	
	public void setState(DrawingState state) {
		switch (state) {
		case DELETE_ELEMENT:;
			setCursor(DELETE_ELEMENT_CURSOR);
			break;
		case INSERT_LINK:
			setCursor(INSERT_LINK_CURSOR);
			break;
		case INSERT_NODE:
			setCursor(INSERT_NODE_CURSOR);
			break;
		case NORMAL:
			setCursor(NORMAL_CURSOR);
			break;
		}
		this.state = state;
	}
	
	public void saveGraph() {
		BufferedImage bf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bf.createGraphics();
		paintComponent(g);
		try {
			ImageIO.write(bf, "png", new File("output.png"));
			JOptionPane.showMessageDialog(null, "Image registered successfully");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
