package firis.yuzukizuflower.common.inventory;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.botania.ManaRecipe;
import firis.yuzukizuflower.common.tileentity.YKTileBaseManaPool;
import firis.yuzukizuflower.common.tileentity.YKTileRuneWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

public class RuneCraftInventory extends IInventoryItemHandler {

	/**
	 * コンストラクタ
	 * @param tile
	 */
	public RuneCraftInventory(TileEntity tile) {
		super(tile);
	}
	
	/**
	 * 内部インベントリ
	 */
	protected IItemHandler getInventory() {
		if (tile == null) return null;
		IItemHandler capability = ((YKTileRuneWorkbench) tile).inventory;
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
	private List<ItemStack> saveStackList = null; 
	
	/**
	 * Grid変更時のイベント処理
	 */
	public void slotChangedCraftingGrid() {
		
		IItemHandler capability = this.getInventory();
				
		//石チェック
		ItemStack stone = capability.getStackInSlot(16);
		if (!BotaniaHelper.recipesRuneAltar.isStone(stone)) {
			//出力スロットへ設定する
			this.setInventorySlotContents(17, ItemStack.EMPTY.copy());
			return;
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
			saveRecipe = BotaniaHelper.recipesRuneAltar.getMatchesRecipe(stackList);
		}
		
		ManaRecipe result = saveRecipe;
		
		//Manaチェック
		if (result != null) {
			if(result.getMana() > ((YKTileRuneWorkbench) tile).getMana()) {
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
		
		//CraftingGridからアイテムを減らす
		for (int slot = 0 ; slot < 17; slot++) {
			
			//ルーンの場合は減らさない
			if (!BotaniaHelper.recipesRuneAltar.isNoShrink(capability.getStackInSlot(slot))) {
				capability.getStackInSlot(slot).shrink(1);
			}
		}
		
		//マナを減らす
		((YKTileRuneWorkbench) tile).recieveMana(-saveRecipe.getMana());
		
		this.slotChangedCraftingGrid();
	}
	
	
	public int getMana() {
		return ((YKTileBaseManaPool) this.tile).getMana();
	}
	
	public int getMaxMana() {
		return ((YKTileBaseManaPool) this.tile).getMaxMana();
	}

}
