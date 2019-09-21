package firis.yuzukizuflower.common.recipe;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

public class BotaniaRecipes {
	
	/**
	 * botania花びらの鉱石辞書
	 */
	public static final String[] OREDICT_PETAL = new String[] {
			"petalWhite", "petalOrange", "petalMagenta", "petalLightBlue",
			"petalYellow", "petalLime", "petalPink", "petalGray",
			"petalLightGray", "petalCyan", "petalPurple", "petalBlue",
			"petalBrown", "petalGreen", "petalRed", "petalBlack"
	};
	
	/**
	 * botaniaルーンの鉱石辞書
	 */
	public static final String[] RUNE = new String[] {
			"runeWaterB", "runeFireB", "runeEarthB", "runeAirB",
			"runeSpringB", "runeSummerB", "runeAutumnB", "runeWinterB",
			"runeManaB", "runeLustB", "runeGluttonyB", "runeGreedB",
			"runeSlothB", "runeWrathB", "runeEnvyB", "runePrideB"
	};
	
	
	/**
	 * botaniaのレシピを追加する
	 */
	public static void init() {
		
		//マナプール変換レシピ
		BotaniaRecipes.addRecipesManaInfusion();
		
		//オアキド変換レシピ
		BotaniaRecipes.addRecipesOrechild();
		
		//ペタルアポセカリー変換レシピ
		BotaniaRecipes.addRecipesPetalApothecary();
		
		//ルーンの祭壇変換レシピ
		BotaniaRecipes.addRecipesRuneAltar();
		
	}
	
	/**
	 * マナプールの変換レシピを追加する
	 */
	protected static void addRecipesManaInfusion() {
		
		RecipeManaInfusion recipe;
    	
    	//エンダーストーン
    	recipe = new RecipeManaInfusion(
    			new ItemStack(Blocks.END_STONE, 1), 
    			new ItemStack(Blocks.STONE, 1, 0),
    			1200);
    	recipe.setCatalyst(Blocks.DRAGON_EGG.getDefaultState());
    	BotaniaAPI.manaInfusionRecipes.add(recipe);
    	
    	//エンダー空気ビン
    	recipe = new RecipeManaInfusion(
    			new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 15), 
    			new ItemStack(Items.GLASS_BOTTLE, 1), 
    			1200);
    	recipe.setCatalyst(Blocks.DRAGON_EGG.getDefaultState());
    	BotaniaAPI.manaInfusionRecipes.add(recipe);
    	
    	//コーラスフルーツ
    	recipe = new RecipeManaInfusion(
    			new ItemStack(Items.CHORUS_FRUIT, 1), 
    			new ItemStack(Items.APPLE, 1), 
    			2400);
    	recipe.setCatalyst(Blocks.DRAGON_EGG.getDefaultState());
    	BotaniaAPI.manaInfusionRecipes.add(recipe);
    	
    	//シェルカーの殻
    	recipe = new RecipeManaInfusion(
    			new ItemStack(Items.SHULKER_SHELL, 1), 
    			new ItemStack(Items.LEATHER, 1), 
    			3600);
    	recipe.setCatalyst(Blocks.DRAGON_EGG.getDefaultState());
    	BotaniaAPI.manaInfusionRecipes.add(recipe);
    	
    	//ドラゴンブレス
    	recipe = new RecipeManaInfusion(
    			new ItemStack(Items.DRAGON_BREATH, 1), 
    			new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 15), 
    			4800);
    	recipe.setCatalyst(Blocks.DRAGON_EGG.getDefaultState());
    	BotaniaAPI.manaInfusionRecipes.add(recipe);
	}
	
	/**
	 * オアキドの変換レシピを追加する
	 */
	protected static void addRecipesOrechild() {
		//グロウストーン
		BotaniaAPI.addOreWeightNether("glowstone", 4900); // Vanilla
	}
	
	/**
	 * ペタルアポセカリー変換レシピを追加する
	 */
	protected static void addRecipesPetalApothecary() {
		//アカリカルチャー
		BotaniaAPI.registerPetalRecipe(
				new ItemStack(YuzuKizuItems.AKARICULTURE), 
				OREDICT_PETAL[1], 
				OREDICT_PETAL[1],
				OREDICT_PETAL[10],
				RUNE[2],
				new ItemStack(Items.DIAMOND_HOE));
		
		//ユクァーリー
		BotaniaAPI.registerPetalRecipe(
				new ItemStack(YuzuKizuItems.YUQUARRY),
				OREDICT_PETAL[10],
				OREDICT_PETAL[10],
				OREDICT_PETAL[10],
				OREDICT_PETAL[1],
				OREDICT_PETAL[11],
				OREDICT_PETAL[14],
				RUNE[8],
				new ItemStack(Items.DIAMOND_PICKAXE));
		
		//アオーシャン
		BotaniaAPI.registerPetalRecipe(
				new ItemStack(YuzuKizuItems.AOCEAN), 
				OREDICT_PETAL[11], 
				OREDICT_PETAL[11],
				OREDICT_PETAL[10],
				RUNE[0],
				new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 2),
				new ItemStack(Items.FISHING_ROD));
		
		//アカネラルド
		BotaniaAPI.registerPetalRecipe(
				new ItemStack(YuzuKizuItems.AKANERALD), 
				OREDICT_PETAL[14], 
				OREDICT_PETAL[14],
				OREDICT_PETAL[10],
				RUNE[3],
				new ItemStack(Blocks.EMERALD_BLOCK),
				new ItemStack(Item.getByNameOrId("botania:storage"), 1, 3));
		
		//育ちすぎた種
		BotaniaAPI.registerPetalRecipe(
				new ItemStack(Item.getByNameOrId("botania:overgrowthseed"), 2), 
				new ItemStack(Item.getByNameOrId("botania:overgrowthseed")), 
				new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 5),
				new ItemStack(Item.getByNameOrId("botania:fertilizer")));
	}
	
	/**
	 * ルーンの祭壇のレシピを追加する
	 */
	protected static void addRecipesRuneAltar() {
		
		//アルフヘイムキー
		BotaniaAPI.registerRuneAltarRecipe(
				new ItemStack(YuzuKizuItems.DIMENSION_KEY, 2),
				150000,
				new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 5),
				new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 18),
				new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 18),
				new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 18),
				new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 18),
				new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 7),
				new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 9));
		
	}

}
