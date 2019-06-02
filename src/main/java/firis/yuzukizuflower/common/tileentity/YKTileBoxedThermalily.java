package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.tileentity.handler.YKLavaFluidHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class YKTileBoxedThermalily extends YKTileBaseBoxedGenFlower implements IYKPlayerServerSendPacket {

	public YKLavaFluidHandler fluidHandler;
	
	/**
	 * 
	 * @param mode
	 */
	public YKTileBoxedThermalily() {
		
		this.maxMana = 18000;

		//発電用レシピ
		this.genFlowerRecipes = null;
		
		//inputスロット
		this.inputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(0));
		
		this.outputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(1));
		
		//upgradeスロット
		this.upgradeSlotIndex = 2;
		
		this.fluidHandler = new YKLavaFluidHandler(this, 4000);
		
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}
	
	/**
	 * 対象stackがupgradeアイテムか判断する
	 * @return
	 */
	@Override
	public boolean isUpgradeParts(ItemStack stack) {
		//サーマリリー
		return BotaniaHelper.isSpecialFlower(stack, "thermalily");
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
		//YKLavaFluidHandler
		fluidHandler.deserializeNBT(compound);
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        //YKLavaFluidHandler
        compound.merge(fluidHandler.serializeNBT());

        return compound;
    }
	
	/********************************************************************************/
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
    }

	@Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler);
		}
    	return super.getCapability(capability, facing);
    
    }
	/********************************************************************************/

	
	/**
	 * inputスロットのレシピチェック用
	 * @param stack
	 * @return
	 */
	@Override
	public boolean isItemValidRecipesForInputSlot(ItemStack stack) {
		
		FluidStack fuildStack = FluidUtil.getFluidContained(stack);
		if (fuildStack != null 
				&& fuildStack.getFluid() == FluidRegistry.getFluid("lava")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 燃料の判断処理
	 */
	@Override
	public boolean isCheckGenerator() {
		
		//オリジナル版
		//900tick 1tickあたり20mana
		//45sec 18000mana
		//6000tickのクールダウンあり
		//クールダウン込みで345秒で18000mana
		
		//7200tick 360秒 割り切れないから少しだけ時間を追加
		//1秒50mana

		//溶岩の残量を確認
		if (this.fluidHandler.getLiquid() < 100) {
			return false;
		}
		
		this.fluidHandler.drain(100, true);
		
		//燃焼を開始する
		this.maxTimer = 720;
		this.genMana = 50;
		this.genCycle = 20;
		
		return true;
	}
	
	
	private int tick = 0;
	
	@Override
	public void update() {
		
		super.update();
		
		if (this.getWorld().isRemote) return;
		
		this.tick++;
		if (this.tick % 10 != 0) return;
		
		ItemStack slotStack = this.getStackInputSlotFirst();
		//バケツのチェック
		//液体タンク系の処理
		FluidStack fuildStack = FluidUtil.getFluidContained(slotStack);
		
		//空き容量があるかつ溶岩であること
		if (this.fluidHandler.canFill() 
				&& fuildStack != null 
				&& fuildStack.getFluid() == FluidRegistry.getFluid("lava")) {
			
			//Stackをひとつだけ取得する
			ItemStack stack = slotStack.copy();
			stack.setCount(1);
			
			//液体Capability
			IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
			
			int emptyLiquidMana = this.fluidHandler.getMaxLiquid() - this.fluidHandler.getLiquid();
			
			//液体容器からマナを取得
			FluidStack hndFluidStack = fluidHandler.drain(emptyLiquidMana, true);
			if (hndFluidStack == null) {
				return;
			}
			
			//処理後の容器
			stack = fluidHandler.getContainer().copy();
			
			//outputスロットがあいていいる or スタック可能アイテムの場合処理を続行する
			if(this.isFillOutputSlotStack(stack)) {
				return;
			}
			
			//液体マナを充填
			this.fluidHandler.fill(hndFluidStack, true);
			
			//アイテムの移動処理
			slotStack.shrink(1); //input
			this.insertOutputSlotItemStack(stack); //output
			
			//同期処理
			this.playerServerSendPacket();
			
		}
	}
	
	@Override
	public void playerServerSendPacket() {
		super.playerServerSendPacket();
	}
}
