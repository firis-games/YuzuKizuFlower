package firis.yuzukizuflower.client.proxy;

import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedEndoflame;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedJadedAmaranthus;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedOrechid;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedPureDaisy;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedRannucarpus;
import firis.yuzukizuflower.client.gui.YKGuiContainerManaTank;
import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy{
	
	
	/**
	 * IGuiHandler.getClientGuiElement
	 * @param ID
	 * @param player
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		//TileEntityを取得する
		IInventory tile = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
		
		switch(ID) {
				//箱入りピュアデイジー
				case YKGuiHandler.BOXED_PURE_DAISY :
					return new YKGuiContainerBoxedPureDaisy(tile, player.inventory);
			
				//箱入りエンドフレイム
				case YKGuiHandler.BOXED_ENDOFLAME :
					return new YKGuiContainerBoxedEndoflame(tile, player.inventory);

				//マナタンク
				case YKGuiHandler.MANA_TANK :
					return new YKGuiContainerManaTank(tile, player.inventory);
				
				//箱入りラナンカーパス
				case YKGuiHandler.BOXED_RANNUCARPUS :
					return new YKGuiContainerBoxedRannucarpus(tile, player.inventory);
				
				//箱入りジェイディッド・アマランサス
				case YKGuiHandler.BOXED_JADED_AMARANTHUS :
					return new YKGuiContainerBoxedJadedAmaranthus(tile, player.inventory);
				
				//箱入りオアキド
				case YKGuiHandler.BOXED_ORECHID :
					return new YKGuiContainerBoxedOrechid(tile, player.inventory);
		}
		return null;
	}
}
