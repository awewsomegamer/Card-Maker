package net.awewsomegaming.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Canvas implements Runnable {
	private Thread program;
	private Window window;
	private Main m;

	public static final int width = 1024;
	public static final int height = 576;
	public static final int scr_height = 560;
	public static boolean running = false;
	public static final double frame_rate = 1.0/60.0;
	
	// Runtime variables
	public int update_wait = 5;
	public int size = 4;
	public int brush_size = 1;
	
	public String[] brush_names = new String[] { "Pencil", "Eraser", "Bucket" };
	public int brush_type = 0;

	public Color background_color = Color.BLACK;

	public Pixel[][] screen = new Pixel[576][1024];

	public ArrayList<Color> colors;
	public ArrayList<String> color_names;
	public int color_pointer = 0;

	public String[] state_names = new String[] { "Empty", "Falling", "Solid", "Exploding" };
	public int current_state = 0;
	
	public boolean mouse_pressed = false;
	public boolean show_text = true;
	public boolean snow_on = true;

	// Useful classes
	public transient Util util;
	private transient KeyboardHandler kbh;
	private transient MouseHandler mh;

	public Main() {
		window = new Window(1024, 576, "Card Maker V2", this);
		util = new Util(this);
		kbh = new KeyboardHandler(this);
		mh = new MouseHandler(this);
		
		for (int y = 0; y < 576; y++) {
			for (int x = 0; x < 1024; x++)
				screen[y][x] = new Pixel(Color.BLACK, 0, x, y, this);
		}
		
		colors = new ArrayList<Color>();
		color_names = new ArrayList<String>();
		
		colors.add(Color.BLACK);			color_names.add("Black");
		colors.add(Color.BLUE);				color_names.add("Blue");
		colors.add(Color.CYAN);				color_names.add("Cyan");
		colors.add(Color.DARK_GRAY);		color_names.add("Dark Gray");
		colors.add(Color.GRAY);				color_names.add("Gray");
		colors.add(Color.GREEN);			color_names.add("Green");
		colors.add(Color.LIGHT_GRAY);		color_names.add("Light Gray");
		colors.add(Color.MAGENTA);			color_names.add("Magenta");
		colors.add(Color.ORANGE);			color_names.add("Orange");
		colors.add(Color.PINK);				color_names.add("Pink");
		colors.add(Color.RED);				color_names.add("Red");
		colors.add(Color.WHITE);			color_names.add("White");
		colors.add(Color.YELLOW);			color_names.add("Yellow");
		colors.add(new Color(92, 52, 1));	color_names.add("Brown");
		colors.add(new Color(1, 94, 3));	color_names.add("Dark Green");
		
		program = new Thread(this, "Card");
		running = true;
		program.start();
	}
	
	@Override
	public void run() {
		m = this;
		
		addMouseListener(mh);
		addKeyListener(kbh);
		requestFocus();
		
		double now = 0.0D;
		double last = System.nanoTime() / 1.0E9D;
		double difference = 0.0D;
		double unprocessed = 0.0D;
		boolean render = false;
		
		while (running) {
			render = false;
			now = System.nanoTime() / 1.0E9D;
			difference = now - last;
			last = now;
			unprocessed += difference;
			
			while (unprocessed >= frame_rate) {
				update();
				render();
				unprocessed -= frame_rate;
			}
			
			if (render) {
				render();
			} else {
				try {
					Thread.sleep(1L);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void update_mouse() {
		if (!mouse_pressed)
			return;
		
		try {
			Point mouse_location = getMousePosition();
			int mx = mouse_location.x / size;
			int my = mouse_location.y / size;
			
			Color fill_color = colors.get(color_pointer);
			int fill_state = current_state;
			
			switch (brush_type) {
			case 1:
				fill_color = background_color;
				fill_state = 0;
			case 0:
				if (brush_size > 1) {
					for (int y = my - (brush_size / 2); y < my + (brush_size / 2); y++) {
						for (int x = mx - (brush_size / 2); x < mx + (brush_size / 2); x++) {
							screen[y][x].color = fill_color;
							screen[y][x].zero_color = background_color;
							screen[y][x].state = fill_state;
						}
					}
					
					break;
				}
				
				screen[my][mx].color = fill_color;
				screen[my][mx].zero_color = background_color;
				screen[my][mx].state = fill_state;
				break;
			case 2:
				Color delimiter;
				ArrayList<Pixel> q = new ArrayList<Pixel>();
				
				mouse_pressed = false;
				
				delimiter = (screen[my][mx]).color;				
				q.add(screen[my][mx]);
				
				while (!q.isEmpty()) {
					Pixel n = q.get(0);
					q.remove(0);
					
					if (n.color.equals(delimiter)) {
						screen[n.getY()][n.getX()].color = colors.get(color_pointer);
						screen[n.getY()][n.getX()].state = current_state;
						
						if (current_state == 0)
							(screen[n.getY()][n.getX()]).zero_color = colors.get(color_pointer);
						
						if (n.getX() + 1 < 1024)
							q.add(screen[n.getY()][n.getX() + 1]);
						
						if (n.getX() - 1 >= 0)
							q.add(screen[n.getY()][n.getX() - 1]);
						
						if (n.getY() + 1 < 576)
							q.add(screen[n.getY() + 1][n.getX()]);
						
						if (n.getY() - 1 >= 0)
							q.add(screen[n.getY() - 1][n.getX()]);
					}
				}
				
				break;
			}
		} catch (Exception exception) {
			// Mouse is probably off the screen
			// No need to print the error
		}
	}

	public void generate_snow() {
		if (!snow_on)
			return;
		
		
		for (int x = 0; x < screen[0].length; x++) {
			Random rand = new Random();
			int r = rand.nextInt(1000000);
			
			if (r > 999000) {
				int c = (new Random()).nextInt(201) + 55;
				screen[0][x].color = new Color(c, c, c);
				screen[0][x].state = 1;
			}
		}
	}

	public void update() {
		update_mouse();
		generate_snow();
		
		new Thread(() -> {
			for (int y = 0; y < 560 / size - 1; y++) {
				for (int x = 0; x < 1024 / size; x++)
					screen[y][x].update(x, y);
				
				try {
					Thread.sleep(update_wait);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1024, 576);
		
		try {
			for (int y = 0; y < screen.length; y++) {
				for (int x = 0; x < (screen[y]).length; x++) {
					g.setColor((screen[y][x]).color);
					g.fillRect(x * size, y * size, size, size);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (show_text) {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, 165, 200);
			g.setColor(Color.WHITE);
			
			int y_coord = 10;
			g.drawString("Color (+, -): " + color_names.get(color_pointer), 10, y_coord);	y_coord += 15;
			g.drawString("State (E,S,F): " + state_names[current_state], 10, y_coord);		y_coord += 15;
			g.drawString("Scale ([, ]): " + size, 10, y_coord);								y_coord += 15;
			g.drawString("Brush size (;, '): " + brush_size, 10, y_coord);					y_coord += 15;
			g.drawString("Brush type (9, 0): " + brush_names[brush_type], 10, y_coord);		y_coord += 15;
			g.drawString("Sim. Speed (., /): " + update_wait, 10, y_coord);					y_coord += 15;
			g.drawString("V to toggle this", 10, y_coord);									y_coord += 15;
			g.drawString("Q save", 10, y_coord);											y_coord += 15;
			g.drawString("L load", 10, y_coord);											y_coord += 15;
			g.drawString("C clear", 10, y_coord);											y_coord += 15;
			g.drawString("1 toggle snow", 10, y_coord);										y_coord += 15;
			g.drawString("2 add new color", 10, y_coord);									y_coord += 15;
			g.drawString("3 change background color", 10, y_coord);							y_coord += 15;
		}

		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Main();
	}
}
