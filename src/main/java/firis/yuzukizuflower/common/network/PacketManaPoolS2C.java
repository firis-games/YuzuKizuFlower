package firis.yuzukizuflower.common.network;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.tileentity.YKTileBaseManaPool;
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
public class PacketManaPoolS2C implements IMessageHandler<PacketManaPoolS2C.MessageManaPool, IMessage> {
	
	@Override
	public IMessage onMessage(PacketManaPoolS2C.MessageManaPool message, MessageContext ctx) {
		
		EntityPlayer player = YuzuKizuFlower.proxy.getPlayerPacket(ctx);
		World world = player.getEntityWorld();
		
		//読み込み済みか判断する
		if (!world.isBlockLoaded(message.blockPos)) return null;
		
		//TileEntity取得
		TileEntity tile = world.getTileEntity(message.blockPos);
		
		//対象がパケットを受け取れる場合は処理をキックする
		if (tile instanceof YKTileBaseManaPool) {
			YKTileBaseManaPool packetTile = (YKTileBaseManaPool)tile;
			packetTile.setClientMana(message.mana);
		}
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageManaPool implements IMessage {
		
		//ディメンションIDは使わない
		public BlockPos blockPos;
		public int mana;
		
		public MessageManaPool() {
		}

		public MessageManaPool(BlockPos pos, int mana) {
			this.blockPos = pos;
			this.mana = mana;
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
			this.mana = buf.readInt();
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
			buf.writeInt(this.mana);
		}
	}
}