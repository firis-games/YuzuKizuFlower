package firis.yuzukizuflower.common.tileentity;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class YKTileScrollChest extends TileEntity {
	
	ItemStackHandler itemHandler;
	
	public YKTileScrollChest() {
		
		/**
		 * 通常ラージチェストの2倍のサイズ
		 */
		this.itemHandler = new ItemStackHandler(9 * 6 * 2);
		
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
		//ItemHandler
		itemHandler.deserializeNBT(compound);
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
		//ItemHandler
        compound.merge(itemHandler.serializeNBT());

        return compound;
    }
	
	public IInventory getIInventory() {
		return new YKTileScrollChest.IScrollInventoryHandler(this.itemHandler, this);
	}
	
	@Override
	public void markDirty() {
		
		super.markDirty();
		
		//同期処理
		//サーバの場合のみ
		if (!world.isRemote) {
			//this.markDirty();
			List<EntityPlayer> list = world.playerEntities;
			Packet<?> pkt = this.getUpdatePacket();
			if (pkt != null) {
				for (EntityPlayer player : list) {
					EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
					mpPlayer.connection.sendPacket(pkt);
				}
			}
		}
	}
	
	/**
	 * IInventoryItemHandler
	 * @author computer
	 *
	 */
	public static class IInventoryHandler implements IInventory {

		public IInventoryHandler(IItemHandler handler, TileEntity tile) {
			this.tile = tile;
			this.hand = handler;
		}
		
		protected IItemHandler hand;
		protected IItemHandler getItemHandler() {
			return this.hand;
			/*
			TileEntity tile1 = null;
			tile1 = tile.getWorld().getTileEntity(tile.getPos());
			
			IItemHandler capability = tile1.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			return capability;
			*/ 
		}
		
		protected TileEntity tile;
		
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
			return this.getItemHandler().getSlots();
		}

		@Override
		public boolean isEmpty() {
			for (int slot = 0; slot < this.getItemHandler().getSlots(); slot++) {
				ItemStack stack = this.getItemHandler().getStackInSlot(slot);
				if (!stack.isEmpty()) {
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
			return this.getItemHandler().getStackInSlot(index);
		}

		/**
		 * 指定したスロットから指定数分のアイテムを取得する
		 */
		@Override
		public ItemStack decrStackSize(int index, int count) {
			ItemStack stack = this.getItemHandler().getStackInSlot(index).splitStack(count);
	        if (!stack.isEmpty()) {
	        	this.markDirty();
	        }
	        return stack;
		}

		@Override
		public ItemStack removeStackFromSlot(int index) {
			return this.getItemHandler().insertItem(index, ItemStack.EMPTY, false);
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			ItemStack slot = this.getItemHandler().getStackInSlot(index);
			this.getItemHandler().extractItem(index, slot.getCount(), false);
			this.getItemHandler().insertItem(index, stack, false);
			this.markDirty();
		}

		@Override
		public int getInventoryStackLimit() {
			return 64;
		}

		@Override
		public void markDirty() {
			tile.markDirty();
			SPacketUpdateTileEntity packet = tile.getUpdatePacket();
			if(packet != null && tile.getWorld() instanceof WorldServer) {
				PlayerChunkMapEntry chunk = ((WorldServer) tile.getWorld()).getPlayerChunkMap().getEntry(tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4);
				if(chunk != null) {
					chunk.sendPacket(packet);
				}
			}	
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
			return this.getItemHandler().isItemValid(index, stack);
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
		
	}
	
	/**
	 * スクロール用のIInventory
	 * IScrollInventoryHandler
	 * @author computer
	 *
	 */
	public static class IScrollInventoryHandler implements IInventory {

		public IScrollInventoryHandler(IItemHandler handler, TileEntity tile) {
			this.tile = tile;
			this.hand = handler;
			this.page = 0;
			this.maxPage = 0;
			
			//最大ページ数を計算する
			float calPage = handler.getSlots() - this.inventoryCount;
			if (calPage > 0) {
				this.maxPage = (int) Math.ceil(calPage / (float)this.inventoryRowCount);
			}
		}
		
		/**
		 * 現在ページを設定
		 * @param page
		 */
		public void setPage(int page) {
			page = Math.min(this.maxPage, page);
			page = Math.max(0, page);
			this.page = page;
		}
		
		public int getMaxPage() {
			return this.maxPage;
		}
		
		protected int page = 0;
		protected int maxPage = 0;
		
		/**
		 * 見せ掛けのインベントリの数
		 */
		protected int inventoryCount = 54;
		
		/**
		 * 1ページあたりのカウント
		 */
		protected int inventoryRowCount = 9;
		
		protected IItemHandler hand;
		protected IItemHandler getItemHandler() {
			return this.hand;
		}
		
		protected TileEntity tile;
		
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
			for (int slot = 0; slot < this.getItemHandler().getSlots(); slot++) {
				ItemStack stack = this.getItemHandler().getStackInSlot(slot);
				if (!stack.isEmpty()) {
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
			index = index + this.page * this.inventoryRowCount;
			return this.getItemHandler().getStackInSlot(index);
		}

		/**
		 * 指定したスロットから指定数分のアイテムを取得する
		 */
		@Override
		public ItemStack decrStackSize(int index, int count) {
			
			index = index + this.page * this.inventoryRowCount;
			
			ItemStack stack = this.getItemHandler().getStackInSlot(index).splitStack(count);
	        if (!stack.isEmpty()) {
	        	this.markDirty();
	        }
	        return stack;
		}

		@Override
		public ItemStack removeStackFromSlot(int index) {
			
			index = index + this.page * this.inventoryRowCount;
			
			return this.getItemHandler().insertItem(index, ItemStack.EMPTY, false);
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			
			index = index + this.page * this.inventoryRowCount;
			
			ItemStack slot = this.getItemHandler().getStackInSlot(index);
			this.getItemHandler().extractItem(index, slot.getCount(), false);
			this.getItemHandler().insertItem(index, stack, false);
			this.markDirty();
		}

		@Override
		public int getInventoryStackLimit() {
			return 64;
		}

		@Override
		public void markDirty() {
			tile.markDirty();
			SPacketUpdateTileEntity packet = tile.getUpdatePacket();
			if(packet != null && tile.getWorld() instanceof WorldServer) {
				PlayerChunkMapEntry chunk = ((WorldServer) tile.getWorld()).getPlayerChunkMap().getEntry(tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4);
				if(chunk != null) {
					chunk.sendPacket(packet);
				}
			}	
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
			
			index = index + this.page * this.inventoryRowCount;
			
			return this.getItemHandler().isItemValid(index, stack);
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
		
	}
}
