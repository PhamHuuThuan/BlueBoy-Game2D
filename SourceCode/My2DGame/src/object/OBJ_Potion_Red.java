package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

	GamePanel gp;
	
	public OBJ_Potion_Red(GamePanel gp) {
		super(gp);

		this.gp = gp;
		value = 5;
		type = type_Consumable;
		name = "Red Potion";
		down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
		description = "[" + name + "]\nHeals your life by " + value + ".";
	}
	public void use(Entity entity) {
		gp.gameState = gp.dialogueState;
		gp.ui.currentDialogue = "You drink the " + name + "!"
				+ "Your life has been recovered by " + value + ".";
		entity.life += value;
		gp.playSE(3);
		
	}
}
