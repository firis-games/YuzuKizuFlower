package firis.yuzukizuflower;

import org.apache.logging.log4j.Logger;

import firis.yuzukizuflower.client.tesr.YKTileManaTankSpRenderer;
import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.block.YKBlockBaseBoxed;
import firis.yuzukizuflower.common.block.YKBlockBoxedAkariculture;
import firis.yuzukizuflower.common.block.YKBlockBoxedEndoflame;
import firis.yuzukizuflower.common.block.YKBlockBoxedGourmaryllis;
import firis.yuzukizuflower.common.block.YKBlockBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.block.YKBlockBoxedOrechid;
import firis.yuzukizuflower.common.block.YKBlockBoxedPureDaisy;
import firis.yuzukizuflower.common.block.YKBlockBoxedRannuncarpus;
import firis.yuzukizuflower.common.block.YKBlockManaTank;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.proxy.CommonProxy;
import firis.yuzukizuflower.common.recipe.BotaniaRecipes;
import firis.yuzukizuflower.common.recipe.RecipeBoxedFlower;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedAkariculture;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedEndoflame;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedGourmaryllis;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedOrechid;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedPureDaisy;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedRannuncarpus;
import firis.yuzukizuflower.common.tileentity.YKTileManaTank;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12.2-14.23.5.2768,);required-after:baubles@[1.5.2,);required-after:botania@[r1.10,);after:jei@[1.12.2-4.13.1.220,)";
    public static final String MOD_ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]";

    private static Logger logger;
    
    @Instance(YuzuKizuFlower.MODID)
    public static YuzuKizuFlower INSTANCE;
    
    @SidedProxy(serverSide = "firis.yuzukizuflower.common.proxy.CommonProxy", 
    		clientSide = "firis.yuzukizuflower.client.proxy.ClientProxy")
    public static CommonProxy proxy;
    
    /**
     * クリエイティブタブ
     */
    public static final CreativeTabs YuzuKizuCreativeTab = new CreativeTabs("tabYuzuKizuFlower") {
    	@SideOnly(Side.CLIENT)
    	@Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(YuzuKizuFlower.YuzuKizuBlocks.FLOWER_BOX);
        }
    };
    
    /**
     * アイテムインスタンス保持用
     */
    @ObjectHolder(YuzuKizuFlower.MODID)
    public static class YuzuKizuItems{
    	public final static Item FLOWER_BOX = null;
    	public final static Item BOXED_PURE_DAISY = null;
    	public final static Item BOXED_ENDOFLAME = null;
    	public final static Item MANA_TANK = null;
    	public final static Item BOXED_RANNUNCARPUS = null;
    	public final static Item BOXED_JADED_AMARANTHUS = null;
    	public final static Item MANASTEEL_BOXED_ENDOFLAME = null;
    	public final static Item BOXED_ORECHID = null;
    	public final static Item BOXED_GOURMARYLLIS = null;
    	public final static Item BOXED_AKARICULTURE = null;
    }
    /**
     * ブロックインスタンス保持用
     */
    @ObjectHolder(YuzuKizuFlower.MODID)
    public static class YuzuKizuBlocks{
    	public final static Block FLOWER_BOX = null;
    	public final static Block BOXED_PURE_DAISY = null;
    	public final static Block BOXED_ENDOFLAME = null;
    	public final static Block MANA_TANK = null;
    	public final static Block BOXED_RANNUNCARPUS = null;
    	public final static Block BOXED_JADED_AMARANTHUS = null;
    	public final static Block MANASTEEL_BOXED_ENDOFLAME = null;
    	public final static Block BOXED_ORECHID = null;
    	public final static Block BOXED_GOURMARYLLIS = null;
    	public final static Block BOXED_AKARICULTURE = null;
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
        
        GameRegistry.registerTileEntity(YKTileBoxedRannuncarpus.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_rannuncarpus"));
        
        GameRegistry.registerTileEntity(YKTileBoxedJadedAmaranthus.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_jaded_amaranthus"));
        
        GameRegistry.registerTileEntity(YKTileBoxedOrechid.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_orechid"));
        
        GameRegistry.registerTileEntity(YKTileBoxedGourmaryllis.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_gourmaryllis"));
        
        GameRegistry.registerTileEntity(YKTileBoxedAkariculture.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_akariculture"));
        
        //ネットワーク登録
        NetworkHandler.init();
        
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	//botaniaのレシピを追加
    	BotaniaRecipes.init();
    	
    	//GUIの登録
    	NetworkRegistry.INSTANCE.registerGuiHandler(YuzuKizuFlower.INSTANCE, new YKGuiHandler());
        
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    }
    
    /**
     * ブロックを登録するイベント
     */
    @SubscribeEvent
    protected static void registerBlocks(RegistryEvent.Register<Block> event)
    {
    	
        //　フラワーボックス
        event.getRegistry().register(
                new YKBlockBaseBoxed()
                .setRegistryName(MODID, "flower_box")
                .setUnlocalizedName("flower_box")
        );
        
        // マナタンク
        event.getRegistry().register(
                new YKBlockManaTank()
                .setRegistryName(MODID, "mana_tank")
                .setUnlocalizedName("mana_tank")
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
        
        // 箱入りラナンカーパス
        event.getRegistry().register(
                new YKBlockBoxedRannuncarpus()
                .setRegistryName(MODID, "boxed_rannuncarpus")
                .setUnlocalizedName("boxed_rannuncarpus")
        );
        
        // 箱入りジェイディッド・アマランサス
        event.getRegistry().register(
                new YKBlockBoxedJadedAmaranthus()
                .setRegistryName(MODID, "boxed_jaded_amaranthus")
                .setUnlocalizedName("boxed_jaded_amaranthus")
        );
        
        /*
        // 箱入りマナスチールエンドフレイム
        event.getRegistry().register(
                new YKBlockBoxedEndoflame(1)
                .setRegistryName(MODID, "manasteel_boxed_endoflame")
                .setUnlocalizedName("manasteel_boxed_endoflame")
        );
        */
        
        // 箱入りオアキド
        event.getRegistry().register(
                new YKBlockBoxedOrechid()
                .setRegistryName(MODID, "boxed_orechid")
                .setUnlocalizedName("boxed_orechid")
        );
        
        // 箱入りガーマリリス
        event.getRegistry().register(
                new YKBlockBoxedGourmaryllis(0)
                .setRegistryName(MODID, "boxed_gourmaryllis")
                .setUnlocalizedName("boxed_gourmaryllis")
        );
        
        // 箱入りアカリルチャー
        event.getRegistry().register(
                new YKBlockBoxedAkariculture()
                .setRegistryName(MODID, "boxed_akariculture")
                .setUnlocalizedName("boxed_akariculture")
        );
        
        
    }
    
    /**
     * アイテムを登録するイベント
     */
    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event)
    {
    	// フラワーボックス
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.FLOWER_BOX)
    			.setRegistryName(MODID, "flower_box")
    	);
    	
    	// 箱入りマナタンク
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.MANA_TANK)
    			.setRegistryName(MODID, "mana_tank")
    	);
    	
    	// 箱入りピュアデイジー
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_PURE_DAISY)
    			.setRegistryName(MODID, "boxed_pure_daisy")
    	);
    	
    	// 箱入りエンドフレイム
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_ENDOFLAME)
    			.setRegistryName(MODID, "boxed_endoflame")
    	);
    	
    	// 箱入りラナンカーパス
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_RANNUNCARPUS)
    			.setRegistryName(MODID, "boxed_rannuncarpus")
    	);
    	
    	// 箱入りジェイディッド・アマランサス
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_JADED_AMARANTHUS)
    			.setRegistryName(MODID, "boxed_jaded_amaranthus")
    	);
    	
    	/*
    	// 箱入りエンドフレイム
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.MANASTEEL_BOXED_ENDOFLAME)
    			.setRegistryName(MODID, "manasteel_boxed_endoflame")
    	);
    	*/
    	
    	// 箱入りオアキド
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_ORECHID)
    			.setRegistryName(MODID, "boxed_orechid")
    	);
    	
    	// 箱入りガーマリリス
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_GOURMARYLLIS)
    			.setRegistryName(MODID, "boxed_gourmaryllis")
    	);
    	
    	// 箱入りアカリカルチャー
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_AKARICULTURE)
    			.setRegistryName(MODID, "boxed_akariculture")
    	);
    }
    
    /**
     * モデル登録イベント
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void registerModels(ModelRegistryEvent event)
    {
    	// フワラーボックス
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.FLOWER_BOX, 0,
    			new ModelResourceLocation(YuzuKizuItems.FLOWER_BOX.getRegistryName(), "inventory"));
    	
    	// 箱入りピュアデイジー
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_PURE_DAISY, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_PURE_DAISY.getRegistryName(), "inventory"));

    	// 箱入りエンドフレイム
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_ENDOFLAME, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_ENDOFLAME.getRegistryName(), "inventory"));
    	
    	// マナタンク
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.MANA_TANK, 0,
    			new ModelResourceLocation(YuzuKizuItems.MANA_TANK.getRegistryName(), "inventory"));
    	
    	// 箱入りラナンカーパス
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_RANNUNCARPUS, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_RANNUNCARPUS.getRegistryName(), "inventory"));
    	
    	// 箱入りジェイディッド・アマランサス
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_JADED_AMARANTHUS, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_JADED_AMARANTHUS.getRegistryName(), "inventory"));
    	
    	/*
    	// 箱入りエンドフレイム
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.MANASTEEL_BOXED_ENDOFLAME, 0,
    			new ModelResourceLocation(YuzuKizuItems.MANASTEEL_BOXED_ENDOFLAME.getRegistryName(), "inventory"));
		*/
    	
    	// 箱入りオアキド
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_ORECHID, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_ORECHID.getRegistryName(), "inventory"));
    	
    	// 箱入りガーマリリス
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_GOURMARYLLIS, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_GOURMARYLLIS.getRegistryName(), "inventory"));
    	
    	// 箱入りアカリカルチャー
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BOXED_AKARICULTURE, 0,
    			new ModelResourceLocation(YuzuKizuItems.BOXED_AKARICULTURE.getRegistryName(), "inventory"));
    	
    	
    	//マナタンク
    	ClientRegistry.bindTileEntitySpecialRenderer(YKTileManaTank.class, new YKTileManaTankSpRenderer());
    }
    
    @SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
    	
    	for (String key : RecipeBoxedFlower.flowerMap.keySet()) {
    		//レシピの追加
        	event.getRegistry().register(new RecipeBoxedFlower(key));
    	}
    }
}
