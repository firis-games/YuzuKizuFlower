package firis.yuzukizuflower.common.network;

import java.awt.Color;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.botania.common.Botania;

/**
 * 箱入りお花の汎用Client to ServerのPacketクラス
 * @author computer
 *
 */
public class PacketTileParticle implements IMessageHandler<PacketTileParticle.MessageTileParticle, IMessage> {
	
	@Override
	public IMessage onMessage(PacketTileParticle.MessageTileParticle message, MessageContext ctx) {
		
		BlockPos pos = message.particlePos;
		
		//紫色パーティクルを表示
		Color color = new Color(167, 87, 168);
		
		//マナプールと同じパーティクル
		for (int i = 0; i < 3; i++) {
			Botania.proxy.wispFX(
					pos.getX() + 0.3 + Math.random() * 0.5, 
					pos.getY() + 0.6 + Math.random() * 0.25, 
					pos.getZ() + Math.random(), 
					color.getRed() / 255F, 
					color.getGreen() / 255F, 
					color.getBlue() / 255F, 
					(float) Math.random() / 2.0F, 
					(float) -Math.random() / 15.0F, 
					0.8F);
		}
		
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageTileParticle implements IMessage {
		
		public BlockPos particlePos;
		
		public MessageTileParticle() {
		}

		public MessageTileParticle(BlockPos particlePos) {
			this.particlePos = particlePos;
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
			this.particlePos = new BlockPos(x, y, z);
		}

		/**
		 * byteへ変換
		 * @param buf
		 */
		@Override
		public void toBytes(ByteBuf buf) {
			//intを書き込み
			buf.writeInt(this.particlePos.getX());
			buf.writeInt(this.particlePos.getY());
			buf.writeInt(this.particlePos.getZ());
		}
	}
}