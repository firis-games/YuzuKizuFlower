package firis.yuzukizuflower.common.network;

import firis.yuzukizuflower.common.tileentity.YKTileBoxedRannucarpus;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRannucarpus implements IMessageHandler<MessageRannucarpus, IMessage> {
	@Override
	public IMessage onMessage(MessageRannucarpus message, MessageContext ctx) {
		
		
		//ここでいろいろ処理をやる
		//ワールドを取得する
		
		World world = ctx.getServerHandler().player.getServerWorld();
		
		TileEntity tile = world.getTileEntity(message.blockPos);
		
		//
		if (tile instanceof YKTileBoxedRannucarpus) {
			
			YKTileBoxedRannucarpus flowerTile = (YKTileBoxedRannucarpus)tile;
			
			flowerTile.changeMode();
		}
		
		
		
		return null;
	}
}
