package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedLoonium;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedLoonium extends YKBlockBaseManaPool {

	public YKBlockBoxedLoonium() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_LOONIUM;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedLoonium();
	}
}
