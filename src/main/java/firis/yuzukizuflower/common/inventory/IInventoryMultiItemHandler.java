package firis.yuzukizuflower.common.inventory;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.common.tileentity.YKTileCorporeaChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * 複数のIItemHandlerをひとつのIInventoryとして扱う
 * @author computer
 *
 */
public class IInventoryMultiItemHandler implements IScrollInventory {
	
	/**
	 * コンストラクタ
	 * @param posList
	 * @param world
	 */
	public IInventoryMultiItemHandler(YKTileCorporeaChest tile) {
		this.tile = tile;
		this.world = tile.getWorld();
		this.page = 0;
		this.createCapability();
	}
	
	/**
	 * YKTileCorporeaChest
	 */
	protected YKTileCorporeaChest tile;
	
	/**
	 * 現在のワールド
	 */
	protected World world;
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Capabilityリスト
	 */
	protected List<IItemHandler> itemHandlerList;
	
	/**
	 * 複数のCapabilityをひとつとして扱う
	 */
	protected List<MultiCapability> multiCapabilityList;
	protected class MultiCapability {
		public MultiCapability(int capabilityNo, int capabilityIdx) {
			this.capabilityNo = capabilityNo;
			this.capabilityIdx = capabilityIdx;
		}
		public int capabilityNo;
		public int capabilityIdx;
	}
	
	/**
	 * コーポリアネットワークのPosListを取得
	 * @return
	 */
	protected List<BlockPos> getPosList() {
		return this.tile.getCorporeaBlockPosList();
	}
	
	/**
	 * BlockPosからIItemHandlerのCapabilityを生成する
	 */
	protected void createCapability() {
		
		itemHandlerList = new ArrayList<IItemHandler>();
	
		for (BlockPos pos : this.getPosList()) {
			TileEntity tile = this.getWorld().getTileEntity(pos);
			if (tile == null) continue;
			
			IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (capability == null) continue;
			
			itemHandlerList.add(capability);
		}
		
		
		//管理用MultiCapabilityを生成する
		List<MultiCapability> multiCapabilityList = new ArrayList<MultiCapability>();
		for (int capaNo = 0; capaNo < itemHandlerList.size(); capaNo++) {
			IItemHandler handler = itemHandlerList.get(capaNo);
			for (int idx = 0; idx < handler.getSlots(); idx++) {
				
				//テキスト判断を行う
				if ("".equals(this.textSearch)) {
					multiCapabilityList.add(new MultiCapability(capaNo, idx));					
				} else {
					ItemStack stack = handler.getStackInSlot(idx);
					if (stack.getDisplayName().indexOf(textSearch) != -1
							|| stack.getItem().getRegistryName().toString().indexOf(textSearch) != -1 ) {
						multiCapabilityList.add(new MultiCapability(capaNo, idx));
					}
				}

			}
		}
		this.multiCapabilityList = multiCapabilityList;
		
		//MaxPageを計算する
		this.page = 0;
		this.maxPage = 0;
		float calPage = this.multiCapabilityList.size() - this.inventoryCount;
		if (calPage > 0) {
			this.maxPage = (int) Math.ceil(calPage / (float)this.inventoryRowCount);
		}
	}
	
	//**************************************************
	
	/**
	 * 見せ掛けのインベントリの数
	 */
	protected int inventoryCount = 54;
	
	/**
	 * 1ページあたりのカウント
	 */
	protected int inventoryRowCount = 9;

	protected int page = 0;
	protected int maxPage = 0;
	
	/**
	 * 見せ掛けのindexから実際のCapabilityとindexを取得する
	 * @param index
	 * @return
	 */
	protected MultiCapability getMultiCapability(int index) {
		
		//リストが更新された場合は再構築
		if (this.tile.getUpdateCorporeaBlockPosList()) {
			this.createCapability();
		}
		
		index = index + this.page * this.inventoryRowCount;
		if (this.multiCapabilityList.size() == 0 || this.multiCapabilityList.size() <= index) return null;
		
		MultiCapability mcapa = this.multiCapabilityList.get(index);

		return mcapa;
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
		//ある程度実装してから作る
		return false;
		/*
		for (int slot = 0; slot < this.getItemHandler().getSlots(); slot++) {
			ItemStack stack = this.getItemHandler().getStackInSlot(slot);
			if (!stack.isEmpty()) {
				return false;
			}
		}
        return true;
        */
	}

	/**
	 * 指定されたIndexのItemStackを返却する
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return ItemStack.EMPTY.copy();
		IItemHandler handler = this.itemHandlerList.get(mcapa.capabilityNo);
		index = mcapa.capabilityIdx;

		return handler.getStackInSlot(mcapa.capabilityIdx);
	}

	/**
	 * 指定したスロットから指定数分のアイテムを取得する
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {

		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return ItemStack.EMPTY.copy();
		IItemHandler handler = this.itemHandlerList.get(mcapa.capabilityNo);
		index = mcapa.capabilityIdx;
		
		ItemStack stack = handler.extractItem(index, count, false);
		if (!stack.isEmpty()) {
        	this.markDirty();
        }
        return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		
		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return ItemStack.EMPTY.copy();
		return this.itemHandlerList.get(mcapa.capabilityNo).insertItem(mcapa.capabilityIdx, ItemStack.EMPTY, false);
	}

	/**
	 * 初期の同期で呼び出される
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return;
		IItemHandler handler = this.itemHandlerList.get(mcapa.capabilityNo);
		
		//やり取りできる場合のみ処理を行う
		if (this.isItemValidForSlot(index, stack)) {
			ItemStack slot = handler.getStackInSlot(mcapa.capabilityIdx);
			handler.extractItem(mcapa.capabilityIdx, slot.getCount(), false);
			handler.insertItem(mcapa.capabilityIdx, stack, false);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		this.tile.animationController.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		this.tile.animationController.closeInventory(player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return false;
		
		//01.isItemValidチェック
		IItemHandler handler = this.itemHandlerList.get(mcapa.capabilityNo);
		index = mcapa.capabilityIdx;
		boolean ret = handler.isItemValid(index, stack);
		
		//02.insertItemチェック
		if (ret) {
			//シミュレート
			ItemStack insertStack = handler.insertItem(index, stack, true);
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
			return this.multiCapabilityList.size();
		} else if (id == 1) {
			return this.maxPage;
		} else if (id == 2) {
			return this.page;
		}
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 3;
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
		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return true;
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
		return this.multiCapabilityList.size();
	}

	@Override
	public void setScrollInitInfo(int inventorySize, int scrollMaxPage) {
	}

	@Override
	public void setTextChanged(String text) {
		this.textSearch = text;
		this.createCapability();
		this.tile.playerServerSendPacket();
	}
	
	protected String textSearch = "";
	public String getTextSearch() {
		return textSearch;
	}
	
	public void resetTextChanged() {
		this.createCapability();
		this.tile.playerServerSendPacket();
	}

	@Override
	public void sortInventory() {}
	
}
