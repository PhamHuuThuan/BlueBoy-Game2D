package main;

import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.OBJ_Axe;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Blue;
import tile_interactive.IT_DryTree;
import tile_interactive.InteractiveTile;

public class AssetSetter {
	GamePanel gp;

	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	public void setObject() {
		int i = 0;
//		gp.obj[i] = new OBJ_Key(gp);
//		gp.obj[i].worldX = gp.tileSize*25;
//		gp.obj[i].worldY = gp.tileSize*23;
//		
//		i++;
//		gp.obj[i] = new OBJ_Key(gp);
//		gp.obj[i].worldX = gp.tileSize*21;
//		gp.obj[i].worldY = gp.tileSize*19;
//		
//		i++;
//		gp.obj[i] = new OBJ_Key(gp);
//		gp.obj[i].worldX = gp.tileSize*26;
//		gp.obj[i].worldY = gp.tileSize*21;
//		
//		i++;
		gp.obj[0][i] = new OBJ_Axe(gp);
		gp.obj[0][i].worldX = gp.tileSize*28;
		gp.obj[0][i].worldY = gp.tileSize*21;
		
		i++;
		gp.obj[0][i] = new OBJ_Shield_Blue(gp);
		gp.obj[0][i].worldX = gp.tileSize*25;
		gp.obj[0][i].worldY = gp.tileSize*16;
		
		i++;
		gp.obj[0][i] = new OBJ_Potion_Red(gp);
		gp.obj[0][i].worldX = gp.tileSize*26;
		gp.obj[0][i].worldY = gp.tileSize*16;
	}
	public void setNPC() {
		
		int map = 0;
		int i = 0;
		
		gp.npc[map][i] = new NPC_OldMan(gp);
		gp.npc[map][i].worldX = gp.tileSize*21;
		gp.npc[map][i].worldY = gp.tileSize*21;
		
		map = 1;
		i++;
		gp.npc[map][i] = new NPC_OldMan(gp);
		gp.npc[map][i].worldX = gp.tileSize*12;
		gp.npc[map][i].worldY = gp.tileSize*7;
		
	}
	public void setMonster() {
		int i = 0;
		
		gp.monster[0][i] = new MON_GreenSlime(gp);
		gp.monster[i][0].worldX = gp.tileSize*23;
		gp.monster[0][i].worldY = gp.tileSize*36;
		
		i++;
		gp.monster[0][i] = new MON_GreenSlime(gp);
		gp.monster[0][i].worldX = gp.tileSize*23;
		gp.monster[0][i].worldY = gp.tileSize*38;
		
		i++;
		gp.monster[0][i] = new MON_GreenSlime(gp);
		gp.monster[0][i].worldX = gp.tileSize*21;
		gp.monster[0][i].worldY = gp.tileSize*37;
		
		i++;
		gp.monster[0][i] = new MON_GreenSlime(gp);
		gp.monster[0][i].worldX = gp.tileSize*34;
		gp.monster[0][i].worldY = gp.tileSize*42;
		
		i++;
		gp.monster[0][i] = new MON_GreenSlime(gp);
		gp.monster[0][i].worldX = gp.tileSize*38;
		gp.monster[0][i].worldY = gp.tileSize*42;
		
	}
	public void setInteractiveTile() {
		int i = 0;
		
		gp.iTile[0][i] = new IT_DryTree(gp, 27, 12);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 28, 12);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 29, 12);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 30, 12);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 31, 12);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 32, 12);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 33, 12);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 31, 21);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 18, 40);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 17, 40);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 16, 40);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 15, 40);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 14, 40);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 13, 40);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 13, 41);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 12, 41);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 11, 41);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 10, 41);
		
		i++;
		gp.iTile[0][i] = new IT_DryTree(gp, 10, 40);
	}
}
