package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Predicates;

import firis.yuzukizuflower.common.botania.ManaInfusionAPI;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ManaNetworkHandler;

public class YKTileManaTank extends YKTileBaseInventory 
								implements ITickable, IManaPool, ISparkAttachable {
	
	
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
	 * 内部インベントリのサイズ
	 */
	@Override
	public int getSizeInventory() {
		return 7;
	}

	
	private int tick = 0;
	
	/**
	 * @interface ITickable
	 */
	@Override
	public void update() {
		//計算用
		tick = tick < 10000000 ? tick + 1 : 0;
				
		if(!ManaNetworkHandler.instance.isPoolIn(this) && !isInvalid())
			ManaNetworkEvent.addPool(this);
		
		//クライアントは処理を行わない
		if (this.getWorld().isRemote) {
			return;
		}
		
		//負荷軽減のため10tickに1回処理を行う
		if (tick % 10 != 0) {
			return;
		}
		
		//マナ変換処理
		if (updateManaPoolConvert()) {
		} else if (updateManaRelease()) {
		} else if (updateManaLotus()){
		}
		
		//充電処理
		updateManaCharge();
	}
	
	/**
	 * マナ変換処理
	 * @return
	 */
	protected boolean updateManaPoolConvert() {
		
		boolean ret = false;

		ItemStack stack = this.getStackInSlot(0);
		
		IBlockState catalystState = null;
		ItemStack catalyst = this.getStackInSlot(6); 
		if (catalyst.getItem() instanceof ItemBlock) {
			catalystState = ((ItemBlock)(catalyst.getItem())).getBlock().getDefaultState();
		}
		
				
		//レシピ変換
		//RecipeManaInfusion recipe = ManaInfusionAPI.getMatchingRecipe(stack, getWorld().getBlockState(getPos().down()));
		RecipeManaInfusion recipe = ManaInfusionAPI.getMatchingRecipe(stack, catalystState);
		
		if (recipe != null) {
			
			//必要な情報を準備
			int recipeMana = recipe.getManaToConsume();
			ItemStack outputSlot = this.getStackInSlot(1);
			ItemStack outputRecipe = recipe.getOutput().copy();
			
			//マナが既定数以下
			if (recipeMana > getMana()) {
				return ret;
			}
			
			//resultのアイテムとoutputのアイテムが同一かチェック違う場合は中止
			if (!ItemStack.areItemsEqual(outputSlot, outputRecipe)
					&& !outputSlot.isEmpty()) {
				return ret;
			}
			
			//スタックの最大をチェック
			if (!outputSlot.isEmpty() && outputSlot.getCount() >= outputSlot.getMaxStackSize()) {
				return ret;
			}
			
			//問題ない場合は処理を行う
			//マナをマイナス
			this.recieveMana(-recipeMana);
			
			//スタックをひとつ減らす
			stack.shrink(1);
			
			//outputスロットに設定
			if (outputSlot.isEmpty()) {
				outputSlot = outputRecipe;
			} else {
				outputSlot.setCount(outputSlot.getCount() + outputRecipe.getCount());
			}
			this.setInventorySlotContents(1, outputSlot);
			
			//同期
			this.playerServerSendPacket();
			
			ret = true;
			
		}
		
		return ret;
		
	}
	
	/**
	 * マナチャージ系アイテム
	 * @return
	 */
	protected boolean updateManaRelease() {
		
		ItemStack stack = this.getStackInSlot(0);
		
		if (stack.isEmpty() || !(stack.getItem() instanceof IManaItem)) {
			return false;
		}
		
		//マナアイテム
		IManaItem manaItem = (IManaItem) stack.getItem();
		
		//マナ出力できるかの判断
		if (!manaItem.canExportManaToPool(stack, this)) {
			return false;
		}
		
		//outputが空かどうか判断
		ItemStack outputSlot = this.getStackInSlot(1);
		if (!outputSlot.isEmpty()) {
			return false;
		}
		
		//マナを抽出（1回あたり5万）
		int reciveMana = Math.min(getMaxMana() - getMana(), manaItem.getMana(stack));
		reciveMana = Math.min(50000, reciveMana);
		
		manaItem.addMana(stack, -reciveMana);
		this.recieveMana(reciveMana);
		this.playerServerSendPacket();
		
		//マナタンクが最大値の場合は処理を終了する
		//マナアイテムの中身が空っぽの場合もおわる
		if (getCurrentMana() >= getMaxMana() || manaItem.getMana(stack) == 0) {
			//outputへ移動する
			this.setInventorySlotContents(1, stack.copy());
			this.setInventorySlotContents(0, ItemStack.EMPTY);
			//同期
			this.playerServerSendPacket();
			return true;
		}
		
		return true;
	}
	
	/**
	 * マナチャージ系アイテムのチャージ
	 * @return
	 */
	protected boolean updateManaCharge() {
		
		boolean ret = false;
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		stackList.add(this.getStackInSlot(2));
		stackList.add(this.getStackInSlot(3));
		stackList.add(this.getStackInSlot(4));
		stackList.add(this.getStackInSlot(5));
		
		//4つ処理を行う
		for (ItemStack stack : stackList) {
			
			//マナタブレット系アイテム
			if (!stack.isEmpty() && stack.getItem() instanceof IManaItem) {
				
				IManaItem manaItem = (IManaItem) stack.getItem();
				
				//マナ入力できるかの判断
				if (manaItem.canReceiveManaFromPool(stack, this)) {
					//マナを抽出（1回あたり5万）
					int reciveMana = Math.min(getMana(), manaItem.getMaxMana(stack) - manaItem.getMana(stack));
					reciveMana = Math.min(50000, reciveMana);
					
					//マナ注入
					manaItem.addMana(stack, reciveMana);
					this.recieveMana(-reciveMana);
					
					ret = true;
				}
			}
			
			//マナツール系（こっちは独自実装）
			if (!stack.isEmpty() && stack.getItem() instanceof IManaUsingItem) {
				
				IManaUsingItem manaItem = (IManaUsingItem) stack.getItem();
				
				//マナ入力できるかの判断
				if (manaItem.usesMana(stack)) {
					
					//ツールかどうかを暫定で判断する
					if (stack.getMaxDamage() > 0 && stack.getMaxStackSize() == 1) {
						
						//対象が消耗状態かどうかを判断する
						if (stack.getItemDamage() > 0) {
						
							//一律で回復10回復（1あたり100mana）
							int damage = Math.min(stack.getItemDamage(), 10);
							int reciveMana = Math.min(getMana(), damage * 100);
							//端数を考慮して計算
							reciveMana = (int)(Math.floor((double)reciveMana / 100) * 100); 
							damage = reciveMana / 100;
							
							//マナ注入
							this.recieveMana(-reciveMana);
							stack.setItemDamage(stack.getItemDamage() - damage);
							ret = true;
						}
						
					}
				}				
			}
		}
		
		if (ret) {
			//同期処理
			this.playerServerSendPacket();
		}
		
		return true;
	}
	
	/**
	 * 黒いスイレン専用の変換処理
	 * @return
	 */
	protected boolean updateManaLotus() {
		
		boolean ret = false;
		//スイレンを取り込む
			
		ItemStack stack = this.getStackInSlot(0);
		
		//botania:blacklotus
		if (!stack.isEmpty()) {
			//アイテムIDを確認する
			if (stack.getItem().getRegistryName().equals(new ResourceLocation("botania:blacklotus"))) {
				if (!this.isFull()) {
					//黒スイレンの判断
					int meta = stack.getMetadata();
					//マナ数を判断
					int mana = 0;
					
					if (meta == 1) {
						mana = 100000;
					} else if (meta == 0) {
						mana = 8000;
					}
					
					this.recieveMana(mana);
					stack.shrink(1);
					//同期
					this.playerServerSendPacket();
					ret = true;
				}
				//満タンの場合はアイテムを移動する
				if (this.isFull()) {
					this.setInventorySlotContents(1, stack.copy());
					this.setInventorySlotContents(0, ItemStack.EMPTY);
					this.playerServerSendPacket();
					ret = true;
				}
			}
			
		}		
		return ret;
	}
	
	/**
	 * 黒いスイレンなどのマナ系アイテム
	 * 作ろうと思ったけど、こっちはEntityItemがある前提の実装になってる
	 * とりあえずあんまり使わないだろうし無視する
	 * @return
	 */
	protected boolean updateManaDissolvable() {
		
		ItemStack stack = this.getStackInSlot(0);
		
		if (stack.isEmpty() || !(stack.getItem() instanceof IManaDissolvable)) {
			return false;
		}
		
		/*
		//マナアイテム
		IManaDissolvable manaItem = (IManaDissolvable) stack.getItem();
		manaItem.onDissolveTick(this, stack, item);
*/
		
		return false;
	}
	/**
	 * マナプールの変換対象のアイテムかの判断を行う
	 * マナの残量は考慮しない
	 */
	public static boolean isManaPoolItemStack(ItemStack stack) {
		
		boolean ret = false;
		
		//空の場合は何もしない
		if (stack.isEmpty()) {
			return false;
		}
		
		//チャージアイテム
		if(!stack.isEmpty() && stack.getItem() instanceof IManaItem) {
			//チャージ式アイテムの場合は許可
			ret = true;
		}
		if (ManaInfusionAPI.getMatchingRecipe(stack, null) != null) {
			ret = true;
		}
		return ret;
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
			//@here
			//markDispatchable();
			//同期処理
			this.playerServerSendPacket();
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
			if (slot != 1) {
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
