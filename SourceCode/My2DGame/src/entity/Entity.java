package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	
	GamePanel  gp;
	
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public BufferedImage image, image2, image3;
	public Rectangle solidArea = new Rectangle(0 , 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	String dialogues[] = new String[20];
	public boolean collision = false;
	
	//STATE
	public int worldX, worldY;
	public String direction = "down";
	public int spriteNum = 1;
	int dialogueIndex = 0;
	public boolean collisonOn = false;
	public boolean invincible = false;
	public boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	boolean hpBarOn = false;
	
	//COUNTER
	public int invincibleCounter = 0;
	public int actionLockCounter = 0;
	public int spriteCounter = 0;
	public int shotAvailableCounter = 0;
	int dyingCounter = 0;
	int hpBarCounter = 0;
	
	//CHARACTER ATTIBUTES
	public String name;
	public int speed;
	public int maxLife;
	public int life;
	public int maxMana;
	public int mana;
	public int ammo;
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int coin;
	public Entity currentWeapon;
	public Entity currentShield;
	public ProjectTile projecttile;
	
	//ITEM ATTIBUTES
	public int value;
	public int attackValue;
	public int defenseValue;
	public String description = "";
	public int useCost;
	
	//TYPE
	public int type; // 0: player, 1: npc, 2: moster
	public final int type_Player = 0;
	public final int type_Npc = 1;
	public final int type_Monster = 2;
	public final int type_Sword = 3;
	public final int type_Axe = 4;
	public final int type_Shield = 5;
	public final int type_Consumable = 6;
	public final int type_pickupOnly = 7;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	public void setAction() {
		
	}
	public void damageReaction() {
		
	}
	public void speak() {
		if(dialogues[dialogueIndex]==null) {
			dialogueIndex = 0;
		}
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		
		switch (gp.player.direction) {
		case "up": 
			direction = "down";
			break;
		case "down": 
			direction = "up";
			break;
		case "left": 
			direction = "right";
			break;
		case "right": 
			direction = "left";
			break;
		}
	}
	public void use(Entity entity) {
		
	}
	public void checkDrop() {
		
	}
	public void checkManyDrop() {
		
	}
	public void dropItem(Entity droppedItem) {
		
		for(int i = 0; i < gp.obj.length; i++) {
			if(gp.obj[i]==null) {
				gp.obj[i] = droppedItem;
				if(i%4==0) {
					gp.obj[i].worldX = worldX - new Random().nextInt(75) + new Random().nextInt(20);
					gp.obj[i].worldY = worldY + new Random().nextInt(60);;
				}
				else if(i%4==1){
					gp.obj[i].worldX = worldX - new Random().nextInt(60);
					gp.obj[i].worldY = worldY - new Random().nextInt(75) + new Random().nextInt(20);
				}else if(i%4==2){
					gp.obj[i].worldX = worldX + new Random().nextInt(60);
					gp.obj[i].worldY = worldY + new Random().nextInt(75) - new Random().nextInt(20);
				}else{
					gp.obj[i].worldX = worldX + new Random().nextInt(60);
					gp.obj[i].worldY = worldY + new Random().nextInt(75) - new Random().nextInt(20);
				}
				break;
			}
		}
	}
	public Color getParticleColor() {
		Color color = null;
		return color;
	}
	public int getParticleSize() {
		int size = 0; // six pixels
		return size;
	}
	public int getPraticleSpeed() {
		int speed = 0;
		return speed;
	}
	public int getPraticleMaxLife() {
		int maxLife = 0;
		return maxLife;
	}
	public void generateParticle(Entity generator, Entity target) {
		Color color = generator.getParticleColor();
		int size = generator.getParticleSize();
		int speed = generator.getPraticleSpeed();
		int maxLife = generator.getPraticleMaxLife();
		
		Particle pl = new Particle(gp, generator, color, size, speed, maxLife, -1, -1);
		gp.particleList.add(pl);
	}
	public void update() {
		setAction();
		
		collisonOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		gp.cChecker.checkEntity(this, gp.iTile);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		if(this.type == type_Monster && contactPlayer == true) {
			damagePlayer(attack);
		}
		
		//IF COLLISION IS FALSE, PLAYER CAN MOVE
		if(collisonOn == false) {
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
		
		spriteCounter++;
		if(spriteCounter > 10) {
			if(spriteNum == 1)
				spriteNum =2;
			else if(spriteNum == 2)
				spriteNum = 1;
			spriteCounter = 0;
		}
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > 40) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if(shotAvailableCounter < 60) {
			shotAvailableCounter++;
		}
	}
	public void damagePlayer(int attack) {
		if(gp.player.invincible == false) {
			//we can give damage;
			int damage = attack - gp.player.defense;
			
			if(damage < 0) {
				damage = 0;
			}
			gp.playSE(6);
			gp.player.life -= damage;
			if(gp.player.life<0)
				gp.player.life = 0;
			gp.player.invincible = true;
		}
	}
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX 
				&& worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			
			switch (direction) {
				case "up":
					if(spriteNum == 1) {
						image = up1;
					}
					if(spriteNum == 2) {
						image = up2;
					}
					break;
				case "down":
					if(spriteNum == 1) {
						image = down1;
					}
					if(spriteNum == 2) {
						image = down2;
					}
					break;
				case "left":
					if(spriteNum == 1) {
						image = left1;
					}
					if(spriteNum == 2) {
						image = left2;
					}
					break;
				case "right":
					if(spriteNum == 1) {
						image = right1;
					}
					if(spriteNum == 2) {
						image = right2;
					}
					break;
			}
			
			//NPC Name
			if(type == 1) {
				
				g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
				
				int length = (int)g2.getFontMetrics().getStringBounds(name, g2).getWidth();
				int x = screenX-length/2 + gp.tileSize/2;
				
				g2.setColor(Color.white);
				g2.drawString(name, x-1, screenY-10);
				
				g2.setColor(Color.black);
				g2.drawString(name, x, screenY-11);
	
			}
			
			//Monster HP bar
			if(type == 2 && hpBarOn == true) {
				double oneScale = (double) gp.tileSize/maxLife;
				double hpBar = oneScale*life;
				
				g2.setColor(new Color(35, 35, 35));
				g2.fillRect(screenX-1, screenY-11, gp.tileSize+2, 12);
				
				g2.setColor(new Color(255, 0, 30));
				g2.fillRect(screenX, screenY-10, (int)hpBar, 10);
				
				hpBarCounter++;
				if(hpBarCounter > 600) {
					hpBarCounter = 0;
					hpBarOn = false;
				}
			}
			
			if(invincible == true) {
				hpBarOn = true;
				hpBarCounter = 0;
				changeAlpha(g2, 0.4f);
			}
			
			if(dying == true) {
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, screenX, screenY, null);
			
			changeAlpha(g2, 1f);
		}
	}
	public void dyingAnimation(Graphics2D g2) {
		dyingCounter++;
		int i = 5;
		if(dyingCounter <= i) {
			changeAlpha(g2, 0f);
		}else if(dyingCounter <= i*2) {
			changeAlpha(g2, 1f);
		}else if(dyingCounter <= i*3) {
			changeAlpha(g2, 0f);
		}else if(dyingCounter <= i*4) {
			changeAlpha(g2, 1f);
		}else if(dyingCounter <= i*5) {
			changeAlpha(g2, 0f);
		}else if(dyingCounter <= i*6) {
			changeAlpha(g2, 1f);
		}else if(dyingCounter <= i*7) {
			changeAlpha(g2, 0f);
		}else if(dyingCounter <= i*8) {
			changeAlpha(g2, 1f);
		}else{
			alive = false;
		}
	}
	public void changeAlpha(Graphics2D g2, float alpha) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	}
	public BufferedImage setup(String imagePath, int width, int height) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream( imagePath + ".png"));
			image = uTool.scaledImage(image, width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
}
