package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileTerraPlate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockTerraPlate extends YKBlockBaseManaPool {

	public YKBlockTerraPlate() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.TERRA_PLATE;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileTerraPlate();
	}	
}
