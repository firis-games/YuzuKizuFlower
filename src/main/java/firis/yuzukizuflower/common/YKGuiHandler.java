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
	
	//箱入りガーマリリス
	public static final int BOXED_GOURMARYLLIS = 7;
		
	//箱入りアカリカルチャー
	public static final int BOXED_AKARICULTURE = 8;
	
	//箱入りユクァーリー
	public static final int BOXED_YUQUARRY = 9;
	
	//箱入りアオーシャン
	public static final int BOXED_AOCEAN = 10;
	
	//箱入りアオーシャン
	public static final int BOXED_AKANERALD = 11;
	
	//スクロールチェスト
	public static final int SCROLL_CHEST = 12;
	
	//リモートチェスト
	public static final int REMOTE_CHEST = 13;
	
	//コーポリアチェスト
	public static final int CORPOREA_CHEST = 14;
	
	//花びら作業台
	public static final int PETAL_WORKBENCH = 15;
	
	//ルーン作業台
	public static final int RUNE_WORKBENCH = 16;
		
	//ルーン作業台
	public static final int TERRA_PLATE = 17;
	
	//リモートチェスト(キーから表示)
	public static final int REMOTE_CHEST_KEY = 18;
		
		
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return YuzuKizuFlower.proxy.getServerGuiElement(ID, player, world, x, y, z);
	}	

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return YuzuKizuFlower.proxy.getClientGuiElement(ID, player, world, x, y, z);
	}	
}
