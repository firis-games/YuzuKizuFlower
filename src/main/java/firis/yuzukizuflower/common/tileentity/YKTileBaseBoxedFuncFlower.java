package firis.yuzukizuflower.common.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.botania.IManaRecipes;
import firis.yuzukizuflower.common.botania.ManaRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.CapabilityItemHandler;

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
	 * 素材アイテム
	 */
	protected ItemStack inputItemStack = ItemStack.EMPTY.copy();
	
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
	    
	    NBTTagCompound itemNbt = (NBTTagCompound) compound.getTag("inputItemStack");
	    this.inputItemStack = new ItemStack(itemNbt);
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
        
        NBTTagCompound itemNbt = new NBTTagCompound();
        inputItemStack.writeToNBT(itemNbt);
        compound.setTag("inputItemStack", itemNbt);

        return compound;
    }
	
	
	
	/**
	 * 空きスロットにアイテムをいれる
	 * @param stack
	 * @return
	 */
	public boolean insertInventoryItemStack(ItemStack stack) {
		
		boolean ret = false;
		
		int start = 1;
		int end = this.getSizeInventory();
		
		//今回は全部対象だから普通にループする
		//インベントリの中を上からループでまわして挿入できるかを確認する
		//1週目は空スロットは無視する
		for (int i = start; i < end; i++) {
			ItemStack inv = this.getStackInSlot(i);
			if (inv.isEmpty()) {
				//空スロットは無視する
				////空の場合はそのまま挿入する
				//this.setInventorySlotContents(i, stack.copy());
				//ret = true;
				//break;
			} else if (ItemStack.areItemsEqual(stack, inv)
					&& inv.getCount() + stack.getCount() <= inv.getMaxStackSize()) {
				//同じアイテムかつ空き容量が1件以上ある場合
				inv.setCount(inv.getCount() + stack.getCount());
				this.setInventorySlotContents(i, inv.copy());
				ret = true;
				break;
			}
		}
		if (!ret) {
			for (int i = start; i < end; i++) {
				ItemStack inv = this.getStackInSlot(i);
				if (inv.isEmpty()) {
					//空の場合はそのまま挿入する
					this.setInventorySlotContents(i, stack.copy());
					ret = true;
					break;
				}
			}
		}
		
		
		return ret;
	}
	
	/**
	 * すべてのスロットがうまったらっていう判断
	 * 最後尾だけ除外、これはちょっといろいろ変えるつもり
	 * @return
	 */
	public boolean isInventoryFill() {
		
		for (int i = 1; i < this.getSizeInventory(); i++) {
			ItemStack itemstack = this.getStackInSlot(i);
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
		if (this.getWorld().isRemote) {
			return;
		}
		
		//レッドストーン入力がある場合は停止する
		if(isRedStonePower()) {
			return;
		}
		
		//実行状態の確認
		if (this.timer == 0) {
			
			//outputスロットの容量を確認
			if (this.isInventoryFill()) {
				if (this.timer != 0) {
					this.timer = 0;
					this.playerServerSendPacket();
				}
				return;
			}
			
			ItemStack stack = this.getStackInSlot(0);
			
			//レシピの確認
			ManaRecipe recipe = funcFlowerRecipes.getMatchesRecipe(stack);
			
			if (recipe == null) {
				return;
			}
			
			//レシピの数値を設定
			this.manaCost = recipe.getMana();
			this.maxTimer = recipe.getTime();
			this.inputItemStack = recipe.getInputItemStack();
			
			resultRecipe = recipe;
			
			//アイテムスロットの制御
			stack.shrink(1);
			this.setInventorySlotContents(0, stack);
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
		
		//変換する
		ManaRecipe recipe = this.resultRecipe;
		if (recipe == null) {
			recipe = funcFlowerRecipes.getMatchesRecipe(this.inputItemStack);
			//例外
			if (recipe == null) {
				this.timer = 0;
				this.maxTimer = 0;
				this.inputItemStack = ItemStack.EMPTY.copy();
				return;
			}
		}				
		//石を変換
		ItemStack stack = recipe.getOutputItemStack();
		
		this.insertInventoryItemStack(stack);
		//タイマーリセット
		this.timer = 0;
		this.maxTimer = 0;
		this.inputItemStack = ItemStack.EMPTY.copy();
		//同期をとる
		this.playerServerSendPacket();
		
	}
	
	
	
	//**********
	
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
			//0だけ出力拒否
			if (slot == 0) {
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
