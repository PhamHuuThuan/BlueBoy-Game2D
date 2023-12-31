package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPress;
	
	boolean checkDrawTime = false;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		//TITLE STATE
		if(gp.gameState == gp.titleState) {
			titleState(code);
		}
		
		//PLAY STATE
		else if(gp.gameState == gp.playState) {
			playState(code);
		}
		//PAUSE STATE
		else if(gp.gameState == gp.pauseState) {
			pauseState(code);
		}
		
		//DIALOGUE STATE
		else if(gp.gameState == gp.dialogueState) {
			dialogueState(code);
		}
		//CHARACTER STATE
		else if(gp.gameState == gp.characterState) {
			characterState(code);
		}
		//OPTION STATE
		else if(gp.gameState == gp.optionState) {
			optionState(code);
		}
		//GAME OVER STATE
		else if(gp.gameState == gp.gameOverState) {
			gameOverState(code);
		}
		
	}
	public void titleState(int code) {
		if(gp.ui.titleScreenState==0) {
			if(code==KeyEvent.VK_1) {
				gp.ui.commandNum = 0;
			}
			if(code==KeyEvent.VK_2) {
				gp.ui.commandNum = 1;
			}
			if(code==KeyEvent.VK_3) {
				gp.ui.commandNum = 2;
			}
			if(code==KeyEvent.VK_UP || code==KeyEvent.VK_W) {
				if(gp.ui.commandNum == 0)
					gp.ui.commandNum = 2;
				else
					gp.ui.commandNum--;
			}
			if(code==KeyEvent.VK_DOWN || code==KeyEvent.VK_S) {
				if(gp.ui.commandNum == 2)
					gp.ui.commandNum = 0;
				else
					gp.ui.commandNum++;
			}
			if(code==KeyEvent.VK_ENTER) {
				if(gp.ui.commandNum == 0) {
					gp.ui.titleScreenState = 1;
					
				}
				if(gp.ui.commandNum == 1) {
					
				}
				if(gp.ui.commandNum == 2) {
					System.exit(0);
				}
			}
		}
		else if(gp.ui.titleScreenState==1) {
			if(code==KeyEvent.VK_1) {
				gp.ui.commandNum = 0;
			}
			if(code==KeyEvent.VK_2) {
				gp.ui.commandNum = 1;
			}
			if(code==KeyEvent.VK_3) {
				gp.ui.commandNum = 2;
			}
			if(code==KeyEvent.VK_4) {
				gp.ui.commandNum = 3;
			}
			if(code==KeyEvent.VK_UP || code==KeyEvent.VK_W) {
				if(gp.ui.commandNum == 0)
					gp.ui.commandNum = 3;
				else
					gp.ui.commandNum--;
			}
			if(code==KeyEvent.VK_DOWN || code==KeyEvent.VK_S) {
				if(gp.ui.commandNum == 3)
					gp.ui.commandNum = 0;
				else
					gp.ui.commandNum++;
			}
			if(code==KeyEvent.VK_ENTER) {
				if(gp.ui.commandNum == 0) {
					gp.gameState = gp.playState;
					gp.playMusic(0);
				}
				if(gp.ui.commandNum == 1) {
					System.out.println("Do some thief specific stuff!");
				}
				if(gp.ui.commandNum == 2) {
					System.out.println("Do some sorcerer specific stuff!");
				}
				if(gp.ui.commandNum == 3) {
					gp.ui.commandNum = 0;
					gp.ui.titleScreenState = 0;
				}
			}
		}
	}
	public void playState(int code) {
		if(code==KeyEvent.VK_W || code==KeyEvent.VK_UP) {
			upPressed = true;
		}
		if(code==KeyEvent.VK_S || code==KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if(code==KeyEvent.VK_A || code==KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if(code==KeyEvent.VK_D || code==KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if(code==KeyEvent.VK_P) {
			gp.gameState = gp.pauseState;
		}
		if(code==KeyEvent.VK_C) {
			gp.gameState = gp.characterState;
		}
		if(code==KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if(code==KeyEvent.VK_F) {
			shotKeyPress = true;
		}
		if(code==KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.optionState;
		}
		if(code==KeyEvent.VK_T) {
			if(checkDrawTime==false)
				checkDrawTime = true;
			else
				checkDrawTime = false;
		}
		if(code == KeyEvent.VK_R) {
			switch(gp.currentMap) {
			case 0:	gp.tileM.loadMap("/maps/worldV3.txt", 0); break;
			case 1: gp.tileM.loadMap("/maps/interior01.txt", 1); break;
			}
		}
	}
	public void pauseState(int code) {
		if(code==KeyEvent.VK_P) 
			gp.gameState = gp.playState;
	}
	public void dialogueState(int code) {
		if(code==KeyEvent.VK_ENTER) {
			gp.gameState = gp.playState;
		}
	}
	public void characterState(int code) {
		if(code==KeyEvent.VK_C || code==KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.playState;
		}
		if(code==KeyEvent.VK_W || code==KeyEvent.VK_UP) {
			if(gp.ui.slotRow!=0)
				gp.ui.slotRow--;
			gp.playSE(9);
		}
		if(code==KeyEvent.VK_S || code==KeyEvent.VK_DOWN) {
			if(gp.ui.slotRow!=3)
				gp.ui.slotRow++;
			gp.playSE(9);
		}
		if(code==KeyEvent.VK_A || code==KeyEvent.VK_LEFT) {
			if(gp.ui.slotCol!=0)
				gp.ui.slotCol--;
			gp.playSE(9);
		}
		if(code==KeyEvent.VK_D || code==KeyEvent.VK_RIGHT) {
			if(gp.ui.slotCol!=4)
				gp.ui.slotCol++;
			gp.playSE(9);
		}
		if(code==KeyEvent.VK_ENTER) {
			gp.player.selectItem();
		}
		
	}
	public void optionState(int code) {
		if(code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.playState;
		}
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		
		int maxCommanNum = 0;
		switch(gp.ui.subState) {
		case 0: maxCommanNum = 5;	break;
		case 3: maxCommanNum = 1;	break;
		}
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			gp.playSE(9);
			if(gp.ui.commandNum<0){
				gp.ui.commandNum = maxCommanNum;
			}
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			gp.playSE(9);
			if(gp.ui.commandNum>maxCommanNum){
				gp.ui.commandNum = 0;
			}
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			if(gp.ui.subState == 0) {
				if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
					gp.music.volumeScale--;
					gp.music.checkVolume();
					gp.playSE(9);
				}
				if(gp.ui.subState == 0) {
					if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
						gp.se.volumeScale--;
						gp.playSE(9);
					}
				}
			}
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
				gp.music.volumeScale++;
				gp.music.checkVolume();
				gp.playSE(9);
			}
			if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
				gp.se.volumeScale++;
				gp.playSE(9);
			}
		}
	}
	public void gameOverState(int code) {
		int maxCommandNum = 1;
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			gp.ui.commandNum++;
			if(gp.ui.commandNum > maxCommandNum) {
				gp.ui.commandNum = 0;
			}
			gp.playSE(9);
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum--;
			if(gp.ui.commandNum < 0) {
				gp.ui.commandNum = maxCommandNum;
			}
			gp.playSE(9);
		}
		if(code == KeyEvent.VK_ENTER) {
			if(gp.ui.commandNum == 0) {
				gp.retry();
				gp.playMusic(0);
				gp.gameState = gp.playState;
			}
			if(gp.ui.commandNum == 1) {
				gp.gameState = gp.titleState;
				gp.restart();
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code==KeyEvent.VK_W || code==KeyEvent.VK_UP) {
			upPressed = false;
		}
		if(code==KeyEvent.VK_S || code==KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		if(code==KeyEvent.VK_A || code==KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if(code==KeyEvent.VK_D || code==KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		if(code==KeyEvent.VK_F) {
			shotKeyPress = false;
		}
	}

}
