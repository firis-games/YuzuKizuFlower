package firis.yuzukizuflower.common.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

/**
 * Inventory基本ロジックのベース
 * IInventoryのNBT保存とかをまとめる
 * @author computer
 *
 */
public abstract class YKTileBaseInventory extends TileEntity implements ISidedInventory {
	
	/**
	 * IInventoryの保存用領域
	 */
	protected NonNullList<ItemStack> inventorySlotItemStack = 
				NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
	
	/**
	 * IInventory
	 *********************************************************************************/
	
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
	public abstract int getSizeInventory();
	
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

	/**
	 * Inventoryを開く際の処理
	 */
	@Override
	public void openInventory(EntityPlayer player) {
		this.markDirty();
	}

	/**
	 * Inventoryを閉じる際の処理
	 */
	@Override
	public void closeInventory(EntityPlayer player) {
		this.markDirty();
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
		inventorySlotItemStack.clear();
	}
	
	
	/**
	 * ISidedInventory
	 *********************************************************************************/
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] slot = new int[this.getSizeInventory()];
		for (int i = 0; i < this.getSizeInventory(); i++) {
			slot[i] = i;
		}
		return slot;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}
	
	
	
	/**
	 * **************************************************
	 * NBT関連
	 * 基本的にreadFromNBT/writeToNBTでデータのやり取りを行う
	 * ただし、同期させるには手動で同期を行うかICrafting等を利用する必要あり
	 * 手動同期の場合はWorldのworldObj.markBlockForUpdate(this.pos);
	 * で行うことができる
	 * パケット通信についてはバニラデフォルトの機能を利用
	 * カスタムパケットはまた今度
	 * **************************************************
	 */
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
        this.inventorySlotItemStack = 
        		NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventorySlotItemStack);
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.inventorySlotItemStack);
        return compound;
    }
	
	/**
	 * ログインのタイミングの同期に使われてる？
	 * チャンクデータの同期のタイミングでつかわれてるっぽい
	 * SPacketChunkData
	 */
	@Override
    public NBTTagCompound getUpdateTag()
    {
        //return super.getUpdateTag();
		//NBTTagにTileEntityの情報を書き込む
		return this.writeToNBT(new NBTTagCompound());
    }
	
    /**
     * Called when the chunk's TE update tag, gotten from {@link #getUpdateTag()}, is received on the client.
     * <p>
     * Used to handle this tag in a special way. By default this simply calls {@link #readFromNBT(NBTTagCompound)}.
     *
     * @param tag The {@link NBTTagCompound} sent from {@link #getUpdateTag()}
     * 
     * チャンクのほう
     */
	@Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }
	
	/**
	 * 旧getDescriptionPacketっぽい
	 */
	@Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
		return new SPacketUpdateTileEntity(this.pos, 0, this.writeToNBT(new NBTTagCompound()));
    }

    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
	/**
	 * クライアントサイドでgetDescriptionPacketの処理のあとに呼び出されるとおもわれる
	 * NBTからクラス情報への反映等を行う?
	 * 
	 * 
	 * コメントによるとパケットを受け取ったタイミングで処理が行われるらしい
	 */
	@Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {
		this.readFromNBT(pkt.getNbtCompound());
    }
	
	/**
	 * 追加処理分
	 *********************************************************************************/
	
	/**
	 * サーバー->クライアントのデータ同期用
	 * サーバーからクライアントへデータを送信する
	 */
	protected void playerServerSendPacket() {
		//player listを取得
		World world = this.getWorld();
		
		//サーバの場合のみ
		if (!world.isRemote) {
			this.markDirty();
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

}
