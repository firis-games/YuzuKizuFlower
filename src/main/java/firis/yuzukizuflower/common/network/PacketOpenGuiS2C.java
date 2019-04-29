package firis.yuzukizuflower.common.network;

import firis.yuzukizuflower.common.item.YKItemRemoteChest;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
		
		//アミュレット枠から縁結びの輪を取得する
		ItemStack chest = YKItemRemoteChest.getBaublesItemStack(player);
		
		//条件を満たす場合にGUIを表示する
		YKItemRemoteChest.openGui(chest, player);
		
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