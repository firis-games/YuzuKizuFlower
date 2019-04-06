package firis.yuzukizuflower.common.proxy;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.container.YKContainerBoxedAkanerald;
import firis.yuzukizuflower.common.container.YKContainerBoxedAkariculture;
import firis.yuzukizuflower.common.container.YKContainerBoxedAocean;
import firis.yuzukizuflower.common.container.YKContainerBoxedEndoflame;
import firis.yuzukizuflower.common.container.YKContainerBoxedGourmaryllis;
import firis.yuzukizuflower.common.container.YKContainerBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.container.YKContainerBoxedOrechid;
import firis.yuzukizuflower.common.container.YKContainerBoxedPureDaisy;
import firis.yuzukizuflower.common.container.YKContainerBoxedRannuncarpus;
import firis.yuzukizuflower.common.container.YKContainerBoxedYuquarry;
import firis.yuzukizuflower.common.container.YKContainerCorporeaChest;
import firis.yuzukizuflower.common.container.YKContainerManaTank;
import firis.yuzukizuflower.common.container.YKContainerScrollChest;
import firis.yuzukizuflower.common.inventory.IInventoryMultiItemHandler;
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
		TileEntity tile = world.getTileEntity(new BlockPos(x, y ,z));
		IItemHandler capability;
		
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
					YKTileScrollChest yktile = (YKTileScrollChest) tile;
					IScrollInventoryHandler iinv = (IScrollInventoryHandler) yktile.getIInventory();
					return new YKContainerScrollChest(iinv, player.inventory);
				
				//リモートチェスト
				case YKGuiHandler.REMOTE_CHEST :
					capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					if (capability != null) {
						IScrollInventoryHandler ciinv = new IScrollInventoryHandler(capability, tile);
						return new YKContainerScrollChest(ciinv, player.inventory);
					}
				
				//コーポリアチェスト
				case YKGuiHandler.CORPOREA_CHEST :
					YKTileCorporeaChest corpTile = (YKTileCorporeaChest) tile;
					IInventoryMultiItemHandler handler = corpTile.getIInventoryFromCorporeaNetwork();
					return new YKContainerCorporeaChest(handler, player.inventory);
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
}
