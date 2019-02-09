package firis.yuzukizuflower.common.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.wand.IWandBindable;

public abstract class YKTileBotaniaGenBase extends TileEntity implements ITickable, IInventory, IWandBindable {


	//IInventory
	//************************************************************
	/**
	 * IInventoryの保存用領域
	 */
	protected NonNullList<ItemStack> inventorySlotItemStack = 
				NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
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
		/*
		return this.world.getTileEntity(this.pos) != this 
				? false : player.getDistanceSq((double)this.pos.getX() + 0.5D,
						(double)this.pos.getY() + 0.5D, 
						(double)this.pos.getZ() + 0.5D) <= 64.0D;
		 */
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
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public int getFieldCount() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void clear() {
		inventorySlotItemStack.clear();
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
        
        this.maxTimer = compound.getInteger("maxTimer");
        this.timer = compound.getInteger("timer");

        this.mana = compound.getInteger("mana");

    }
	
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.inventorySlotItemStack);
        
        compound.setInteger("maxTimer", this.maxTimer);
        compound.setInteger("timer", this.timer);
        
        compound.setInteger("mana", this.mana);
        
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
		System.out.println("getUpdateTag");
		
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
		System.out.println("handleUpdateTag");
        this.readFromNBT(tag);
    }
	
	/**
	 * 旧getDescriptionPacketっぽい
	 */
	@Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
		System.out.println("getUpdatePacket");
        //return super.getUpdatePacket();
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
		System.out.println("onDataPacket");
		
		this.readFromNBT(pkt.getNbtCompound());
    }
	

	@Override
	public void update() {

		//マナスプレッダーとリンクする
		linkCollector();
		
		//燃焼してない場合
		if (this.timer == 0) {
			
			//かまどの燃料かどうか
			int burnTime = TileEntityFurnace.getItemBurnTime(this.getStackInSlot(0));
			//マナが最大値の場合は燃焼ストップ
			if (burnTime <= 0 || mana >= maxMana) {
				this.timer = 0;
				this.maxTimer = 0;
				return;
			}
			
			//
			this.timer = 0;
			this.maxTimer = burnTime;
			this.decrStackSize(0, 1);
			//同期をとる
			this.playerServerSendPacket();
		}
		
		/*
		//マナが最大値の場合は燃焼ストップ
		if (mana >= maxMana) {
			return;
		}
		*/

		this.timer += 1;
		
		//マナを加算する
		//エンドフレイムのマナ仕様
		/*
		 * 1tickあたり3mana
		 * 保持できる最大マナは300mana
		 * 
		 */
		//サーバー側しかやらないようにする
		if (this.getWorld().isRemote) {
			//System.out.println("確認用");
			return;
		}
		mana = Math.min(maxMana, mana + 3);
		
		//マナをスプレッダーへ補充
		emptyManaIntoCollector();
		
		//タイマーリセット
		if (this.timer >= this.maxTimer) {
			this.timer = 0;
		}
		
		//同期をとる
		this.playerServerSendPacket();

	}
	
	//******************************************************************************************
	//マナスプレッダーを設定する
	protected TileEntity linkedCollector = null;
	BlockPos cachedCollectorCoordinates = null;
	public int mana = 0;
	public int maxMana = 300;
	public static final int LINK_RANGE = 6;
	public void linkCollector() {
		boolean needsNew = false;
		if(linkedCollector == null) {
			needsNew = true;

			if(cachedCollectorCoordinates != null) {
				needsNew = false;
				if(this.getWorld().isBlockLoaded(cachedCollectorCoordinates)) {
					needsNew = true;
					TileEntity tileAt = this.getWorld().getTileEntity(cachedCollectorCoordinates);
					if(tileAt != null && tileAt instanceof IManaCollector && !tileAt.isInvalid()) {
						linkedCollector = tileAt;
						needsNew = false;
					}
					cachedCollectorCoordinates = null;
				}
			}
		} else {
			TileEntity tileAt = this.getWorld().getTileEntity(linkedCollector.getPos());
			if(tileAt != null && tileAt instanceof IManaCollector)
				linkedCollector = tileAt;
		}

		//if(needsNew && ticksExisted == 1) { // New flowers only
		if(needsNew) { // New flowers only
			IManaNetwork network = BotaniaAPI.internalHandler.getManaNetworkInstance();
			int size = network.getAllCollectorsInWorld(this.getWorld()).size();
			//if(BotaniaAPI.internalHandler.shouldForceCheck() || size != sizeLastCheck) {
			if(BotaniaAPI.internalHandler.shouldForceCheck()) {
				linkedCollector = network.getClosestCollector(this.getPos(), this.getWorld(), LINK_RANGE);
				//sizeLastCheck = size;
			}
		}
	}
	public void emptyManaIntoCollector() {
		if(linkedCollector != null && isValidBinding()) {
			IManaCollector collector = (IManaCollector) linkedCollector;
			if(!collector.isFull() && mana > 0) {
				int manaval = Math.min(mana, collector.getMaxMana() - collector.getCurrentMana());
				mana -= manaval;
				collector.recieveMana(manaval);
			}
		}
	}
	public boolean isValidBinding() {
		return linkedCollector != null && !linkedCollector.isInvalid() && this.getWorld().getTileEntity(linkedCollector.getPos()) == linkedCollector;
	}
	//******************************************************************************************

		
//******************************************************************************************
	public void playerServerSendPacket() {
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
	
	
	//タイマーの設定はインターフェース化とかしたい
	public int timer = 0;
		
		/** チック */
	public int maxTimer = 200;


	//@IWandable
	//******************************************************************************************
	@Override
	public BlockPos getBinding() {
		if(linkedCollector == null)
			return null;
		return linkedCollector.getPos();
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		int range = 6;
		range *= range;
		double dist = pos.distanceSq(this.getPos());
		if(range >= dist) {
			TileEntity tile = player.world.getTileEntity(pos);
			if(tile instanceof IManaCollector) {
				linkedCollector = tile;
				return true;
			}
		}
		return false;
	}
	
	
	
	
	

}
