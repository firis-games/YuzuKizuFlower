package firis.yuzukizuflower.client.proxy;

import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedAkanerald;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedAkariculture;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedAocean;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedEndoflame;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedGourmaryllis;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedJadedAmaranthus;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedOrechid;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedPureDaisy;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedRannuncarpus;
import firis.yuzukizuflower.client.gui.YKGuiContainerBoxedYuquarry;
import firis.yuzukizuflower.client.gui.YKGuiContainerBaseScrollInventory;
import firis.yuzukizuflower.client.gui.YKGuiContainerManaTank;
import firis.yuzukizuflower.client.gui.YKGuiContainerScrollChest;
import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.inventory.IInventoryMultiItemHandler;
import firis.yuzukizuflower.common.proxy.CommonProxy;
import firis.yuzukizuflower.common.tileentity.YKTileCorporeaChest;
import firis.yuzukizuflower.common.tileentity.YKTileScrollChest;
import firis.yuzukizuflower.common.tileentity.YKTileScrollChest.IScrollInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ClientProxy extends CommonProxy{
	
	
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
		IItemHandler capability;
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
					YKTileScrollChest yktile = (YKTileScrollChest) tile;
					IScrollInventoryHandler iinv = (IScrollInventoryHandler) yktile.getIInventory();
					return new YKGuiContainerScrollChest(iinv, player.inventory);
				
				//リモートチェスト
				case YKGuiHandler.REMOTE_CHEST :
					capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					if (capability != null) {
						IScrollInventoryHandler ciinv = new IScrollInventoryHandler(capability, tile);
						return new YKGuiContainerScrollChest(ciinv, player.inventory);
					}
				
				//コーポリアチェスト
				case YKGuiHandler.CORPOREA_CHEST :
					YKTileCorporeaChest corpTile = (YKTileCorporeaChest) tile;
					IInventoryMultiItemHandler handler = corpTile.getIInventoryFromCorporeaNetwork();
					return new YKGuiContainerBaseScrollInventory(handler, player.inventory);

		}
		return null;
	}
}
