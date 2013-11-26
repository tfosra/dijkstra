package com.graphs.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.graphs.model.Node;
import com.graphs.model.interfaces.INode;

public class NodeUI extends FormUI {

	private INode node;
	private Point center;
	private int radius = 20;
	public int x, y;
	private static int currentNumber = 0;
	
	public NodeUI(Point center) {
		this.center = center;
		x = center.x;
		y = center.y;
		isDraggable = true;
		node = new Node(currentNumber++, false);
		rect = new Rectangle(2 * radius, 2 * radius);
		formColor = Color.black;
		labelColor = Color.magenta;
		label = node.getNumber() + "";
		node.setProperty("name", label);
		node.setProperty("h", -1L);
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
		g2d.setColor(formColor);
		g2d.fillOval(rect.x, rect.y, 2 * radius, 2 * radius);
		g2d.setColor(labelColor);
		// Calcul des coordonnées sur lesquelles placer le texte
		FontMetrics fm = g2d.getFontMetrics();
		int hd = fm.getHeight();
		int wd = fm.stringWidth(label);
		g.drawString(label, center.x - wd / 2, center.y + hd / 2 - fm.getDescent());
	}

	@Override
	public boolean isOverForm(Point p) {
		double dist = Math.sqrt(Math.pow((p.x - center.x), 2) + Math.pow((p.y - center.y), 2));
		return dist <= radius;
	}

	@Override
	public void setState(Etats etat) {
		switch (etat) {
		case OVER:
			if (this.etat != Etats.SELECTED) {
				formColor = Color.red;
				labelColor = Color.orange;
			}			
			break;
		case CLICKED:
			formColor = Color.blue;
			labelColor = Color.orange;
			break;
		case SELECTED:
			this.etat = etat;
			formColor = Color.pink;
			break;
		case EDITING:
			editForm();
			break;
		case NORMAL:
			this.etat = etat;
			formColor = Color.black;
			labelColor = Color.magenta;
			break;
		default:
			break;
		}
	}

	@Override
	public void editForm() {
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
		if (option == JOptionPane.CANCEL_OPTION) return;
		if (option == JOptionPane.OK_OPTION) {
			try {
				String name = jtfname.getText();
				Long distance = Long.valueOf(jtfdistance.getText());
				node.setProperty("name", name);
				node.setProperty("h", distance);
				label = name;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}

	}

	@Override
	public void drag(Point p) {
		center = p;
		this.x = center.x;
		this.y = center.y;
	}
	
	@Override
	public String toString() {
		return "Node " + node.getNumber();
	}

}
