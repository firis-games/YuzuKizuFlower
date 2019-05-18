package firis.yuzukizuflower.common.inventory;

import firis.yuzukizuflower.common.tileentity.YKTileCorporeaChest;
import firis.yuzukizuflower.common.tileentity.YKTileScrollChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class IScrollInventoryClientItemHandler implements IScrollInventory {

	/**
	 * IInventoryの保存用領域
	 */
	protected NonNullList<ItemStack> inventorySlotItemStack;
	
	protected TileEntity tile = null;
	
	public IScrollInventoryClientItemHandler(int invSize, TileEntity tile) {
		this.inventorySlotItemStack = NonNullList.<ItemStack>withSize(invSize, ItemStack.EMPTY);
		this.tile = tile;
	}
	
	public IScrollInventoryClientItemHandler(int invSize) {
		this.inventorySlotItemStack = NonNullList.<ItemStack>withSize(invSize, ItemStack.EMPTY);
	}
	
	/**
	 * IInventory制御のみ
	 */
	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	/**
	 * Inventoryのサイズは使用する際に定義する
	 */
	@Override
	public int getSizeInventory() {
		return this.inventorySlotItemStack.size();
	}
	
	/**
	 * Inventoryが空かどうか判定する
	 */
	@Override
	public boolean isEmpty() {
        for (ItemStack itemstack : this.inventorySlotItemStack)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }
        return true;
	}
	/**
	 * 指定されたIndexのItemStackを返却する
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		return inventorySlotItemStack.get(index);
	}

	/**
	 * 指定したスロットから指定数分のアイテムを取得する
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(inventorySlotItemStack, index, count);
        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }
        return itemstack;
	}

	/**
	 * 指定したスロットからItemStackを取得する
	 * 取得先のスロットは空になる
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(inventorySlotItemStack, index);
	}

	/**
	 * 指定したスロットにアイテムスタックをセットする
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		
		inventorySlotItemStack.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
	}

	/**
	 * InventoryのItemStackの上限数
	 * 通常であればそのまま利用できる
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * PlayerがTileEntityを使用できるかどうか
	 */
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
		if(this.tile != null && this.tile instanceof YKTileCorporeaChest) {
			((YKTileCorporeaChest) this.tile).animationController.openInventory(player);
		} else if(this.tile != null && this.tile instanceof YKTileScrollChest) {
			((YKTileScrollChest) this.tile).animationController.openInventory(player);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if(this.tile != null && this.tile instanceof YKTileCorporeaChest) {
			((YKTileCorporeaChest) this.tile).animationController.closeInventory(player);
		} else if(this.tile != null && this.tile instanceof YKTileScrollChest) {
			((YKTileScrollChest) this.tile).animationController.closeInventory(player);
		}
	}


	/**
	 * 対象スロットの許可不許可チェック
	 * Capabilityのチェックで利用する
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		if (id == 0) {
			return this.inventorySize;
		} else if (id == 1) {
			return this.scrollMaxPage;
		}
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		if (id == 0) {
			this.inventorySize = value;
		} else if (id == 1) {
			this.scrollMaxPage = value;
		} else if (id == 2) {
			this.scrollPage = value;
		}
	}

	@Override
	public int getFieldCount() {
		return 3;
	}

	@Override
	public void clear() {
		inventorySlotItemStack.clear();
	}
	
	@Override
	public void markDirty() {
	}
	
	/************************************************************/

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int getScrollMaxPage() {
		return this.scrollMaxPage;
	}

	@Override
	public int getScrollPage() {
		return this.scrollPage;
	}

	@Override
	public void setScrollPage(int page) {
		page = Math.min(this.scrollMaxPage, page);
		page = Math.max(0, page);
		this.scrollPage = page;
	}

	@Override
	public boolean isLockedScrollSlot(int index) {
		
		index = getCapabilityIndex(index);
		if (this.inventorySize <= index) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 見せ掛けのインベントリの数
	 */
	protected int inventoryCount = 54;
	
	/**
	 * 1ページあたりのカウント
	 */
	protected int inventoryRowCount = 9;


	@Override
	public int getScrollSlotRowCount() {
		return this.inventoryRowCount;
	}

	@Override
	public int getScrollSlotPageCount() {
		return this.inventorySlotItemStack.size();
	}

	@Override
	public int getOrgSizeInventory() {
		return this.inventorySize;
	}

	protected int inventorySize = 0;
	protected int scrollMaxPage = 0;
	protected int scrollPage = 0;
	
	@Override
	public void setScrollInitInfo(int inventorySize, int scrollMaxPage) {
		this.inventorySize = inventorySize;
		this.scrollMaxPage = scrollMaxPage;
	}
	
	
	/**
	 * Capabilityのindexを取得する
	 * @param index
	 * @return
	 */
	public int getCapabilityIndex(int index) {
		return index + this.scrollPage * this.inventoryRowCount;
	}

	@Override
	public void setTextChanged(String text) {
		textSearch = text;
	}
	
	protected String textSearch = "";
	public String getTextSearch() {
		return textSearch;
	}
}
