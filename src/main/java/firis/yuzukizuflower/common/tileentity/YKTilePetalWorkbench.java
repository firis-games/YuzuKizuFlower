package firis.yuzukizuflower.common.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class YKTilePetalWorkbench extends TileEntity implements ITickable {
	
	public ItemStackHandler inventory;
	
	public YKWaterFluidHandler fluidHandler;
	
	public YKTilePetalWorkbench() {
	
		//Inventory初期化
		this.inventory = new ItemStackHandler(18);
		
		this.fluidHandler = new YKWaterFluidHandler(10000);
		
	}
	
	/********************************************************************************/
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
		//ItemHandler
		inventory.deserializeNBT(compound);
		
		//YKWaterFluidHandler
		fluidHandler.deserializeNBT(compound);
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        //ItemHandler
        compound.merge(inventory.serializeNBT());

        //YKWaterFluidHandler
        compound.merge(fluidHandler.serializeNBT());

        return compound;
    }
	
	/********************************************************************************/
	/**
	 * サーバー->クライアントのデータ同期用
	 * サーバーからクライアントへデータを送信する
	 */
	protected void playerServerSendPacket() {
		//Server Side
		if (!this.getWorld().isRemote) {
			
			this.markDirty();
			
			List<EntityPlayer> list = world.playerEntities;
			Packet<?> pkt = this.getUpdatePacket();
			if (pkt != null) {
				for (EntityPlayer player : list) {
					EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
					mpPlayer.connection.sendPacket(pkt);
				}
			}
		}
	}
	
	@Override
    public NBTTagCompound getUpdateTag()
    {
		return this.writeToNBT(new NBTTagCompound());
    }
	
	@Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }
	
	@Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
		return new SPacketUpdateTileEntity(this.pos, 0, this.writeToNBT(new NBTTagCompound()));
    }

	@Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {
		this.readFromNBT(pkt.getNbtCompound());
    }
	
	/********************************************************************************/
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
    }

	@Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		} else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler);
		}
    	return super.getCapability(capability, facing);
    
    }
	/********************************************************************************/

	private int tick = 0;
	@Override
	public void update() {
		tick++;
		if (tick % 10 != 0) return;
		
		//最大の場合は何もしない
		if (this.fluidHandler.getLiquid() == this.fluidHandler.getMaxLiquid()) return;
		
		//下のブロックを走査
		IBlockState state = this.getWorld().getBlockState(this.getPos().down());
		//水源か判断する
		if (state.getMaterial().equals(Material.WATER)
				&& state.getValue(BlockLiquid.LEVEL).intValue() == 0) {
			
			//液体を増やす
			FluidStack stack = new FluidStack(FluidRegistry.getFluid("water"), 500);
			this.fluidHandler.fill(stack, true);
			
		}
		
	}
	
	
}
