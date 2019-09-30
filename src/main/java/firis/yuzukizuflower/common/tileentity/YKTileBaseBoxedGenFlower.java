package firis.yuzukizuflower.common.tileentity;

import java.awt.Color;
import java.util.List;

import firis.yuzukizuflower.common.botania.IManaGenerator;
import firis.yuzukizuflower.common.botania.ManaGenerator;
import firis.yuzukizuflower.common.inventory.BoxedFieldConst;
import firis.yuzukizuflower.common.network.ITileEntityPacketReceive;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.network.PacketTileEntityS2C;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.common.Botania;

/**
 * 発電系の箱入りお花ベースクラス
 * @author computer
 *
 */
public abstract class YKTileBaseBoxedGenFlower extends YKTileBaseManaPool 
											implements IYKTileGuiBoxedFlower, ITileEntityPacketReceive {
	
	
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
	 * @interface IManaPool
	 * スパークの入力を拒否するために常にtrueとする
	 */
	@Override
	public boolean isFull() {
		return true;
	}

	/**
	 * upgradeスロットのindex
	 */
	protected int upgradeSlotIndex = -1;
	
	
	/**
	 * パーティクル制御用変数
	 */
	protected boolean isParticle = false;
	protected int particleDelayTime = 0;
	
	@Override
	public void update() {
		
		super.update();
		
		//Clientはパーティクル制御のみ行う
		if (this.getWorld().isRemote) {
			if (isParticle) {
				this.clientSpawnParticle();
			}
			return;
		}
		
		//パーティクル制御処理
		boolean active = isActive();
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
		
		//レッドストーン入力がある場合は停止する
		if(isRedStonePower()) {
			return;
		}
		
		//Manaの容量が最大の場合は稼動しない
		if (this.mana >= this.maxMana) {
			return;
		}
		//実行状態の確認
		if (this.timer == 0) {
			//燃料処理の判断
			if(!this.isCheckGenerator()) return;
		}
		
		
		//アップデートの分だけtickを加算する
		int boosts = getUpgradePartsCount();

		//タイマーの加算回数
		int timerCount = 1 + boosts;
		
		for (int i = 0; i < timerCount; i++) {
			//タイマー加算
			this.timer += 1;
			//アップグレードのブースト分のマナの損失は無視
			//サイクルtickのみマナの加算
			if (this.timer % this.genCycle == 0) {
				//マナの加算
				mana = Math.min(maxMana, mana + this.genMana);
			}
			//規定数をすぎたらリセット
			//タイマーリセット
			if (this.timer >= this.maxTimer) {
				//処理状態を初期化
				clearRecipeWork();
				break;
			}
		}

		//マナを移動する
		moveManaTank();
		
	}
	
	/**
	 * 燃料の判断処理
	 */
	public boolean isCheckGenerator() {
		
		List<ItemStack> stackList = getStackInputSlotList();
		
		//レシピの確認
		ManaGenerator recipe = genFlowerRecipes.getMatchesRecipe(stackList);
		if (recipe == null) {
			return false;
		}
		
		//燃料消費して準備
		this.decrStackSizeInputSlot(1);
		
		this.maxTimer = recipe.getTime();
		this.genMana = recipe.getMana();
		this.genCycle = recipe.getCycle();

		return true;
	}
	
	
	/**
	 * マナタンクが直結されている場合マナを移動する
	 */
	public boolean moveManaTank() {
		
		if (this.mana <= 0) {
			return false;
		}
		
		//マナを移動する
		for(EnumFacing dir : EnumFacing.VALUES) {
			
			//指定方向のBlockPos
			BlockPos pos = this.getPos().offset(dir, 1);
			
			TileEntity tile = this.getWorld().getTileEntity(pos);
			
			//マナタンクの場合のみ処理を行う
			if (tile != null && tile instanceof YKTileManaTank) {
				
				//マナタンクの場合
				YKTileManaTank manaTile = (YKTileManaTank)tile;
				
				//容量がある場合は移動する
				if(!manaTile.isFull()) {
					
					//対象の空き容量を確認
					int emptyMana = manaTile.getMaxMana() - manaTile.getCurrentMana(); 
					
					//内部マナの最大値or1000
					int maxMoveMana = Math.min(1000, this.mana);
					
					//実際に移動するマナ
					int moveMana = Math.min(emptyMana, maxMoveMana);
					
					//マナを移動
					this.recieveMana(-moveMana);
					manaTile.recieveMana(moveMana);
					return true;
				}
				
			}
		}
		return false;
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
	 * inputスロットのレシピチェック用
	 * @param stack
	 * @return
	 */
	@Override
	public boolean isItemValidRecipesForInputSlot(ItemStack stack) {
		return this.genFlowerRecipes.isMatchesItemStackSlot(stack);
	}
	
	/**
	 * 対象スロットの使用許可
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		//upgradeスロット
		if (index == this.upgradeSlotIndex) {
			//レシピで判断する
			boolean recipe = this.isUpgradeParts(stack);
			if (!recipe) {
				return false;
			}
			return true;
		}
		
		return super.isItemValidForSlot(index, stack);
	}
	
	/**
	 * GUIパラメータ同期用
	 */
	@Override
	public int getField(int id) {
		if (id == BoxedFieldConst.MANA) {
			return this.mana;
		} else if (id == BoxedFieldConst.MAX_MANA) {
			return this.maxMana;
		} else if (id == BoxedFieldConst.TIMER) {
			return this.timer;
		} else if (id == BoxedFieldConst.MAX_TIMER) {
			return this.maxTimer;
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
		return 4;
	}
	
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
}
