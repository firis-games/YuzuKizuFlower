package firis.yuzukizuflower.common.inventory;

import firis.yuzukizuflower.common.tileentity.YKTileBaseManaPool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class ClientInventory implements IInventory {
	
	/**
	 * Inventory保存領域
	 */
	protected final NonNullList<ItemStack> inventorySlotItemStack;
	
	/**
	 * Field保存領域
	 */
	protected final NonNullList<Integer> fieldList;
	
	protected final IInventory iinventory;
	
	protected final BlockPos pos;
	public BlockPos getPos() {
		return this.pos;
	}
	
	public ClientInventory(IInventory inventory, BlockPos pos) {
		
		this.inventorySlotItemStack = NonNullList.<ItemStack>withSize(inventory.getSizeInventory(), ItemStack.EMPTY);
		
		this.fieldList = NonNullList.<Integer>withSize(inventory.getFieldCount(), 0);
		//初期値設定
		for (int i = 0; i < inventory.getFieldCount(); i++) {
			this.fieldList.set(i, inventory.getField(i));
		}
		
		this.iinventory = inventory;
		
		this.pos = pos;
	}


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
		return this.inventorySlotItemStack.size();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventorySlotItemStack.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(inventorySlotItemStack, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(inventorySlotItemStack, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inventorySlotItemStack.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
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
	}

	@Override
	public void closeInventory(EntityPlayer player) {		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return this.iinventory.isItemValidForSlot(index, stack);
	}

	@Override
	public int getField(int id) {
		//マルチ対策
		//マルチの場合sendWindowPropertyがintからshortへ切り捨てられるため
		//数値の大きなmanaとmaxmanaはTileEntityから取得するように変更
		if (id == BoxedFieldConst.MANA) {
			return ((YKTileBaseManaPool) this.iinventory).getMana();
		} else if(id == BoxedFieldConst.MAX_MANA) {
			return ((YKTileBaseManaPool) this.iinventory).getMaxMana();
		}
		return this.fieldList.get(id);
	}

	@Override
	public void setField(int id, int value) {
		this.fieldList.set(id, value);
	}

	@Override
	public int getFieldCount() {
		return this.fieldList.size();
	}

	@Override
	public void clear() {
	}

}
