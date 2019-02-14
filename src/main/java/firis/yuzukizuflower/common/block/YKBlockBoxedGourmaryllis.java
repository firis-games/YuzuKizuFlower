package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedGourmaryllis;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedGourmaryllis extends YKBlockBaseManaPool {

	/**
	 * コンストラクタ
	 */
	public YKBlockBoxedGourmaryllis(int mode) {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_GOURMARYLLIS;
		
		this.mode = mode;
	}
	
	protected int mode = 0;
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedGourmaryllis(mode);
	}
}
