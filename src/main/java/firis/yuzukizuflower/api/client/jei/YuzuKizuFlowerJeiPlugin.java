package firis.yuzukizuflower.api.client.jei;

import javax.annotation.Nonnull;

import firis.yuzukizuflower.common.recipe.RecipeBoxedFlower;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

@JEIPlugin
public class YuzuKizuFlowerJeiPlugin implements IModPlugin {

	@Override
	public void register(@Nonnull IModRegistry registry) {
		registry.handleRecipes(RecipeBoxedFlower.class, 
				BoxedFlowerRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
	}
}
