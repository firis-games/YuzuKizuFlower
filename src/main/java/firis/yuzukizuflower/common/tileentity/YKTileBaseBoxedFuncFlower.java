package firis.yuzukizuflower.common.tileentity;

import java.awt.Color;

import firis.yuzukizuflower.common.botania.IManaRecipes;
import firis.yuzukizuflower.common.botania.ManaRecipe;
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
public abstract class YKTileBaseBoxedFuncFlower extends YKTileBaseManaPool implements IYKTileGuiBoxedFlower {
	
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
	
	@Override
	public void update() {
		
		super.update();
		
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
					this.playerServerSendPacket();
				}
				return;
			}
			
			ItemStack stack = getStackInputSlotFirst();
			
			//レシピの確認
			ManaRecipe recipe = funcFlowerRecipes.getMatchesRecipe(stack, true);
			
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
		
		if (mana == 0) {
			this.playerServerSendPacket();
		}
		
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
}
