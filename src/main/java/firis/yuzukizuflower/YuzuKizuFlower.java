package firis.yuzukizuflower;

import org.apache.logging.log4j.Logger;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.block.YKBlock;
import firis.yuzukizuflower.common.block.YKBlockBoxedEndoflame;
import firis.yuzukizuflower.common.block.YKBlockBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.block.YKBlockBoxedOrechid;
import firis.yuzukizuflower.common.block.YKBlockBoxedPureDaisy;
import firis.yuzukizuflower.common.block.YKBlockBoxedRannucarpus;
import firis.yuzukizuflower.common.block.YKBlockManaTank;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedEndoflame;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedOrechid;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedPureDaisy;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedRannucarpus;
import firis.yuzukizuflower.common.tileentity.YKTileManaTank;
import firis.yuzukizuflower.common.tileentity.YKTileManaTankSpRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

@Mod(
		modid = YuzuKizuFlower.MODID, 
		name = YuzuKizuFlower.NAME,
		version = YuzuKizuFlower.VERSION,
		dependencies = YuzuKizuFlower.MOD_DEPENDENCIES,
		acceptedMinecraftVersions = YuzuKizuFlower.MOD_ACCEPTED_MINECRAFT_VERSIONS
)
@EventBusSubscriber
public class YuzuKizuFlower
{
    public static final String MODID = "yuzukizuflower";
    public static final String NAME = "YuzuKizu Flower";
    public static final String VERSION = "0.1";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12.2-14.23.5.2768,);required-after:baubles@[1.5.2,);required-after:botania@[r1.10-358,);after:jei@[1.12.2-4.13.1.220,)";
    public static final String MOD_ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]";

    private static Logger logger;
    
    @Instance(YuzuKizuFlower.MODID)
    public static YuzuKizuFlower INSTANCE;
    
    /**
     * クリエイティブタブ
     */
    public static final CreativeTabs YuzuKizuCreativeTab = new CreativeTabs("tabYuzuKoto") {
    	@SideOnly(Side.CLIENT)
    	@Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Blocks.DIAMOND_BLOCK);
        }
    };
    
    /**
     * アイテムインスタンス保持用
     */
    @ObjectHolder(YuzuKizuFlower.MODID)
    public static class YuzuKizuItems{
    	public final static Item METAL_FRAME = null;
    	public final static Item BOXED_PURE_DAISY = null;
    	public final static Item BOXED_ENDOFLAME = null;
    	public final static Item MANA_TANK = null;
    	public final static Item BOXED_RANNUCARPUS = null;
    	public final static Item BOXED_JADED_AMARANTHUS = null;
    	public final static Item MANASTEEL_BOXED_ENDOFLAME = null;
    	public final static Item BOXED_ORECHID = null;
    }
    /**
     * アイテムインスタンス保持用
     */
    @ObjectHolder(YuzuKizuFlower.MODID)
    public static class YuzuKizuBlocks{
    	public final static Block METAL_FRAME = null;
    	public final static Block BOXED_PURE_DAISY = null;
    	public final static Block BOXED_ENDOFLAME = null;
    	public final static Block MANA_TANK = null;
    	public final static Block BOXED_RANNUCARPUS = null;
    	public final static Block BOXED_JADED_AMARANTHUS = null;
    	public final static Block MANASTEEL_BOXED_ENDOFLAME = null;
    	public final static Block BOXED_ORECHID = null;
    }


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        
        logger.info("YuzuKizuFlower Starting...");
        
        GameRegistry.registerTileEntity(YKTileBoxedPureDaisy.class, 
					new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_pure_daisy"));

        GameRegistry.registerTileEntity(YKTileBoxedEndoflame.class, 
					new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_endoflame"));
        
        GameRegistry.registerTileEntity(YKTileManaTank.class, 
					new ResourceLocation(YuzuKizuFlower.MODID, "ykte_mana_tank"));
        
        GameRegistry.registerTileEntity(YKTileBoxedRannucarpus.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_rannucarpus"));
        
        GameRegistry.registerTileEntity(YKTileBoxedJadedAmaranthus.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_jaded_amaranthus"));
        
        GameRegistry.registerTileEntity(YKTileBoxedOrechid.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_orechid"));
        
        
        
        //ネットワーク登録
        NetworkHandler.init();
        
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
    	//GUIの登録
    	NetworkRegistry.INSTANCE.registerGuiHandler(YuzuKizuFlower.INSTANCE, new YKGuiHandler());
        
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    	
    	
    	//インゴット倍化
    	
    	//botania
    	//BotaniaAPI.registerManaInfusionRecipe(output, input, mana);
    	//registerManaInfusionRecipeだと触媒の設定ができないよう
    	RecipeManaInfusion recipe = new RecipeManaInfusion(new ItemStack(Items.IRON_INGOT, 2), new ItemStack(Items.IRON_INGOT, 1), 5000);
    	recipe.setCatalyst(RecipeManaInfusion.conjurationState);
    	BotaniaAPI.manaInfusionRecipes.add(recipe);
    	
    	recipe = new RecipeManaInfusion(new ItemStack(Items.GOLD_INGOT, 2), new ItemStack(Items.GOLD_INGOT, 1), 5000);
    	recipe.setCatalyst(RecipeManaInfusion.conjurationState);
    	BotaniaAPI.manaInfusionRecipes.add(recipe);
    	
    	//エンダーストーン
    	recipe = new RecipeManaInfusion(new ItemStack(Blocks.END_STONE, 1), new ItemStack(Blocks.STONE, 1, 0), 5000);
    	recipe.setCatalyst(Blocks.DRAGON_EGG.getDefaultState());
    	BotaniaAPI.manaInfusionRecipes.add(recipe);
    	
    	//エンダー空気ビン
    	recipe = new RecipeManaInfusion(new ItemStack(Item.getByNameOrId("botania:manaresource"), 1, 15), new ItemStack(Items.GLASS_BOTTLE, 1), 5000);
    	recipe.setCatalyst(Blocks.DRAGON_EGG.getDefaultState());
    	BotaniaAPI.manaInfusionRecipes.add(recipe);
    }
    
    /**
     * ブロックを登録するイベント
     */
    @SubscribeEvent
    protected static void registerBlocks(RegistryEvent.Register<Block> event)
    {
    	
        //　メタルフレームブロック
        event.getRegistry().register(
                new YKBlock(Material.IRON)
                .setRegistryName(MODID, "metal_frame")
                .setCreativeTab(YuzuKizuCreativeTab)
                .setUnlocalizedName("metal_frame")
                .setHardness(0.5F)
                .setResistance(1.0F)
        );
        
        // 箱入りピュアデイジー
        event.getRegistry().register(
                new YKBlockBoxedPureDaisy()
                .setRegistryName(MODID, "boxed_pure_daisy")
                .setUnlocalizedName("boxed_pure_daisy")
        );
        
        // 箱入りエンドフレイム
        event.getRegistry().register(
                new YKBlockBoxedEndoflame(0)
                .setRegistryName(MODID, "boxed_endoflame")
                .setUnlocalizedName("boxed_endoflame")
        );
        
        // マナタンク
        event.getRegistry().register(
                new YKBlockManaTank()
                .setRegistryName(MODID, "mana_tank")
                .setCreativeTab(YuzuKizuCreativeTab)
                .setUnlocalizedName("mana_tank")
                .setHardness(0.5F)
                .setResistance(1.0F)
        );
        
        // 箱入りラナンカーパス
        event.getRegistry().register(
                new YKBlockBoxedRannucarpus()
                .setRegistryName(MODID, "boxed_rannucarpus")
                .setCreativeTab(YuzuKizuCreativeTab)
                .setUnlocalizedName("boxed_rannucarpus")
                .setHardness(0.5F)
                .setResistance(1.0F)
        );
        
        // 箱入りジェイディッド・アマランサス
        event.getRegistry().register(
                new YKBlockBoxedJadedAmaranthus()
                .setRegistryName(MODID, "boxed_jaded_amaranthus")
                .setUnlocalizedName("boxed_jaded_amaranthus")
        );
        
        // 箱入りマナスチールエンドフレイム
        event.getRegistry().register(
                new YKBlockBoxedEndoflame(1)
                .setRegistryName(MODID, "manasteel_boxed_endoflame")
                .setUnlocalizedName("manasteel_boxed_endoflame")
        );
        
        // 箱入りオアキド
        event.getRegistry().register(
                new YKBlockBoxedOrechid()
                .setRegistryName(MODID, "boxed_orechid")
                .setUnlocalizedName("boxed_orechid")
        );
        
        
    }
    
    /**
     * アイテムを登録するイベント
     */
    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event)
    {
    	// メタルフレーム
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.METAL_FRAME)
    			.setRegistryName(MODID, "metal_frame")
    	);
    	
    	// 箱入りピュアデイジー
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_PURE_DAISY)
    			.setRegistryName(MODID, "boxed_pure_daisy")
    	);
    	
    	// 箱入りエンドフレイム
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_ENDOFLAME)
    			.setRegistryName(MODID, "boxed_endoflame")
    	);
    	
    	// 箱入りエンドフレイム
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.MANA_TANK)
    			.setRegistryName(MODID, "mana_tank")
    	);
    	
    	// 箱入りラナンカーパス
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_RANNUCARPUS)
    			.setRegistryName(MODID, "boxed_rannucarpus")
    	);
    	
    	// 箱入りジェイディッド・アマランサス
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_JADED_AMARANTHUS)
    			.setRegistryName(MODID, "boxed_jaded_amaranthus")
    	);
    	
    	// 箱入りエンドフレイム
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.MANASTEEL_BOXED_ENDOFLAME)
    			.setRegistryName(MODID, "manasteel_boxed_endoflame")
    	);
    	
    	// 箱入りオアキド
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_ORECHID)
    			.setRegistryName(MODID, "boxed_orechid")
    	);
    }
    
    /**
     * モデル登録イベント
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void registerModels(ModelRegistryEvent event)
    {
    	// メタルフレーム
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.METAL_FRAME, 0,
    			new ModelResourceLocation(YuzuKizuItems.METAL_FRAME.getRegistryName(), "inventory"));
    	
    	// 箱入りピュアデイジー
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_PURE_DAISY, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_PURE_DAISY.getRegistryName(), "inventory"));

    	// 箱入りピュアデイジー
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_ENDOFLAME, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_ENDOFLAME.getRegistryName(), "inventory"));
    	
    	// 箱入りピュアデイジー
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.MANA_TANK, 0,
    			new ModelResourceLocation(YuzuKizuItems.MANA_TANK.getRegistryName(), "inventory"));
    	
    	// 箱入りラナンカーパス
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_RANNUCARPUS, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_RANNUCARPUS.getRegistryName(), "inventory"));
    	
    	// 箱入りジェイディッド・アマランサス
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_JADED_AMARANTHUS, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_JADED_AMARANTHUS.getRegistryName(), "inventory"));
    	
    	// 箱入りピュアデイジー
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.MANASTEEL_BOXED_ENDOFLAME, 0,
    			new ModelResourceLocation(YuzuKizuItems.MANASTEEL_BOXED_ENDOFLAME.getRegistryName(), "inventory"));
    	
    	// 箱入りオアキド
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_ORECHID, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_ORECHID.getRegistryName(), "inventory"));
    	
    	
    	//マナタンク
    	ClientRegistry.bindTileEntitySpecialRenderer(YKTileManaTank.class, new YKTileManaTankSpRenderer());
    }
}
