package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;

/**
 * マナプール系処理
 * @author computer
 *
 */
public class YKTileBoxedOrechid extends YKTileBaseManaPool {

	
	public YKTileBoxedOrechid() {
		this.maxMana = 10000;
		this.maxTimer = 100; //オリジナルは30 かつ 座標をランダムで選択してそこに植える、植えれない場合はスキップ
		this.manaCost = 17500;  //オリジナルは100
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
	
	//0 がオアキド
	//1がオアキド・イグニス
	protected int mode = 0;
	
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
		
		//マナがない場合はなにもしない
		//1tick あたりの マナの消費
		int mana = this.manaCost / this.maxTimer;
		if (this.getMana() < mana) {
			return;
		}
		
		//タイマーが0の場合のみ判定を行う　実行判定
		if (timer == 0) {
		
			//動くかどうかの判断を行う
			if (this.isInventoryFill()) {
				if (this.timer != 0) {
					this.timer = 0;
					this.playerServerSendPacket();
				}
				return;
			}
			
			// スロットのアイテムが石かどうかだけ確認する
			ItemStack stack = this.getStackInSlot(0);
			if (!stack.isEmpty() 
					&& stack.getItem().getRegistryName().equals(new ResourceLocation("minecraft:stone"))
					&& stack.getMetadata() == 0) {
				//石の場合だけ処理を行う
				//ひとつへらす
				mode = 0;
				stack.shrink(1);
				this.setInventorySlotContents(0, stack);
				this.playerServerSendPacket();
			}else if (!stack.isEmpty() 
						&& stack.getItem().getRegistryName().equals(new ResourceLocation("minecraft:netherrack"))) {
				//石の場合だけ処理を行う
				//ひとつへらす
				mode = 1;
				stack.shrink(1);
				this.setInventorySlotContents(0, stack);
				this.playerServerSendPacket();
			} else {
				return;
			}
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
		
		//石を変換
		ItemStack stack = getOreToPut();
		
		this.insertInventoryItemStack(stack);
		//タイマーリセット
		this.timer = 0;
		//同期をとる
		this.playerServerSendPacket();
		
	}
	
	public static Map<String, Integer> joinOreWeights = null;
	public Map<String, Integer> getOreMap_old() {
		
		if (YKTileBoxedOrechid.joinOreWeights == null) {
			YKTileBoxedOrechid.joinOreWeights = new HashMap<String, Integer>();
			YKTileBoxedOrechid.joinOreWeights.putAll(BotaniaAPI.oreWeights);
			YKTileBoxedOrechid.joinOreWeights.putAll(BotaniaAPI.oreWeightsNether);
		}
		//return BotaniaAPI.oreWeights;
		//return BotaniaAPI.oreWeightsNether;
		
		return YKTileBoxedOrechid.joinOreWeights;
	}
	public Map<String, Integer> getOreMap() {
		
		if (mode == 0) {
			return BotaniaAPI.oreWeights;
		} else {
			return BotaniaAPI.oreWeightsNether;
			
		}
	}
	
	public ItemStack getOreToPut() {
		List<WeightedRandom.Item> values = new ArrayList<>();
		Map<String, Integer> map = getOreMap();
		for(String s : map.keySet())
			values.add(new StringRandomItem(map.get(s), s));

		String ore = ((StringRandomItem) WeightedRandom.getRandomItem(this.getWorld().rand, values)).s;

		List<ItemStack> ores = OreDictionary.getOres(ore);

		for(ItemStack stack : ores) {
			Item item = stack.getItem();
			String clname = item.getClass().getName();

			// This poem is dedicated to Greg
			//
			// Greg.
			// I get what you do when
			// others say it's a grind.
			// But take your TE ores
			// and stick them in your behind.
			if(clname.startsWith("gregtech") || clname.startsWith("gregapi"))
				continue;
			if(!(item instanceof ItemBlock))
				continue;

			return stack;
		}

		return getOreToPut();
	}
	private static class StringRandomItem extends WeightedRandom.Item {

		public final String s;

		public StringRandomItem(int par1, String s) {
			super(par1);
			this.s = s;
		}

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
