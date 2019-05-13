package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import net.minecraft.item.ItemStack;

public class YKTileBoxedKekimurus extends YKTileBaseBoxedGenFlower {

	/**
	 * 
	 * @param mode
	 */
	public YKTileBoxedKekimurus() {
		
		this.maxMana = 12600;

		//発電用レシピ
		this.genFlowerRecipes = BotaniaHelper.recipesKekimurus;
		
		//inputスロット
		this.inputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(0));
		
		//upgradeスロット
		this.upgradeSlotIndex = 1;
		
	}

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
		return BotaniaHelper.isSpecialFlower(stack, "kekimurus");
	}
	
	
}
