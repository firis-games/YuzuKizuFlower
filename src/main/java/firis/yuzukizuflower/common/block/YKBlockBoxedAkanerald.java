package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedAkanerald;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedAkanerald extends YKBlockBaseManaPool {

	public YKBlockBoxedAkanerald() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_AKANERALD;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedAkanerald();
	}
}
