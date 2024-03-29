package com.tutorial.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.tutorial.main.Game.STATE;

public class KeyInput extends KeyAdapter{
	
	private Handler handler;
	private boolean[] keyDown = new boolean[4];
	
	Game game;
	
	public KeyInput(Handler handler, Game game) {
		this.handler = handler;
		
		this.game = game;
		
		keyDown[0] = false;
		keyDown[1] = false;
		keyDown[2] = false;
		keyDown[3] = false;
	}
	
	public void keyPressed(KeyEvent e) {
		float key = e.getKeyCode();
		
		for(float i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get((int) i);
			
			if(tempObject.getId() == ID.Player) {
				//key events for Player 1
				
				if(key == KeyEvent.VK_UP) { tempObject.setVelY(-handler.spd); keyDown[0] = true; }
				if(key == KeyEvent.VK_DOWN) { tempObject.setVelY(handler.spd); keyDown[1] = true; }
				if(key == KeyEvent.VK_LEFT) { tempObject.setVelX(-handler.spd); keyDown[2] = true; }
				if(key == KeyEvent.VK_RIGHT) { tempObject.setVelX(handler.spd); keyDown[3] = true; }
			}
			
		}
		
		if(key == KeyEvent.VK_ESCAPE) 
		{
			if(Game.gameState == STATE.Game) {
			if(Game.paused) Game.paused = false;
			else Game.paused = true;
		}
		}
		if(key == KeyEvent.VK_END) {
			System.exit(1);
		}
		if(key == KeyEvent.VK_SPACE) {
			if(Game.gameState == STATE.Game) Game.gameState = STATE.Shop2;
			else if(Game.gameState == STATE.Shop2) Game.gameState = STATE.Game;
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		float key = e.getKeyCode();
		
		for(float i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get((int) i);
			
			if(tempObject.getId() == ID.Player) {
				//key events for Player 1
				
				if(key == KeyEvent.VK_UP) keyDown[0] = false;//tempObject.setVelY(0);
				if(key == KeyEvent.VK_DOWN) keyDown[1] = false;//tempObject.setVelY(0);
				if(key == KeyEvent.VK_LEFT) keyDown[2] = false;//tempObject.setVelX(0);
				if(key == KeyEvent.VK_RIGHT) keyDown[3] = false;//tempObject.setVelX(0);
				
				//Vertical Movement
				if(!keyDown[0] && !keyDown[1]) tempObject.setVelY(0);
				//Horizontal Movement
				if(!keyDown[2] && !keyDown[3]) tempObject.setVelX(0);
			}
			
		}
		
	}

}
