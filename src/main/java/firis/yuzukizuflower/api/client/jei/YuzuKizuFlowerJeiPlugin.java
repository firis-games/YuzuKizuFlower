package firis.yuzukizuflower.api.client.jei;

import javax.annotation.Nonnull;

import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedBrewery;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedOrechid;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedPureDaisy;
import firis.yuzukizuflower.client.gui.YKGuiContainerManaTank;
import firis.yuzukizuflower.client.gui.YKGuiContainerPetalWorkbench;
import firis.yuzukizuflower.client.gui.YKGuiContainerRuneWorkbench;
import firis.yuzukizuflower.common.recipe.RecipeBoxedFlower;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeCategory;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeCategory;
import vazkii.botania.client.integration.jei.orechid.OrechidIgnemRecipeCategory;
import vazkii.botania.client.integration.jei.orechid.OrechidRecipeCategory;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeCategory;
import vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeCategory;
import vazkii.botania.client.integration.jei.runicaltar.RunicAltarRecipeCategory;

@JEIPlugin
public class YuzuKizuFlowerJeiPlugin implements IModPlugin {

	@Override
	public void register(@Nonnull IModRegistry registry) {
		registry.handleRecipes(RecipeBoxedFlower.class, 
				BoxedFlowerRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
		
		//「レシピを見る」の設定
		registry.addRecipeClickArea(YKGuiContainerManaTank.class, 131, 44, 16, 14, ManaPoolRecipeCategory.UID);
		registry.addRecipeClickArea(YKGuiContainerPetalWorkbench.class, 121, 50, 24, 17, PetalApothecaryRecipeCategory.UID);
		registry.addRecipeClickArea(YKGuiContainerRuneWorkbench.class, 121, 50, 24, 17, RunicAltarRecipeCategory.UID);
		registry.addRecipeClickArea(YKGuiContainerBoxedBrewery.class, 121, 50, 24, 17, BreweryRecipeCategory.UID);
		
		registry.addRecipeClickArea(YKGuiContainerBoxedPureDaisy.class, 79, 34, 24, 17, PureDaisyRecipeCategory.UID);
		
		registry.addRecipeClickArea(YKGuiContainerBoxedOrechid.class, 73, 40, 24, 17, 
				OrechidRecipeCategory.UID, OrechidIgnemRecipeCategory.UID);
		
	}
}
