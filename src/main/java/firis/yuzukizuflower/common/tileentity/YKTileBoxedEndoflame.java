package firis.yuzukizuflower.common.tileentity;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import net.minecraft.item.ItemStack;

public class YKTileBoxedEndoflame extends YKTileBaseBoxedGenFlower {

	/**
	 * 
	 * @param mode
	 */
	public YKTileBoxedEndoflame(int mode) {
		
		this.maxMana = 10000;
		
		this.mode = mode;
		if (mode == 1) {
			this.maxMana = 100000;
		}

		//発電用レシピ
		this.genFlowerRecipes = BotaniaHelper.recipesEndoflame;
		
		//inputスロット
		this.inputSlotIndex = 0;
		
		this.upgradeSlotIndex = 1;
		
	}

	protected int mode;

	@Override
	public int getSizeInventory() {
		return 2;
	}
	
	/**
	 * 対象stackがupgradeアイテムか判断する
	 * @return
	 */
	@Override
	public boolean isUpgradeParts(ItemStack stack) {
		//エンドフレイム
		return BotaniaHelper.isSpecialFlower(stack, "endoflame");
	}
	
	
}
