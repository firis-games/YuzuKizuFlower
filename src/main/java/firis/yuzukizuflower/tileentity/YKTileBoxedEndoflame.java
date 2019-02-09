package firis.yuzukizuflower.tileentity;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TileBellows;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;

public class YKTileBoxedEndoflame extends TileEntity
									implements ITickable, IInventory, 
													IWandBindable, IManaPool, ISparkAttachable {

	protected int mode;
	public YKTileBoxedEndoflame(int mode) {
		this.mode = mode;
		
		if (mode == 1) {
			this.maxMana = 100000;			
		}
	}
	
	//************************************************************
	
	/**
	 * IInventoryの保存用領域
	 */
	protected NonNullList<ItemStack> inventorySlotItemStack = 
				NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
	
	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO 自動生成されたメソッド・スタブ
		return 2;
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
		if (index != 0) {
			return false;
		}
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

	private int tick = 0;

	@Override
	public void update() {

		//マナプール用の制御？
		manaPoolUpdate();
		
		tick += 1;
		if (tick % 2 != 0) {
			return;
		}
		
		
		//マナスプレッダーとリンクする
		//linkCollector();
		
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
			this.maxTimer = burnTime / 4;
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

		
		//ここから倍率計算
		ItemStack boosts = this.getStackInSlot(1);
		//制限してるから基本エンドフレイムのはず
		//気になるならココでチェックする
		int boostsCnt = boosts.getCount();
		
		//boosts分加算
		this.timer += 1;
		for (int i = 0; i < boostsCnt; i++) {
			if (this.timer >= this.maxTimer) {
				boostsCnt = i;
				break;
			}
			this.timer += 1;
		}
		
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
		mana = Math.min(maxMana, mana + (3 * boostsCnt));
		
		//マナをスプレッダーへ補充
		//emptyManaIntoCollector();
		
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
	protected int mana = 0;
	protected int maxMana = 10000;
	
	public int getMana() {
		return this.mana;
	}
	public int getMaxMana() {
		return this.maxMana;
	}
	
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
	
	
	
	
	
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		
		return super.hasCapability(capability, facing);
    }
	
	net.minecraftforge.items.IItemHandler handlerInv = new net.minecraftforge.items.wrapper.InvWrapper(this) {
		@Override
	    @Nonnull
	    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	    {
			//net.minecraftforge.items.wrapper.InvWrapperのスロットのチェックはisItemValidForSlotを利用している
			//こっちで判定する
			/*
			if (slot == 0 && TileEntityFurnace.isItemFuel(stack)) {
				//
				return stack;
			}
			
			ここで判定からアイテムの追加まで一式やる
			つまり処理を自前でかかないといけないから
			isItemValidForSlotを拡張して判断したほうが楽ね
			
			*/
			return super.insertItem(slot, stack, simulate);
	    }
		@Override
	    @Nonnull
	    public ItemStack extractItem(int slot, int amount, boolean simulate)
	    {
			//出力を拒否
			//decrStackSizeで制御できるようなのでそっちを確認する
/*			if (slot == 0) {
				return ItemStack.EMPTY;
			}
			*/
			return super.extractItem(slot, amount, simulate);
	    }
	};
	@Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
    	    /*
    		@SuppressWarnings("unchecked")をつかわない書き方が下記
    		return (T) handlerInv;
    	    */
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handlerInv);
		}
    	
    	return super.getCapability(capability, facing);
    }
	
	
	//ここからマナプール用
	
//******************************************************************************************
	

	/**
	 * 容量がMAXかどうかの確認
	 * 下にmanaVoidがある場合は問答無用でだめみたい
	 */
	@Override
	public boolean isFull() {
		
		
		Block blockBelow = world.getBlockState(pos.down()).getBlock();
		
		/*
		 * manaCapはblockstateからManaPoolの要領とおもわれるものを取得してるよう
		 * なんでこうしてるかは不明のためとりあえず手持ちのものにあわせる
		 */
		//return blockBelow != ModBlocks.manaVoid && getCurrentMana() >= manaCap;
		return blockBelow != ModBlocks.manaVoid && getCurrentMana() >= maxMana;
		
		
	}

	@Override
	public void recieveMana(int mana) {
		
		//manaCapのかわりに上限を利用してる
		int old = this.mana;
		this.mana = Math.max(0, Math.min(getCurrentMana() + mana, maxMana));
		if(old != this.mana) {
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
			//botaniaの処理いったんは何もしない
			//@here
			//markDispatchable();
			this.playerServerSendPacket();
		}
		
	}

	/**
	 * マナバーストを受け取れる？
	 */
	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}
	
	
	@Override
	public int getCurrentMana() {
		
		/* クリエイティブかどうかで変更されてる？
		 * たぶんクリエイティブ用のマナプールかの判断をやってるっぽい
		 * 無視してよさそうね
		if(world != null) {
			IBlockState state = world.getBlockState(getPos());
			if(state.getProperties().containsKey(BotaniaStateProps.POOL_VARIANT))
				return state.getValue(BotaniaStateProps.POOL_VARIANT) == PoolVariant.CREATIVE ? MAX_MANA : mana;
		}
		*/
		if(world != null) {
			return mana;
		}
		
		return 0;
	}

	/**
	 * 外部から何か供給を受けてたら？
	 * よくわからない
	 * ワンドでスニーククリックしたタイミングで
	 * フラグがトグルしてるみたい？
	 * とりあえず無視してやる
	 */
	@Override
	public boolean isOutputtingPower() {
		
		/*
		 * Returns false if the mana pool is accepting power from other power items,
	     * true if it's sending power into them.
		 */
		return false;
	}

	@Override
	public EnumDyeColor getColor() {
		// TODO 自動生成されたメソッド・スタブ
		return EnumDyeColor.WHITE;
	}

	@Override
	public void setColor(EnumDyeColor color) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	
	//独自update
	public void manaPoolUpdate() {
		if(!ManaNetworkHandler.instance.isPoolIn(this) && !isInvalid())
			ManaNetworkEvent.addPool(this);

		//パーティクルの表示制御
		/*
		if(world.isRemote) {
			double particleChance = 1F - (double) getCurrentMana() / (double) maxMana * 0.1;
			if(Math.random() > particleChance)
				Botania.proxy.wispFX(pos.getX() + 0.3 + Math.random() * 0.5, pos.getY() + 0.6 + Math.random() * 0.25, pos.getZ() + Math.random(), PARTICLE_COLOR.getRed() / 255F, PARTICLE_COLOR.getGreen() / 255F, PARTICLE_COLOR.getBlue() / 255F, (float) Math.random() / 3F, (float) -Math.random() / 25F, 2F);
			return;
		}
		*/
	}
	
	
	
	//スパーク制御
	//******************************************************************************************

	

	//poolとおなじ
	@Override
	public boolean canAttachSpark(ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}

	@Override
	public void attachSpark(ISparkEntity entity) {
		//poolは何もしていない
		
	}

	//基本poolと同じにしている
	@Override
	public int getAvailableSpaceForMana() {
		int space = Math.max(0, maxMana - getCurrentMana());
		if(space > 0)
			return space;
		else if(world.getBlockState(pos.down()).getBlock() == ModBlocks.manaVoid)
			return maxMana;
		else return 0;
	}

	@Override
	public ISparkEntity getAttachedSpark() {
		List<Entity> sparks = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up(), pos.up().add(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
		if(sparks.size() == 1) {
			Entity e = (Entity) sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		//poolとおなじ
		return false;
	}
	
	
	
	
	

}
