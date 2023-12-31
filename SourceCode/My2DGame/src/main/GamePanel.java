package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import monster.MON_GreenSlime;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable{
	//SCREEN SETTING
	final int originalTileSize = 16;
	final int scale = 3;
	public final int tileSize = originalTileSize*scale;
	public final int maxScreenCol = 20;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize*maxScreenCol; // 960 pixels
	public final int screenHeight = tileSize*maxScreenRow;
	
	//WORLD SETTING	
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int maxMap = 10;
	public int currentMap = 0;
	
	//FOR FULL SCREEN
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	public boolean fullScreenOn = false;
	
	//FPS
	int FPS = 60;
	int drawCount = 0;
	
	//SYSTEM
	TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	public Config config = new Config(this);
	Thread gameThread;
	
	//ENTITY AND OBJECTS
	public Player player = new Player(this, keyH);
	public Entity obj[][] = new Entity[maxMap][20];
	public Entity npc[][] = new Entity[maxMap][10];
	public Entity monster[][] = new Entity[maxMap][20];
	public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
	public ArrayList<Entity> particleList = new ArrayList<>();
	public ArrayList<Entity> projecttileList = new ArrayList<>();
	ArrayList<Entity> entityList = new ArrayList<>();
	
	// GAME STATE
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int characterState = 4;
	public final int optionState = 5;
	public final int gameOverState = 6;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	public void setupGame() {
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
//		playMusic(0);
		gameState = titleState;
		
		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D) tempScreen.getGraphics();
		
		if(fullScreenOn == true)
			setFullScreen();
	}
	public void retry() {
		player.setDefaultPosition();
		player.restoreLifeAndMana();
		aSetter.setNPC();
		aSetter.setMonster();
		
	}
	public void restart() {
		player.setDefaultValues();
		player.setDefaultPosition();
		player.setItems();
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
		
	}
	public void setFullScreen() {
		
		//GET LOCAL SCREEN DEVICE
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
		
		//GET FULL SCREEN WIDTH AND HEIGHT
		screenWidth2 = Main.window.getWidth();
		screenHeight2 = Main.window.getHeight();
	}
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCountTemp = 0;
		
		while (gameThread!=null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
//				repaint();
				drawToTempScreen(); // draw everything to the buffered image
				drawToScreen(); // draw the buffered image to the screen
				delta--;
				drawCountTemp++;
			}
			if(timer > 1000000000) {
				drawCount = drawCountTemp;
				drawCountTemp = 0;
				timer = 0;
			}
		}
		
	}
	public void update() {
		
		if(gameState==playState) {
			//PLAYER
			player.update();
			
			//NPC 
			for(int i = 0; i < npc[currentMap].length; i++) {
				if(npc[currentMap][i]!=null)
					npc[currentMap][i].update();
			}
			//MONSTER
			for(int i = 0; i < monster[currentMap].length; i++) {
				if(monster[currentMap][i]!=null) {
					if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
						monster[currentMap][i].update();
					}
					if(monster[currentMap][i].alive == false) {
						monster[currentMap][i].checkManyDrop();
						monster[currentMap][i] = null;
					}
				}
			}
			//PROJECTTILE
			for(int i = 0; i < projecttileList.size(); i++) {
				if(projecttileList.get(i)!=null) {
					if(projecttileList.get(i).alive == true) {
						projecttileList.get(i).update();
					}
					if(projecttileList.get(i).alive == false) {
						projecttileList.remove(i);
					}
				}
			}
			for(int i = 0; i < particleList.size(); i++) {
				if(particleList.get(i)!=null) {
					if(particleList.get(i).alive == true) {
						particleList.get(i).update();
					}
					if(particleList.get(i).alive == false) {
						particleList.remove(i);
					}
				}
			}
			for(int i = 0; i < iTile[currentMap].length; i++) {
				if(iTile[currentMap][i]!=null) {
					iTile[currentMap][i].update();
				}
			}
		}	
		else if(gameState == pauseState) {
			//nothing to do
		}
		
	}
	public void drawToTempScreen() {
		//DEBUG
		long drawStart = 0;
		if(keyH.checkDrawTime == true)
			drawStart = System.nanoTime();
		
		//TITLE SCREEN
		if(gameState == titleState) {
			ui.draw(g2);
		}else {
			//TILE
			tileM.draw(g2);
	
			for(int i = 0; i < iTile[currentMap].length; i++) {
				if(iTile[currentMap][i]!=null) {
					iTile[currentMap][i].draw(g2);
				}
			}
			
			//ADD ENTITIES TO ARRAYLIST
			entityList.add(player);
			
			for(int i = 0; i < npc[currentMap].length; i++) {
				if(npc[i][currentMap]!=null) {
					entityList.add(npc[currentMap][i]);
				}
			}
			
			for(int i = 0; i < obj[currentMap].length; i++) {
				if(obj[currentMap][i]!=null) {
					entityList.add(obj[currentMap][i]);
				}
			}
			
			for(int i = 0; i < monster[currentMap].length; i++) {
				if(monster[currentMap][i]!=null) {
					entityList.add(monster[currentMap][i]);
				}
			}
			for(int i = 0; i < projecttileList.size(); i++) {
				if(projecttileList.get(i)!=null) {
					entityList.add(projecttileList.get(i));
				}
			}
			for(int i = 0; i < particleList.size(); i++) {
				if(particleList.get(i)!=null) {
					entityList.add(particleList.get(i));
				}
			}
			//SORT
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					int result = Integer.compare(e1.worldY, e2.worldY);
					return result;
				}
			});
			
			//DRAW ENTITIES
			for(int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}
			//EMPTY ENTITES LIST
			entityList.clear();
			
			//UI
			ui.draw(g2);
		}
		
		//FPS
		ui.drawFPS(g2, drawCount);
		
		if(keyH.checkDrawTime == true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw Time: " + passed, 10, 400);
			System.out.println("Draw Time: " + passed);
		}
	}
	public void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
	}
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		music.stop();
	}
	public void playSE(int i) {
		se.setFile(i);
		se.play();
	}
}
