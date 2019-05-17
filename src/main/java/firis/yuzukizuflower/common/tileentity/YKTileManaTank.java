package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.botania.ManaRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaUsingItem;

public class YKTileManaTank extends YKTileBaseBoxedProcFlower {
	
	/**
	 * コンストラクタ
	 */
	public YKTileManaTank() {
		
		//Slotの設定
		
		//inputスロット
		this.inputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(0));
		//outputスロット
		this.outputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(1));
		
		//chargeスロット
		this.chargeSlotIndex = new ArrayList<Integer>(
				Arrays.asList(2, 3, 4, 5));
		//catalystスロット
		this.catalystSlotIndex = 6;
		
		//10tickに1回処理を行う
		this.setCycleTick(10);
		
	}
	
	/** メタデータ対応 */
	/*************************************************************************/
	/**
	 * メタデータ対応
	 * @param meta
	 */
	public YKTileManaTank(int meta) {
		
		this();
		this.metadata = meta;
		setMaxManaToMetadata(meta);
	}
	
	public Integer getMetadata() {
		return this.metadata;
	}
	protected int metadata = 0;
	protected void setMaxManaToMetadata(int meta) {
		switch(meta) {
		case 0:
			this.maxMana = 1000000;
			break;
		case 1:
			this.maxMana = 2000000;
			break;
		case 2:
			this.maxMana = 5000000;
			break;
		case 3:
			this.maxMana = 10000000;
			break;
		}
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
        this.metadata = compound.getInteger("metadata");
        this.setMaxManaToMetadata(this.metadata);
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        compound.setInteger("metadata", this.metadata);
        
        return compound;
    }
	/*************************************************************************/
	
	/**
	 * catalystスロットのindex
	 */
	protected Integer catalystSlotIndex = -1;
	
	/**
	 * chargeスロットのindex
	 */
	protected List<Integer> chargeSlotIndex = new ArrayList<Integer>();
	
	/**
	 * 内部インベントリのサイズ
	 */
	@Override
	public int getSizeInventory() {
		return 7;
	}
	
	@Override
	public void update() {
		
		super.update();
		
		//同期処理
		if(!this.getWorld().isRemote) {
			playerServerSendPacketOnce();
		}
	}
	
	/**
	 * 指定tickごとに処理を行う
	 * @interface YKTileBaseBoxedProcFlower
	 */
	@Override
	public void updateProccessing() {
		
		//クライアントは処理を行わない
		if (this.getWorld().isRemote) {
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

		ItemStack stack = this.getStackInputSlotFirst();
		ItemStack catalyst = this.getStackInSlot(this.catalystSlotIndex);
		
		//レシピ取得
		ManaRecipe recipe = BotaniaHelper.recipesManaPool.getMatchesRecipe(stack, catalyst);
		
		if (recipe != null) {
			
			//必要な情報を準備
			int recipeMana = recipe.getMana();
			ItemStack outputSlot = this.getStackInSlot(this.outputSlotIndex.get(0));
			ItemStack outputRecipe = recipe.getOutputItemStack();
			
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
			this.setInventorySlotContents(this.outputSlotIndex.get(0), outputSlot);
			
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
		
		ItemStack stack = this.getStackInputSlotFirst();
		
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
		//chargeスロット
		for (int i : chargeSlotIndex) {
			
			ItemStack stack = this.getStackInSlot(i);
			
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
			
		ItemStack stack = this.getStackInputSlotFirst();
		
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
					this.setInventorySlotContents(this.outputSlotIndex.get(0), stack.copy());
					this.setInventorySlotContents(this.inputSlotIndex.get(0), ItemStack.EMPTY);
					this.playerServerSendPacket();
					ret = true;
				}
			}
			
		}		
		return ret;
	}
	
	//******************************************************************************************
	// 同期制御
	//******************************************************************************************
	boolean playerServerSendPacketFlg = false;
	
	@Override
	public void playerServerSendPacket() {
		this.playerServerSendPacketFlg = true;
	}
	
	/**
	 * 1tick 1回だけ呼ばれる
	 */
	public void playerServerSendPacketOnce() {
		if (playerServerSendPacketFlg) {
			super.playerServerSendPacket();
		}
		this.playerServerSendPacketFlg = false;
	}

	//******************************************************************************************
	// SubTile設定
	//******************************************************************************************
	@Override
	public int getFlowerRange() {
		return 0;
	}
	
}
