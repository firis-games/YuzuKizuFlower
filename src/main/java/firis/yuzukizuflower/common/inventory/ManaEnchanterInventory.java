package firis.yuzukizuflower.common.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import firis.yuzukizuflower.common.YKConfig;
import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.botania.ManaRecipe;
import firis.yuzukizuflower.common.botania.RecipesManaEnchanter;
import firis.yuzukizuflower.common.tileentity.YKTileBaseManaPool;
import firis.yuzukizuflower.common.tileentity.YKTileManaEnchanter;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

public class ManaEnchanterInventory extends IInventoryItemHandler {

	/**
	 * コンストラクタ
	 * @param tile
	 */
	public ManaEnchanterInventory(TileEntity tile) {
		super(tile);
	}
	
	/**
	 * 内部インベントリ
	 */
	protected IItemHandler getInventory() {
		if (tile == null) return null;
		IItemHandler capability = ((YKTileManaEnchanter) tile).inventory;
		return capability;
	}
	
	
	@Override
	public ItemStack getStackInSlot(int index) {
		ItemStack stack = super.getStackInSlot(index);
		
		//indexが0の場合のみGridの判断を行う
		if (index == 0) {
			this.slotChangedCraftingGrid();
		}
		
		return stack;
	}
	
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = super.decrStackSize(index, count);
		//CraftGridの場合
		if (0 <= index && index <= 16) {
			this.slotChangedCraftingGrid();
		}
		//Outputスロットの場合
		if (index == 17) {
			this.slotChangedCraftingResult();
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		super.setInventorySlotContents(index, stack);
		
		//CraftGridの場合
		if (0 <= index && index <= 16) {
			this.slotChangedCraftingGrid();
		}
	}
	
	private ManaRecipe saveRecipe = null;
	private ItemStack saveCatalyst = null;
	private List<ItemStack> saveStackList = null; 
	
	/**
	 * Grid変更時のイベント処理
	 */
	public void slotChangedCraftingGrid() {
		
		IItemHandler capability = this.getInventory();
		//触媒チェック
		ItemStack stackCatalyst = capability.getStackInSlot(16);
		if (!BotaniaHelper.recipesManaEnchanter.isCatalyst(stackCatalyst)) {
			//出力スロットへ設定する
			saveCatalyst = null;
			saveRecipe = null;
			this.setInventorySlotContents(17, ItemStack.EMPTY.copy());
			return;
		}
		if (saveCatalyst == null
				|| saveCatalyst.getItem() != stackCatalyst.getItem()
				|| saveCatalyst.getMetadata() != stackCatalyst.getMetadata()) {
			saveCatalyst = stackCatalyst.copy();
			saveStackList = null;
			saveRecipe = null;
		}
		
		//変換レシピチェック
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		for (int slot = 0; slot < 16; slot++) {
			if (!capability.getStackInSlot(slot).isEmpty()) {
				stackList.add(capability.getStackInSlot(slot));
			}
		}
		
		//保持分と突合せ、一致しない場合はレシピ再取得
		boolean checkFlg = false;
		if (saveStackList != null
				&& saveStackList.size() == stackList.size()) {
			//スタックリストが一致するかチェックする
			boolean chk = true;
			for (int i = 0; i < stackList.size(); i++) {
				if (!ItemStack.areItemsEqual(stackList.get(i), saveStackList.get(i))) {
					chk = false;
				}
			}
			//チェック
			if (chk) {
				checkFlg = true;
			}
		}
		
		if (!checkFlg) {
			saveStackList = stackList;
			saveRecipe = BotaniaHelper.recipesManaEnchanter.getMatchesRecipe(stackList, stackCatalyst);
		}
		
		ManaRecipe result = saveRecipe;
		
		//Manaチェック
		if (result != null) {
			if(result.getMana() > ((YKTileManaEnchanter) tile).getMana()) {
				result = null;
			}
		}
		

		//出力スロットへ設定する
		if (result != null) {
			this.setInventorySlotContents(17, result.getOutputItemStack());
		} else {
			this.setInventorySlotContents(17, ItemStack.EMPTY.copy());
		}
		
	}
	
	/**
	 * Outputスロットからアイテム取得時
	 */
	public void slotChangedCraftingResult() {
	
		IItemHandler capability = this.getInventory();
		
		//付与されたエンチャント取得
		List<EnchantmentData> encRetDataList = RecipesManaEnchanter.getEnchantmentDataList(this.saveRecipe.getOutputItemStack());
		
		//CraftingGridからアイテムを減らす
		if (YKConfig.CONSUME_ENCHANTMENT_BOOK) {
			for (int slot = 0 ; slot < 17; slot++) {
				
				//エンチャント本したかどうかは関係なく
				//同じエンチャントを含む本は一括で減らす
				if (!capability.getStackInSlot(slot).isEmpty()
						&& capability.getStackInSlot(slot).getItem() == Items.ENCHANTED_BOOK) {
					
					//対象エンチャントがすべて削除済みの場合はスキップ
					if (encRetDataList.size() == 0) {
						continue;
					}
					
					//エンチャント本のエンチャントリスト
					List<EnchantmentData> encBookDataList = RecipesManaEnchanter.getEnchantmentDataList(capability.getStackInSlot(slot));
					boolean deleteBook = false;
					for (EnchantmentData encBook : encBookDataList) {
						ListIterator<EnchantmentData> retIterator = encRetDataList.listIterator();
						while (retIterator.hasNext()) {
							EnchantmentData encRetData = retIterator.next();
							//エンチャントとレベルが一致する場合は削除
							if (encBook.enchantment == encRetData.enchantment
									&& encBook.enchantment == encRetData.enchantment) {
								deleteBook = true;
								retIterator.remove();
							}
						}
					}
					//エンチャントブック削除
					if (deleteBook) {
						capability.getStackInSlot(slot).shrink(1);
					}
				} else {
					capability.getStackInSlot(slot).shrink(1);
				}
			}
		} else {
			//武器スロットのみ消費する
			capability.getStackInSlot(16).shrink(1);
		}
		
		//マナを減らす
		((YKTileManaEnchanter) tile).recieveMana(-saveRecipe.getMana());
		
		this.slotChangedCraftingGrid();
	}
	
	
	public int getMana() {
		return ((YKTileBaseManaPool) this.tile).getMana();
	}
	
	public int getMaxMana() {
		return ((YKTileBaseManaPool) this.tile).getMaxMana();
	}
	
	public int getProgressMana() {
		if (saveRecipe == null) return 0;
		return saveRecipe.getMana();
	}

}
