package firis.yuzukizuflower.common.botania;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import firis.yuzukizuflower.common.YKConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class RecipesManaEnchanter {
	
	/**
	 * マナエンチャントのレシピ
	 * @param stack
	 * @param simulate
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(List<ItemStack> stackList, ItemStack catalystStack) {
		
		ManaRecipe ret = null;
		
		//同列エンチャントを許可するかどうか
		boolean isDuplicate = YKConfig.DUPLICATE_ENCHANTMENT;
		
		List<ItemStack> inputStackList = new ArrayList<>();
		List<EnchantmentData> enchantList = new ArrayList<>();
		
		//対象アイテムはItems.ENCHANTED_BOOKのみ
		for (ItemStack encBook : stackList) {
			
			//エンチャント取得
			List<EnchantmentData> bookEnchantDataList = RecipesManaEnchanter.getEnchantmentDataList(encBook);
			
			for (EnchantmentData bookEnchantData : bookEnchantDataList) {
				
				Enchantment bookEnchant = bookEnchantData.enchantment;
				int bookEnchantLv = bookEnchantData.enchantmentLevel;
				
				//付与できる
				if (bookEnchant.canApply(catalystStack)) {
					//既に対象がある場合は除外
					boolean isEnchant = true;
					ListIterator<EnchantmentData> iterator = enchantList.listIterator();
					while (iterator.hasNext()) {
						EnchantmentData enchantData = iterator.next();
						if (enchantData.enchantment == bookEnchant) {
							//レベルが高い場合は差し替え処理を行う
							if (enchantData.enchantmentLevel < bookEnchantLv) {
								isEnchant = true;
								iterator.remove();
							} else {
								isEnchant = false;
							}
							break;
						}
						//許可しない場合のみ同列エンチャントのチェック
						if(!isDuplicate) {
							if(!bookEnchant.isCompatibleWith(enchantData.enchantment)) {
								isEnchant = false;
								break;
							}
						}
					}
					if (isEnchant) {
						enchantList.add(new EnchantmentData(bookEnchant, bookEnchantLv));
						inputStackList.add(encBook);
					}
				}
			}
		}
		
		//エンチャント対象がある場合はレシピを生成する
		if (enchantList.size() > 0) {
			
			ItemStack resultStack = catalystStack.copy();
			int manaCost = 0;
			for (EnchantmentData enchant : enchantList) {
				resultStack.addEnchantment(enchant.enchantment, enchant.enchantmentLevel);
				//マナコストの計算
				manaCost += (int)
						(5000F  * ((15 - Math.min(15, enchant.enchantment.getRarity().getWeight()))
									* 1.05F)
								* ((3F + enchant.enchantmentLevel * enchant.enchantmentLevel)
									* 0.25F)
								* (0.9F + enchantList.size() * 0.05F)
								* (enchant.enchantment.isTreasureEnchantment() ? 1.25F : 1F));
			}
			
			//エンチャントレシピ
			ret = new ManaRecipe(ItemStack.EMPTY.copy(), 
					resultStack,
					manaCost,
					0);
			
		}
		
		return ret;
	}
	
	/**
	 * 触媒のチェック処理
	 * @param stack
	 * @return
	 */
	public boolean isCatalyst(ItemStack stack) {
		
		boolean ret = false;
		
		if (stack.isEmpty()) {
			return false;
		}
		
		//エンチャントできるアイテム かつ スタック数が1 かつ 本でない
		if (stack.isItemEnchantable() 
				&& stack.getCount() == 1 
				&& stack.getItem() != Items.BOOK) {
			ret = true;
		}
		return ret;
	}

	protected static class RecipesInventory extends ItemStackHandler {
		
		public RecipesInventory(List<ItemStack> stackList) {
			stacks = NonNullList.create();
			
			for (ItemStack stack : stackList) {
				stacks.add(stack);
			}
		}
	}
	
	/**
	 * ItemStackに含まれるEnchantmentDataのリストを生成する
	 * @param stack
	 * @return
	 */
	public static List<EnchantmentData> getEnchantmentDataList(ItemStack stack) {

		NBTTagList nbtEnchantList = stack.getEnchantmentTagList();
		if (!stack.isEmpty() && Items.ENCHANTED_BOOK == stack.getItem()) {
			nbtEnchantList = ItemEnchantedBook.getEnchantments(stack);
		}
		
		List<EnchantmentData> encDataList = new ArrayList<>();
		for (int i = 0; i < nbtEnchantList.tagCount(); i++) {
			NBTTagCompound nbtEnchant = nbtEnchantList.getCompoundTagAt(i);
			int enchantId = nbtEnchant.getInteger("id");
			int enchantLv = nbtEnchant.getInteger("lvl");
			Enchantment enchant = Enchantment.getEnchantmentByID(enchantId);
			encDataList.add(new EnchantmentData(enchant, enchantLv));
		}
		return encDataList;
	}
}
