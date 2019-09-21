package firis.yuzukizuflower.common.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuItems;
import firis.yuzukizuflower.common.event.PlayerInteractEventHandler;
import firis.yuzukizuflower.common.world.dimension.DimensionHandler;
import firis.yuzukizuflower.common.world.dimension.TeleporterAlfheim;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.common.block.ModBlocks;

public class YKItemDimensionKey extends Item {

	//Dimensionキーの扱い(0:通常　1:不滅)
	//別アイテム扱いにするためコンストラクタで擬似的なmetadataを渡して判断する
	private final int metadata;
	
	/**
	 * コンストラクタ
	 */
	public YKItemDimensionKey(int metadata) {

		super();
		
		this.metadata = metadata;
		
		//初期化
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		
		if (this.metadata == 0) {
			this.setMaxStackSize(64);
		} else if (this.metadata == 1) {
			this.setMaxStackSize(1);
		}		
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
		boolean ret = dimensionKeyTeleport(playerIn, handIn);
		if (!ret) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    	}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
	
	/**
	 * アルフヘイムキーのテレポート処理
	 * @param player
	 * @param hand
	 * @return
	 */
	protected boolean dimensionKeyTeleport(EntityPlayer player, EnumHand hand) {

		boolean ret = false;
		World world = player.getEntityWorld();
		if (world.provider.getDimension() == DimensionType.OVERWORLD.getId()) {
			//オーバーワールド -> アルフヘイム
			ret = this.alfheimTeleport(player, hand);
		} else if (world.provider.getDimension() != DimensionType.OVERWORLD.getId()) {
			//アルフヘイム -> オーバーワールド
			ret = this.overWorldTeleport(player, hand);
		}
		
		//テレポート成功時
		//クリエイティブ判定
		if (ret && !player.isCreative() && this.metadata == 0) {
			ItemStack stack = player.getHeldItem(hand);
			stack.shrink(1);
		}
		//イモータルディメンションキー
		else if (ret && !player.isCreative() && this.metadata == 1) {
			player.getCooldownTracker().setCooldown(YuzuKizuItems.IMMORTAL_DIMENSION_KEY, 1200);
		}
		
		return ret;
	}
	
	
	/**
	 * アルフヘイムへのテレポート判定処理
	 * @param player
	 * @param hand
	 * @return
	 */
	protected boolean alfheimTeleport(EntityPlayer player, EnumHand hand) {
		
		boolean ret = false;

		World world = player.getEntityWorld();
		
		//playerの足元3×3を取得
		if (this.metadata == 0) {
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
		} else {
			//イモータルの場合は条件無視
			ret = true;
		}
		
		//クリエイティブモードの場合は条件を無視する
		if (!ret && !player.isCreative()) return ret;
		
		ret = true;

		//成功時テレポートを行う
		if(world.isRemote) return ret;
		
		//アルフヘイムポータルが起動中に転移を行う
		WorldServer server;
		server = player.getServer().getWorld(DimensionHandler.dimensionAlfheim.getId());
		player.changeDimension(DimensionHandler.dimensionAlfheim.getId(), new TeleporterAlfheim(server));
		return ret;
	}
	
	/**
	 * アルフヘイムへのテレポート判定処理
	 * @param player
	 * @param hand
	 * @return
	 */
	protected boolean overWorldTeleport(EntityPlayer player, EnumHand hand) {
		
		boolean ret = false;
		
		//ゲート付近のみとしようとしたが
		//Server側とclient側のプレイヤー座標のずれが発生するため
		//簡単にアルフヘイムディメンションで使った場合にテレポートするように変更
		
		//オーバーワールドへ戻る
		PlayerInteractEventHandler.overWorldTeleport(player);
		ret = true;
		
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
			String info = "info.dimension_key";
			if (this.metadata == 1) {
				info = "info.immortal_dimension_key";
			}
			tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(info));			
		}
    }
	
	
	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		
		if (this.metadata == 1) {
			return BotaniaAPI.rarityRelic;
		}
		return EnumRarity.RARE;		
	}
	
}
