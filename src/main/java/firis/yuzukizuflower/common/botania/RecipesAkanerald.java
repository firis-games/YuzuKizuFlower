package firis.yuzukizuflower.common.botania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.registries.GameData;

/**
 * アカネラルドのレシピ
 * @author computer
 *
 */
public class RecipesAkanerald implements IManaRecipes {
	
	private static Random rand = new Random();
	
	//村人職業Registry
	private static RegistryNamespaced<ResourceLocation, VillagerProfession> VillagerProfessionRegistry = GameData.getWrapper(VillagerProfession.class);
	
	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(@Nonnull ItemStack stack, boolean simulate) {
		
		ManaRecipe recipe = null;
		
		//50000mana 100tick
		recipe = new ManaRecipe(
				stack,
				createVillagerItem(simulate),
				50000, 100);
		
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
	private List<ItemStack> populateBuyingList() {
		
		//村人のトレードアイテムリスト
		MerchantRecipeList buyingList = new MerchantRecipeList();
		
		//全レベルの交換アイテムを取得する
		List<EntityVillager.ITradeList> trades = new ArrayList<EntityVillager.ITradeList>();
		
		//職業をランダムで取得
		VillagerProfession prof = VillagerProfessionRegistry.getRandomObject(rand);
		
		//村人タイプの種類
		int profCareer = prof.getRandomCareer(rand);
		for (int level = 0; level < 100; level++) {
			List<EntityVillager.ITradeList> work = prof.getCareer(profCareer).getTrades(level);
			if (work != null && work.size() > 0) {
				trades.addAll(work);
			} else {
				//レベル上限と判断
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
		
		//生成失敗時ははずれを設定
		if (result.size() == 0) {
			result.add(new ItemStack(Blocks.COBBLESTONE));
		}
		return result;
	}
	
}
