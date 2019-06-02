package firis.yuzukizuflower.common.botania;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class GeneratorEndoflame implements IManaGenerator {
	
	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaGenerator getMatchesRecipe(@Nonnull List<ItemStack> stackList) {
		
		ManaGenerator generator = null;
		
		if (stackList.size() <= 0) {
			return generator;
		}
		
		ItemStack stack = stackList.get(0);
		
		if (stack.getItem().hasContainerItem(stack)) {
			return generator;
		}
		
		//かまどを検索
		int burnTime = TileEntityFurnace.getItemBurnTime(stack);
		if (burnTime == 0) {
			return generator;
		}
		
		//2tickあたり3mana
		int time = burnTime / 2;
		
		ItemStack fuel = stack.copy();
		fuel.setCount(1);
		
		//レシピへ変換
		generator = new ManaGenerator(
				fuel,
				3,
				time,
				2);
		
		return generator;
	}
	
	/**
	 * スロットのチェック処理
	 */
	public boolean isMatchesItemStackSlot(@Nonnull ItemStack stack) {
		if(TileEntityFurnace.getItemBurnTime(stack) <= 0
				|| stack.getItem().hasContainerItem(stack)) {
			return false;
		}
		return true;
	}
	
	
}
