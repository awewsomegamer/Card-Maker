package net.awewsomegaming.ChristmasCard2021.main;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame {
	public Window(int width, int height, String title, Main main) {
		this.setTitle(title);
		this.setSize(new Dimension(width, height));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(main);
		
		this.setVisible(true);
	}
}
