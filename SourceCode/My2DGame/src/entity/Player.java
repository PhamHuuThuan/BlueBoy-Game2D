package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Axe;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Rock;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity{
	KeyHandler keyH;
	public final int screenX;
	public final int screenY;
	int standCounter = 0;
	public boolean attackCanceled = false;
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = 20;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		
		this.keyH = keyH;

		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle(8, 16, 32, 32);
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
//		attackArea.width = 36;
//		attackArea.height = 36;
		
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}
	public void setDefaultValues() {
		worldX = gp.tileSize*23;
		worldY = gp.tileSize*21;
		speed = 4;
		direction = "down";
		type = type_Player;
		
		//PLAYER STATUS
		level = 1;
		maxLife = 6;
		life = maxLife;
		maxMana = 4;
		mana = maxMana;
		strength = 1;	//the more strength he has, the more damage he gives.
		dexterity = 1;	//the more dexterity he has, the less defense he receives.
		exp = 0;
		nextLevelExp = 5;
		coin = 0;
		currentWeapon = new OBJ_Sword_Normal(gp);
		currentShield = new OBJ_Shield_Wood(gp);
		projecttile = new  OBJ_Fireball(gp);
		attack = getAttack();	//The total attackValue is decided by strength and weapon
		defense = getDefense();	//The total defense is decided by dexterity and shield
	}
	public void setItems() {
		inventory.add(currentWeapon);
		inventory.add(currentShield);
	}
	public int getDefense() {
		return dexterity*currentShield.defenseValue;
	}
	public int getAttack() {
		attackArea = currentWeapon.attackArea;
		return strength*currentWeapon.attackValue;
	}
	public void getPlayerImage() {
		up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
		up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
		down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
		down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
		left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
		left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
		right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
		right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
	}
	public void getPlayerAttackImage() {
		
		if(currentWeapon.type == type_Sword) {
			attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
			attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
			attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
			attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
			attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
			attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
			attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
			attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
		}
		if(currentWeapon.type == type_Axe) {
			attackUp1 = setup("/player/boy_axe_up_1", gp.tileSize, gp.tileSize*2);
			attackUp2 = setup("/player/boy_axe_up_2", gp.tileSize, gp.tileSize*2);
			attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize, gp.tileSize*2);
			attackDown2 = setup("/player/boy_axe_down_2", gp.tileSize, gp.tileSize*2);
			attackLeft1 = setup("/player/boy_axe_left_1", gp.tileSize*2, gp.tileSize);
			attackLeft2 = setup("/player/boy_axe_left_2", gp.tileSize*2, gp.tileSize);
			attackRight1 = setup("/player/boy_axe_right_1", gp.tileSize*2, gp.tileSize);
			attackRight2 = setup("/player/boy_axe_right_2", gp.tileSize*2, gp.tileSize);
		}
		
	}
	public void update() {
		
		if(attacking == true) {
			attacking();
		}
		
		if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {
			
			if(keyH.upPressed==true) {
				direction = "up";
			}
			else if(keyH.downPressed==true) {
				direction = "down";
			}
			else if(keyH.leftPressed==true) {
				direction = "left";
			}
			else if(keyH.rightPressed==true) {
				direction = "right";
			}
			
			//CHECK TILE COLLISION	
			collisonOn = false;
			gp.cChecker.checkTile(this);
			
			//CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			//CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			//CHECK MONSTER COLLISION
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			
			//CHECK INTERACTIVE TILE COLLISION
			int iTile = gp.cChecker.checkEntity(this, gp.iTile);
			
			
			//CHECK EVENT
			gp.eHandler.checkEvent();
			
			//IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(collisonOn == false && keyH.enterPressed == false) {
				switch(direction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
			}
			
			if(keyH.enterPressed == true && attackCanceled == false) {
				gp.playSE(7);
				attacking = true;
				spriteCounter = 0;
			}
			
			attackCanceled = false;
			gp.keyH.enterPressed = false;
			
			spriteCounter++;
			if(spriteCounter > 10) {
				if(spriteNum == 1)
					spriteNum =2;
				else if(spriteNum == 2)
					spriteNum = 1;
				spriteCounter = 0;
			}
			
		}else {
			standCounter++;
			if(standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}
		
		if(keyH.shotKeyPress == true && projecttile.alive == false && shotAvailableCounter == 60 && projecttile.haveResource(this)==true) {
			//SET DEFAULT COORDINATES, DIRECTRION AND USER
			projecttile.set(worldX, worldY, direction, true, this);
			
			//SUBTRACT THE COST (MANA, AMMO ETC)
			projecttile.subtractResource(this);
			
			//ADD IT TO THE LIST
			gp.projecttileList.add(projecttile);
			
			shotAvailableCounter = 0;
			
			gp.playSE(10);
		}
		
		//This needs to be outside of key if statement!
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if(shotAvailableCounter < 60) {
			shotAvailableCounter++;
		}
		if(life>maxLife) {
			life = maxLife;
		}
	}
	public void attacking() {
		spriteCounter++;
		if(spriteCounter <= 5) {
			spriteNum = 1;
		}
		if(spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
			
			//Save the current worldX, worldY, solidArea
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			
			//Adjust player's worldX/Y for the AttackArea
			switch(direction) {
			case "up":		worldY -= attackArea.height; break;
			case "down":	worldY += attackArea.height; break;
			case "left":	worldX -= attackArea.width; break;
			case "right":	worldX += attackArea.width; break;
			}
			
			//attackArea becomes solidArea
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			
			//check monster collision with the update worldX, worldY and solidArea
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex, attack);
			
			int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
			damageInteractiveTile(iTileIndex);
			
			//after checking collision, restore the original data
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
		}
		if(spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}
	public void pickUpObject(int index) {
		
		if(index != 999) {
			//PICK UP ONLY ITEMS
			if(gp.obj[index].type == type_pickupOnly) {
				gp.obj[index].use(this);
				gp.obj[index] = null;
			}
			
			//INVENTORY ITEMS
			else {
				String text = "";
				if(inventory.size() != maxInventorySize) {
					inventory.add(gp.obj[index]);
					gp.playSE(1);
					text = "Got a " + gp.obj[index].name + "!";
					gp.obj[index] = null;
				}else {
					text = "You cannot carry anymore!";
				}
				gp.ui.addMessage(text);
			}
		}
		
	}
	public void interactNPC(int i) {
		
		if(gp.keyH.enterPressed == true) {
			if(i != 999) {
				attackCanceled = true;
				gp.gameState = gp.dialogueState;
				gp.npc[i].speak();
			}
		}
	}
	public void contactMonster(int i) {
		if(i!=999) {
			if(invincible == false && gp.monster[i].dying == false) {
				gp.playSE(6);
				int damage = gp.monster[i].attack - defense;
				
				if(damage < 0) {
					damage = 0;
				}
				life -= damage;
				if(life<0)
					life = 0;
				invincible = true;
			}	
		}
	}
	public void damageMonster(int i, int attack) {
		if(i!=999) {
			if(gp.monster[i].invincible == false && gp.player.life > 0) {
				gp.playSE(5);
				
				int damage = attack - gp.monster[i].defense;
				
				if(damage < 0) {
					damage = 0;
				}
				gp.monster[i].life -= damage;
				gp.ui.addMessage(damage + " damage!");
				gp.monster[i].invincible = true;
				gp.monster[i].damageReaction();
				
				if(gp.monster[i].life <= 0) {
					gp.monster[i].dying = true;
					gp.ui.addMessage("Killed the " + gp.monster[i].name + "!");
					exp += gp.monster[i].exp;
					gp.ui.addMessage("+ " + gp.monster[i].exp + "Exp!");
					checkLevelUp();
				}
			}	
		}
//		else if(gp.player.life <= 0) {
//			gp.ui.addMessage("You are not enough hp!");
//		}
//		else {
//			gp.ui.addMessage("Miss!");
//		}
	}
	public void damageInteractiveTile(int i) {
		if(i != 999 && gp.iTile[i].destructible == true && gp.iTile[i].isCorrectItem(this) == true && gp.iTile[i].invincible == false) {
			gp.iTile[i].playSE();
			gp.iTile[i].life-=1;
			gp.iTile[i].invincible = true;
			
			generateParticle(gp.iTile[i], gp.iTile[i]);
			
			if(gp.iTile[i].life == 0)
				gp.iTile[i] = gp.iTile[i].getDestroyedForm();
		}else if(i != 999 && gp.iTile[i].destructible == true && gp.iTile[i].isCorrectItem(this) == false && gp.iTile[i].invincible == false) {
			gp.iTile[i].invincible = true;
			gp.ui.addMessage("Can't cutting tree! You have to use Axe!");
		}
	}
	public void checkLevelUp() {
		if(exp >= nextLevelExp) {
			level++;
			exp -= nextLevelExp;
			nextLevelExp *= 2;
			maxLife += 2;
			strength++;
			dexterity++;
			attack = getAttack();
			defense = getDefense();
			gp.playSE(8);
//			gp.gameState = gp.dialogueState;
//			gp.ui.currentDialogue = "You are level " + level + "now!!! You feel stronger!";
			gp.ui.setMessageCenter("LEVEL UP!");
		}
	}
	public void selectItem() {
		int itemIndex  = gp.ui.getItemIndexOnSlot();
		if(itemIndex < inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);
			if(selectedItem.type == type_Sword || selectedItem.type == type_Axe) {
				currentWeapon = selectedItem;
				attack = getAttack();
				getPlayerAttackImage();
			}
			if(selectedItem.type == type_Shield) {
				currentShield = selectedItem;
				defense = getDefense();
			}
			if(selectedItem.type == type_Consumable) {
				selectedItem.use(this);
				inventory.remove(itemIndex);
			}
		}
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		switch (direction) {
			case "up":
				if(attacking == false) {
					if(spriteNum == 1) {image = up1;}
					if(spriteNum == 2) {image = up2;}
				}else {
					tempScreenY -= gp.tileSize;
					if(spriteNum == 1) {image = attackUp1;}
					if(spriteNum == 2) {image = attackUp2;}
				}
				
				break;
			case "down":
				if(attacking == false) {
					if(spriteNum == 1) {image = down1;}
					if(spriteNum == 2) {image = down2;}
				}else {
					if(spriteNum == 1) {image = attackDown1;}
					if(spriteNum == 2) {image = attackDown2;}
				}
				break;
			case "left":
				if(attacking == false) {
					if(spriteNum == 1) {image = left1;}
					if(spriteNum == 2) {image = left2;}
				}else {
					tempScreenX -= gp.tileSize;
					if(spriteNum == 1) {image = attackLeft1;}
					if(spriteNum == 2) {image = attackLeft2;}
				}
				break;
			case "right":
				if(attacking == false) {
					if(spriteNum == 1) {image = right1;}
					if(spriteNum == 2) {image = right2;}
				}else {
					if(spriteNum == 1) {image = attackRight1;}
					if(spriteNum == 2) {image = attackRight2;}
				}
				break;
		}
		
		if(invincible == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		}
		
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		
		//Reset Alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}
