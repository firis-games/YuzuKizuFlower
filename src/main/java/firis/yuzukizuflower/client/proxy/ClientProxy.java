package firis.yuzukizuflower.client.proxy;

import java.util.Map;

import firis.yuzukizuflower.client.gui.YKGuiContainerAutoWorkbench;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedAkanerald;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedAkariculture;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedAocean;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedClayconia;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedEndoflame;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedEntropinnyum;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedGourmaryllis;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedJadedAmaranthus;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedKekimurus;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedLoonium;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedOrechid;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedPureDaisy;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedRannuncarpus;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedThermalily;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedYuquarry;
import firis.yuzukizuflower.client.gui.YKGuiContainerCorporeaChest;
import firis.yuzukizuflower.client.gui.YKGuiContainerManaTank;
import firis.yuzukizuflower.client.gui.YKGuiContainerPetalWorkbench;
import firis.yuzukizuflower.client.gui.YKGuiContainerRemoteChest;
import firis.yuzukizuflower.client.gui.YKGuiContainerRuneWorkbench;
import firis.yuzukizuflower.client.gui.YKGuiContainerScrollChest;
import firis.yuzukizuflower.client.gui.YKGuiContainerTerraPlate;
import firis.yuzukizuflower.client.gui.YKGuiInventoryContainer;
import firis.yuzukizuflower.client.layer.YKBackPackLayer;
import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.event.KeyBindingHandler;
import firis.yuzukizuflower.common.inventory.IScrollInventoryClientItemHandler;
import firis.yuzukizuflower.common.inventory.InventoryItemStack;
import firis.yuzukizuflower.common.inventory.PetalInventory;
import firis.yuzukizuflower.common.inventory.RuneCraftInventory;
import firis.yuzukizuflower.common.item.YKItemBackpackChest;
import firis.yuzukizuflower.common.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

	/**
	 * IGuiHandler.getClientGuiElement
	 * @param ID
	 * @param player
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		//TileEntityを取得する
		TileEntity tile = world.getTileEntity(new BlockPos(x, y ,z));
		IScrollInventoryClientItemHandler handler;
		
		switch(ID) {
				//箱入りピュアデイジー
				case YKGuiHandler.BOXED_PURE_DAISY :
					return new YKGuiContainerBoxedPureDaisy((IInventory) tile, player.inventory);
			
				//箱入りエンドフレイム
				case YKGuiHandler.BOXED_ENDOFLAME :
					return new YKGuiContainerBoxedEndoflame((IInventory) tile, player.inventory);

				//マナタンク
				case YKGuiHandler.MANA_TANK :
					return new YKGuiContainerManaTank((IInventory) tile, player.inventory);
				
				//箱入りラナンカーパス
				case YKGuiHandler.BOXED_RANNUNCARPUS :
					return new YKGuiContainerBoxedRannuncarpus((IInventory) tile, player.inventory);
				
				//箱入りジェイディッド・アマランサス
				case YKGuiHandler.BOXED_JADED_AMARANTHUS :
					return new YKGuiContainerBoxedJadedAmaranthus((IInventory) tile, player.inventory);
				
				//箱入りオアキド
				case YKGuiHandler.BOXED_ORECHID :
					return new YKGuiContainerBoxedOrechid((IInventory) tile, player.inventory);
					
				//箱入りガーマリリス
				case YKGuiHandler.BOXED_GOURMARYLLIS :
					return new YKGuiContainerBoxedGourmaryllis((IInventory) tile, player.inventory);
				
				//箱入りアカリカルチャー
				case YKGuiHandler.BOXED_AKARICULTURE :
					return new YKGuiContainerBoxedAkariculture((IInventory) tile, player.inventory);

				//箱入りユクァーリー
				case YKGuiHandler.BOXED_YUQUARRY :
					return new YKGuiContainerBoxedYuquarry((IInventory) tile, player.inventory);

				//箱入りアオーシャン
				case YKGuiHandler.BOXED_AOCEAN :
					return new YKGuiContainerBoxedAocean((IInventory) tile, player.inventory);
				
				//箱入りアカネラルド
				case YKGuiHandler.BOXED_AKANERALD :
					return new YKGuiContainerBoxedAkanerald((IInventory) tile, player.inventory);

				//スクロールチェスト
				case YKGuiHandler.SCROLL_CHEST :
					handler = new IScrollInventoryClientItemHandler(54, tile);
					//ITextScrollInventoryItemHandler iinv = new ITextScrollInventoryItemHandler(tile, true);
					return new YKGuiContainerScrollChest(handler, player.inventory);
				
				//リモートチェスト
				case YKGuiHandler.REMOTE_CHEST :
					handler = new IScrollInventoryClientItemHandler(54);
					return new YKGuiContainerRemoteChest(handler, player.inventory, false);
				
				//リモートチェスト(KEY)
				case YKGuiHandler.REMOTE_CHEST_KEY :
					handler = new IScrollInventoryClientItemHandler(54);
					return new YKGuiContainerRemoteChest(handler, player.inventory, true);
				
				//コーポリアチェスト
				case YKGuiHandler.CORPOREA_CHEST :
					handler = new IScrollInventoryClientItemHandler(54, tile);
					return new YKGuiContainerCorporeaChest(handler, player.inventory);
					
				//花びら作業台
				case YKGuiHandler.PETAL_WORKBENCH :
					return new YKGuiContainerPetalWorkbench(new PetalInventory(tile), 
							player.inventory);

				//ルーン作業台
				case YKGuiHandler.RUNE_WORKBENCH :
					return new YKGuiContainerRuneWorkbench(new RuneCraftInventory(tile), 
							player.inventory);
					
				//テラプレート
				case YKGuiHandler.TERRA_PLATE :
					return new YKGuiContainerTerraPlate((IInventory) tile, player.inventory);

				//バックパックチェスト
				case YKGuiHandler.BACKPACK_CHEST :
					//PlayerのInventoryのItemStackから取得
					EnumHand hand = x == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
					ItemStack stack = player.getHeldItem(hand);
					return new YKGuiInventoryContainer(new InventoryItemStack(stack), player.inventory, y);

				//バックパックチェスト(KEY)
				case YKGuiHandler.BACKPACK_CHEST_KEY :
					//PlayerのInventoryのItemStackから取得
					return new YKGuiInventoryContainer(
							new InventoryItemStack(YKItemBackpackChest.getBackpackChest(player)), 
							player.inventory, y);
				//自動作業台
				case YKGuiHandler.AUTO_WORKBENCH :
					return new YKGuiContainerAutoWorkbench((IInventory) tile, player.inventory);

				//箱入りケキムラス
				case YKGuiHandler.BOXED_KEKIMURUS :
					return new YKGuiContainerBoxedKekimurus((IInventory) tile, player.inventory);

				//箱入りエントロピウム
				case YKGuiHandler.BOXED_ENTROPINNYUM :
					return new YKGuiContainerBoxedEntropinnyum((IInventory) tile, player.inventory);
					
				//箱入りクレイコニア
				case YKGuiHandler.BOXED_CLAYCONIA :
					return new YKGuiContainerBoxedClayconia((IInventory) tile, player.inventory);

				//箱入りルーニウム
				case YKGuiHandler.BOXED_LOONIUM :
					return new YKGuiContainerBoxedLoonium((IInventory) tile, player.inventory);

					//箱入りルーニウム
				case YKGuiHandler.BOXED_THERMALILY :
					return new YKGuiContainerBoxedThermalily((IInventory) tile, player.inventory);

		}
		return null;
	}
	
	@Override
	public void registerKeyBinding() {
		//キーバインディング設定
		KeyBindingHandler.init();
	}
	
	/**
	 * PlayerLayerを登録する
	 */
	@Override
	public void initLayerRenderer() {
		
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		
		RenderPlayer render;
		render = skinMap.get("default");
		render.addLayer(new YKBackPackLayer());

		render = skinMap.get("slim");
		render.addLayer(new YKBackPackLayer());
		
	}
}
