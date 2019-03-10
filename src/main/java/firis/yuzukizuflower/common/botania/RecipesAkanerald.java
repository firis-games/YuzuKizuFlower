package firis.yuzukizuflower.common.botania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

/**
 * アカネラルドのレシピ
 * @author computer
 *
 */
public class RecipesAkanerald implements IManaRecipes {
	
	private static Random rand = new Random();
	
	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(@Nonnull ItemStack stack, boolean simulate) {
		
		ManaRecipe recipe = null;
		
		//20000mana 40tick
		recipe = new ManaRecipe(
				stack,
				createVillagerItem(simulate),
				20000, 40);
		
		return recipe;
	}
	
	private ItemStack createVillagerItem(boolean simulate) {
		
		//シミュレートonの場合は何もしない
		if (simulate) {
			return ItemStack.EMPTY.copy();
		}
		
		ItemStack result = ItemStack.EMPTY.copy();
		List<ItemStack> stackList = populateBuyingList();
		int idx = rand.nextInt(stackList.size());
		result = stackList.get(idx);
		return result;
		
	}
	
	/**
	 * 村人のトレードアイテムを生成する
	 */
	@SuppressWarnings("deprecation")
	private List<ItemStack> populateBuyingList() {
		
		//村人のトレードアイテムリスト
		MerchantRecipeList buyingList = new MerchantRecipeList();
		
		//職業をランダムで取得
		int careerId = VillagerRegistry.FARMER.getRandomCareer(rand);
		
		//全レベルの交換アイテムを取得する
		List<EntityVillager.ITradeList> trades = new ArrayList<EntityVillager.ITradeList>();
		//村人タイプ
		VillagerProfession prof = VillagerRegistry.getById(careerId);
		//村人タイプの種類
		int profCareer = prof.getRandomCareer(rand);
		int level = 0;
		while (true) {
			List<EntityVillager.ITradeList> work = prof.getCareer(profCareer).getTrades(level);
			if (work != null) {
				trades.addAll(work);
				level += 1;
			} else {
				break;
			}
		}
		
		//交換レシピをMerchantRecipeListへ格納する
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
		for (EntityVillager.ITradeList list : trades) {
			list.addMerchantRecipe(new EntityVillager(world), buyingList, rand);
		}
		
		//エメラルド以外を取得
		List<ItemStack> result = new ArrayList<ItemStack>();
		for (MerchantRecipe recipe : buyingList) {
			if (!recipe.getItemToSell().getItem().equals(Items.EMERALD)) {
				result.add(recipe.getItemToSell());
			}
		}
		
		//再帰処理
		if (result.size() == 0) {
			result = populateBuyingList();
		}
		
		return result;
	}
	
}
