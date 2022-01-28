package net.awewsomegaming.ChristmasCard2021.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JOptionPane;

public class Main extends Canvas implements Runnable {
	// Create a cellular automata program that will make it rain snow (blocky snow) around a christmas tree
	// Once the entire screen is filled with snow the user can draw a snowman, then it will snow without filling up the screen
	// The text Merry christmas will be displayed at the top
	
	private Thread card;
	private Window window;
	private Main m;
	
	public int draws = 0;
	
	public static boolean running = false;
	public static double frame_rate = 1.0/60.0;
	
	private final int width = 1024;
	private final int height = width/16 * 9;
	
	private int update_wait = 5;
	private int size = 4;
	private int screen_size = 4;
	private int brush_size = 1;

	private boolean updating_screen_size = false;
	public Pixel[][] screen = new Pixel[(int)Math.round(33.5*screen_size)][64*screen_size];
	public ArrayList<ArrayList<Pixel>> screen_copy = new ArrayList<ArrayList<Pixel>>();
	public HashMap<Point, Color> background = new HashMap<Point, Color>();
	
	private final Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW, new Color(92,52,1), new Color(1,94,3)};
	private final String[] color_names = {"Black", "Blue", "Cyan", "Dark Gray", "Gray", "Green", "Light Gray", "Magenta", "Orange", "Pink", "Red", "White", "Yellow", "Brown", "Dark Green"};
	private int color_pointer = 0;
	private int current_state = 0;
	
	private boolean mouse_pressed = false;
	private boolean show_text = true;
	private boolean snow_on = true;
	
	public Main() {
		window = new Window(width,height,"Card Maker", this);
		
		card = new Thread(this, "Card");
		running = true;
		card.start();
	}
	
	@Override
	public void run() {
		m = this;
		
		for (int y = 0; y < screen.length; y++) {
			for (int x = 0; x < screen[y].length; x++) {
				screen[y][x] = new Pixel(Color.BLACK, 0, this);
			}
		}
		
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				mouse_pressed = false;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				mouse_pressed = true;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_EQUALS:
					if (color_pointer < colors.length-1)
						color_pointer++;
					
					break;
				case KeyEvent.VK_MINUS:
					if (color_pointer > 0)
						color_pointer--;
					
					break;
				case KeyEvent.VK_W:
					current_state = 3;
					
					break;
				case KeyEvent.VK_S:
					current_state = 2;
					
					break;
				case KeyEvent.VK_F:
					current_state = 1;
					
					break;
				case KeyEvent.VK_E:
					current_state = 0;
					
					break;
				case KeyEvent.VK_V:
					show_text = !show_text;
					
					break;
				case KeyEvent.VK_Q:
					save();
					
					break;
				case KeyEvent.VK_L:
					load();
					
					break;
				case KeyEvent.VK_1:
					snow_on = !snow_on;
					
					break;
				case KeyEvent.VK_C:
					save();
					
					background.clear();
					for (int y = 0; y < screen.length; y++) {
						for (int x = 0; x < screen[y].length; x++) {
							screen[y][x] = new Pixel(Color.BLACK, 0, m);
						}
					}
					
					break;
				case 91:
					if (screen_size > 2) {
						screen_size /= 2;
						
						for (int y = 0; y < screen.length; y++) {
							ArrayList<Pixel> row;
							
							if (screen_copy.size() > y+1 && screen_copy.get(y).size() >= screen[y].length)
								row = screen_copy.get(y);
							else
								row = new ArrayList<Pixel>();
							
							for (int x = 0; x < screen[y].length; x++) {
								if (screen_copy.size() > y+1 && screen_copy.get(y).size() >= screen[y].length)
									row.set(x, screen[y][x]);
								else
									row.add(screen[y][x]);
							}
							
							if (screen_copy.size() >= screen.length)
								screen_copy.set(y, row);
							else
								screen_copy.add(row);
						}
						
						screen = new Pixel[(int)Math.round(33.5*screen_size)][64*screen_size];
						
						for (int y = 0; y < screen.length; y++) {
							for (int x = 0; x < screen[y].length; x++) {
								screen[y][x] = new Pixel(Color.BLACK, 0, m);

								if (background.get(new Point(x,y)) != null)
									screen[y][x] = new Pixel(background.get(new Point(x,y)), 0, m);
							}
						}
						
						for (int y = 0; y < screen.length; y++) {
							for (int x = 0; x < screen[y].length; x++) {
								screen[y][x] = screen_copy.get(y).get(x);
							}
						}
					}
					
					break;
				case 93:
					if (screen_size < 16) {
						System.out.println(screen_copy.size());
						
						screen_size *= 2;
						
						updating_screen_size = true;
						
						screen = new Pixel[(int)Math.round(33.5*screen_size)][64*screen_size];
						
						for (int y = 0; y < screen.length; y++) {
							for (int x = 0; x < screen[y].length; x++) {
								screen[y][x] = new Pixel(Color.BLACK, 0, m);

								if (background.get(new Point(x,y)) != null)
									screen[y][x] = new Pixel(background.get(new Point(x,y)), 0, m);
							}
						}
						
						for (int y = 0; y < (screen.length > screen_copy.size() ? screen_copy.size() : screen.length); y++) {
							for (int x = 0; x < (screen[y].length > screen_copy.get(y).size() ? screen_copy.get(y).size() : screen[y].length); x++) {
								screen[y][x] = screen_copy.get(y).get(x);
							}
						}
						
						updating_screen_size = false;
					}
					break;
				case KeyEvent.VK_SEMICOLON:
					if (brush_size > 1)
						brush_size--;
					
					break;
				case KeyEvent.VK_QUOTE:
					brush_size++;
					
					break;
				}
			}
		});
		
		this.requestFocus();
		
		double now = 0;
		double last = System.nanoTime()/1000000000.0;
		double difference = 0;
		double unprocessed = 0;
		boolean render = false;
		
		while (running) {
			draws = 0;
			
			render = false;
			
			now = System.nanoTime()/1000000000.0;
			difference = now - last;
			last = now;
			unprocessed += difference;
			
			while (unprocessed >= frame_rate) {
				if (!updating_screen_size) {
					draws++;
					
					update();
					render();
				}
				
				unprocessed -= frame_rate;
			}
			
			if (render) {
				if (!updating_screen_size)
					render();
			}else {
				try {
					Thread.sleep(1);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		try {
			for (int y = 0; y < screen.length; y++) {
				for (int x = 0; x < screen[y].length; x++){
					g.setColor(screen[y][x].color);
					
					g.fillRect(x*size,y*size,size,size);
				}
			}
		}catch(Exception e) {
			
		}
		
		if (show_text) {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, 150, 140);
			
			String state = "";
			
			switch (current_state){
			case 0:
				state = "Empty";
				break;
			case 1:
				state = "Falling";
				break;
			case 2:
				state = "Solid";
				break;
			case 3:
				state = "Exploding";
				break;
			}
			
			g.setColor(Color.WHITE);
			g.drawString("Color (+, -): "+color_names[color_pointer].toString(), 10, 10);
			g.drawString("State (E,S,F): "+state, 10, 25);
			g.drawString("Scale ([, ]): "+size, 10, 40);
			g.drawString("Brush size (;, '): "+brush_size, 10, 55);
			g.drawString("V to toggle this", 10, 70);
			g.drawString("Q save", 10, 85);
			g.drawString("L load", 10, 100);
			g.drawString("1 toggle snow", 10, 115);
			g.drawString("C clear", 10, 130);
		}
		
		g.dispose();
		bs.show();
	}
	
	public void update() {
		if (screen_size == 16) size = 1;
		if (screen_size == 8) size = 2;
		if (screen_size == 4) size = 4;
		
		try {
			Point mouse_location = this.getMousePosition();
			
			int mx = mouse_location.x/size;
			int my = mouse_location.y/size;
			
			if (mouse_pressed) {
				if (brush_size > 1) {
					for (int y = my-(brush_size/2); y < my; y++) {
						for (int x = mx-(brush_size/2); x < mx; x++) {
							if (current_state == 0)
								background.put(new Point(x, y), colors[color_pointer]);

							screen[y][x].color = colors[color_pointer];
							screen[y][x].state = current_state;
						}
					}
				}else {
					if (current_state == 0)
						background.put(new Point(mx, my), colors[color_pointer]);
					
					screen[my][mx].color = colors[color_pointer];
					screen[my][mx].state = current_state;
				}
			}
		}catch(Exception e) {
			// Mouse out of bounds
		}
		
		if (snow_on) {
			Random rand = new Random();

			for (int x = 0; x < screen[0].length; x++) {
				int r = rand.nextInt(1000000);
				if (r > 999000) {
					int c = new Random().nextInt(201)+55;
					screen[0][x].color = new Color(c,c,c);
					screen[0][x].state = 1;
				}
			}
		}
		
		new Thread(()->{
			try {
				for (int y = 0; y < screen.length; y++) {
					for (int x = 0; x < screen[y].length; x++) {
						screen[y][x].update(x, y);
					}
					Thread.sleep(update_wait);
				}
			}catch (Exception e) {
			
			}
		}).start();
	}
	
	private void save() {
		String result = JOptionPane.showInputDialog(window, "Name ("+System.getProperty("user.dir").toString()+"):", "Save as", JOptionPane.INFORMATION_MESSAGE);
		
		if (result != null) {
			File file = new File((System.getProperty("user.dir").toString().replace("\\", "/"))+"/"+result);

			try {
				FileWriter writer = new FileWriter(file);
				
				writer.append("I:"+size+","+screen_size+","+snow_on+","+show_text+","+screen.length+","+screen[0].length+","+"\n");
				
				for (int y = 0; y < screen.length; y++) {
					for (int x = 0; x < screen[y].length; x++) {
						if (screen.length > screen_copy.size()) {
							writer.append(screen[y][x].toString()+" ");
						}else {
							writer.append(screen_copy.get(y).get(x).toString()+" ");
						}
					}
					writer.append("\n");
				}
				
				writer.flush();
				writer.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void load() {
		String result = JOptionPane.showInputDialog(window, "Name ("+System.getProperty("user.dir").toString()+"):", "Load from", JOptionPane.INFORMATION_MESSAGE);
		
		if (result != null) {
			try {
				File file = new File((System.getProperty("user.dir").toString().replace("\\", "/"))+"/"+result);
				
				BufferedReader reader = new BufferedReader(new FileReader(file));
				
				int y = 0;
				String line;
				
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("I:")) {
						String[] data = line.split("I:")[1].split(",");
						
						size = Integer.valueOf(data[0]);
						screen_size = Integer.valueOf(data[1]);
						snow_on = Boolean.valueOf(data[2]);
						show_text = Boolean.valueOf(data[3]);
						screen = new Pixel[Integer.valueOf(data[4])][Integer.valueOf(data[5])];
						
						for (int i = 0; i < screen.length; i++) {
							for (int j = 0; j < screen[i].length; j++) {
								screen[i][j] = new Pixel(Color.BLACK, 0, this);
							}
						}
					}else {
						String[] data = line.split(" ");
						
						ArrayList<Pixel> row = new ArrayList<Pixel>();
						
						for (int x = 0; x < data.length; x++) {
							String[] data_data = data[x].split(",");
							
							Color color = new Color(Integer.valueOf(data_data[0]),Integer.valueOf(data_data[1]),Integer.valueOf(data_data[2])); 
							int state = Integer.valueOf(data_data[3]);
							
							if (x < screen[0].length) {
								screen[y][x].color = color; 
								screen[y][x].state = state;
								
								if (screen[y][x].state == 0)
									background.put(new Point(x,y), screen[y][x].color);
							}
							
							row.add(new Pixel(color, state, this));
						}
						
						y++;
					}
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		new Main();
	}
}
