package entity;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class NPC_OldMan extends Entity{

	public NPC_OldMan(GamePanel gp) {
		super(gp);
		direction = "down";
		speed = 1;
		getNPCImage();
		setDialogue();
	}
	public void getNPCImage() {
		up1 = setup("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
		left2 = setup("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
	}
	public void setDialogue() {
		dialogues[0] = "Hello, Guy";
		dialogues[1] = "So you've come to this island to find the treasure?";
		dialogues[2] = "I used to a great wizard but now... I'm a bit too old for taking an adventure.";
		dialogues[3] = "Well, goodluck on you";
	}
	public void setAction(){
		actionLockCounter++;
		
		if(actionLockCounter == 120) {
			Random random = new Random();
			int i = random.nextInt(100)+1; //pick up a number from 1 to 100
			if(i<=25) {
				direction = "up";
			}
			else if(i<=50) {
				direction = "down";
			}
			else if(i<=75) {
				direction = "left";
			}
			else if(i<=100) {
				direction = "right";
			}
			actionLockCounter = 0;
		}	
	}
	public void speak() {
		//do this character specific stuff
		super.speak();
	}
}
