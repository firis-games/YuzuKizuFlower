package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;

import firis.yuzukizuflower.common.botania.BotaniaHelper;

/**
 * 箱入りクレイコニア
 */
public class YKTileBoxedClayconia extends YKTileBaseBoxedFuncFlower {

	public YKTileBoxedClayconia() {

		this.maxMana = 1000;
		this.funcFlowerRecipes = BotaniaHelper.recipesClayconia;
		
		//inputスロット
		this.inputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(0));
		
		//outputスロット
		this.outputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		
		//workスロット
		this.workSlotIndex = new ArrayList<Integer>(
				Arrays.asList(10));
	}
	
	@Override
	public int getSizeInventory() {
		return 11;
	}

	
}
