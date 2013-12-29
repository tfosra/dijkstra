package com.graphs.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import com.graphs.ui.FormUI.Etats;

public class GraphUISelection {

	private LinkedList<FormUI> forms;
	
	private Point originPoint;
	
	private Rectangle bounds;
	
	private boolean dragging;
	
	public GraphUISelection() {
		forms = new LinkedList<>();
		originPoint = new Point();
		bounds = new Rectangle();
	}
	
	public void updateSelection(FormUI f) {
		if (containsForm(f)) removeForm(f);
		else addForm(f);
	}
	
	public void addForm(FormUI f) {
		if (f == null) return;
		forms.add(f);
		f.setState(Etats.SELECTED);
	}
	
	public void removeForm(FormUI f) {
		if (f == null) return;
		forms.remove(f);
		f.setState(Etats.NORMAL);
	}
	
	public List<FormUI> getSelection() {
		return forms;
	}
	
	public FormUI getFirstSelected() {
		return forms.peekFirst();
	}
	
	public void setSelection(FormUI f) {
		clearSelection();
		addForm(f);
	}
	
	public void setSelection(List<FormUI> forms) {
		clearSelection();
		this.forms.addAll(forms);
		for (FormUI node : this.forms) {
			node.setState(Etats.SELECTED);
		}
	}
	
	public void clearSelection() {
		for (FormUI node : this.forms) {
			node.setState(Etats.NORMAL);
		}
		forms.clear();
	}
	
	public void setOrigin(Point origin) {
		this.originPoint = origin;
	}
	
	public Point getOrigin() {
		return this.originPoint;
	}
	
	public void beginDrag() {
		dragging = true;
		for (FormUI f : forms) {
			f.setState(Etats.CLICKED);
		}
	}
	
	public void endDrag() {
		for (FormUI f : forms) {
			f.setState(Etats.SELECTED);
		}
		dragging = false;
	}
	
	public boolean containsForm(FormUI f) {
		return forms.contains(f);
	}
	
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}
	
	public void dragTo(Point p) {
		if (isEmpty()) return;
		int tx = p.x - originPoint.x;
		int ty = p.y - originPoint.y;
		translate(tx, ty);
		originPoint = p;
	}
	
	public void translate(int tx, int ty) {
		if (isEmpty()) return;
		for (FormUI node : forms) {
			if (node instanceof LinkUI) continue;
			((NodeUI)node).translate(tx, ty);
		}
		this.bounds.translate(tx, ty);
	}
	
	public boolean isDragging() {
		return this.dragging;
	}
	
	public boolean isEmpty() {
		return forms.isEmpty();
	}

}
