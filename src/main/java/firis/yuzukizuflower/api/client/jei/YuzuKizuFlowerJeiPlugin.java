package firis.yuzukizuflower.api.client.jei;

import javax.annotation.Nonnull;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import firis.yuzukizuflower.api.client.jei.transfer.BotaniaWorkbenchRecipeTransferHandler;
import firis.yuzukizuflower.api.client.jei.transfer.BotaniaWorkbenchRecipeTransferInfo;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedBrewery;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedOrechid;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedPureDaisy;
import firis.yuzukizuflower.client.gui.YKGuiContainerManaTank;
import firis.yuzukizuflower.client.gui.YKGuiContainerPetalWorkbench;
import firis.yuzukizuflower.client.gui.YKGuiContainerRuneWorkbench;
import firis.yuzukizuflower.common.container.YKContainerBoxedBrewery;
import firis.yuzukizuflower.common.container.YKContainerPetalWorkbench;
import firis.yuzukizuflower.common.container.YKContainerRuneWorkbench;
import firis.yuzukizuflower.common.recipe.RecipeBoxedFlower;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.runtime.JeiHelpers;
import net.minecraft.item.ItemStack;
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
		
		//IRecipeのJEI表示設定
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
		
		
		//JEI表示の右クリックからレシピを表示する関連付け
		//レシピ表示の左側にもアイコンが表示される
		registry.addRecipeCatalyst(new ItemStack(YuzuKizuBlocks.MANA_TANK, 1, 0), ManaPoolRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(YuzuKizuBlocks.MANA_TANK, 1, 1), ManaPoolRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(YuzuKizuBlocks.MANA_TANK, 1, 2), ManaPoolRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(YuzuKizuBlocks.MANA_TANK, 1, 3), ManaPoolRecipeCategory.UID);
		
		registry.addRecipeCatalyst(new ItemStack(YuzuKizuBlocks.PETAL_WORKBENCH), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(YuzuKizuBlocks.RUNE_WORKBENCH), RunicAltarRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(YuzuKizuBlocks.BOXED_BREWERY), BreweryRecipeCategory.UID);
		
		registry.addRecipeCatalyst(new ItemStack(YuzuKizuBlocks.BOXED_PURE_DAISY), PureDaisyRecipeCategory.UID);
		
		//JEIの「+」ボタン連携
		//Botaniaのレシピ登録が花びら調合所などのクラフト台もinputに含まれているため汎用登録処理が使えない
		//registry.getRecipeTransferRegistry().addRecipeTransferHandler(YKContainerBoxedBrewery.class, 
		//		BreweryRecipeCategory.UID, 0, 16, 18, 36);
		
		//花びら作業台
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(
				new BotaniaWorkbenchRecipeTransferHandler<YKContainerPetalWorkbench>((JeiHelpers) registry.getJeiHelpers(),
				new BotaniaWorkbenchRecipeTransferInfo<YKContainerPetalWorkbench>(YKContainerPetalWorkbench.class, PetalApothecaryRecipeCategory.UID)),
				PetalApothecaryRecipeCategory.UID);
		
		//ルーンの作業台
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(
				new BotaniaWorkbenchRecipeTransferHandler<YKContainerRuneWorkbench>((JeiHelpers) registry.getJeiHelpers(),
				new BotaniaWorkbenchRecipeTransferInfo<YKContainerRuneWorkbench>(YKContainerRuneWorkbench.class, RunicAltarRecipeCategory.UID)),
				RunicAltarRecipeCategory.UID);
		
		//Botania醸造台
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(
				new BotaniaWorkbenchRecipeTransferHandler<YKContainerBoxedBrewery>((JeiHelpers) registry.getJeiHelpers(),
				new BotaniaWorkbenchRecipeTransferInfo<YKContainerBoxedBrewery>(YKContainerBoxedBrewery.class, BreweryRecipeCategory.UID)),
				BreweryRecipeCategory.UID);
	}
}
