package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.tileentity.YKTileBoxedEndoflame;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.handler.HUDHandler;

public abstract class YKBotaniaGenBase extends BlockContainer implements IWandHUD, IWandable {

	/*
	 * コンストラクタ
	 */
	protected YKBotaniaGenBase(Material materialIn) {
		super(materialIn);
	}

	@Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
    /**
     * 重ねた際の描画OFF or ON
     */
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	/**
	 * テクスチャ重ねを有効化
	 * TRANSLUCENTの場合はアイテム状態のモデルがおかしくなる（モデル全体が透過してる？）
	 * CUTOUT_MIPPEDの場合は同じサイズのモデルの重ねの場合は正常に表示される（草ブロックと同じ）
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
	public abstract TileEntity createNewTileEntity(World worldIn, int meta);
	

	//IWandHUD
	//******************************************************************************************
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		
		/*
		if (!(world.getTileEntity(pos) instanceof YKTileBoxedEndoflame)) {
        	return;
        }
        YKTileBoxedEndoflame te = (YKTileBoxedEndoflame)world.getTileEntity(pos);
        
		//SubTileGeneratingでやっていることを実装
		//String name = I18n.format("tile.botania:flower." + getUnlocalizedName() + ".name");
		String name = I18n.format("tile.boxed_endoflame.name");
		
		//int color = getColor();
		int color = 0x785000;
//一度クリックするとマナの容量をみえるようになる
		//-1だと不明な状態って表示される
		BotaniaAPI.internalHandler.drawComplexManaHUD(color, 
				te.mana, 
//				-1, 
				//getMaxMana(), 
				te.maxMana,
				name, 
				res, 
				BotaniaAPI.internalHandler.getBindDisplayForFlowerType(new SubTileFunctional()),
				//new ItemStack(YuzuKizuBlocks.BOXED_ENDOFLAME),
				//new ItemStack(YuzuKizuBlocks.BOXED_ENDOFLAME),
				//isValidBinding());
				false);
		*/
		
		if (!(world.getTileEntity(pos) instanceof YKTileBoxedEndoflame)) {
        	return;
        }
        YKTileBoxedEndoflame te = (YKTileBoxedEndoflame)world.getTileEntity(pos);
		
		//ItemStack pool = new ItemStack(YuzuKizuBlocks.BOXED_ENDOFLAME);
		//String name = I18n.format(pool.getUnlocalizedName().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD) + ".name");
		String name = I18n.format("tile.boxed_endoflame.name");
		int color = 0x4444FF;
		HUDHandler.drawSimpleManaHUD(color, te.getMana(), te.getMaxMana(), name, res);

		/* ここはたぶんマナタブレットを表示するところ
		int x = res.getScaledWidth() / 2 - 11;
		int y = res.getScaledHeight() / 2 + 30;

		int u = outputting ? 22 : 0;
		int v = 38;

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		mc.renderEngine.bindTexture(HUDHandler.manaBar);
		RenderHelper.drawTexturedModalRect(x, y, 0, u, v, 22, 15);
		GlStateManager.color(1F, 1F, 1F, 1F);

		ItemStack tablet = new ItemStack(ModItems.manaTablet);
		ItemManaTablet.setStackCreative(tablet);

		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		mc.getRenderItem().renderItemAndEffectIntoGUI(tablet, x - 20, y);
		mc.getRenderItem().renderItemAndEffectIntoGUI(pool, x + 26, y);
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		*/

		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
	}
	//******************************************************************************************
	
	//IWandable
	//******************************************************************************************
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	//******************************************************************************************

	
}
