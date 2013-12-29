package com.graphs.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.graphs.model.Node;
import com.graphs.model.interfaces.INode;

public class NodeUI extends FormUI {

	private final static int DEFAULT_RADIUS = 16;
	
	private INode node;
	private Point center;
	private int radius;
	private ImageIcon icone;
	public int x, y;
	private static int currentNumber = 0;
	private final static ImageIcon ICONE_NOEUD_HOVER = new ImageIcon(ImageIcon.class.getResource("/image/new/noeud_jaune32.png"));
	private final static ImageIcon ICONE_NOEUD_NORMAL = new ImageIcon(ImageIcon.class.getResource("/image/new/noeud_vert32.png"));
	private final static ImageIcon ICONE_NOEUD_SELECTIONNE = new ImageIcon(ImageIcon.class.getResource("/image/new/noeud_bleu32.png"));
	
	public NodeUI(Point center) {
		this(center, currentNumber++);
	}
	
	public NodeUI(Point center, int number) {
		this.center = center;
		this.x = center.x;
		this.y = center.y;
		this.radius = DEFAULT_RADIUS;
		isDraggable = true;
		node = new Node(number, false);
		rect = new Rectangle(2 * radius, 2 * radius);
		formColor = Color.black;
		labelColor = Color.magenta;
		label = node.getNumber() + "";
		node.setProperty("name", label);
		node.setProperty("h", -1L);
		icone = ICONE_NOEUD_NORMAL;
		radius = icone.getIconWidth() / 2;
	}
	
	public INode getNode() {
		return node;
	}
	
	public Point getCenter() {
		return center;
	}
	
	public int getRadius() {
		return this.radius;
	}
	
	@Override
	public void renderForm(Graphics g) {
		rect = new Rectangle(2 * radius, 2 * radius);
		rect.setLocation(center.x - radius, center.y - radius);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2d.setColor(formColor);
//		g2d.fillOval(rect.x, rect.y, 2 * radius, 2 * radius);
		
		g2d.drawImage(icone.getImage(), rect.x, rect.y, null);
		
		if (displayLabel()) {
			// Calcul des coordonnées sur lesquelles placer le texte
			FontMetrics fm = g2d.getFontMetrics();
			int hd = fm.getHeight();
			int wd = fm.stringWidth(label);
			g2d.setColor(labelColor);
			g2d.setFont(new Font("Arial", Font.BOLD, 12));
			g2d.drawString(label, center.x - wd / 2, center.y + hd / 2 - fm.getDescent());
		}
//		if (displayBorders) {
//			g2d.setColor(Color.blue);
//			g2d.fillRect(rect.getLocation().x - 1, rect.getLocation().y - 1, 2, 2);
//			g2d.fillRect(rect.getLocation().x + rect.width - 1, rect.getLocation().y - 1, 2, 2);
//			g2d.fillRect(rect.getLocation().x - 1, rect.getLocation().y + rect.height - 1, 2, 2);
//			g2d.fillRect(rect.getLocation().x + rect.width - 1, rect.getLocation().y  + rect.height - 1, 2, 2);
//		}
	}

	@Override
	public boolean isOverForm(Point p) {
		return center.distance(p) <= radius;
	}

	@Override
	public void setState(Etats etat) {
		displayBorders = false;
		switch (etat) {
		case OVER:
			if (this.etat != Etats.SELECTED) {
//				formColor = Color.red;
				labelColor = Color.blue;
				icone = ICONE_NOEUD_HOVER;
			}
			break;
		case CLICKED:
//			formColor = Color.blue;
			icone = ICONE_NOEUD_SELECTIONNE;
			radius = icone.getIconWidth() / 2;
			labelColor = Color.blue;
			displayBorders = true;
			break;
		case SELECTED:
			this.etat = etat;
//			formColor = Color.pink;
			icone = ICONE_NOEUD_SELECTIONNE;
			radius = icone.getIconWidth() / 2;
			break;
		case EDITING:
			editForm();
			break;
		case NORMAL:
			this.etat = etat;
//			formColor = Color.black;
			icone = ICONE_NOEUD_NORMAL;
			radius = icone.getIconWidth() / 2;
			labelColor = Color.black;
			break;
		default:
			break;
		}
	}

	@Override
	public boolean editForm() {
		// TODO Auto-generated method stub
		JLabel Number, Name, Distance;
		JTextField jtfnumber,jtfname,jtfdistance;
		JCheckBox Nfinal;
		JPanel panel,panNumero,panName,panDistance;
	
		Number = new JLabel("Number");
		Name = new JLabel("Name");
		Distance = new JLabel("Distance");

		jtfnumber = new JTextField(node.getNumber()+"");
		jtfname = new JTextField(String.valueOf(node.getProperty("name")));
		jtfdistance = new JTextField(String.valueOf(node.getProperty("h")));
		Nfinal = new JCheckBox("Noeud final");
		Nfinal.setSelected(node.isEndNode());

		jtfnumber.setEditable(false);

		panNumero = new JPanel();
		panDistance = new JPanel();
		panName = new JPanel();
		panel = new JPanel(new FlowLayout(2));

		jtfnumber.setPreferredSize(new Dimension(100, 25));
		panNumero.setBorder(BorderFactory.createTitledBorder("Node number"));
		panNumero.add(Number);
		panNumero.add(jtfnumber);

		jtfname.setPreferredSize(new Dimension(100, 25));
		panName.setBorder(BorderFactory.createTitledBorder("Node name"));
		panName.add(Name);
		panName.add(jtfname);

		jtfdistance.setPreferredSize(new Dimension(100, 25));
		panDistance.setBorder(BorderFactory
				.createTitledBorder("Distance p/r final"));
		panDistance.add(Distance);
		panDistance.add(jtfdistance);

		panel.add(panNumero);
		panel.add(panName);
		panel.add(panDistance);
		panel.add(Nfinal);

		int option = JOptionPane.showConfirmDialog(null, panel, "Editing parameters", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.CANCEL_OPTION) return false;
		if (option == JOptionPane.OK_OPTION) {
			try {
				String name = jtfname.getText();
				Long distance = Long.valueOf(jtfdistance.getText());
				node.setProperty("name", name);
				node.setProperty("h", distance);
				label = name;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
				return false;
			}
		}
		
		return true;
	}

	public void moveTo(Point p) {
		center = p;
		this.x = center.x;
		this.y = center.y;
	}
	
	public void translate(int tx, int ty) {
		center.translate(tx, ty);
		this.x = center.x;
		this.y = center.y;
	}
	
	@Override
	public void refreshProperties() {
		label = (String) node.getProperty("name");
	}
	
	@Override
	public String toString() {
		return "Node " + node.getNumber();
	}

}
