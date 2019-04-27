package firis.yuzukizuflower.common.inventory;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.tileentity.YKTilePetalWorkbench;
import firis.yuzukizuflower.common.tileentity.YKWaterFluidHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class PetalInventory extends IInventoryItemHandler {

	/**
	 * コンストラクタ
	 * @param tile
	 */
	public PetalInventory(TileEntity tile) {
		super(tile);
	}
	
	/**
	 * 内部インベントリ
	 */
	protected IItemHandler getInventory() {
		if (tile == null) return null;
		IItemHandler capability = ((YKTilePetalWorkbench) tile).inventory;
		return capability;
	}
	
	/**
	 * 液体Handlerを取得
	 * @return
	 */
	public YKWaterFluidHandler getFluidHandler() {
		if (tile == null) return null;
		IFluidHandler capability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
		return (YKWaterFluidHandler) capability;
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
	
	/**
	 * Grid変更時のイベント処理
	 */
	public void slotChangedCraftingGrid() {
		
		IItemHandler capability = this.getInventory();
		
		//液体チェック
		YKWaterFluidHandler fluidCapability = this.getFluidHandler();
		if (fluidCapability.getLiquid() < 1000) {
			//出力スロットへ設定する
			this.setInventorySlotContents(17, ItemStack.EMPTY.copy());
			return;
		}
		
		//種チェック
		ItemStack seed = capability.getStackInSlot(16);
		if (!BotaniaHelper.recipesPetal.isSeed(seed)) {
			//出力スロットへ設定する
			this.setInventorySlotContents(17, ItemStack.EMPTY.copy());
			return;
		}
		
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		for (int slot = 0; slot < 16; slot++) {
			if (!capability.getStackInSlot(slot).isEmpty()) {
				stackList.add(capability.getStackInSlot(slot));
			}
		}
		
		//変換レシピチェック
		ItemStack result = BotaniaHelper.recipesPetal.getMatchesRecipe(stackList);

		//出力スロットへ設定する
		this.setInventorySlotContents(17, result);
		
	}
	
	/**
	 * Outputスロットからアイテム取得時
	 */
	public void slotChangedCraftingResult() {
	
		IItemHandler capability = this.getInventory();
		
		//CraftingGridからアイテムを減らす
		for (int slot = 0 ; slot < 17; slot++) {
			capability.getStackInSlot(slot).shrink(1);	
		}
		
		//水を減らす
		YKWaterFluidHandler fluidCapability = this.getFluidHandler();
		fluidCapability.drain(1000, true);
		
		this.slotChangedCraftingGrid();
	}

}
