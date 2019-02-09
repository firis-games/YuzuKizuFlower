package firis.yuzukizuflower.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

/**
 * マナプール系処理
 * @author computer
 *
 */
public class YKTileBoxedJadedAmaranthus extends YKTileBaseManaPool {

	
	public YKTileBoxedJadedAmaranthus() {
		this.maxMana = 10000;
		this.maxTimer = 150; //オリジナルは30 かつ 座標をランダムで選択してそこに植える、植えれない場合はスキップ
		this.manaCost = 600;  //オリジナルは100
	}
	
		@Override
	public int getSizeInventory() {
		return 10;
	}
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
	    this.maxTimer = compound.getInteger("maxTimer");
	    this.timer = compound.getInteger("timer");
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
        return compound;
    }
	

	protected int timer = 0;
	public int getTimer() {
		return this.timer;
	}
	protected int maxTimer = 0;
	public int getMaxTimer() {
		return this.maxTimer;
	}
	
	private int manaCost = 100;
	
	
	/**
	 * 空きスロットにアイテムをいれる
	 * @param stack
	 * @return
	 */
	public boolean insertInventoryItemStack(ItemStack stack) {
		
		boolean ret = false;
		
		//今回は全部対象だから普通にループする
		//インベントリの中を上からループでまわして挿入できるかを確認する
		for (int i = 0; i < this.getSizeInventory() - 1; i++) {
			
			ItemStack inv = this.getStackInSlot(i);
			if (inv.isEmpty()) {
				//空の場合はそのまま挿入する
				this.setInventorySlotContents(i, stack.copy());
				ret = true;
				break;
			} else if (ItemStack.areItemsEqual(stack, inv)
					&& inv.getCount() + stack.getCount() <= inv.getMaxStackSize()) {
				//同じアイテムかつ空き容量が1件以上ある場合
				inv.setCount(inv.getCount() + stack.getCount());
				this.setInventorySlotContents(i, inv.copy());
				ret = true;
				break;
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
		
		for (int i = 0; i < this.getSizeInventory() - 1; i++) {
			ItemStack itemstack = this.getStackInSlot(i);
			if (itemstack.isEmpty())
            {
                return false;
            }
		}
        return true;
	}
	
	
	public boolean isRedStonePower() {
		int redstone = 0;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = this.getWorld().getRedstonePower(this.getPos().offset(dir), dir);
			redstone = Math.max(redstone, redstoneSide);
		}
		
		/* 方向ごとにRedStoneを受け付けてるの判断ができる
		int redstoneSide = this.getWorld().getRedstonePower(this.getPos().offset(dir), dir);
		redstoneSignal = Math.max(redstoneSignal, redstoneSide);
		*/
		
		//個別に判断
		//this.world.getRedstonePower(pos, facing)
		//シグナルがきていればOK
		return redstone > 0;
	}
	
	@Override
	public void update() {
		
		/*
		//5tickに1回
		if (tick % 5 != 0) {
			return;
		}
		*/
		
		//レッドストーン入力がある場合は停止する
		if(isRedStonePower()) {
			return;
		}
		
		//動くかどうかの判断を行う
		if (this.isInventoryFill()) {
			if (this.timer != 0) {
				this.timer = 0;
				this.playerServerSendPacket();
			}
			return;
		}
		
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
		
		//クライアントは処理をしない
		if (this.getWorld().isRemote) {
			return;
		}
		
		ItemStack stack;
		
		//ハサミがある場合
		ItemStack shearsStack = this.getStackInSlot(9);
		if (!shearsStack.isEmpty()
				&& shearsStack.getItem().getRegistryName().equals(new ResourceLocation("minecraft:shears"))) {
			//ハサミの場合処理分岐
			shearsStack.setItemDamage(shearsStack.getItemDamage() + 1);
			
			if (shearsStack.getItemDamage() > shearsStack.getMaxDamage()) {
				//ハサミを破壊する
				shearsStack.shrink(1);
			}
			
			//サーバー側は
			EnumDyeColor color = EnumDyeColor.byMetadata(this.getWorld().rand.nextInt(16));
			stack = new ItemStack(ModItems.petal, 2, color.getDyeDamage());
			
		} else {
			//サーバー側は
			EnumDyeColor color = EnumDyeColor.byMetadata(this.getWorld().rand.nextInt(16));
			stack = new ItemStack(ModBlocks.flower, 1, color.getDyeDamage());
		}
		
		this.insertInventoryItemStack(stack);
		//タイマーリセット
		this.timer = 0;
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
		
		if (index != 9) {
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
			//9だけ出力拒否
			if (slot == 9) {
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
