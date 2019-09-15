package firis.yuzukizuflower.plugin.client.waila;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.plugin.client.waila.provider.DataProviderManaPool;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import vazkii.botania.api.mana.IManaPool;

@WailaPlugin(value=YuzuKizuFlower.MODID)
public class WailaPluginYuzuKizuFlower implements IWailaPlugin {

	
	@Override
	public void register(IWailaRegistrar registrar) {
		
		//IMana対応
		registrar.registerTailProvider(new DataProviderManaPool(), IManaPool.class);

	}
}
