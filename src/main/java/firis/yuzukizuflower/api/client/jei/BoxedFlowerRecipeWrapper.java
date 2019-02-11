package firis.yuzukizuflower.api.client.jei;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.recipe.RecipeBoxedFlower;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class BoxedFlowerRecipeWrapper implements IRecipeWrapper {

	protected List<ItemStack> inputsItemStack;

	protected ItemStack outputItemStack;
	
	/**
	 * コンストラクタ
	 * @param recipe
	 */
	public BoxedFlowerRecipeWrapper(RecipeBoxedFlower recipe) {
		
		//箱入りレシピ
		inputsItemStack = new ArrayList<ItemStack>();
		inputsItemStack.add(new ItemStack(YuzuKizuFlower.YuzuKizuBlocks.FLOWER_BOX));
		inputsItemStack.add(
				ItemBlockSpecialFlower.ofType(
						new ItemStack(ModBlocks.specialFlower), recipe.flowerType));
		
		outputItemStack = new ItemStack(RecipeBoxedFlower.flowerMap.get(recipe.flowerType));
    }
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		//レシピ表示設定
		ingredients.setInputs(ItemStack.class, inputsItemStack);
        ingredients.setOutput(ItemStack.class, outputItemStack);
	}
}
