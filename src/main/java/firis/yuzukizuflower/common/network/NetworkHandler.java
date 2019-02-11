package firis.yuzukizuflower.common.network;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	
	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(YuzuKizuFlower.MODID);
	
	public static void init() {
		
		//箱入りお花の汎用
		network.registerMessage(PacketTileBoxedFlower.class, PacketTileBoxedFlower.MessageTileBoxedFlower.class, 0, Side.SERVER);
		
    }

}
