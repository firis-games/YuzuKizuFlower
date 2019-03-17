package firis.yuzukizuflower.common.event;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuFluids;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * MinecraftForge.EVENT_BUS.registerの代わりに@EventBusSubscriber
 */
@EventBusSubscriber
public class PlayerFillBucketEvent {

	/**
	 * 液体マナのバケツイベント
	 * @param events
	 */
	@SubscribeEvent
	public static void fillBucketEvent(FillBucketEvent event) {
		
		if (event.getTarget() == null) {
			return;
		}
		
		//バケツイベント
		EntityPlayer player = event.getEntityPlayer();
		EnumFacing posSide = event.getTarget().sideHit;
		BlockPos pos = event.getTarget().getBlockPos();
		ItemStack stack = event.getEmptyBucket();
		World world = event.getWorld();
		
		if (!player.canPlayerEdit(pos.offset(posSide), posSide, stack)) {
			return;
		}
				
		IBlockState state = world.getBlockState(pos);

		//液体マナ
		if (state.getBlock() == YuzuKizuBlocks.LIQUID_MANA 
				&& ((Integer)state.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
			//液体マナで水源の場合
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
			player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
			ItemStack fstack = FluidUtil.getFilledBucket(FluidRegistry.getFluidStack(YuzuKizuFluids.LIQUID_MANA.getName(), Fluid.BUCKET_VOLUME));
			event.setFilledBucket(fstack);
			event.setResult(Result.ALLOW);
        }
		
	}
	
	
}
