package net.awewsomegaming.ChristmasCard2021.main;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Pixel {
	public Color color = Color.BLACK;
	public int state = 0;
	public int direction_x = 0; // 0 -> left, 1 -> straight, 2 -> right
	public int direction_y = 0; // 0 -> up, 1 -> none, 2 -> down
	public int total_draws = 0;
	
	private Main m;
	
	public Pixel(Color color, int state, Main m) {
		this.color = color;
		this.state = state;
		this.m = m;
	}
	
	public void update(int x, int y) {
		total_draws += m.draws;
		
		if (y < m.screen.length-1 && m.screen[y][x].state == 1) {
			boolean go_side = false;
			
			if (m.screen[y+1][x].state == 0) {
				m.screen[y+1][x].color = m.screen[y][x].color;
				m.screen[y+1][x].state = m.screen[y][x].state;
				
				m.screen[y][x].state = 0;
				m.screen[y][x].color = m.background.get(new Point(x,y)) != null ? m.background.get(new Point(x,y)) : Color.BLACK;
			}else {
				go_side = true;
			}
			
			if (go_side) {
				int dir = new Random().nextInt(2);
				
				if (x+1 < m.screen[y].length-1 && y < m.screen.length-1 && m.screen[y+1][x+1].state == 0 && dir == 1) {
					m.screen[y+1][x+1].color = m.screen[y][x].color;
					m.screen[y+1][x+1].state = m.screen[y][x].state;
					
					m.screen[y][x].state = 0;
					m.screen[y][x].color = m.background.get(new Point(x,y)) != null ? m.background.get(new Point(x,y)) : Color.BLACK;
				}else if (x-1 > 0 && y < m.screen.length-1 && m.screen[y+1][x-1].state == 0) {
					m.screen[y+1][x-1].color = m.screen[y][x].color;
					m.screen[y+1][x-1].state = m.screen[y][x].state;
					
					m.screen[y][x].state = 0;
					m.screen[y][x].color = m.background.get(new Point(x,y)) != null ? m.background.get(new Point(x,y)) : Color.BLACK;
				}
			}
		}
		
		if (m.screen[y][x].state == 3 && new Random().nextInt(10) > 6) {
			total_draws = 0;
			
			int c1 = 0;
			int c2 = 0;
			
			for (int y2 = (y == 0 ? 0 : y-1); y2 < (y+2 == m.screen.length ? m.screen.length : y+2); y2++) {
				for (int x2 = (x == 0 ? 0 : x-1); x2 < (x+2 == m.screen[y2].length ? m.screen.length : x+2); x2++) {
					if (!(x2 == x && y2 == y)) {
						m.screen[y2][x2].color = m.screen[y][x].color;
						m.screen[y2][x2].state = 4;
						m.screen[y2][x2].direction_x = c1;
						m.screen[y2][x2].direction_y = c2;
					}
					
					System.out.println(c1+" "+c2);
					
					c1++;
				}
				
				c1 = 0;
				c2++;
			}
		}
		
		if (m.screen[y][x].state == 4) {
			switch(m.screen[y][x].direction_x) {
			case 0:
				if (x-1 > 0 && m.screen[y][x-1].state == 0) {
					m.screen[y][x-1].color = m.screen[y][x].color;
					m.screen[y][x-1].state = m.screen[y][x].state;
					m.screen[y][x-1].direction_x = m.screen[y][x].direction_x;
					m.screen[y][x-1].direction_y = m.screen[y][x].direction_y;
					
					
					m.screen[y][x].state = 0;
					m.screen[y][x].color = m.background.get(new Point(x,y)) != null ? m.background.get(new Point(x,y)) : Color.BLACK;
				}
				
				break;
				
			case 2:
				if (x+1 < m.screen[y].length && m.screen[y][x+1].state == 0) {
					m.screen[y][x+1].color = m.screen[y][x].color;
					m.screen[y][x+1].state = m.screen[y][x].state;
					m.screen[y][x+1].direction_x = m.screen[y][x].direction_x;
					m.screen[y][x+1].direction_y = m.screen[y][x].direction_y;
					
					m.screen[y][x].state = 0;
					m.screen[y][x].color = m.background.get(new Point(x,y)) != null ? m.background.get(new Point(x,y)) : Color.BLACK;
				}
				
				break;
			}
			
			switch(m.screen[y][x].direction_y) {
			case 0:
				if (y-1 > 0 && m.screen[y-1][x].state == 0) {
					m.screen[y-1][x].color = m.screen[y][x].color;
					m.screen[y-1][x].state = m.screen[y][x].state;
					m.screen[y-1][x].direction_x = m.screen[y][x].direction_x;
					m.screen[y-1][x].direction_y = m.screen[y][x].direction_y;
					
					m.screen[y][x].state = 0;
					m.screen[y][x].color = m.background.get(new Point(x,y)) != null ? m.background.get(new Point(x,y)) : Color.BLACK;
				}
				
				break;
				
			case 2:
				if (y+1 < m.screen.length && m.screen[y+1][x].state == 0) {
					m.screen[y+1][x].color = m.screen[y][x].color;
					m.screen[y+1][x].state = m.screen[y][x].state;
					m.screen[y+1][x].direction_x = m.screen[y][x].direction_x;
					m.screen[y+1][x].direction_y = m.screen[y][x].direction_y;
					
					m.screen[y][x].state = 0;
					m.screen[y][x].color = m.background.get(new Point(x,y)) != null ? m.background.get(new Point(x,y)) : Color.BLACK;
				}
				
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		return color.getRed()+","+color.getGreen()+","+color.getBlue()+","+state;
	}
}
