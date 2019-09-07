package firis.yuzukizuflower.common.botania;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.wand.ICoordBoundItem;
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
	 * ルーニウム変換レシピ
	 */
	public static IManaRecipes recipesLoonium = new RecipesLoonium();
	
	
	/**
	 * アオーシャン変換レシピ
	 */
	public static IManaRecipes recipesAocean = new RecipesAocean();
	
	/**
	 * アカネラルド変換レシピ
	 */
	public static IManaRecipes recipesAkanerald = new RecipesAkanerald();
	
	/**
	 * クレイコニア変換レシピ
	 * Clayconia
	 */
	public static IManaRecipes recipesClayconia = new RecipesClayconia();
	
	/**
	 * エンドフレイムの発電レシピ
	 */
	public static IManaGenerator recipesEndoflame = new GeneratorEndoflame();
	
	/**
	 * ガーマリリスの発電レシピ
	 */
	public static IManaGenerator recipesGourmaryllis = new GeneratorGourmaryllis();
	
	/**
	 * ケキムラスの発電レシピ
	 */
	public static IManaGenerator recipesKekimurus = new GeneratorKekimurus();
	
	/**
	 * エントロピウムの発電レシピ
	 */
	public static IManaGenerator recipesEntropinnyum = new GeneratorEntropinnyum();
	
	/**
	 * 花びら調合所のレシピ
	 */
	public static RecipesPetal recipesPetal = new RecipesPetal();
	
	/**
	 * ルーンの祭壇のレシピ
	 */
	public static RecipesRuneAltar recipesRuneAltar = new RecipesRuneAltar();
	
	/**
	 * ボタニア醸造台のレシピ
	 */
	public static RecipesBrewery recipesBrewery = new RecipesBrewery();
	
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
	
	/**
	 * 対象アイテムがbotania:twingwandかチェックする
	 * ICoordBoundItemインターフェースをもつアイテムかどうかの判断
	 * @param stack
	 * @return 
	 */
	public static boolean isTwingWand(ItemStack stack) {
		
		if (stack.getItem() instanceof ICoordBoundItem) {
			return true;
		}
		return false;
	}
	
	/**
	 * 森の杖(botania:twingwand)のモードを取得する
	 * ICoordBoundItemインターフェースとbindModeの判断
	 * @param stack
	 * @return mode
	 */
	public static TwingWandMode getTwingWandMode(ItemStack stack) {
		
		if(!isTwingWand(stack)) {
			return TwingWandMode.NONE;
		}
		
		//bindModeがfalseの場合は機能モード
		//それ以外が接続モード（初期値はbindModeがない）
		if (stack.hasTagCompound() 
				&& stack.getTagCompound().hasKey("bindMode")
				&& stack.getTagCompound().getBoolean("bindMode") == false) {
			//機能モード
			return TwingWandMode.FUNC;
		}
		
		//接続モード
		return TwingWandMode.BIND;
	}
	
	/**
	 * 森の杖(botania:twingwand)のモード
	 * NONE:森の杖でない
	 * BIND:接続モード
	 * FUNC:機能モード
	 */
	public enum TwingWandMode {
		NONE,
		BIND,
		FUNC
	};

}
