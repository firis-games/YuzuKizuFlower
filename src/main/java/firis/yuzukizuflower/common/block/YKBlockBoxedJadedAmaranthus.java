package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedJadedAmaranthus;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedJadedAmaranthus extends YKBlockBaseManaPool {

	public YKBlockBoxedJadedAmaranthus() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.YK_BOXED_JADED_AMARANTHUS;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedJadedAmaranthus();
	}
}
