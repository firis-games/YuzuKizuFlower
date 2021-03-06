package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import net.minecraft.item.ItemStack;

public class YKTileBoxedEndoflame extends YKTileBaseBoxedGenFlower {

	public YKTileBoxedEndoflame() {
		this(0);
	}
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
		this.inputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(0));
		
		//upgradeスロット
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
