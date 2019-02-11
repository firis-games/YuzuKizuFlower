package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedEndoflame;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedEndoflame extends YKBlockBaseManaPool {

	/**
	 * コンストラクタ
	 */
	public YKBlockBoxedEndoflame(int mode) {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_ENDOFLAME;
		
		this.mode = mode;
	}
	
	protected int mode = 0;
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedEndoflame(mode);
	}
}
