package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.botania.ManaRecipe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;

/**
 * マナプール系処理
 * @author computer
 *
 */
public class YKTileBoxedJadedAmaranthus extends YKTileBaseBoxedFuncFlower {

	public YKTileBoxedJadedAmaranthus() {

		this.maxMana = 10000;
		this.funcFlowerRecipes = BotaniaHelper.recipesJadedAmaranthus;
		
		//inputスロット
		this.inputSlotIndex = new ArrayList<Integer>();
		
		//outputスロット
		this.outputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
		
		//workスロット
		this.workSlotIndex = new ArrayList<Integer>();
		
		//upgradeスロット
		this.upgradeSlotIndex = 9;
	}
	
	//upgradeスロット
	protected int upgradeSlotIndex = -1;
	
	@Override
	public int getSizeInventory() {
		return 10;
	}
	
	
	/**
	 * レシピの出力結果を取得する
	 */
	@Override
	public ItemStack getRecipesResult() {
		
		//アップグレードスロット
		ItemStack upgrade = this.getStackInSlot(upgradeSlotIndex);
		
		ItemStack stack = this.getStackInputSlotFirst();
		if (!upgrade.isEmpty()
				&& upgrade.getItem() instanceof ItemShears) {
			stack = upgrade.copy();
			
			//ハサミの耐久度を消費
			upgrade.setItemDamage(upgrade.getItemDamage() + 1);
			
			if (upgrade.getItemDamage() > upgrade.getMaxDamage()) {
				//ハサミを破壊する
				upgrade.shrink(1);
			}
			
		}
		
		ManaRecipe recipe = funcFlowerRecipes.getMatchesRecipe(stack, false);
		if (recipe == null) {
			//例外
			clearRecipeWork();
			return null;
		}
		
		return recipe.getOutputItemStack();		
	}
	
	/**
	 * 対象スロットの使用許可
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		//upgradeスロット
		if (upgradeSlotIndex == index) {
		
			if (!stack.isEmpty()
					&& stack.getItem() instanceof ItemShears) {
				return true;
			} else {
				return false;
			}
			
		}
		return super.isItemValidForSlot(index, stack);
	}
	
}