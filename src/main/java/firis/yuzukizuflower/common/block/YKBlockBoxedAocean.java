package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedAocean;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedAocean extends YKBlockBaseManaPool {

	public YKBlockBoxedAocean() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_AOCEAN;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedAocean();
	}
}
