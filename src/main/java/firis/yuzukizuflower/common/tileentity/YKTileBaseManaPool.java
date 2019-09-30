package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ManaNetworkHandler;

/**
 * マナを利用するブロックのベースクラス
 * @author computer
 *
 */
public abstract class YKTileBaseManaPool extends YKTileBaseInventory 
										implements ITickable, IManaPool, ISparkAttachable, 
										ISubTileContainer, IWandBindable {
	/**
	 * マナ最大容量
	 */
	protected int maxMana = 1000000;
	
	public int getMaxMana() {
		return this.maxMana;
	}
	/**
	 * 現時点のマナ
	 */
	protected int mana = 0;
	public int getMana() {
		return this.mana;
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
        this.mana = compound.getInteger("mana");

    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        compound.setInteger("mana", this.mana);
        
        return compound;
    }
	
	/**
	 * レッドストーン信号を受けているかを判断する
	 * @return
	 */
	public boolean isRedStonePower() {
		int redstone = 0;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = this.getWorld().getRedstonePower(this.getPos().offset(dir), dir);
			redstone = Math.max(redstone, redstoneSide);
		}
		return redstone > 0;
	}
	
	/**
	 * コンパレータ用の制御
	 * @param mana
	 * @param max
	 * @return
	 */
	public int getComparatorInputOverride() {
		
		if (this.maxMana == 0) {
			return 0;
		}
		
		int val = (int) ((double) this.mana / (double) this.maxMana * 15.0);
		if(mana > 0)
			val = Math.max(val, 1);
		return val;
	}
	
	/**
	 * ****************************************************************************************************
	 * IManaPool
	 * ****************************************************************************************************
	 */
	/**
	 * @interface IManaPool
	 * 容量がMAXかどうかの確認
	 */
	@Override
	public boolean isFull() {
		Block blockBelow = world.getBlockState(pos.down()).getBlock();
		return blockBelow != ModBlocks.manaVoid && getCurrentMana() >= maxMana;	
	}

	/**
	 * @interface IManaPool
	 */
	@Override
	public void recieveMana(int mana) {
		
		//manaCapのかわりに上限を利用してる
		int old = this.mana;
		this.mana = Math.max(0, Math.min(getCurrentMana() + mana, maxMana));
		
		if(old != this.mana) {
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
			//botaniaの処理いったんは何もしない
			//markDispatchable();
		}
		
	}

	/**
	 * マナバーストの受け取り判定
	 * @interface IManaPool
	 */
	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}
	
	
	/**
	 * @interface IManaPool
	 */
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
	 * @interface IManaPool
	 * おそらくマナタブレットの出し入れのモードの制御をやっている？
	 */
	@Override
	public boolean isOutputtingPower() {
		/*
		 * Returns false if the mana pool is accepting power from other power items,
	     * true if it's sending power into them.
		 */
		return false;
	}

	/**
	 * @interface IManaPool
	 */
	@Override
	public EnumDyeColor getColor() {
		return EnumDyeColor.WHITE;
	}

	/**
	 * @interface IManaPool
	 */
	@Override
	public void setColor(EnumDyeColor color) {
	}
	
	/**
	 * ****************************************************************************************************
	 * ISparkAttachable
	 * ****************************************************************************************************
	 */

	/**
	 * @interface ISparkAttachable
	 */
	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	/**
	 * @interface ISparkAttachable
	 */
	@Override
	public void attachSpark(ISparkEntity entity) {		
	}

	/**
	 * @interface ISparkAttachable
	 */
	@Override
	public int getAvailableSpaceForMana() {
		int space = Math.max(0, maxMana - getCurrentMana());
		if(space > 0)
			return space;
		else if(world.getBlockState(pos.down()).getBlock() == ModBlocks.manaVoid)
			return maxMana;
		else return 0;
	}

	/**
	 * @interface ISparkAttachable
	 */
	@Override
	public ISparkEntity getAttachedSpark() {
		List<Entity> sparks = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up(), pos.up().add(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
		if(sparks.size() == 1) {
			Entity e = (Entity) sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	/**
	 * @interface ISparkAttachable
	 */
	@Override
	public boolean areIncomingTranfersDone() {
		return false;
	}
	
	
	//******************************************************************************************
	// アイテムの入出力スロット操作用
	//******************************************************************************************
	
	/**
	 * inputスロットのindex
	 */
	protected List<Integer> inputSlotIndex = new ArrayList<Integer>();

	/**
	 * outputスロットのindex
	 */
	protected List<Integer> outputSlotIndex = new ArrayList<Integer>();
	
	/**
	 * workスロットのindex
	 */
	protected List<Integer> workSlotIndex = new ArrayList<Integer>();
	
	
	/**
	 * 入力側のItemStackを取得する
	 * @return
	 */
	public ItemStack getStackInputSlotFirst() {
		if (inputSlotIndex.size() <= 0) {
			return ItemStack.EMPTY.copy();
		}
		return getStackInSlot(inputSlotIndex.get(0));
	}
	
	/**
	 * 入力側のItemStackのリストを取得する
	 * @return
	 */
	public List<ItemStack> getStackInputSlotList() {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		if (inputSlotIndex.size() <= 0) {
			return stackList;
		}
		
		for(int idx : inputSlotIndex) {
			stackList.add(getStackInSlot(idx));
		}
		return stackList;
	}
	
	/**
	 * 入力側のItemStackを取得する
	 * @return
	 */
	public ItemStack getStackWorkSlotFirst() {
		if (workSlotIndex.size() <= 0) {
			return ItemStack.EMPTY.copy();
		}
		return getStackInSlot(workSlotIndex.get(0));
	}
	
	/**
	 * inputスロットのアイテムを指定数分減らす
	 */
	public void decrStackSizeInputSlot(int count) {
		
		for(int idx : this.inputSlotIndex) {
			
			this.decrStackSize(idx, count);
		}
	}
	
	/**
	 * 空きoutputスロットにアイテムを入れる
	 * @param stack
	 * @return
	 */
	public boolean insertOutputSlotItemStack(ItemStack stack) {
		
		boolean ret = false;
		
		for(int idx : outputSlotIndex) {
			ItemStack inv = this.getStackInSlot(idx);
			 if (ItemStack.areItemsEqual(stack, inv)
						&& inv.getCount() + stack.getCount() <= inv.getMaxStackSize()) {
				//同じアイテムかつ空き容量が1件以上ある場合
				inv.setCount(inv.getCount() + stack.getCount());
				this.setInventorySlotContents(idx, inv.copy());
				ret = true;
				break;
			}
		}
		if(ret) return ret;
		
		for(int idx : outputSlotIndex) {
			ItemStack inv = this.getStackInSlot(idx);
			if (inv.isEmpty()) {
				//空の場合はそのまま挿入する
				this.setInventorySlotContents(idx, stack.copy());
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * outputスロットがすべて埋まっているかの判断を行う
	 * @return
	 */
	public boolean isFillOutputSlot() {
		
		for(int idx : outputSlotIndex) {
			ItemStack itemstack = this.getStackInSlot(idx);
			if (itemstack.isEmpty())
            {
                return false;
            }
		}
        return true;
	}
	
	/**
	 * outputスロットがすべて埋まっているかの判断を行う
	 * スタックできるかも確認する
	 * @return
	 */
	public boolean isFillOutputSlotStack(ItemStack stack) {
		
		for(int idx : outputSlotIndex) {
			ItemStack itemstack = this.getStackInSlot(idx);
			if (itemstack.isEmpty())
            {
                return false;
            } else if (ItemStack.areItemsEqual(stack, itemstack)
            		&& stack.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize()) {
            	//ItemStackが同じ場合 かつ 最大値以下の場合
            	return false;
            }
		}
        return true;
	}
	
	//******************************************************************************************
	// 機能系のお花との自動リンク用
	//******************************************************************************************
	protected boolean autoManaLink() {
		return true;
	}
	
	/**
	 * @interface ITickable
	 */
	@Override
	public void update() {
		//マナプール管理へ追加する
		if(autoManaLink() && !ManaNetworkHandler.instance.isPoolIn(this) && !isInvalid())
			ManaNetworkEvent.addPool(this);
	}
	
	/**
	 * マナプール管理から除外する
	 */
	@Override
	public void invalidate() {
		super.invalidate();
		if (autoManaLink()) {
			ManaNetworkEvent.removePool(this);
		}
	}

	/**
	 * マナプール管理から除外する
	 */
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (autoManaLink()) {
			ManaNetworkEvent.removePool(this);
		}
	}
	
		
	//******************************************************************************************
	// アイテムの入出力の制御
	//******************************************************************************************
	
	/**
	 * 入力スロットの制御
	 * @Intarface ISidedInventory
	 */
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (this.inputSlotIndex.indexOf(index) < 0) {
			return false;
		}
		return isItemValidRecipesForInputSlot(itemStackIn);
	}

	/**
	 * 出力スロットの制御
	 * @Intarface ISidedInventory
	 */
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (this.outputSlotIndex.indexOf(index) < 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 対象スロットの使用許可
	 * @Intarface IInventory
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		//inputスロット以外は不許可
		if (this.inputSlotIndex.indexOf(index) < 0) {
			return false;
		}
		
		//レシピで判断する
		return isItemValidRecipesForInputSlot(stack);
	}
	
	/**
	 * inputスロットのレシピチェック用
	 * @param stack
	 * @return
	 */
	public boolean isItemValidRecipesForInputSlot(ItemStack stack) {
		return true;
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
			YKTileBaseManaPool tile = (YKTileBaseManaPool) this.getInv();
			//Capability経由はinputスロットのみ許可
			if (tile.inputSlotIndex.indexOf(slot) < 0) {
				return stack;
			}
			return super.insertItem(slot, stack, simulate);
		}
		
		@Override
	    @Nonnull
	    public ItemStack extractItem(int slot, int amount, boolean simulate)
	    {
			YKTileBaseManaPool tile = (YKTileBaseManaPool) this.getInv();
			
			//Capability経由はoutputスロットのみ許可
			if (tile.outputSlotIndex.indexOf(slot) < 0) {
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
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handlerInv);
		}
    	return super.getCapability(capability, facing);
    
    }
	
	//******************************************************************************************
	// vazkii.botania.client.core.handler.BlockHighlightRenderHandler
	// 箱入りお花の範囲表示用のダミーのSubTileEntityを作成する
	//******************************************************************************************
	/**
	 * @ISubTileContainer
	 */
	public SubTileEntity getSubTile() {
		return null;
	}

	/**
	 * @ISubTileContainer
	 */
	public void setSubTile(String name) {
		//なにもしない
	}
	
	
	
	//******************************************************************************************
	// vazkii.botania.client.core.handler.BlockHighlightRenderHandler
	// 箱入りお花の範囲表示でbindするために追加
	//******************************************************************************************
	/**
	 * @IWandBindable
	 */
	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	/**
	 * @IWandBindable
	 */
	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	/**
	 * @IWandBindable
	 */
	public BlockPos getBinding() {
		return null;
	}

}
