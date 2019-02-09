package firis.yuzukizuflower.network;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	
	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(YuzuKizuFlower.MODID);
	
	public static void init() {
		
		//箱入りラナンカーパス
		network.registerMessage(PacketRannucarpus.class, MessageRannucarpus.class, 0, Side.SERVER);
		
    }

}
