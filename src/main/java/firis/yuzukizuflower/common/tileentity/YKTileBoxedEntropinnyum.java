package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import net.minecraft.item.ItemStack;

public class YKTileBoxedEntropinnyum extends YKTileBaseBoxedGenFlower {

	/**
	 * 
	 */
	public YKTileBoxedEntropinnyum() {
		
		this.maxMana = 13000;

		//発電用レシピ
		this.genFlowerRecipes = BotaniaHelper.recipesEntropinnyum;
		
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
		return BotaniaHelper.isSpecialFlower(stack, "entropinnyum");
	}
	
	
}
