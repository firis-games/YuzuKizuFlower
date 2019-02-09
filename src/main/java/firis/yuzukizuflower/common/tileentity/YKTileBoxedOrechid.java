package firis.yuzukizuflower.common.tileentity;

import firis.yuzukizuflower.common.botania.BotaniaHelper;

/**
 * マナプール系処理
 * @author computer
 *
 */
public class YKTileBoxedOrechid extends YKTileBaseBoxedFuncFlower {

	public YKTileBoxedOrechid() {

		this.maxMana = 10000;
		this.funcFlowerRecipes = BotaniaHelper.recipesOrechid;
		
	}
	
	@Override
	public int getSizeInventory() {
		return 10;
	}

	
}
