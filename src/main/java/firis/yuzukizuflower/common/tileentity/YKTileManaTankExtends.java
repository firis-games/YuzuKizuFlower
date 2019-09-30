package firis.yuzukizuflower.common.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuFluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * マナを液体として扱う
 * @author computer
 *
 */
public class YKTileManaTankExtends extends YKTileManaTank implements IFluidHandler, IFluidTankProperties {
	
	public YKTileManaTankExtends() {
		super();
	}
	
	public YKTileManaTankExtends(int meta) {
		super(meta);
	}
	
	/************************************************
	 * 液体管理用
	 *************************************************/
	protected final Fluid fluidType = FluidRegistry.getFluid("liquid_mana");
	
	/************************************************
	 * マナと液体マナの変換用
	 *************************************************/
	int liquidManaRate = 10;
	
	protected int getLiquidMana() {
		int mana = this.getMana();
		mana = (int) Math.floor(mana / liquidManaRate);
		return mana;
	}
	
	protected int getLiquidMaxMana() {
		int mana = this.getMaxMana();
		mana = (int) Math.floor(mana / liquidManaRate);
		return mana;
	}
	
	protected void recieveLiquidMana(int mana) {
		mana = mana * liquidManaRate;
		this.recieveMana(mana);
	}
	
	
	/************************************************
	 * IFluidHandler
	 * capabilityで受け渡すための処理
	 *************************************************/
	protected IFluidTankProperties[] tankProperties;
	
	/**
	 * @Intarface IFluidHandler
	 */
	@Override
    public IFluidTankProperties[] getTankProperties()
    {
        if (this.tankProperties == null)
        {
            this.tankProperties = new IFluidTankProperties[] {this};
        }
        return this.tankProperties;
    }

	/**
	 * @Intarface IFluidHandler
	 */
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		
		//対象外
		if (!canFillFluidType(resource) 
				|| resource == null 
				|| resource.amount <= 0) {
            return 0;
		}
        
		//シミュレート
    	if (!doFill) {
    		return Math.min(this.getLiquidMaxMana() - this.getLiquidMana(), resource.amount);
    	}
    	
    	//実際に処理を行う
    	int filled = this.getLiquidMaxMana() - this.getLiquidMana();

    	//最大値より受け入れる値が低い場合はそのまま加算
        if (resource.amount < filled) {
            this.recieveLiquidMana(resource.amount);
            filled = resource.amount;
        } else {
        	this.recieveLiquidMana(filled);
        }

        /* IFluidTankを継承している場合は呼ばれる
        FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(
        		this.getContents(), 
        		this.getWorld(), 
        		this.getPos(), 
        		this, 
        		filled));
        */
        return filled;
	}
	
	
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		
		if (!canFillFluidType(resource)) {
			return null;
		}
		return this.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		
		//空の場合
		if (this.getLiquidMana() <= 0 || maxDrain <= 0) {
            return null;
        }
		
		int drained = maxDrain;
        if (this.getLiquidMana() < drained) {
            drained = this.getLiquidMana();
        }

        //返却用の流体Stack
        FluidStack stack = new FluidStack(this.fluidType, drained);

        //シミュレート
        if (doDrain) {
        	//マナ操作
            this.recieveLiquidMana(-drained);
            /* IFluidTankを継承している場合は呼ばれる
            FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, tile.getWorld(), tile.getPos(), this, drained));
            */
        }
        
        return stack;
	}
	
	/************************************************
	 * IFluidTankProperties
	 * IFluidHandlerで必要なインタフェース
	 *************************************************/
	@Override
	public FluidStack getContents() {
		FluidStack contents = new FluidStack(fluidType, this.getLiquidMana());
        return contents == null ? null : contents.copy();
	}

	@Override
	public int getCapacity() {
		 return this.getLiquidMaxMana();
	}

	/**
	 * 入力可否
	 */
	@Override
	public boolean canFill() {
		return true;
	}

	/**
	 * 出力可否
	 */
	@Override
	public boolean canDrain() {
		return true;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluidStack) {
		//同じ液体か判断する
		return fluidStack.isFluidEqual(this.getContents()) && canFill();
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluidStack) {
		//同じ液体か判断する
		return fluidStack.isFluidEqual(this.getContents()) && canDrain();
	}
	
	
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) this;
        return super.getCapability(capability, facing);
    }

    
    /************************************************
	 * 液体バケツ系も対応する
	 *************************************************/
	/**
	 * 黒いスイレン専用の変換処理
	 * @return
	 */
	protected boolean updateManaLotus() {
		
		if (super.updateManaLotus()) {
			return true;
		}
		ItemStack slotStack = this.getStackInputSlotFirst();
		
		//液体タンク系の処理
		FluidStack fuildStack = FluidUtil.getFluidContained(slotStack);
		
		//マナタンクに空き容量がある かつ 液体マナが入った液体容器であること
		if (!this.isFull() && fuildStack != null && fuildStack.getFluid() == YuzuKizuFluids.LIQUID_MANA) {
			
			//Stackをひとつだけ取得する
			ItemStack stack = slotStack.copy();
			stack.setCount(1);
			
			//液体Capability
			IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
			
			int emptyLiquidMana = this.getLiquidMaxMana() - this.getLiquidMana();
			
			//液体容器からマナを取得
			FluidStack hndFluidStack = fluidHandler.drain(emptyLiquidMana, true);
			if (hndFluidStack == null) {
				return false;
			}
			
			//処理後の容器
			stack = fluidHandler.getContainer().copy();
			
			//outputスロットがあいていいる or スタック可能アイテムの場合処理を続行する
			if(this.isFillOutputSlotStack(stack)) {
				return false;
			}
			
			//液体マナを充填
			this.fill(hndFluidStack, true);
			
			//アイテムの移動処理
			slotStack.shrink(1); //input
			this.insertOutputSlotItemStack(stack); //output
						
			return true;
		}
		return false;
	}
}
