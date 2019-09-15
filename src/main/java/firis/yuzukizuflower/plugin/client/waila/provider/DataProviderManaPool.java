package firis.yuzukizuflower.plugin.client.waila.provider;

import java.text.NumberFormat;
import java.util.List;

import javax.annotation.Nonnull;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.api.mana.IManaPool;

public class DataProviderManaPool implements IWailaDataProvider {

	/**
	 * registerTailProviderで登録すると実行される
	 */
	@Override
	@Nonnull
	public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		
		TileEntity tile = accessor.getTileEntity();
		if (tile instanceof IManaPool) {
			
			IManaPool manapool = (IManaPool) tile;
			
			Integer mana = manapool.getCurrentMana();

			//アイコンの生成
			//String liquidMana = SpecialChars.getRenderString("waila.stack",
			//		"1",
			//		"botania:twigwand",
			//		String.valueOf(1), 
			//		String.valueOf(0));
			tooltip.add(NumberFormat.getNumberInstance().format(mana) + " Mana");
		}
		return tooltip;
    }
}
