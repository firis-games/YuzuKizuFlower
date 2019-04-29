package firis.yuzukizuflower.common.network;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.item.YKItemRemoteChest;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * 縁結びの輪のGuiOpen処理
 * @author computer
 *
 */
public class PacketOpenGuiS2C implements IMessageHandler<PacketOpenGuiS2C.MessageOpenGui, IMessage> {
	
	@Override
	public IMessage onMessage(MessageOpenGui message, MessageContext ctx) {
		
		//指定のTileEntityのnetwork連動メソッドを呼び出す
		EntityPlayerMP player = ctx.getServerHandler().player;
		
		//アミュレット枠
		IBaublesItemHandler baublesHandler = BaublesApi.getBaublesHandler(player);
		
		ItemStack chest = ItemStack.EMPTY.copy();
		for (int i = 0; i < BaubleType.AMULET.getValidSlots().length; i++) {
			int slot = BaubleType.AMULET.getValidSlots()[i];
			
			ItemStack work = baublesHandler.getStackInSlot(slot);
			
			if (work.getItem() instanceof YKItemRemoteChest) {
				//チェストの登録情報をもっていること
				BlockPos pos = YKItemRemoteChest.getNbtBlockPos(work);
				if(pos != null) {
					chest = work;
					break;
				}
			}
		}
		
		//存在する場合
		if (!chest.isEmpty()) {
			NBTTagCompound nbt = chest.getTagCompound();
        	Integer posX = nbt.getInteger("BlockPosX");
        	Integer posY = nbt.getInteger("BlockPosY");
        	Integer posZ = nbt.getInteger("BlockPosZ");
        	
        	BlockPos pos = new BlockPos(posX, posY, posZ);
        	TileEntity tile = player.getEntityWorld().getTileEntity(pos);
        	
        	if (tile != null && !tile.isInvalid()) {
        		IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        		if (capability != null) {
	            	//右クリックでGUIを開く
        			player.openGui(YuzuKizuFlower.INSTANCE, YKGuiHandler.REMOTE_CHEST,
	        				player.getEntityWorld(), posX, posY, posZ);
        		}
        	}
		}
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageOpenGui implements IMessage {
		
		public int mode;
		
		public MessageOpenGui() {
		}

		public MessageOpenGui(int mode) {
			this.mode = mode;
		}
		
		/**
		 * byteからの復元
		 * @param buf
		 */
		@Override
		public void fromBytes(ByteBuf buf) {
			
			//書き込んだ順番で読み込み
			this.mode = buf.readInt();
		}

		/**
		 * byteへ変換
		 * @param buf
		 */
		@Override
		public void toBytes(ByteBuf buf) {
			//intを書き込み
			buf.writeInt(this.mode);
		}
	}
}