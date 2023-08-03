package net.awewsomegaming.main;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;

public class Util {
	private Main m;

	public Util(Main m) {
		this.m = m;
	}

	public void save() {
		String result = JOptionPane.showInputDialog(null, "Name (" + System.getProperty("user.dir").toString() + "):", "Save as", 1);
		if (result != null)
			try {
				FileOutputStream fo = new FileOutputStream(String.valueOf(System.getProperty("user.dir").toString().replace("\\", "/")) + "/" + result);
				
				for (int i = 0; i < 576; i++) {
					for (int k = 0; k < 1024; k++) {
						Pixel p = m.screen[i][k];
						byte[] data = { (byte) p.color.getRed(), (byte) p.color.getGreen(), (byte) p.color.getBlue(),
								(byte) p.zero_color.getRed(), (byte) p.zero_color.getGreen(),
								(byte) p.zero_color.getBlue(), (byte) p.state };
						fo.write(data);
					}
				}
				
				byte[] color_count = { (byte) (m.colors.size() - 15 >> 0 & 0xFF),
									   (byte) (m.colors.size() - 15 >> 8 & 0xFF),
									   (byte) (m.colors.size() - 15 >> 16 & 0xFF),
									   (byte) (m.colors.size() - 15 >> 24 & 0xFF) };
				fo.write(color_count);
				
				for (int i = 15; i < m.colors.size(); i++) {
					byte[] bytes = { (byte) ((Color) m.colors.get(i)).getRed(),
									 (byte) ((Color) m.colors.get(i)).getGreen(), 
									 (byte) ((Color) m.colors.get(i)).getBlue() };
					fo.write(bytes);
				}
				
				for (int i = 15; i < m.color_names.size(); i++) {
					fo.write(((String) m.color_names.get(i)).getBytes());
					fo.write(0);
				}
				
				byte[] color_pointer = { (byte) (m.color_pointer >> 0 & 0xFF),
										 (byte) (m.color_pointer >> 8 & 0xFF),
										 (byte) (m.color_pointer >> 16 & 0xFF),
										 (byte) (m.color_pointer >> 24 & 0xFF) };
				fo.write(color_pointer);
				
				fo.write((byte) m.current_state);
				fo.write((byte) m.size);
				fo.write((byte) m.update_wait);
				fo.write((byte) m.brush_size);
				fo.write((byte) m.brush_type);
				
				byte[] background_color = { (byte) m.background_color.getRed(),
											(byte) m.background_color.getGreen(),
											(byte) m.background_color.getBlue() };
				fo.write(background_color);
				
				fo.write((byte) ((m.snow_on ? 1 : 0) << 1 | (m.show_text ? 1 : 0)));
				
				fo.flush();
				fo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void load() {
		String result = JOptionPane.showInputDialog(null, "Name (" + System.getProperty("user.dir").toString() + "):","Load file", 1);
		if (result != null)
			try {
				FileInputStream fi = new FileInputStream(String.valueOf(System.getProperty("user.dir").toString().replace("\\", "/")) + "/" + result);
				
				for (int i = 0; i < 576; i++) {
					for (int j = 0; j < 1024; j++) {
						byte[] data = new byte[7];
						fi.read(data, 0, 7);
						m.screen[i][j].color = new Color(data[0] & 0xFF, data[1] & 0xFF, data[2] & 0xFF);
						m.screen[i][j].zero_color = new Color(data[3] & 0xFF, data[4] & 0xFF, data[5] & 0xFF);
						m.screen[i][j].state = data[6] & 0xFF;
					}
				}
				
				byte[] color_count = new byte[4];
				fi.read(color_count, 0, 4);
				int color_count_i = color_count[0] & 0xFF | (color_count[1] & 0xFF) << 8 | (color_count[2] & 0xFF) << 16 | (color_count[3] & 0xFF) << 24;
				
				for (int i = 0; i < color_count_i; i++) {
					byte[] bytes = new byte[3];
					fi.read(bytes, 0, 3);
					m.colors.add(new Color(bytes[0] & 0xFF, bytes[1] & 0xFF, bytes[2] & 0xFF));
				}
				
				for (int i = 0; i < color_count_i; i++) {
					char c;
					String name = "";
					
					do {
						c = (char) (fi.read() & 0xFF);
						if (c != 0x00)
							name = String.valueOf(name) + c;
					} while (c != 0x00);
					
					m.color_names.add(name);
				}
				
				byte[] color_pointer = new byte[4];
				fi.read(color_pointer, 0, 4);
				m.color_pointer = color_pointer[0] & 0xFF | (color_pointer[1] & 0xFF) << 8 | (color_pointer[2] & 0xFF) << 16 | (color_pointer[3] & 0xFF) << 24;
				
				m.current_state = fi.read();
				m.size = fi.read();
				m.update_wait = fi.read();
				m.brush_size = fi.read();
				m.brush_type = fi.read();
				
				byte[] background_color = new byte[3];
				fi.read(background_color, 0, 3);
				m.background_color = new Color(background_color[0] & 0xFF, background_color[1] & 0xFF, background_color[2] & 0xFF);
				
				int status = fi.read();
				m.show_text = ((status & 0x1) == 1);
				m.snow_on = ((status >> 1 & 0x1) == 1);
				
				fi.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void clrscr() {
		for (int y = 0; y < m.screen.length; y++) {
			for (int x = 0; x < (m.screen[y]).length; x++) {
				m.screen[y][x].color = Color.BLACK;
				m.screen[y][x].zero_color = m.background_color;
				m.screen[y][x].state = 0;
			}
		}
	}

	public void add_color(String name, Color c) {
		m.colors.add(c);
		m.color_names.add(name);
	}
}
