package firis.yuzukizuflower.common.tileentity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import firis.yuzukizuflower.common.botania.IManaRecipes;
import firis.yuzukizuflower.common.botania.ManaRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.botania.common.Botania;

/**
 * 機能系の箱入りお花ベースクラス
 * @author computer
 *
 */
public abstract class YKTileBaseBoxedFuncFlower extends YKTileBaseManaPool {
	
	
	protected IManaRecipes funcFlowerRecipes = null;
	
	/**
	 * 処理時間
	 */
	protected int timer = 0;
	public int getTimer() {
		return this.timer;
	}
	/**
	 * 処理完了までの時間
	 */
	protected int maxTimer = 0;
	public int getMaxTimer() {
		return this.maxTimer;
	}
	
	/**
	 * 必要マナコスト
	 */
	protected int manaCost = 0;
	
	/**
	 * 変換レシピ
	 */
	protected ManaRecipe resultRecipe = null;
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
	    this.maxTimer = compound.getInteger("maxTimer");
	    this.timer = compound.getInteger("timer");
	    this.manaCost = compound.getInteger("manaCost");
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        compound.setInteger("maxTimer", this.maxTimer);
        compound.setInteger("timer", this.timer);
        compound.setInteger("manaCost", this.manaCost);
        
        return compound;
    }
	
	/**
	 * inputスロットのindex
	 */
	protected Integer inputSlotIndex = 0;

	/**
	 * outputスロットのindex
	 */
	protected List<Integer> outputSlotIndex = new ArrayList<Integer>();
	
	/**
	 * workスロットのindex
	 */
	protected Integer workSlotIndex = 0;
	
	
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
	
	@Override
	public void update() {
		
		//クライアントは処理をしない
		if (this.getWorld().isRemote
				&& isActive()) {
			
			//パーティクル判定
			this.clientSpawnParticle();
			
			return;
		}
		
		//レッドストーン入力がある場合は停止する
		if(isRedStonePower()) {
			return;
		}
		
		//マナが0の場合は稼動しない
		if (this.mana == 0) {
			return;
		}
		
		//実行状態の確認
		if (getStackWorkSlot().isEmpty()) {
			
			//outputスロットの容量を確認
			if (this.isFillOutputSlot()) {
				if (this.timer != 0) {
					this.timer = 0;
					this.playerServerSendPacket();
				}
				return;
			}
			
			ItemStack stack = getStackInputSlot();
			
			//レシピの確認
			ManaRecipe recipe = funcFlowerRecipes.getMatchesRecipe(stack, true);
			
			if (recipe == null) {
				return;
			}
			
			//レシピの数値を設定
			this.manaCost = recipe.getMana();
			this.maxTimer = recipe.getTime();
			
			resultRecipe = recipe;
			
			//入力とworkスロットを制御する
			this.shrinkStackInputSlotToWorkSlot(recipe);
			this.playerServerSendPacket();
			
		}
		
		
		//マナがない場合はなにもしない
		//1tick あたりの マナの消費
		int mana = this.manaCost / this.maxTimer;
		if (this.getMana() < mana) {
			return;
		}
		
		//カウントアップ
		this.timer += 1;
		this.recieveMana(-mana);
		
		//規定値であれば何もしない
		if (this.timer < this.maxTimer) {
			return;
		}
		
		//レシピの変換結果を取得する
		ItemStack stack = getRecipesResult();
		this.insertOutputSlotItemStack(stack);
		
		//処理状態を初期化
		clearRecipeWork();
		
		//同期をとる
		this.playerServerSendPacket();
		
	}
	
	/**
	 * 入力側のItemStackを取得する
	 * @return
	 */
	public ItemStack getStackInputSlot() {
		if (inputSlotIndex == -1) {
			return ItemStack.EMPTY.copy();
		}
		return getStackInSlot(inputSlotIndex);
	}
	
	/**
	 * 入力側のItemStackを取得する
	 * @return
	 */
	public ItemStack getStackWorkSlot() {
		if (workSlotIndex == -1) {
			return ItemStack.EMPTY.copy();
		}
		return getStackInSlot(workSlotIndex);
	}
	
	/**
	 * recipeに設定された入力側のItemStackをWorkスロットへ移動する
	 */
	public void shrinkStackInputSlotToWorkSlot(ManaRecipe recipe) {
		
		ItemStack stack = getStackInputSlot();
		
		//inputとworkが設定されていない場合は何もしない
		if (inputSlotIndex == -1
				&& workSlotIndex == -1) {
			return;
		}
		
		//workSlotへ設定
		this.setInventorySlotContents(workSlotIndex, recipe.getInputItemStack());

		stack.shrink(recipe.getInputItemStack().getCount());
		this.setInventorySlotContents(inputSlotIndex, stack);
	}
	
	/**
	 * 処理状態を初期化する
	 */
	public void clearRecipeWork() {
		//タイマーリセット
		this.timer = 0;
		this.maxTimer = 0;
		if (workSlotIndex != -1) {
			this.removeStackFromSlot(workSlotIndex);			
		}
	}
	
	/**
	 * レシピの出力結果を取得する
	 */
	public ItemStack getRecipesResult() {
		
		ManaRecipe recipe = funcFlowerRecipes.getMatchesRecipe(this.getStackWorkSlot(), false);
		if (recipe == null) {
			//例外
			clearRecipeWork();
			return null;
		}
		
		return recipe.getOutputItemStack();		
	}
	
	/**
	 * ランダムでパーティクルを表示する
	 */
	public void clientSpawnParticle() {
		//クライアントの場合
		if(this.getWorld().isRemote) {
			
			double particleChance = 0.85F;
			
			Color color = new Color(0x818181);
			//color = new Color(0x00C6FF);
			
			if(Math.random() > particleChance) {
				/*
				//お花のパーティクル
				BotaniaAPI.internalHandler.sparkleFX(this.getWorld(), 
						this.getPos().getX() + 0.3 + Math.random() * 0.5, 
						this.getPos().getY() + 0.5 + Math.random() * 0.5, 
						this.getPos().getZ() + 0.3 + Math.random() * 0.5, 
						color.getRed() / 255F, 
						color.getGreen() / 255F, 
						color.getBlue() / 255F, 
						(float) Math.random() * 0.5F, 
						10);
				*/
				//マナプールと同じパーティクル
				Botania.proxy.wispFX(
						pos.getX() + 0.3 + Math.random() * 0.5, 
						pos.getY() + 0.6 + Math.random() * 0.25, 
						pos.getZ() + Math.random(), 
						color.getRed() / 255F, 
						color.getGreen() / 255F, 
						color.getBlue() / 255F, 
						(float) Math.random() / 10F, 
						(float) -Math.random() / 120F, 
						1F);
			}
		}
	}
	
	/**
	 * お花が稼動状態か判断する
	 * @return
	 */
	public boolean isActive() {
		
		//レッドストーン入力がある場合は停止状態
		if(isRedStonePower()) {
			return false;
		}
		
		//マナが足りない場合は停止状態
		if (this.maxTimer != 0) {
			int mana = this.manaCost / this.maxTimer;
			if (this.getMana() < mana) {
				return false;
			}
		}
		
		//timerが0の場合は停止状態
		if (this.timer == 0) {
			return false;
		}
		
		return true;
	}
	
	
	//******************************************************************************************
	// アイテムの入出力の制御
	//******************************************************************************************
	
	/**
	 * 対象スロットの使用許可
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		//inputスロット以外は不許可
		if (index != this.inputSlotIndex) {
			return false;
		}
		
		//レシピで判断する
		ManaRecipe recipe = this.funcFlowerRecipes.getMatchesRecipe(stack, true);
		if (recipe == null) {
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
	    public ItemStack extractItem(int slot, int amount, boolean simulate)
	    {
			YKTileBaseBoxedFuncFlower tile = (YKTileBaseBoxedFuncFlower) this.getInv();
			
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
}
