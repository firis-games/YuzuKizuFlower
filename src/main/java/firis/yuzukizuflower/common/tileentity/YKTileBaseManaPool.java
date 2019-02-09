package firis.yuzukizuflower.common.tileentity;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.common.block.ModBlocks;

public abstract class YKTileBaseManaPool extends YKTileBaseInventory 
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
}
