package firis.yuzukizuflower.common.event;

import org.lwjgl.input.Keyboard;

import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.network.PacketOpenGuiS2C;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * キーボードイベント
 * @author computer
 *
 */
@SideOnly(Side.CLIENT)
public class KeyBindingHandler {

	public static final KeyBinding openGui = new KeyBinding("key.open_gui", Keyboard.KEY_N, "itemGroup.tabYuzuKizuFlower");

	public static final KeyBinding openBackPackGui = new KeyBinding("key.open_backpack_gui", Keyboard.KEY_M, "itemGroup.tabYuzuKizuFlower");
	
	/**
	 * キーバインド初期化
	 */
	public static void init() {
		
		ClientRegistry.registerKeyBinding(openGui);
		ClientRegistry.registerKeyBinding(openBackPackGui);
	}
	
	/**
	 * キー入力イベント
	 * @param event
	 */
	@SubscribeEvent
	public void onKeyInputEvent(KeyInputEvent event) {
	
		if (openGui.isKeyDown()) {
			//Server側へ処理を投げる
			//縁結びの輪のGUI表示処理
			NetworkHandler.network.sendToServer(
					new PacketOpenGuiS2C.MessageOpenGui(0));
		} else if (openBackPackGui.isKeyDown()) {
			//Server側へ処理を投げる
			//背負いチェストのGUI表示処理
			NetworkHandler.network.sendToServer(
					new PacketOpenGuiS2C.MessageOpenGui(1));
		}
		
	}
	
}
