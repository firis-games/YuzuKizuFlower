package firis.yuzukizuflower.common.tileentity;

import java.awt.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import firis.yuzukizuflower.common.botania.IManaGenerator;
import firis.yuzukizuflower.common.botania.ManaGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.botania.common.Botania;

/**
 * 機能系の箱入りお花ベースクラス
 * @author computer
 *
 */
public abstract class YKTileBaseBoxedGenFlower extends YKTileBaseManaPool implements IYKTileGuiBoxedFlower{
	
	
	protected IManaGenerator genFlowerRecipes = null;
	
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
	 * 生産マナ量
	 */
	protected int genMana = 0;
	
	/**
	 * 生産サイクル
	 */
	protected int genCycle = 0;
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
	    this.maxTimer = compound.getInteger("maxTimer");
	    this.timer = compound.getInteger("timer");
	    this.genMana = compound.getInteger("genMana");
	    this.genCycle = compound.getInteger("genCycle");
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
        compound.setInteger("genMana", this.genMana);
        compound.setInteger("genCycle", this.genCycle);
        
        return compound;
    }

	/**
	 * inputスロットのindex
	 */
	protected int inputSlotIndex = -1;
	
	/**
	 * upgradeスロットのindex
	 */
	protected int upgradeSlotIndex = -1;
	
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
		
		//Manaの容量が最大の場合は稼動しない
		if (this.mana >= this.maxMana) {
			return;
		}
		
		boolean syncFlg = false;
		
		//実行状態の確認
		if (this.timer == 0) {
			
			ItemStack stack = getStackInputSlot();
			
			//レシピの確認
			ManaGenerator recipe = genFlowerRecipes.getMatchesRecipe(stack);
			if (recipe == null) {
				return;
			}
			
			//燃料消費して準備
			this.decrStackSize(inputSlotIndex, 1);
			
			this.maxTimer = recipe.getTime();
			this.genMana = recipe.getMana();
			this.genCycle = recipe.getCycle();
			
			//同期
			syncFlg = true;
		}
		
		//タイマー加算
		this.timer += 1;
		
		//サイクルtickのみマナの加算
		if (this.timer % this.genCycle == 0) {
			
			int boosts = getUpgradePartsCount();
			int boostsTime = this.timer;
			int boostsCount = 0;
			
			//個数分だけ値を増やす
			for (int i = 0; i < boosts; i++) {
				boostsTime += this.genCycle;
				if (boostsTime >= this.maxTimer) {
					break;
				}
				boostsCount += 1;
			}
			//必要tickを加算する
			this.timer += boostsCount * this.genCycle;
			
			//マナの加算
			mana = Math.min(maxMana, mana + this.genMana + this.genMana * boostsCount);
			syncFlg = true;
		}
				
		//規定数をすぎたらリセット
		//タイマーリセット
		if (this.timer >= this.maxTimer) {
			//処理状態を初期化
			clearRecipeWork();
			syncFlg = true;
		}
		
		//同期をとる
		if (syncFlg) {
			this.playerServerSendPacket();
		}
		
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
	 * 処理状態を初期化する
	 */
	public void clearRecipeWork() {
		//タイマーリセット
		this.timer = 0;
		this.maxTimer = 0;
		this.genMana = 0;
		this.genCycle = 0;
	}
	
	/**
	 * 対象stackがupgradeアイテムか判断する
	 * @return
	 */
	public abstract boolean isUpgradeParts(ItemStack stack);
	
	/**
	 * アップグレードスロットのアイテム数を取得
	 */
	public int getUpgradePartsCount() {
		int ret = 0;
		ItemStack stack = this.getStackInSlot(this.upgradeSlotIndex);
		if (isUpgradeParts(stack)) {
			ret = stack.getCount();
		}
		return ret;
	}
	
	/**
	 * ランダムでパーティクルを表示する
	 */
	public void clientSpawnParticle() {
		//クライアントの場合
		if(this.getWorld().isRemote) {
			
			double particleChance = 0.85F;
			
			Color color = Color.red;
			
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
		//Manaの容量が最大の場合は稼動しない
		if (this.mana >= this.maxMana) {
			return false;
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
		
		//inputスロット
		if (index == this.inputSlotIndex) {
			//レシピで判断する
			ManaGenerator recipe = this.genFlowerRecipes.getMatchesRecipe(stack);
			if (recipe == null) {
				return false;
			}
			return true;
		}
		
		//upgradeスロット
		if (index == this.upgradeSlotIndex) {
			//レシピで判断する
			boolean recipe = this.isUpgradeParts(stack);
			if (!recipe) {
				return false;
			}
			return true;
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
	    public ItemStack extractItem(int slot, int amount, boolean simulate)
	    {
			//Capabilityは許可しない
			return ItemStack.EMPTY;
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
