package firis.yuzukizuflower.common.helpler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import firis.yuzukizuflower.common.entity.YKFakePlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Blockヘルパー
 * @author computer
 *
 */
public class YKBlockHelper {

	/**
	 * 指定したブロックを破壊する
	 * 破壊した際に入手できるアイテムを返却する
	 * @param world
	 * @param pos
	 * @param silkTouch
	 * @return
	 */
	public static NonNullList<ItemStack> destroyBlock(World world, BlockPos pos, boolean silkTouch, int fortune) {
		
		NonNullList<ItemStack> drops = NonNullList.create();
		
		IBlockState state = world.getBlockState(pos);
		
		//Dropアイテムを取得
		if (silkTouch && 
				state.getBlock().canSilkHarvest(world, pos, state, new YKFakePlayer(world))) {
			//シルクタッチ
			ItemStack silkItemStack = ItemStack.EMPTY.copy();
			Method method = YKReflectionHelper.findMethod(state.getBlock().getClass(), "getSilkTouchDrop", "func_180643_i", IBlockState.class);
			try {
				silkItemStack = (ItemStack) method.invoke(state.getBlock(), state);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			drops.add(silkItemStack);
		} else {
			//通常Drop
			state.getBlock().getDrops(drops, world, pos, state, fortune);
		}
		
		//Block破壊処理
		//それ以外の場合はブロックを破壊して終了
		//ただのブロック破壊
		world.destroyBlock(pos, false);
		//空気への入れ替え
		world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 3);
		
		return drops;
	}
	
}
