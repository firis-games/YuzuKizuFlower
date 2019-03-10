package firis.yuzukizuflower.common.botania;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class BotaniaHelper {

	/**
	 * マナプールの変換レシピ
	 */
	public static RecipesManaPool recipesManaPool = new RecipesManaPool();
	
	/**
	 * ピュアデイジーのレシピ
	 */
	public static IManaRecipes recipesPureDaisy = new RecipesPureDaisy();
	
	/**
	 * ジェイディッド・アマランサスのレシピ
	 */
	public static IManaRecipes recipesJadedAmaranthus = new RecipesJadedAmaranthus();
	
	/**
	 * オアキド系お花のレシピ
	 */
	public static IManaRecipes recipesOrechid = new RecipesOrechid();
	
	/**
	 * アオーシャン変換レシピ
	 */
	public static IManaRecipes recipesAocean = new RecipesAocean();
	
	/**
	 * エンドフレイムの発電レシピ
	 */
	public static IManaGenerator recipesEndoflame = new GeneratorEndoflame();
	
	/**
	 * ガーマリリスの発電レシピ
	 */
	public static IManaGenerator recipesGourmaryllis = new GeneratorGourmaryllis();
		
	/**
	 * 対象アイテムがbotania:specialFlowerかチェックする
	 * @param stack
	 * @param type
	 * @return 
	 */
	public static boolean isSpecialFlower(ItemStack stack, String type) {
		
		//specialFlower
		if (!stack.isEmpty() 
				&& stack.getItem().getRegistryName().equals(new ResourceLocation("botania:specialflower"))
				&& stack.getItemDamage() == 0
				&& type.equals(ItemBlockSpecialFlower.getType(stack))) {
			return true;
		}
		
		return false;
	}

}
