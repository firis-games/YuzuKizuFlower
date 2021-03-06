package firis.yuzukizuflower.common.network;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	
	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(YuzuKizuFlower.MODID);
	
	public static void init() {
		
		int idx = 1;
		
		//箱入りお花の汎用
		network.registerMessage(PacketTileBoxedFlower.class, PacketTileBoxedFlower.MessageTileBoxedFlower.class, idx++, Side.SERVER);

		//箱入りお花の汎用
		network.registerMessage(PacketTileParticle.class, PacketTileParticle.MessageTileParticle.class, idx++, Side.CLIENT);
		
		//スクロールチェスト用
		network.registerMessage(PacketGuiScroll.class, PacketGuiScroll.MessageGuiScroll.class, idx++, Side.SERVER);
		
		//縁結びの輪表示処理
		network.registerMessage(PacketOpenGuiS2C.class, PacketOpenGuiS2C.MessageOpenGui.class, idx++, Side.SERVER);
		
		//汎用送信処理
		network.registerMessage(PacketTileEntityS2C.class, PacketTileEntityS2C.MessageTileEntity.class, idx++, Side.CLIENT);
		
		//マナプール同期用
		network.registerMessage(PacketManaPoolS2C.class, PacketManaPoolS2C.MessageManaPool.class, idx++, Side.CLIENT);
		
    }

}
