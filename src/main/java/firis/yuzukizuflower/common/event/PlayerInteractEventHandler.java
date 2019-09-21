package firis.yuzukizuflower.common.event;

import firis.yuzukizuflower.common.world.dimension.DimensionHandler;
import firis.yuzukizuflower.common.world.dimension.TeleporterAlfheim;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.block.ModBlocks;

@EventBusSubscriber
public class PlayerInteractEventHandler {

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void onPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
		
		if (event.getWorld().isRemote) return;

		//アルフヘイムのみ動作する
		if (event.getWorld().provider.getDimension() != DimensionHandler.dimensionAlfheim.getId()) return;
		
		//座標 0, y, 0 のみ動作する
		if (event.getPos().getX() != 0 || event.getPos().getZ() != 0) return;
		
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		
		//パイロン系のブロックであること
		if (state.getBlock() != ModBlocks.pylon) return;
		
		//オーバーワールドへテレポート
		overWorldTeleport(event.getEntityPlayer());
		
	}
	
	/**
	 * オーバーワールドへのテレポート処理
	 * @param player
	 */
	public static void overWorldTeleport(EntityPlayer player) {

		if (player == null || player.getEntityWorld().isRemote) return;

		EntityPlayerMP playerMp = (EntityPlayerMP) player;
		
		//オーバーワールドへ戻る
		WorldServer wolrd = playerMp.getServer().getWorld(DimensionType.OVERWORLD.getId());
		
		//移動する
		playerMp.changeDimension(DimensionType.OVERWORLD.getId(), new TeleporterAlfheim(wolrd));
		
		//移動後に座標移動
		BlockPos movePos = playerMp.getBedLocation(DimensionType.OVERWORLD.getId());
		if (movePos == null) {
			movePos = wolrd.provider.getRandomizedSpawnPoint();
		}
		
		//位置調整
		playerMp.connection.setPlayerLocation(movePos.getX(), movePos.getY(), movePos.getZ(), 
				playerMp.rotationYaw, playerMp.rotationPitch);
	}
	
}
