package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedPureDaisy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedPureDaisy extends YKBlockBaseManaPool {

	public YKBlockBoxedPureDaisy() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_PURE_DAISY;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedPureDaisy();
	}
}
