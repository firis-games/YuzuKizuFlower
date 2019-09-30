package firis.yuzukizuflower.common.tileentity;

import java.awt.Color;

import firis.yuzukizuflower.common.botania.IManaRecipes;
import firis.yuzukizuflower.common.botania.ManaRecipe;
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
 * 機能系の箱入りお花ベースクラス
 * @author computer
 *
 */
public abstract class YKTileBaseBoxedFuncFlower extends YKTileBaseManaPool 
										implements IYKTileGuiBoxedFlower, ITileEntityPacketReceive {
	
	/**
	 * 機能系のお花との自動リンク
	 */
	protected boolean autoManaLink() {
		return false;
	}
	
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
		
	protected boolean randomRecipe = true;
	
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
		
		//マナ移動処理
		moveMana();
		
		//マナが0の場合は稼動しない
		//マナ最大値が0の場合はスキップ
		if (this.mana == 0 && this.maxMana != 0) {
			return;
		}
		
		//実行状態の確認
		if (getStackWorkSlotFirst().isEmpty()) {
			
			//outputスロットの容量を確認
			//ランダムレシピの場合は事前チェックを行う
			if (randomRecipe && this.isFillOutputSlot()) {
				if (this.timer != 0) {
					this.timer = 0;
				}
				return;
			}
			
			ManaRecipe recipe = getManaRecipe();
			if (recipe == null) {
				return;
			}
			
			//ノーマルレシピの場合はチェックを行う
			if (!randomRecipe && isFillOutputSlotStack(recipe.getOutputItemStack())) {
				return;
			}
			
			//レシピの数値を設定
			this.manaCost = recipe.getMana();
			this.maxTimer = recipe.getTime();

			//入力とworkスロットを制御する
			this.shrinkStackInputSlotToWorkSlot(recipe);			
		}
		//例外処理
		if (this.maxTimer == 0) {
			return;
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
	}
	
	/**
	 * マナ変換レシピを取得する
	 * @return
	 */
	public ManaRecipe getManaRecipe() {
		
		ItemStack stack = getStackInputSlotFirst();
		
		//レシピの確認
		return funcFlowerRecipes.getMatchesRecipe(stack, true);
	}
	
	/**
	 * recipeに設定された入力側のItemStackをWorkスロットへ移動する
	 */
	public void shrinkStackInputSlotToWorkSlot(ManaRecipe recipe) {
		
		ItemStack stack = getStackInputSlotFirst();
		
		//inputとworkが設定されていない場合は何もしない
		if (inputSlotIndex.size() == 0
				&& workSlotIndex.size() == 0) {
			return;
		}
		
		//inputからworkSlotへ設定
		this.setInventorySlotContents(workSlotIndex.get(0), recipe.getInputItemStack());

		stack.shrink(recipe.getInputItemStack().getCount());
		this.setInventorySlotContents(inputSlotIndex.get(0), stack);
	}
	
	/**
	 * 処理状態を初期化する
	 */
	public void clearRecipeWork() {
		//タイマーリセット
		this.timer = 0;
		this.maxTimer = 0;
		for(Integer idx : workSlotIndex) {
			this.removeStackFromSlot(idx);
		}
	}
	
	/**
	 * レシピの出力結果を取得する
	 */
	public ItemStack getRecipesResult() {
		
		ManaRecipe recipe = funcFlowerRecipes.getMatchesRecipe(this.getStackWorkSlotFirst(), false);
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
			if (tile != null && (tile instanceof YKTileManaTank 
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
	// アイテムの入出力の制御
	//******************************************************************************************
	/**
	 * inputスロットのレシピチェック用
	 * @param stack
	 * @return
	 */
	@Override
	public boolean isItemValidRecipesForInputSlot(ItemStack stack) {
		ManaRecipe recipe = this.funcFlowerRecipes.getMatchesRecipe(stack, true);
		if (recipe == null) {
			return false;
		}		
		return true;
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
