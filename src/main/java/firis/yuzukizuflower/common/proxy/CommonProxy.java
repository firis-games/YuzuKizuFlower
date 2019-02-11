package firis.yuzukizuflower.common.proxy;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.container.YKContainerBoxedEndoflame;
import firis.yuzukizuflower.common.container.YKContainerBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.container.YKContainerBoxedOrechid;
import firis.yuzukizuflower.common.container.YKContainerBoxedPureDaisy;
import firis.yuzukizuflower.common.container.YKContainerBoxedRannuncarpus;
import firis.yuzukizuflower.common.container.YKContainerManaTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommonProxy {
	
	
	/**
	 * IGuiHandler.getServerGuiElement
	 * @param ID
	 * @param player
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		//TileEntityを取得する
		IInventory tile = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
		
		switch(ID) {
				//箱入りピュアデイジー
				case YKGuiHandler.BOXED_PURE_DAISY :
					return new YKContainerBoxedPureDaisy(tile, player.inventory);
			
				//箱入りエンドフレイム
				case YKGuiHandler.BOXED_ENDOFLAME :
					return new YKContainerBoxedEndoflame(tile, player.inventory);

				//マナタンク
				case YKGuiHandler.MANA_TANK :
					return new YKContainerManaTank(tile, player.inventory);
				
				//箱入りラナンカーパス
				case YKGuiHandler.BOXED_RANNUNCARPUS :
					return new YKContainerBoxedRannuncarpus(tile, player.inventory);
				
				//箱入りジェイディッド・アマランサス
				case YKGuiHandler.BOXED_JADED_AMARANTHUS :
					return new YKContainerBoxedJadedAmaranthus(tile, player.inventory);
				
				//箱入りオアキド
				case YKGuiHandler.BOXED_ORECHID :
					return new YKContainerBoxedOrechid(tile, player.inventory);
		}
		return null;
	}
	
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
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
}
