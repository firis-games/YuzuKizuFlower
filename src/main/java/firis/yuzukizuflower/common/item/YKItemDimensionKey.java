package firis.yuzukizuflower.common.item;

import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.world.dimension.DimensionHandler;
import firis.yuzukizuflower.common.world.dimension.TeleporterAlfheim;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.common.block.ModBlocks;

public class YKItemDimensionKey extends Item {

	/**
	 * コンストラクタ
	 */
	public YKItemDimensionKey() {

		super();
		
		//初期化
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		this.setMaxStackSize(64);
	}
	
	/**
     * Called when a Block is right-clicked with this Item
     */
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		boolean ret = alfheimTeleport(player, hand);
		if (!ret) {
			return EnumActionResult.PASS;
    	}
		return EnumActionResult.SUCCESS;
    }
    
	/**
     * Called when the equipped item is right clicked.
     */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		boolean ret = alfheimTeleport(playerIn, handIn);
		if (!ret) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    	}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
	
	protected boolean alfheimTeleport(EntityPlayer player, EnumHand hand) {
		
		boolean ret = false;

		World world = player.getEntityWorld();
		
		//playerの足元3×3を取得
		BlockPos basePos = player.getPosition().down();
		for (BlockPos pos : BlockPos.getAllInBox(basePos.north(1).east(1), basePos.south(1).west(1))) {
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() == ModBlocks.alfPortal) {
				if (state.getProperties().get(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.ON_X
						|| state.getProperties().get(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.ON_Z) {
					ret = true;
					break;
				}
			}
		}
		
		if (!ret) return ret;
		
		//クリエイティブ判定
		if (!player.isCreative()) {
			ItemStack stack = player.getHeldItem(hand);
			stack.shrink(1);
		}

		//成功時テレポートを行う
		if(world.isRemote) return ret;
		
		//アルフヘイムポータルが起動中に転移を行う
		WorldServer server;
		server = player.getServer().getWorld(DimensionHandler.dimensionAlfheim.getId());
		player.changeDimension(DimensionHandler.dimensionAlfheim.getId(), new TeleporterAlfheim(server));
		return ret;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if (flagIn.isAdvanced()) {
			tooltip.add(TextFormatting.DARK_PURPLE + I18n.format("info.dimension_key"));			
		}
    }
	
	
}
