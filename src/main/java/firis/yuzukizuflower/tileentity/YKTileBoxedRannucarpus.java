package firis.yuzukizuflower.tileentity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemPiston;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.item.IFlowerPlaceable;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

/**
 * マナプール系処理
 * @author computer
 *
 */
public class YKTileBoxedRannucarpus extends YKTileBaseManaPool {

	
	public YKTileBoxedRannucarpus() {
		this.maxMana = 1000;
		this.mode = 1;
	}
	
	/**
	 * マナのお花のモード
	 * 1がミニ
	 * 2がミニマナ
	 * 3がノーマル
	 * 4がノーマルマナ
	 */
	protected int mode = 1;
	public int getMode() {
		return this.mode;
	}
	/*
	 * 暫定でつくったお花の名前
	 */
	public String getModeName() {
		String message = "";
		if (this.getMode()==1) {
			message = "小さなお花";
		} else if (this.getMode()==2) {
			message = "小さなマナのお花";
		} else if (this.getMode()==3) {
			message = "お花";
		} else if (this.getMode()==4) {
			message = "マナのお花";
		}
		return message;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public void changeMode() {
		this.mode = mode < 4 ? mode+1 : 1 ;
		this.playerServerSendPacket();
	}
	

	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAreaWidth() {
		int area = 0;
		if (this.mode == 1) {
			area = 2;
		}else if(this.mode == 2) {
			area = 3;
		}else if(this.mode == 3) {
			area = 6;
		}else if(this.mode == 4) {
			area = 8;
		}
		return area;
	}
	
	/**
	 * モードに応じて範囲をきめる
	 * @return
	 */
	public int getAreaHeight() {
		int area = 0;
		if (this.mode == 1) {
			area = 2;
		}else if(this.mode == 2) {
			area = 2;
		}else if(this.mode == 3) {
			area = 6;
		}else if(this.mode == 4) {
			area = 6;
		}
		return area;
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
        this.mode = compound.getInteger("mode");

    }
	
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        compound.setInteger("mode", this.mode);
        
        return compound;
    }
	

	private int tick = 0;
	@Override
	public void update() {
		
		tick += 1;
		
		//クライアントは処理をしない
		if (this.getWorld().isRemote) {
			return;
		}
		
		//10tickに1回処理
		if (tick % 10 != 0) {
			return;
		}
		
		//下を取得
		IBlockState filter = this.getWorld().getBlockState(this.getPos().down());
		
		List<BlockPos> validPositions = new ArrayList<>();
		//マナを使い場合の範囲
		int rangePlace = getAreaWidth();
		int rangePlaceY = getAreaHeight();
		
		
		ItemStack stack = this.getStackInSlot(0);
		Item stackItem = stack.getItem();
		
		//ブロックをサーチ
		for(BlockPos pos_ : BlockPos.getAllInBox(pos.add(-rangePlace, -rangePlaceY, -rangePlace), pos.add(rangePlace, rangePlaceY, rangePlace))) {
			IBlockState stateAbove = this.getWorld().getBlockState(pos_.up());
			Block blockAbove = stateAbove.getBlock();
			BlockPos up = pos_.up();
			if(filter == this.getWorld().getBlockState(pos_)
					&& (blockAbove.isAir(stateAbove, this.getWorld(), up)
							|| blockAbove.isReplaceable(this.getWorld(), up)))
				validPositions.add(up);
		}
		
		/*
		if (!validPositions.isEmpty()) {
			
			//配置場所はランダム
			BlockPos coords = validPositions.get(this.getWorld().rand.nextInt(validPositions.size()));
			
			IBlockState stateToPlace = null;
			
			if (stackItem instanceof ItemBlock) {
				int blockMeta = stackItem.getMetadata(stack.getItemDamage());
				stateToPlace = ((ItemBlock) stackItem).getBlock().getStateFromMeta(blockMeta);
			}
		}
		*/
		

		if(!validPositions.isEmpty()) {
			BlockPos coords = validPositions.get(this.getWorld().rand.nextInt(validPositions.size()));

			IBlockState stateToPlace = null;
			if(stackItem instanceof IFlowerPlaceable)
				stateToPlace = ((IFlowerPlaceable) stackItem).getBlockToPlaceByFlower(stack, null, coords);
			if(stackItem instanceof ItemBlock) {
				int blockMeta = stackItem.getMetadata(stack.getItemDamage());

				if(stackItem instanceof ItemPiston) // Workaround because the blockMeta ItemPiston gives crashes getStateFromMeta
					blockMeta = 0;

				stateToPlace = ((ItemBlock) stackItem).getBlock().getStateFromMeta(blockMeta);
			}
			else if(stackItem instanceof ItemBlockSpecial)
				stateToPlace = ((ItemBlockSpecial) stackItem).getBlock().getDefaultState();
			else if(stackItem instanceof ItemRedstone)
				stateToPlace = Blocks.REDSTONE_WIRE.getDefaultState();

			if(stateToPlace != null) {
				if(stateToPlace.getBlock().canPlaceBlockAt(this.getWorld(), coords)) {
					this.getWorld().setBlockState(coords, stateToPlace, 1 | 2);
					if(ConfigHandler.blockBreakParticles)
						this.getWorld().playEvent(2001, coords, Block.getStateId(stateToPlace));
					validPositions.remove(coords);
					ItemBlock.setTileEntityNBT(this.getWorld(), null, coords, stack);

					TileEntity tile = this.getWorld().getTileEntity(coords);
					if(tile != null && tile instanceof ISubTileContainer) {
						ISubTileContainer container = (ISubTileContainer) tile;
						String subtileName = ItemBlockSpecialFlower.getType(stack);
						container.setSubTile(subtileName);
						SubTileEntity subtile = container.getSubTile();
						subtile.onBlockPlacedBy(this.getWorld(), coords, this.getWorld().getBlockState(coords), null, stack);
					}

					if(stackItem instanceof IFlowerPlaceable)
						((IFlowerPlaceable) stackItem).onBlockPlacedByFlower(stack, null, coords);

					stack.shrink(1);

					if(mana > 1)
						mana--;
					return;
				}
			}
		}
		
	}
	
	//**********
	
	/**
	 * パケット用
	 * @author computer
	 *
	 */
	public static class ClientPacket implements Packet<INetHandlerPlayServer> {

		@Override
		public void readPacketData(PacketBuffer buf) throws IOException {
			// TODO 自動生成されたメソッド・スタブ
			System.out.println("readPacketData");
			
		}

		@Override
		public void writePacketData(PacketBuffer buf) throws IOException {
			// TODO 自動生成されたメソッド・スタブ
			System.out.println("writePacketData");

		}

		@Override
		public void processPacket(INetHandlerPlayServer handler) {
			// TODO 自動生成されたメソッド・スタブ
			System.out.println("processPacket");

		}
		
	}

	
}
