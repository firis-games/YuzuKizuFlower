package firis.yuzukizuflower.common.tileentity;

import java.awt.Color;

import firis.yuzukizuflower.common.inventory.BoxedFieldConst;
import firis.yuzukizuflower.common.network.ITileEntityPacketReceive;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.network.PacketTileEntityS2C;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.Botania;

/**
 * 一定周期ごとに処理を行う処理系の箱入りお花ベースクラス
 * @author computer
 *
 */
public abstract class YKTileBaseBoxedProcFlower extends YKTileBaseManaPool 
										implements IYKTileGuiBoxedFlower, ITileEntityPacketReceive {
	
	/**
	 * 機能系のお花との自動リンク
	 */
	protected boolean autoManaLink() {
		return false;
	}
	
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
		
		super.update();
		
		//tickをカウントする
		counterTick = counterTick < Integer.MAX_VALUE ? counterTick + 1 : 0;
		
		//レッドストーン入力がある場合は停止する
		if(!isRedStonePower()) {
			//マナ移動処理
			moveMana();
		}
				
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
	
	/**
	 * マナ移動を行う
	 */
	public boolean moveMana() {
		
		if (this.maxMana <= this.mana) {
			return false;
		}
		
		//マナを移動する
		for(EnumFacing dir : EnumFacing.VALUES) {
			
			//指定方向のBlockPos
			BlockPos pos = this.getPos().offset(dir, 1);
			
			TileEntity tile = this.getWorld().getTileEntity(pos);
			
			//マナタンクor発電系のお花
			//自身がマナタンクの場合はマナタンクは対象外
			if (tile != null && (
					(tile instanceof YKTileManaTank && !(this instanceof YKTileManaTank))
					|| tile instanceof YKTileBaseBoxedGenFlower)) {
				
				//マナタンクの場合
				YKTileBaseManaPool manaTile = (YKTileBaseManaPool)tile;
				
				//容量がある場合は処理を行う
				if(manaTile.getCurrentMana() > 0) {
					
					//お花の空き容量を確認
					int emptyMana = this.getMaxMana() - this.getCurrentMana(); 
					
					//内部マナの空き容量or1000
					int maxMoveMana = Math.min(1000, emptyMana);
					
					//実際に移動するマナ
					int moveMana = Math.min(manaTile.getCurrentMana(), maxMoveMana);
					
					//マナを移動
					this.recieveMana(moveMana);
					manaTile.recieveMana(-moveMana);
					return true;
				}
				
			}
		}
		return false;
	}

	//******************************************************************************************
	// モノクルの範囲描画用
	//******************************************************************************************
	/**
	 * 箱入りお花用のSubTileEntity
	 * @author computer
	 */
	public static class BoxedSubTileEntity extends SubTileEntity {
		
		private BlockPos pos;
		private int range;
		
		public BoxedSubTileEntity(BlockPos pos, int range) {
			super();
			this.pos = pos;
			this.range = range;
		}
		
		@SideOnly(Side.CLIENT)
		public RadiusDescriptor getRadius() {
			return new RadiusDescriptor.Square(pos, range);
		}
	}
	
	/**
	 * お花の有効範囲を返す
	 * @return
	 */
	public abstract int getFlowerRange();
	
	/**
	 * SubTileEntityの有効化設定
	 * @return
	 */
	public boolean isSubTile() {
		int range = this.getFlowerRange();
		if (range < 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public SubTileEntity getSubTile() {
		
		if (!isSubTile()) {
			return null;
		}
		
		return new BoxedSubTileEntity(this.getPos(), this.getFlowerRange());
	}
	
	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		if (!isSubTile()) {
			return super.canSelect(player, wand, pos, side);
		}
		return true;
	}
	
	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		if (!isSubTile()) {
			return super.bindTo(player, wand, pos, side);
		}
		//バインドはできないが解除処理のためにtrueを返す
		return true;
	}
	
	//******************************************************************************************
	// GUIパラメータ同期用
	//******************************************************************************************
	
	/**
	 * GUIパラメータ同期用
	 */
	@Override
	public int getField(int id) {
		if (id == BoxedFieldConst.MANA) {
			return this.mana;
		} else if (id == BoxedFieldConst.MAX_MANA) {
			return this.maxMana;
		}
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}
	
	/**
	 * GUIパラメータ同期用
	 */
	@Override
	public int getFieldCount() {
		return 2;
	}
	
	/**
	 * パーティクル制御用変数
	 */
	protected boolean isParticle = false;
	protected int particleDelayTime = 0;
	
	/**
	 * Client受信用
	 */
	@Override
	public void receivePacket(int value) {
		
		if (value == 1) {
			this.isParticle = true;
		} else {
			this.isParticle = false;
		}
	}
	
	/**
	 * パーティクルのチェック処理
	 */
	public void checkSpawnParticle() {
		
		//Clientはパーティクル制御のみ行う
		if (this.getWorld().isRemote) {
			if (isParticle) {
				this.clientSpawnParticle();
			}
			return;
		}
		
		//パーティクル制御処理
		boolean active = false;
		if(!isRedStonePower()
				&& !this.getStackInputSlotFirst().isEmpty()) {
			active = true;
		}
		
		particleDelayTime = Math.max(0, particleDelayTime - 1);
		
		//Active状態が変更された場合
		if (active != isParticle && particleDelayTime == 0) {
			
			this.isParticle = active;
			
			int mode = this.isParticle ? 1 : 0;
			
			//活性の場合はそのままパケットを送る
			//クライアントへ送信
			NetworkHandler.network.sendToAll(
					new PacketTileEntityS2C.MessageTileEntity(pos, mode));
			
			//Packet負荷を抑えるために20tickのディレイ
			particleDelayTime = 20;
			
		}
	}
	
}