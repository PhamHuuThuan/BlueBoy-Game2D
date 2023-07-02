package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import object.OBJ_Key;

public class UI {

	GamePanel gp;
	Font arial_20;
	Font arial_40B;
	BufferedImage keyImage;
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	public boolean gameFinished = false;
	
	double playTime = 0;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	
	public UI(GamePanel gp) {
		this.gp = gp;
		this.arial_20 = new Font("Arial", Font.PLAIN, 20);
		this.arial_40B = new Font("Arial", Font.BOLD, 40);
		OBJ_Key key = new OBJ_Key();
		keyImage = key.image;
	}
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
	public void draw(Graphics2D g2) {
		
		if(gameFinished == true) {
			
			g2.setFont(arial_20);
			g2.setColor(Color.white);
			
			String text;
			int textLength;
			int x;
			int y;
			
			text = "You found the treasure!";
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight/2 - (gp.tileSize*2);
			g2.drawString(text, x, y);
			
			text = "Your time is: " + dFormat.format(playTime);
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight/2 + (gp.tileSize*4);
			g2.drawString(text, x, y);
			
			g2.setFont(arial_40B);
			g2.setColor(Color.white);
			
			text = "Congratulations!";
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight/2 + (gp.tileSize*2);
			g2.drawString(text, x, y);
			
			gp.gameThread = null;
			
		}else {
			g2.setFont(arial_20);
			g2.setColor(Color.white);
			g2.drawImage(keyImage, 25, 40, gp.tileSize/2, gp.tileSize/2, null);
			g2.drawString(gp.player.hasKey + "", 50, 60);
			
			//PLAYTIME
			playTime += (double)1/60;
			g2.drawString("Time: " + dFormat.format(playTime) , 25, 95);
			
			//MESSAGE
			if(messageOn == true) {
				g2.drawString(message , gp.tileSize/2, gp.tileSize*5);
				messageCounter++;
				if(messageCounter>120) {
					message = "";
					messageOn = false;
					messageCounter = 0;
				}
			}
		}
	}
	public void drawFPS(Graphics2D g2, int drawCount) {
		g2.setFont(arial_20);
		g2.setColor(Color.white);
		g2.drawString("FPS " + drawCount, 25, 25);
	}
}
