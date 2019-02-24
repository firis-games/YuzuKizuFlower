package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedAkariculture;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedAkariculture extends YKBlockBaseManaPool {

	public YKBlockBoxedAkariculture() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_AKARICULTURE;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedAkariculture();
	}
}
