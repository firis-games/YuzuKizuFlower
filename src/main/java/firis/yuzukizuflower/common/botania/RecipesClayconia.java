package firis.yuzukizuflower.common.botania;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * クレイコニアのレシピ
 * @author computer
 *
 */
public class RecipesClayconia implements IManaRecipes{

	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(@Nonnull ItemStack stack, boolean simulate) {
		
		ManaRecipe recipe = null;
		
		//砂系の場合
		if (stack.getItem() == Item.getItemFromBlock(Blocks.SAND)) {
			
			ItemStack input = stack.copy();
			input.setCount(1);
			
			//粘土のレシピ
			recipe = new ManaRecipe(
					input,
					new ItemStack(Items.CLAY_BALL),
					80,
					5);			
		}
		return recipe;
	}
	
}
