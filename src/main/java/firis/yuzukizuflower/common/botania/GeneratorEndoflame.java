package firis.yuzukizuflower.common.botania;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class GeneratorEndoflame implements IManaGenerator {
	
	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaGenerator getMatchesRecipe(@Nonnull ItemStack stack) {
		
		ManaGenerator generator = null;
		
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
	
	
}
