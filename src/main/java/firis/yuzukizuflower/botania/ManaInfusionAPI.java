package firis.yuzukizuflower.botania;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

/**
 * マナプール変換レシピ用API
 * @author computer
 *
 */
public class ManaInfusionAPI {

	/**
	 * マナレシピを検索する
	 * @param stack
	 * @param state
	 * @return
	 */
	public static RecipeManaInfusion getMatchingRecipe(@Nonnull ItemStack stack, @Nonnull IBlockState state) {
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
