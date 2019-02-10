package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;

import firis.yuzukizuflower.common.botania.BotaniaHelper;

public class YKTileBoxedPureDaisy  extends YKTileBaseBoxedFuncFlower {

	public YKTileBoxedPureDaisy() {

		this.maxMana = 0;
		this.funcFlowerRecipes = BotaniaHelper.recipesPureDaisy;
		
		//inputスロット
		this.inputSlotIndex = 0;
		
		//outputスロット
		this.outputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(1));
		
		//workスロット
		this.workSlotIndex = 2;
		
		//ノーマルレシピ
		randomRecipe = false;
	}
	
	@Override
	public int getSizeInventory() {
		return 3;
	}

	
}
