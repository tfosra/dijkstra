package com.graphs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.graphs.model.interfaces.ILink;

public class LinkUI extends FormUI {
	
	private NodeUI originNode, endNode;
	private ILink link;
	
	public LinkUI(NodeUI originNode, NodeUI endNode, ILink link) {
		this.originNode = originNode;
		this.endNode = endNode;
		this.label = link.getProperty("k") + "";
		isDraggable = false;
		this.link = link;
		formColor = Color.black;
		labelColor = Color.magenta;
		rect = new Rectangle(Math.abs(endNode.x - originNode.x), Math.abs(endNode.y - originNode.y));
		setState(Etats.EDITING);
	}
	
	public void renderForm(Graphics g) {
		rect = new Line2D.Double(originNode.getCenter(), endNode.getCenter()).getBounds();
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(formColor);
		g2d.drawLine(originNode.x, originNode.y, endNode.x, endNode.y);
		// Dessin de la flèche
		// Paramètres a et b de l'equation de l'arc y = ax + b
		double a = (double)(originNode.y - endNode.y) / (double)(originNode.x - endNode.x);
		
		double length = Point2D.distance(originNode.x, originNode.y, endNode.x, endNode.y);
		double alpha = endNode.getRadius() / length;
		Point edge = new Point();
		edge.x = (int)(alpha * (originNode.x - endNode.x) + endNode.x);
		edge.y = (int)(alpha * (originNode.y - endNode.y) + endNode.y);
		Point p0 = new Point();
		Point p1 = new Point();
		Point p2 = new Point();
		
		alpha = 20.0 / length;
		p0.x = (int)(alpha * (originNode.x - edge.x) + edge.x);
		p0.y = (int)(alpha * (originNode.y - edge.y) + edge.y);
		double c = a * p0.y + p0.x;
		
		alpha = 1 + a * a;
		double beta = p0.y + a * (c - p0.x);
		double gamma = Math.pow(c - p0.x, 2) + p0.y * p0.y - 25;
		double delta = beta * beta - alpha * gamma;
		p1.y = (int)((beta - Math.sqrt(delta)) / alpha);
		p1.x = (int)(c - a * p1.y);
		p2.y = (int)((beta + Math.sqrt(delta)) / alpha);
		p2.x = (int)(c - a * p2.y);
		
		if (Math.abs(originNode.x - endNode.x) < 25) {
			p1.x = p0.x - 5;
			p1.y = p0.y;
			p2.x = p0.x + 5;
			p2.y = p0.y;
		}
		
		g2d.setColor(formColor);
		g2d.fillPolygon(new int[]{edge.x, p1.x, p0.x, p2.x}, new int[]{edge.y, p1.y, p0.y, p2.y}, 4);
		
		Point middle = new Point();
		middle.x = rect.width / 2 + rect.x;
		middle.y = rect.height / 2 + rect.y;
		g2d.setColor(labelColor);
		g2d.drawString(label, middle.x, middle.y);
	}
	
	@Override
	public boolean isOverForm(Point p) {
		if (!contains(p)) return false;
		double coef1, coef2;
		coef1 = (double) (endNode.x - originNode.x) / (double) (p.x - originNode.x);
		coef2 = (double) (endNode.y - originNode.y) / (double) (p.y - originNode.y);
		double erreur = Math.abs(coef1 - coef2);
		return erreur < 0.1;
	}

	@Override
	public void setState(Etats etat) {
		switch (etat) {
		case OVER:
			formColor = Color.red;
			labelColor = Color.orange;
			break;
		case CLICKED:
			formColor = Color.blue;
			labelColor = Color.orange;
			break;
		case SELECTED:
			break;
		case EDITING:
			editForm();
			break;
		case NORMAL:
			formColor = Color.black;
			labelColor = Color.magenta;
			break;
		default:
			break;
		}
	}
	
	public NodeUI getOriginNode() {
		return originNode;
	}
	
	public NodeUI getEndNode() {
		return endNode;
	}
	
	public ILink getLink() {
		return this.link;
	}

	@Override
	public void editForm() {
//		HashMap<String, Object> properties = link.getProperties();
		JPanel pan = new JPanel(new BorderLayout(5, 0));
		JPanel pan1 = new JPanel(new GridLayout(0, 1));
		JPanel pan2 = new JPanel(new GridLayout(0, 1));
		JTextField fieldLength = new JTextField(String.valueOf(link.getProperty("k")));
		pan1.add(new JLabel("Initial Node"));
		pan1.add(new JLabel("End Node"));
		pan1.add(new JLabel("Length"));
		pan2.add(new JLabel(originNode.getNode().getProperty("name") + ""));
		pan2.add(new JLabel(endNode.getNode().getProperty("name") + ""));
		pan2.add(fieldLength);
		pan.add(pan1, "West");
		pan.add(pan2, "Center");
		
		int option = JOptionPane.showConfirmDialog(null, pan, "Editing parameters", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.CANCEL_OPTION) return;
		if (option == JOptionPane.OK_OPTION) {
			try {
				long length = Long.valueOf(fieldLength.getText());
				label = length + "";
				link.setProperty("k", length);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
	}

	@Override
	public void drag(Point p) {}
	
	@Override
	public String toString() {
		return "Link from " + originNode.getNode().getNumber() + " to " + endNode.getNode().getNumber();
	}
	
}
