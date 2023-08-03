package net.awewsomegaming.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
	private Main m;

	public MouseHandler(Main m) {
		this.m = m;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1)
			m.mouse_pressed = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1)
			m.mouse_pressed = true;
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
}
