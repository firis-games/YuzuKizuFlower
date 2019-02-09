package firis.yuzukizuflower.gui;

import firis.yuzukizuflower.container.YKContainerBoxedEndoflame;
import firis.yuzukizuflower.container.YKContainerBoxedJadedAmaranthus;
import firis.yuzukizuflower.container.YKContainerBoxedOrechid;
import firis.yuzukizuflower.container.YKContainerBoxedPureDaisy;
import firis.yuzukizuflower.container.YKContainerBoxedRannucarpus;
import firis.yuzukizuflower.container.YKContainerManaTank;
import firis.yuzukizuflower.container.YKGuiContainerBoxedEndoflame;
import firis.yuzukizuflower.container.YKGuiContainerBoxedJadedAmaranthus;
import firis.yuzukizuflower.container.YKGuiContainerBoxedOrechid;
import firis.yuzukizuflower.container.YKGuiContainerBoxedPureDaisy;
import firis.yuzukizuflower.container.YKGuiContainerBoxedRannucarpus;
import firis.yuzukizuflower.container.YKGuiContainerManaTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class YKGuiHandler implements IGuiHandler {

	//箱入りピュアデイジー
	public static int YK_BOXED_PURE_DAISY = 1;
	
	//箱入りエンドフレイム
	public static int YK_BOXED_ENDOFLAME = 2;
	
	//マナタンク
	public static int MANA_TANK = 3;
	
	//マナタンク
	public static int YK_BOXED_RANNUCARPUS = 4;
	
	//お花のお花
	public static int YK_BOXED_JADED_AMARANTHUS = 5;
	
	//オアチド
	public static int YK_BOXED_ORECHID = 6;
		
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		//IDで判断する
		if (ID == YKGuiHandler.YK_BOXED_PURE_DAISY) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKContainerBoxedPureDaisy(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.YK_BOXED_ENDOFLAME) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKContainerBoxedEndoflame(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.MANA_TANK) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKContainerManaTank(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.YK_BOXED_RANNUCARPUS) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKContainerBoxedRannucarpus(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.YK_BOXED_JADED_AMARANTHUS) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKContainerBoxedJadedAmaranthus(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.YK_BOXED_ORECHID) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKContainerBoxedOrechid(inv, player.inventory);
			
		}
		return null;
	}	

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		//IDで判断する
		if (ID == YKGuiHandler.YK_BOXED_PURE_DAISY) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));

			return new YKGuiContainerBoxedPureDaisy(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.YK_BOXED_ENDOFLAME) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKGuiContainerBoxedEndoflame(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.MANA_TANK) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKGuiContainerManaTank(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.YK_BOXED_RANNUCARPUS) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKGuiContainerBoxedRannucarpus(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.YK_BOXED_JADED_AMARANTHUS) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKGuiContainerBoxedJadedAmaranthus(inv, player.inventory);
			
		} else if (ID == YKGuiHandler.YK_BOXED_ORECHID) {
			
			//TileEntityを取得する
			IInventory inv = (IInventory) world.getTileEntity(new BlockPos(x, y ,z));
			
			return new YKGuiContainerBoxedOrechid(inv, player.inventory);
			
		}
		return null;
	}
	
}
