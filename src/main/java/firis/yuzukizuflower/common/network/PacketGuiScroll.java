package firis.yuzukizuflower.common.network;

import firis.yuzukizuflower.common.container.YKContainerScrollChest;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * スクロールチェスト用Client to ServerのPacketクラス
 * @author computer
 *
 */
public class PacketGuiScroll implements IMessageHandler<PacketGuiScroll.MessageGuiScroll, IMessage> {
	
	@Override
	public IMessage onMessage(PacketGuiScroll.MessageGuiScroll message, MessageContext ctx) {
		
		//Playerを取得
		EntityPlayerMP player = ctx.getServerHandler().player;
		
		//現在openしているGUI
		Container container = player.openContainer;
		if (container instanceof YKContainerScrollChest) {
			//ページを設定
			YKContainerScrollChest ykcontainer = (YKContainerScrollChest) container;
			ykcontainer.iTeInv.setPage(message.page);
		}
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageGuiScroll implements IMessage {
		
		public int page = 0;
		public String text = "";
		
		public MessageGuiScroll() {
		}
		
		public MessageGuiScroll(int page, String text) {
			this.page = page;
			this.text = text;
		}
		
		/**
		 * byteからの復元
		 * @param buf
		 */
		@Override
		public void fromBytes(ByteBuf buf) {
			
			//書き込んだ順番で読み込み
			//intを読み込み
			this.page = buf.readInt();

			//stringを読み込み
			ByteBuf stringBuf = buf.readBytes(buf.readableBytes());
			byte[] strBytes = new byte[stringBuf.readableBytes()];
			if (strBytes.length != 0) {
				stringBuf.getBytes(0, strBytes);
				this.text = new String(strBytes);
			}
		}

		/**
		 * byteへ変換
		 * @param buf
		 */
		@Override
		public void toBytes(ByteBuf buf) {
			//intを書き込み
			buf.writeInt(this.page);
			//stringを書き込み
			buf.writeBytes(this.text.getBytes());
		}
	}
}