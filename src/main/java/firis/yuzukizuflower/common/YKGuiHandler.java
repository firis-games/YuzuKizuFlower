package firis.yuzukizuflower.common;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class YKGuiHandler implements IGuiHandler {

	//箱入りピュアデイジー
	public static final int BOXED_PURE_DAISY = 1;
	
	//箱入りエンドフレイム
	public static final int BOXED_ENDOFLAME = 2;
	
	//マナタンク
	public static final int MANA_TANK = 3;
	
	//箱入り・ラナンカーパス
	public static final int BOXED_RANNUNCARPUS = 4;
	
	//箱入りジェイディッド・アマランサス
	public static final int BOXED_JADED_AMARANTHUS = 5;
	
	//箱入りオアキド
	public static final int BOXED_ORECHID = 6;
		
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return YuzuKizuFlower.proxy.getServerGuiElement(ID, player, world, x, y, z);
	}	

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return YuzuKizuFlower.proxy.getClientGuiElement(ID, player, world, x, y, z);
	}	
}
