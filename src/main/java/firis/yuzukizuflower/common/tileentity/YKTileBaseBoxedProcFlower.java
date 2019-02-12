package firis.yuzukizuflower.common.tileentity;

import java.awt.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.botania.common.Botania;

/**
 * 一定周期ごとに処理を行う処理系の箱入りお花ベースクラス
 * @author computer
 *
 */
public abstract class YKTileBaseBoxedProcFlower extends YKTileBaseManaPool implements IYKTileGuiBoxedFlower{
	
	/**
	 * inputスロットのindex
	 */
	protected Integer inputSlotIndex = 0;
	
	
	/**
	 * Tickカウンタ
	 */
	private int counterTick = 0;
	
	/**
	 * 設定された周期ごとに処理を行う
	 * 最低1tick以上設定する
	 */
	private int cycleTick = 1;
	public void setCycleTick(int tick) {
		cycleTick = Math.max(tick, 1);
	}
	
	/**
	 * @interface ITickable
	 */
	@Override
	public void update() {
		
		//tickをカウントする
		counterTick = counterTick < Integer.MAX_VALUE ? counterTick + 1 : 0;
		
		//一定周期ごとに処理を行う
		if (counterTick % cycleTick == 0) {
			this.updateProccessing();
		}
	}
	
	/**
	 * 一定周期ごとに処理を行う
	 */
	public abstract void updateProccessing();
	
	/**
	 * 処理系標準では使用しない
	 */
	public int getMaxTimer() {
		return 0;
	}
	/**
	 * 処理系標準では使用しない
	 */
	public int getTimer() {
		return 0;
	}
	
	/**
	 * ランダムでパーティクルを表示する
	 */
	public void clientSpawnParticle() {
		
		//クライアントの場合
		if(this.getWorld().isRemote) {
			
			double particleChance = 0.85F;
			
			Color color = Color.GREEN;
			
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
						1.5F);
			}
		}
	}
	
	//******************************************************************************************
	// アイテムの入出力の制御
	//******************************************************************************************
	/**
	 * 入力スロットの制御
	 */
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (index != this.inputSlotIndex) {
			return false;
		}
		return true;
	}

	/**
	 * 出力スロットの制御
	 */
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		//出力は許可しない
		return false;
	}
	
	/**
	 * 対象スロットの使用許可
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		//inputスロット
		if (index == this.inputSlotIndex) {
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