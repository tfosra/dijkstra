package com.graphs.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

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
	}
	
	public void renderForm(Graphics g) {
		rect = new Line2D.Double(originNode.getCenter(), endNode.getCenter()).getBounds();
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Dessin du lien en double épaisseur
		g2d.setColor(formColor);
		g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		g2d.drawLine(originNode.x, originNode.y, endNode.x, endNode.y);
		g2d.setStroke(new BasicStroke());
		
		if (link.isOriented()) {
			// Dessin de la flèche
			Point p0 = new Point();
			Point p1 = new Point();
			Point p2 = new Point();
			
			double length = originNode.getCenter().distance(endNode.getCenter());
			double alpha = (endNode.getRadius() - 2.0) / length;
			Point edge = new Point();
			edge.x = (int)(alpha * (originNode.x - endNode.x) + endNode.x);
			edge.y = (int)(alpha * (originNode.y - endNode.y) + endNode.y);
		
			alpha = 20.0 / originNode.getCenter().distance(edge);
			p0.x = (int)(alpha * (originNode.x - edge.x) + edge.x);
			p0.y = (int)(alpha * (originNode.y - edge.y) + edge.y);
			
			Point p4 = new Point();
			p4.x = (int)((10.0/20.0) * (edge.x - p0.x) + p0.x);
			p4.y = (int)((10.0/20.0) * (edge.y - p0.y) + p0.y);
			
			p1.x = (int)(-p4.y + p0.x + p0.y);
			p1.y = (int)(p4.x + p0.y - p0.x);
			
			p2.x = (int)(p4.y + p0.x - p0.y);
			p2.y = (int)(-p4.x + p0.y + p0.x);
		
			g2d.setColor(formColor);
			GeneralPath gp = new GeneralPath();
			gp.moveTo(edge.x, edge.y);
			gp.lineTo(p1.x, p1.y);
			gp.lineTo(p0.x, p0.y);
			gp.lineTo(p2.x, p2.y);
			gp.closePath();
			g2d.fill(gp);
		}
		
		if (displayLabel()) {
			// Représentation du label
			Point middle = new Point();
			middle.x = rect.width / 2 + rect.x;
			middle.y = rect.height / 2 + rect.y;
			g2d.setColor(labelColor);
			g2d.setFont(new Font("Arial", Font.BOLD, 12));
			g2d.drawString(label, middle.x, middle.y);
		}
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
			labelColor = Color.black;
			break;
		case CLICKED:
			formColor = Color.blue;
			labelColor = Color.orange;
			break;
		case SELECTED:
			formColor = Color.red;
			labelColor = Color.black;
			break;
		case EDITING:
			editForm();
			break;
		case NORMAL:
			formColor = Color.black;
			labelColor = Color.magenta;
			break;
		case GRAYED:
			formColor = Color.gray;
			labelColor = Color.gray;
			break;
		default:
			break;
		}
	}
	
	public String getKey() {
		return link.getStartNode().getNumber() + "#" + link.getEndNode().getNumber();
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
	public boolean editForm() {
//		HashMap<String, Object> properties = link.getProperties();
		JPanel pan = new JPanel(new BorderLayout(5, 0));
		JPanel pan1 = new JPanel(new GridLayout(0, 1));
		JPanel pan2 = new JPanel(new GridLayout(0, 1));
		Long l = (Long) link.getProperty("k");
		if (l == null) l = new Long(0);
		JTextField fieldLength = new JTextField(String.valueOf(l));
		pan1.add(new JLabel("Initial Node"));
		pan1.add(new JLabel("End Node"));
		pan1.add(new JLabel("Length"));
		pan2.add(new JLabel(originNode.getNode().getProperty("name") + ""));
		pan2.add(new JLabel(endNode.getNode().getProperty("name") + ""));
		pan2.add(fieldLength);
		pan.add(pan1, "West");
		pan.add(pan2, "Center");
		
		int option = JOptionPane.showConfirmDialog(null, pan, "Editing parameters", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.CANCEL_OPTION) return false;
		if (option == JOptionPane.OK_OPTION) {
			try {
				long length = Long.valueOf(fieldLength.getText());
				label = length + "";
				link.setProperty("k", length);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void refreshProperties() {
		label = String.valueOf(link.getProperty("k"));
	}

	@Override
	public void drag(Point p) {}
	
	@Override
	public String toString() {
		return "Link from " + originNode.getNode().getNumber() + " to " + endNode.getNode().getNumber() + " : " + link.getProperty("k");
	}
	
}
