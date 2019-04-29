package firis.yuzukizuflower.common.inventory;

import firis.yuzukizuflower.common.tileentity.YKTileScrollChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class IScrollInventoryItemHandler implements IScrollInventory {
	
	/**
	 * コンストラクタ
	 * @param posList
	 * @param world
	 */
	public IScrollInventoryItemHandler(TileEntity tile) {
		
		this.tile = tile;
		
		//MaxPageを計算する
		float calPage = this.getItemHandler().getSlots() - this.inventoryCount;
		if (calPage > 0) {
			this.maxPage = (int) Math.ceil(calPage / (float)this.inventoryRowCount);
		}
	}
	
	public IScrollInventoryItemHandler(TileEntity tile, boolean animation) {
		this(tile);
		
		animationFlg = animation;
	}
	
	protected boolean animationFlg = false;
	
	/**
	 * YKTileCorporeaChest
	 */
	protected TileEntity tile;

	/**
	 * YKTileCorporeaChest
	 */
	protected World world;
	public World getWorld() {
		return this.tile.getWorld();
	}
	
	/**
	 * IItemHandlerを取得する
	 * @return
	 */
	protected IItemHandler getItemHandler() {
		
		TileEntity tile = this.getWorld().getTileEntity(this.tile.getPos());
		if (tile == null) return null;
		
		IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		return capability;
	}
		
	//**************************************************
	
	/**
	 * 見せ掛けのインベントリの数
	 */
	public int inventoryCount = 54;
	
	/**
	 * 1ページあたりのカウント
	 */
	public int inventoryRowCount = 9;

	public int getMaxPage() {
		return this.maxPage;
	}
	
	protected int page = 0;
	protected int maxPage = 0;
	
	/**
	 * Capabilityのindexを取得する
	 * @param index
	 * @return
	 */
	public int getCapabilityIndex(int index) {
		return index + this.page * this.inventoryRowCount;
	}
	
	//**************************************************
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int getSizeInventory() {
		return this.inventoryCount;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * 指定されたIndexのItemStackを返却する
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		IItemHandler handler = getItemHandler();
		index = getCapabilityIndex(index);
		if (handler == null || handler.getSlots() <= index) return ItemStack.EMPTY.copy();
		return this.getItemHandler().getStackInSlot(index);
	}

	/**
	 * 指定したスロットから指定数分のアイテムを取得する
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		IItemHandler handler = getItemHandler();
		index = getCapabilityIndex(index);
		if (handler == null || handler.getSlots() <= index) return ItemStack.EMPTY.copy();
		
		ItemStack stack = this.getItemHandler().extractItem(index, count, false);
				
		if (!stack.isEmpty()) {
        	this.markDirty();
        }
        return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		IItemHandler handler = getItemHandler();
		index = getCapabilityIndex(index);
		if (handler == null || handler.getSlots() <= index) return ItemStack.EMPTY.copy();
		return this.getItemHandler().insertItem(index, ItemStack.EMPTY, false);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		
		IItemHandler handler = getItemHandler();
		int orgIndex = getCapabilityIndex(index);
		if (handler == null || handler.getSlots() <= index) return;
		
		//やり取りできる場合のみ処理を行う
		if (this.isItemValidForSlot(index, stack)) {
			ItemStack slot = handler.getStackInSlot(orgIndex);
			handler.extractItem(orgIndex, slot.getCount(), false);
			handler.insertItem(orgIndex, stack, false);
		}
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		this.tile.markDirty();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
		if(animationFlg && this.tile instanceof YKTileScrollChest) {
			((YKTileScrollChest) this.tile).animationController.openInventory(player);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if(animationFlg && this.tile instanceof YKTileScrollChest) {
			((YKTileScrollChest) this.tile).animationController.closeInventory(player);
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		IItemHandler handler = getItemHandler();
		index = getCapabilityIndex(index);
		if (handler == null || handler.getSlots() <= index) return false;
		
		//01.isItemValidチェック
		boolean ret = this.getItemHandler().isItemValid(index, stack);
		
		//02.insertItemチェック(スクロールチェストの場合は判断を行わない)
		if (ret && !this.animationFlg) {
			//シミュレート
			ItemStack insertStack = this.getItemHandler().insertItem(index, stack, true);
			//すべてのアイテムを挿入、または1つ以上挿入可能の場合true
			if (insertStack.isEmpty() || stack.getCount() != insertStack.getCount()) {
				ret = true;
			} else {
				ret = false;
			}
		}
		return ret;
	}

	@Override
	public int getField(int id) {
		if (id == 0) {
			return this.getItemHandler().getSlots();
		} else if (id == 1) {
			return this.maxPage;
		}
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public void clear() {
	}
		
/************************************************************/
	
	/**
	 * @Intarface IScrollInventory
	 * @param page
	 */
	public void setScrollPage(int page) {
		page = Math.min(this.maxPage, page);
		page = Math.max(0, page);
		this.page = page;
	}
	
	/**
	 * @Intarface IScrollInventory
	 */
	public int getScrollPage() {
		return this.page;
	}
	
	/**
	 * @Intarface IScrollInventory
	 */
	@Override
	public int getScrollMaxPage() {
		return this.maxPage;
	}
	
	/**
	 * @Intarface IScrollInventory
	 */
	public boolean isLockedScrollSlot(int index) {
		IItemHandler handler = getItemHandler();
		index = getCapabilityIndex(index);
		if (handler == null || handler.getSlots() <= index) return true;
		
		return false;
	}

	/**
	 * @Intarface IScrollInventory
	 */
	@Override
	public int getScrollSlotRowCount() {
		return this.inventoryRowCount;
	}

	/**
	 * @Intarface IScrollInventory
	 */
	@Override
	public int getScrollSlotPageCount() {
		return this.inventoryCount;
	}

	/**
	 * @Intarface IScrollInventory
	 */
	@Override
	public int getOrgSizeInventory() {
		return this.getItemHandler().getSlots();
	}

	@Override
	public void setScrollInitInfo(int inventorySize, int scrollMaxPage) {
		
	}
}