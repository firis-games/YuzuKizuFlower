package firis.yuzukizuflower.common.botania;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

/**
 * マナプールの変換レシピ
 * @author computer
 *
 */
public class RecipesManaPool {

	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(@Nonnull ItemStack stack, @Nonnull ItemStack catalyst) {
		
		ManaRecipe recipe = null;
		
		//ManaPool
		
		//触媒を変換
		IBlockState catalystState = null;
		if (catalyst.getItem() instanceof ItemBlock) {
			catalystState = ((ItemBlock)(catalyst.getItem())).getBlock().getDefaultState();
		}
		
		//レシピを取得
		RecipeManaInfusion recipeManaInfusion = RecipesManaPool.getMatchingRecipe(stack, catalystState);
		
		if (recipeManaInfusion != null) {
			
			ItemStack input = stack.copy();
			input.setCount(1);
			
			//レシピを変換
			recipe = new ManaRecipe(
					input,
					recipeManaInfusion.getOutput(),
					recipeManaInfusion.getManaToConsume(),
					0);
			
		}
		return recipe;
	}
	
	
	/**
	 * マナレシピを検索する
	 * @param stack
	 * @param state
	 * @return
	 */
	private static RecipeManaInfusion getMatchingRecipe(@Nonnull ItemStack stack, @Nonnull IBlockState state) {
		List<RecipeManaInfusion> matchingNonCatRecipes = new ArrayList<>();
		List<RecipeManaInfusion> matchingCatRecipes = new ArrayList<>();

		for (RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
			if (recipe.matches(stack)) {
				if(recipe.getCatalyst() == null)
					matchingNonCatRecipes.add(recipe);
				else if (recipe.getCatalyst() == state)
					matchingCatRecipes.add(recipe);
			}
		}
		// Recipes with matching catalyst take priority above recipes with no catalyst specified
		return !matchingCatRecipes.isEmpty() ? matchingCatRecipes.get(0) :
			!matchingNonCatRecipes.isEmpty() ? matchingNonCatRecipes.get(0) :
				null;
	}
}
