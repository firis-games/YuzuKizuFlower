package firis.yuzukizuflower.common.tileentity;

import java.util.List;

import javax.annotation.Nonnull;
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
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;

public class YKTileBoxedPureDaisy extends TileEntity implements ITickable, IInventory {

	
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
	 * Capabilityのチェックで利用する
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		//
		if (index == 0) {
			//
			return YKTileBoxedPureDaisy.isRecipe(stack);
		}
		
		return false;
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
		
		//レシピに問題なければカウントアップ 違う場合はリセット
		RecipePureDaisy recipe = YKTileBoxedPureDaisy.resultRecipe(this.getStackInSlot(0));
		if (recipe == null) {
			this.timer = 0;
			this.maxTimer = 150;
			return;
		}
		
		//レシピ情報から
		ItemStack stack = new ItemStack(recipe.getOutputState().getBlock());
		ItemStack slotstack = this.getStackInSlot(1);
		
		//resultのアイテムとoutputのアイテムが同一かチェック違う場合は中止
		if (!ItemStack.areItemsEqual(stack, slotstack)
				&& !slotstack.isEmpty()) {
			this.timer = 0;
			this.maxTimer = 150;
			return;
		}
		
		//スタックの最大数判断
		if (!slotstack.isEmpty() && slotstack.getCount() >= slotstack.getMaxStackSize()) {
			this.timer = 0;
			this.maxTimer = 150;
			return;
		}
		
		//処理時間を設定
		this.maxTimer = recipe.getTime();
		
		//
		this.timer += 1;
		
		if (timer < maxTimer) {
			return;
		}
		
		//サーバー側しかやらないようにする
		if (this.getWorld().isRemote) {
			//System.out.println("確認用");
			return;
		}
		
		//結果
		/*
		//System.out.println("確認用2");
		//アイテムを設定する
		MoonRecipes moonRecipe = getRecipesResult();
		ItemStack stack = moonRecipe.getResultItemStack();
		
		if (moonRecipe.enchantment) {
			//エンチャントの場合
			//結果のスタックは無視
			ItemStack encItemStack = this.getStackInSlot(0);
			int encLevel = 35;
			List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.getWorld().rand, 
					encItemStack, encLevel, true);
			
			for (EnchantmentData enchantmentdata : list) {
				encItemStack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
			}
			
			stack = encItemStack;
		}
		
		System.out.println(stack);
		this.setInventorySlotContents(0, stack);
		
		this.decrStackSize(2, 1);
		this.decrStackSize(3, 1);
		this.decrStackSize(4, 1);
		*/
		
		if (slotstack.isEmpty()) {
			slotstack = stack;
		} else {
			slotstack.setCount(slotstack.getCount() + 1);
		}
		
		this.setInventorySlotContents(1, slotstack);
		this.decrStackSize(0, 1);
		
		
		
		//タイマーリセット
		this.timer = 0;
		this.maxTimer = 150;
		

		//同期をとる
		this.playerServerSendPacket();
		
	}
	
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
	
	
	//******************************************************************************************

	public static boolean isRecipe(ItemStack stack) {
		RecipePureDaisy ret = YKTileBoxedPureDaisy.resultRecipe(stack);
		return ret == null ? false : true;
	}
	
	public static RecipePureDaisy resultRecipe(ItemStack stack) {
		RecipePureDaisy result = null;
		for (RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes) {
			
			boolean ret = false;
			Object input = recipe.getInput();
			
			//文字列で格納されている
			if (input instanceof String) {
				String oredict = (String) input;
				//recipe.isOreDictのコピー
				ret = YKTileBoxedPureDaisy.isOreDict(stack, oredict);
			}
			
			if (ret) {
				result = recipe;
				break;
			}
		}
		return result;
	}
	
	private static boolean isOreDict(ItemStack stack, String entry) {
		if(stack.isEmpty())
			return false;

		for(ItemStack ostack : OreDictionary.getOres(entry, false)) {
			if(OreDictionary.itemMatches(ostack, stack, false)) {
				return true;
			}
		}

		return false;
	}
	
	
	
	//******************************************************************************************
	
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
			return super.insertItem(slot, stack, simulate);
	    }
		@Override
	    @Nonnull
	    public ItemStack extractItem(int slot, int amount, boolean simulate)
	    {
			//出力を拒否
			//decrStackSizeで制御できるようなのでそっちを確認する
			if (slot == 0) {
				return ItemStack.EMPTY;
			}
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

}
