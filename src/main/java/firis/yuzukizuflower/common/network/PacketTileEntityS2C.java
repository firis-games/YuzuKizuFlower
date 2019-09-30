package firis.yuzukizuflower.common.network;

import firis.yuzukizuflower.YuzuKizuFlower;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * ServerからClientへフラグを送信する汎用メッセージ
 * @author computer
 *
 */
public class PacketTileEntityS2C implements IMessageHandler<PacketTileEntityS2C.MessageTileEntity, IMessage> {
	
	@Override
	public IMessage onMessage(PacketTileEntityS2C.MessageTileEntity message, MessageContext ctx) {
		
		EntityPlayer player = YuzuKizuFlower.proxy.getPlayerPacket(ctx);
		World world = player.getEntityWorld();
		
		//読み込み済みか判断する
		if (!world.isBlockLoaded(message.blockPos)) return null;
		
		//TileEntity取得
		TileEntity tile = world.getTileEntity(message.blockPos);
		
		//対象がパケットを受け取れる場合は処理をキックする
		if (tile instanceof ITileEntityPacketReceive) {
			ITileEntityPacketReceive packetTile = (ITileEntityPacketReceive)tile;
			packetTile.receivePacket(message.mode);
		}
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageTileEntity implements IMessage {
		
		//ディメンションIDは使わない
		public BlockPos blockPos;
		public int mode;
		
		public MessageTileEntity() {
		}

		public MessageTileEntity(BlockPos pos, int mode) {
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