package net.awewsomegaming.main;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame {
	public Window(int width, int height, String title, Main m) {
		setTitle(title);
		setSize(new Dimension(width, height));
		setResizable(false);
		setLocationRelativeTo((Component) null);
		setDefaultCloseOperation(3);
		add(m);
		setVisible(true);
	}
}
