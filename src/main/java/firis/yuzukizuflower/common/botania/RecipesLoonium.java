package firis.yuzukizuflower.common.botania;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * ルーニウムのレシピ
 * @author computer
 *
 */
public class RecipesLoonium implements IManaRecipes {
	
	private static Random rand = new Random();
	
	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(@Nonnull ItemStack stack, boolean simulate) {
		
		ManaRecipe recipe = null;
		
		//ルーニウムは35000mana消費
		//直接生成なので少し上乗せ
		recipe = new ManaRecipe(
				stack,
				createLootTableDrop(simulate),
				35000 + 5000, 100);
		
		return recipe;
	}
	
	/**
	 * LootTableからドロップを取得する
	 * @return
	 */
	private ItemStack createLootTableDrop(boolean simulate) {

		//シミュレートonの場合は何もしない
		if (simulate) {
			return ItemStack.EMPTY.copy();
		}
		
		//ワールドを取得
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
		LootContext.Builder ctxBuild = new LootContext.Builder((WorldServer) world);
		LootTable loottable = world
				.getLootTableManager()
				.getLootTableFromLocation(LootTableList.CHESTS_SIMPLE_DUNGEON);
		
		List<ItemStack> resultList = loottable.generateLootForPools(rand, ctxBuild.build());
		
		Collections.shuffle(resultList);
		
		ItemStack result = ItemStack.EMPTY.copy();
		for(ItemStack stack : resultList) {
			result = stack;
			break;
		}
		return result;
	}
}
