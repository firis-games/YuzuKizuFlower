package firis.yuzukizuflower.common.network;

import firis.yuzukizuflower.common.tileentity.IYKNetworkTileBoxedFlower;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * 箱入りお花の汎用Client to ServerのPacketクラス
 * @author computer
 *
 */
public class PacketTileBoxedFlower implements IMessageHandler<PacketTileBoxedFlower.MessageTileBoxedFlower, IMessage> {
	
	@Override
	public IMessage onMessage(PacketTileBoxedFlower.MessageTileBoxedFlower message, MessageContext ctx) {
		
		//指定のTileEntityのnetwork連動メソッドを呼び出す
		World world = ctx.getServerHandler().player.getServerWorld();
		
		TileEntity tile = world.getTileEntity(message.blockPos);
		
		//対象がパケットを受け取れる場合は処理をキックする
		if (tile instanceof IYKNetworkTileBoxedFlower) {
			IYKNetworkTileBoxedFlower packetTile = (IYKNetworkTileBoxedFlower)tile;
			packetTile.receiveFromClientMessage(message.mode);
		}
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageTileBoxedFlower implements IMessage {
		
		//ディメンションIDは使わない
		public BlockPos blockPos;
		public int mode;
		
		public MessageTileBoxedFlower() {
		}

		public MessageTileBoxedFlower(BlockPos pos, int mode) {
			this.blockPos = pos;
			this.mode = mode;
		}
		
		/**
		 * byteからの復元
		 * @param buf
		 */
		@Override
		public void fromBytes(ByteBuf buf) {
			
			//書き込んだ順番で読み込み
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();
			this.blockPos = new BlockPos(x, y, z);
			this.mode = buf.readInt();
		}

		/**
		 * byteへ変換
		 * @param buf
		 */
		@Override
		public void toBytes(ByteBuf buf) {
			//intを書き込み
			buf.writeInt(this.blockPos.getX());
			buf.writeInt(this.blockPos.getY());
			buf.writeInt(this.blockPos.getZ());
			buf.writeInt(this.mode);
		}
	}
}