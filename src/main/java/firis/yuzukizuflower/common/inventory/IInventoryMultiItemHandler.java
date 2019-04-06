package firis.yuzukizuflower.common.inventory;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.common.tileentity.YKTileCorporeaChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
public class IInventoryMultiItemHandler implements IInventory {
	
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

		int invSlots = 0;
		
		for (BlockPos pos : this.getPosList()) {
			TileEntity tile = this.getWorld().getTileEntity(pos);
			if (tile == null) continue;
			
			IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (capability == null) continue;
			
			itemHandlerList.add(capability);
			invSlots += capability.getSlots();
		}
		
		
		//管理用MultiCapabilityを生成する
		List<MultiCapability> multiCapabilityList = new ArrayList<MultiCapability>();
		for (int capaNo = 0; capaNo < itemHandlerList.size(); capaNo++) {
			IItemHandler handler = itemHandlerList.get(capaNo);
			for (int idx = 0; idx < handler.getSlots(); idx++) {
				multiCapabilityList.add(new MultiCapability(capaNo, idx));
			}
		}
		this.multiCapabilityList = multiCapabilityList;
		
		//MaxPageを計算する
		float calPage = invSlots - this.inventoryCount;
		if (calPage > 0) {
			this.maxPage = (int) Math.ceil(calPage / (float)this.inventoryRowCount);
		}
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
		return this.itemHandlerList.get(mcapa.capabilityNo).getStackInSlot(mcapa.capabilityIdx);
	}

	/**
	 * 指定したスロットから指定数分のアイテムを取得する
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {

		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return ItemStack.EMPTY.copy();
		ItemStack stack = this.itemHandlerList.get(mcapa.capabilityNo).getStackInSlot(mcapa.capabilityIdx).splitStack(count);
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

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		
		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return;
		IItemHandler handler = this.itemHandlerList.get(mcapa.capabilityNo);
		ItemStack slot = handler.getStackInSlot(mcapa.capabilityIdx);
		handler.extractItem(mcapa.capabilityIdx, slot.getCount(), false);
		handler.insertItem(mcapa.capabilityIdx, stack, false);
		this.markDirty();
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
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return false;
		return this.itemHandlerList.get(mcapa.capabilityNo).isItemValid(mcapa.capabilityIdx, stack);
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
	}
	
	
	/**
	 * @Intarface setScrollPage
	 * @param page
	 */
	public void setScrollPage(int page) {
		page = Math.min(this.maxPage, page);
		page = Math.max(0, page);
		this.page = page;
	}
	
	public int getScrollPage() {
		return this.page;
	}
	
	public boolean getSlotLocked(int index) {
		MultiCapability mcapa = this.getMultiCapability(index);
		if (mcapa == null) return false;
		return true;
	}
	
}
