package net.awewsomegaming.main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

public class KeyboardHandler implements KeyListener {
	private Main m;

	public KeyboardHandler(Main m) {
		this.m = m;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int i;
		Color color;
		String name;
		switch (e.getKeyCode()) {
		case 61:
			if (m.color_pointer < m.colors.size() - 1)
				m.color_pointer++;
			break;
		case 45:
			if (m.color_pointer > 0)
				m.color_pointer--;
			break;
		case 87:
			m.current_state = 3;
			break;
		case 83:
			m.current_state = 2;
			break;
		case 70:
			m.current_state = 1;
			break;
		case 69:
			m.current_state = 0;
			break;
		case 86:
			m.show_text = !m.show_text;
			break;
		case 81:
			m.util.save();
			break;
		case 76:
			m.util.load();
			break;
		case 49:
			m.snow_on = !m.snow_on;
			break;
		case 67:
			m.util.save();
			m.util.clrscr();
			for (i = 0; i < m.colors.size() - 15; i++) {
				m.colors.remove(15);
				m.color_names.remove(15);
			}
			break;
		case 91:
			if (m.size < 16)
				m.size *= 2;
			break;
		case 93:
			if (m.size > 1)
				m.size /= 2;
			break;
		case 59:
			if (m.brush_size > 1)
				m.brush_size--;
			break;
		case 222:
			if (m.brush_size < 255)
				m.brush_size++;
			break;
		case 50:
			color = JColorChooser.showDialog(null, "Add a color", Color.BLACK);
			name = JOptionPane.showInputDialog(null, "Color name", "Color name", 1);
			if (name != null && color != null)
				m.util.add_color(name, color);
			break;
		case 51:
			color = JColorChooser.showDialog(null, "Change background color", Color.BLACK);
			if (color != null)
				m.background_color = color;
			break;
		case 47:
			if (m.update_wait < 255)
				m.update_wait++;
			break;
		case 46:
			if (m.update_wait > 0)
				m.update_wait--;
			break;
		case 57:
			if (m.brush_type > 0)
				m.brush_type--;
			break;
		case 48:
			if (m.brush_type < 2)
				m.brush_type++;
			break;
		}
	}
}
