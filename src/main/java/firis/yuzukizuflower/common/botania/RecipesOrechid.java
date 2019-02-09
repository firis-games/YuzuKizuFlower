package firis.yuzukizuflower.common.botania;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;

/**
 * オアキド/オアキド・イグネムのレシピ
 * @author computer
 *
 */
public class RecipesOrechid implements IManaRecipes{

	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(@Nonnull ItemStack stack) {
		
		ManaRecipe recipe = null;
		
		//石の場合
		if (!stack.isEmpty() 
				&& stack.getItem().getRegistryName().equals(new ResourceLocation("minecraft:stone"))
				&& stack.getMetadata() == 0) {
			
			//オアキドレシピ
			recipe = new ManaRecipe(new ItemStack(Item.getByNameOrId("minecraft:stone"), 1, 0),
					this.botaniaOrechid(0),
					17500, 100);
			
		//ネザーラックの場合
		}else if (!stack.isEmpty() 
				&& stack.getItem().getRegistryName().equals(new ResourceLocation("minecraft:netherrack"))) {
			//オアキド・イグネムレシピ
			recipe = new ManaRecipe(new ItemStack(Item.getByNameOrId("minecraft:netherrack"), 1, 0),
					this.botaniaOrechid(1),
					20000, 100);
		}
		return recipe;
	}
	
	private static Random rand = new Random();
	
	/**
	 * botania
	 * オアキドとオアキド・イグネムのレシピ変換処理
	 * @return
	 */
	private ItemStack botaniaOrechid(int mode) {
		
		List<WeightedRandom.Item> values = new ArrayList<>();
		Map<String, Integer> map;
		
		if (mode == 0) {
			map = BotaniaAPI.oreWeights;
		} else {
			map = BotaniaAPI.oreWeightsNether;
			
		}

		for(String s : map.keySet())
			values.add(new StringRandomItem(map.get(s), s));
		
		String ore = ((StringRandomItem) WeightedRandom.getRandomItem(rand, values)).s;

		List<ItemStack> ores = OreDictionary.getOres(ore);

		for(ItemStack stack : ores) {
			Item item = stack.getItem();
			String clname = item.getClass().getName();

			// This poem is dedicated to Greg
			//
			// Greg.
			// I get what you do when
			// others say it's a grind.
			// But take your TE ores
			// and stick them in your behind.
			if(clname.startsWith("gregtech") || clname.startsWith("gregapi"))
				continue;
			if(!(item instanceof ItemBlock))
				continue;

			return stack;
		}

		return botaniaOrechid(mode);
	}
	
	private static class StringRandomItem extends WeightedRandom.Item {

		public final String s;

		public StringRandomItem(int par1, String s) {
			super(par1);
			this.s = s;
		}

	}
	
}
