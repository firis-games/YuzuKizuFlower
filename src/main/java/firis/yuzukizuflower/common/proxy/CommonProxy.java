package firis.yuzukizuflower.common.proxy;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.container.YKContainerAutoWorkbench;
import firis.yuzukizuflower.common.container.YKContainerBoxedAkanerald;
import firis.yuzukizuflower.common.container.YKContainerBoxedAkariculture;
import firis.yuzukizuflower.common.container.YKContainerBoxedAocean;
import firis.yuzukizuflower.common.container.YKContainerBoxedBrewery;
import firis.yuzukizuflower.common.container.YKContainerBoxedClayconia;
import firis.yuzukizuflower.common.container.YKContainerBoxedEndoflame;
import firis.yuzukizuflower.common.container.YKContainerBoxedEntropinnyum;
import firis.yuzukizuflower.common.container.YKContainerBoxedGourmaryllis;
import firis.yuzukizuflower.common.container.YKContainerBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.container.YKContainerBoxedKekimurus;
import firis.yuzukizuflower.common.container.YKContainerBoxedLoonium;
import firis.yuzukizuflower.common.container.YKContainerBoxedOrechid;
import firis.yuzukizuflower.common.container.YKContainerBoxedPureDaisy;
import firis.yuzukizuflower.common.container.YKContainerBoxedRannuncarpus;
import firis.yuzukizuflower.common.container.YKContainerBoxedThermalily;
import firis.yuzukizuflower.common.container.YKContainerBoxedYuquarry;
import firis.yuzukizuflower.common.container.YKContainerCorporeaChest;
import firis.yuzukizuflower.common.container.YKContainerManaEnchanter;
import firis.yuzukizuflower.common.container.YKContainerManaTank;
import firis.yuzukizuflower.common.container.YKContainerPetalWorkbench;
import firis.yuzukizuflower.common.container.YKContainerRemoteChest;
import firis.yuzukizuflower.common.container.YKContainerRuneWorkbench;
import firis.yuzukizuflower.common.container.YKContainerScrollChest;
import firis.yuzukizuflower.common.container.YKContainerTerraPlate;
import firis.yuzukizuflower.common.container.YKInventoryContainer;
import firis.yuzukizuflower.common.inventory.BreweryInventory;
import firis.yuzukizuflower.common.inventory.IInventoryMultiItemHandler;
import firis.yuzukizuflower.common.inventory.IScrollInventoryItemHandler;
import firis.yuzukizuflower.common.inventory.ITextScrollInventoryItemHandler;
import firis.yuzukizuflower.common.inventory.InventoryItemStack;
import firis.yuzukizuflower.common.inventory.ManaEnchanterInventory;
import firis.yuzukizuflower.common.inventory.PetalInventory;
import firis.yuzukizuflower.common.inventory.RuneCraftInventory;
import firis.yuzukizuflower.common.item.YKItemBackpackChest;
import firis.yuzukizuflower.common.tileentity.YKTileCorporeaChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommonProxy {
	
	
	/**
	 * IGuiHandler.getServerGuiElement
	 * @param ID
	 * @param player
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		//TileEntityを取得する
		TileEntity tile = null;
		
		if (!(ID == YKGuiHandler.BACKPACK_CHEST)) {
			tile = world.getTileEntity(new BlockPos(x, y ,z));
		}
		
		switch(ID) {
				//箱入りピュアデイジー
				case YKGuiHandler.BOXED_PURE_DAISY :
					return new YKContainerBoxedPureDaisy((IInventory) tile, player.inventory);
			
				//箱入りエンドフレイム
				case YKGuiHandler.BOXED_ENDOFLAME :
					return new YKContainerBoxedEndoflame((IInventory) tile, player.inventory);

				//マナタンク
				case YKGuiHandler.MANA_TANK :
					return new YKContainerManaTank((IInventory) tile, player.inventory);
				
				//箱入りラナンカーパス
				case YKGuiHandler.BOXED_RANNUNCARPUS :
					return new YKContainerBoxedRannuncarpus((IInventory) tile, player.inventory);
				
				//箱入りジェイディッド・アマランサス
				case YKGuiHandler.BOXED_JADED_AMARANTHUS :
					return new YKContainerBoxedJadedAmaranthus((IInventory) tile, player.inventory);
				
				//箱入りオアキド
				case YKGuiHandler.BOXED_ORECHID :
					return new YKContainerBoxedOrechid((IInventory) tile, player.inventory);
					
				//箱入りガーマリリス
				case YKGuiHandler.BOXED_GOURMARYLLIS :
					return new YKContainerBoxedGourmaryllis((IInventory) tile, player.inventory);
				
				//箱入りアカリカルチャー
				case YKGuiHandler.BOXED_AKARICULTURE :
					return new YKContainerBoxedAkariculture((IInventory) tile, player.inventory);
				
				//箱入りユクァーリー
				case YKGuiHandler.BOXED_YUQUARRY :
					return new YKContainerBoxedYuquarry((IInventory) tile, player.inventory);

				//箱入りアオーシャン
				case YKGuiHandler.BOXED_AOCEAN :
					return new YKContainerBoxedAocean((IInventory) tile, player.inventory);
					
				//箱入りアカネラルド
				case YKGuiHandler.BOXED_AKANERALD :
					return new YKContainerBoxedAkanerald((IInventory) tile, player.inventory);
				
				//スクロールチェスト
				case YKGuiHandler.SCROLL_CHEST :
					ITextScrollInventoryItemHandler iinv = new ITextScrollInventoryItemHandler(tile, true);
					return new YKContainerScrollChest(iinv, player.inventory);
				
				//リモートチェスト
				case YKGuiHandler.REMOTE_CHEST :
					IScrollInventoryItemHandler cinv1 = new IScrollInventoryItemHandler(tile);
					return new YKContainerRemoteChest(cinv1, player.inventory, false);
				
				//リモートチェスト(KEY)
				case YKGuiHandler.REMOTE_CHEST_KEY :
					IScrollInventoryItemHandler cinv2 = new IScrollInventoryItemHandler(tile);
					return new YKContainerRemoteChest(cinv2, player.inventory, true);
					
				//コーポリアチェスト
				case YKGuiHandler.CORPOREA_CHEST :
					YKTileCorporeaChest corpTile = (YKTileCorporeaChest) tile;
					IInventoryMultiItemHandler handler = corpTile.getIInventoryFromCorporeaNetwork();
					return new YKContainerCorporeaChest(handler, player.inventory);
				
				//花びら作業台
				case YKGuiHandler.PETAL_WORKBENCH :
					return new YKContainerPetalWorkbench(
							new PetalInventory(tile), 
							player.inventory);
				
				//ルーン作業台
				case YKGuiHandler.RUNE_WORKBENCH :
					return new YKContainerRuneWorkbench(
							new RuneCraftInventory(tile), 
							player.inventory);
				
				//テラプレート
				case YKGuiHandler.TERRA_PLATE :
					return new YKContainerTerraPlate((IInventory) tile, player.inventory);
				
				//バックパックチェスト
				case YKGuiHandler.BACKPACK_CHEST :
					//PlayerのInventoryのItemStackから取得
					EnumHand hand = x == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
					ItemStack stack = player.getHeldItem(hand);
					return new YKInventoryContainer(new InventoryItemStack(stack), player.inventory, y);

				//バックパックチェスト(KEY)
				case YKGuiHandler.BACKPACK_CHEST_KEY :
					//PlayerのInventoryのItemStackから取得
					return new YKInventoryContainer(
							new InventoryItemStack(YKItemBackpackChest.getBackpackChest(player)), 
							player.inventory, y);
				
				//自動作業台
				case YKGuiHandler.AUTO_WORKBENCH :
					return new YKContainerAutoWorkbench((IInventory) tile, player.inventory);
				
				//箱入りケキムラス
				case YKGuiHandler.BOXED_KEKIMURUS :
					return new YKContainerBoxedKekimurus((IInventory) tile, player.inventory);
				
				//箱入りエントロピウム
				case YKGuiHandler.BOXED_ENTROPINNYUM :
					return new YKContainerBoxedEntropinnyum((IInventory) tile, player.inventory);
					
				//箱入りクレイコニア
				case YKGuiHandler.BOXED_CLAYCONIA :
					return new YKContainerBoxedClayconia((IInventory) tile, player.inventory);
				
				//箱入りルーニウム
				case YKGuiHandler.BOXED_LOONIUM :
					return new YKContainerBoxedLoonium((IInventory) tile, player.inventory);

				//箱入りサーマリリー
				case YKGuiHandler.BOXED_THERMALILY :
					return new YKContainerBoxedThermalily((IInventory) tile, player.inventory);
					
				//箱入り醸造台
				case YKGuiHandler.BOXED_BREWERY :
					return new YKContainerBoxedBrewery(
							new BreweryInventory(tile), 
							player.inventory);
					
				//マナエンチャンター
				case YKGuiHandler.MANA_ENCHANTER :
					return new YKContainerManaEnchanter(
							new ManaEnchanterInventory(tile), 
							player.inventory);

		}
		return null;
	}
	
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
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	
	/**
	 * キー入力イベント登録
	 */
	public void registerKeyBinding() {
		
	}
	
	/**
	 * PlayerLayerを登録する
	 */
	public void initLayerRenderer() {
		
	}
	
}
