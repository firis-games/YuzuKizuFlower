package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileAutoWorkbench;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockAutoWorkbench extends YKBlockBaseManaPool {

	public YKBlockAutoWorkbench() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.AUTO_WORKBENCH;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileAutoWorkbench();
	}
	
}
