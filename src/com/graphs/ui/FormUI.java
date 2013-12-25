package com.graphs.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class FormUI {
	
	public enum Etats {
		OVER, CLICKED, SELECTED, NORMAL, EDITING, GRAYED
	}
	
	protected Color formColor;
	protected Color labelColor;
	protected String label;
	protected Rectangle rect;
	protected boolean isDraggable;
	protected Etats etat;
	protected boolean displayBorders = false;
	protected boolean displayLabel = true;
	
	public abstract void renderForm(Graphics g);
	
	public boolean contains(Point p) {
		return rect.contains(p);
	}
	
	public Rectangle getRectange() {
		return rect;
	}
	
	public boolean isDraggable() {
		return isDraggable;
	}
	
	public boolean displayLabel() {
		return this.displayLabel;
	}
	
	public void setDisplayLabel(boolean display) {
		this.displayLabel = display;
	}
	
	public Etats getState() {
		return this.etat;
	}
	
	public abstract void drag(Point p);
	
	public abstract boolean isOverForm(Point p);
	
	public abstract void setState(Etats etat);
	
	public abstract boolean editForm();
	
	public abstract void refreshProperties();
	
}
