package firis.yuzukizuflower;

import org.apache.logging.log4j.Logger;

import firis.yuzukizuflower.client.tesr.YKTileCorporeaChestSpRenderer;
import firis.yuzukizuflower.client.tesr.YKTileManaTankSpRenderer;
import firis.yuzukizuflower.client.tesr.YKTileScrollChestSpRenderer;
import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.block.YKBlockAlfSand;
import firis.yuzukizuflower.common.block.YKBlockAlfheimCore;
import firis.yuzukizuflower.common.block.YKBlockAutoWorkbench;
import firis.yuzukizuflower.common.block.YKBlockBaseBoxed;
import firis.yuzukizuflower.common.block.YKBlockBoxedAkanerald;
import firis.yuzukizuflower.common.block.YKBlockBoxedAkariculture;
import firis.yuzukizuflower.common.block.YKBlockBoxedAocean;
import firis.yuzukizuflower.common.block.YKBlockBoxedEndoflame;
import firis.yuzukizuflower.common.block.YKBlockBoxedGourmaryllis;
import firis.yuzukizuflower.common.block.YKBlockBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.block.YKBlockBoxedOrechid;
import firis.yuzukizuflower.common.block.YKBlockBoxedPureDaisy;
import firis.yuzukizuflower.common.block.YKBlockBoxedRannuncarpus;
import firis.yuzukizuflower.common.block.YKBlockBoxedYuquarry;
import firis.yuzukizuflower.common.block.YKBlockCorporeaChest;
import firis.yuzukizuflower.common.block.YKBlockDreamSapling;
import firis.yuzukizuflower.common.block.YKBlockDreamLeaf;
import firis.yuzukizuflower.common.block.YKBlockManaTank;
import firis.yuzukizuflower.common.block.YKBlockOre;
import firis.yuzukizuflower.common.block.YKBlockPetalWorkbench;
import firis.yuzukizuflower.common.block.YKBlockRuneWorkbench;
import firis.yuzukizuflower.common.block.YKBlockScrollChest;
import firis.yuzukizuflower.common.block.YKBlockTerraPlate;
import firis.yuzukizuflower.common.event.OreGenHandler;
import firis.yuzukizuflower.common.event.PopulateChunkEventHandler;
import firis.yuzukizuflower.common.item.YKItemBackpackChest;
import firis.yuzukizuflower.common.item.YKItemBase;
import firis.yuzukizuflower.common.item.YKItemBlueprint;
import firis.yuzukizuflower.common.item.YKItemDimensionKey;
import firis.yuzukizuflower.common.item.YKItemInstantHouse;
import firis.yuzukizuflower.common.item.YKItemRemoteChest;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.proxy.CommonProxy;
import firis.yuzukizuflower.common.recipe.BotaniaRecipes;
import firis.yuzukizuflower.common.recipe.RecipeBoxedFlower;
import firis.yuzukizuflower.common.tileentity.YKTileAutoWorkbench;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedAkanerald;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedAkariculture;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedAocean;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedEndoflame;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedGourmaryllis;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedOrechid;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedPureDaisy;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedRannuncarpus;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedYuquarry;
import firis.yuzukizuflower.common.tileentity.YKTileCorporeaChest;
import firis.yuzukizuflower.common.tileentity.YKTileManaTank;
import firis.yuzukizuflower.common.tileentity.YKTileManaTankExtends;
import firis.yuzukizuflower.common.tileentity.YKTilePetalWorkbench;
import firis.yuzukizuflower.common.tileentity.YKTileRuneWorkbench;
import firis.yuzukizuflower.common.tileentity.YKTileScrollChest;
import firis.yuzukizuflower.common.tileentity.YKTileTerraPlate;
import firis.yuzukizuflower.common.world.biome.BiomeManaPlains;
import firis.yuzukizuflower.common.world.dimension.DimensionHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

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
    public static final String VERSION = "0.8";
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
    	public final static Item AKARICULTURE = null;
    	public final static Item YUQUARRY = null;
    	public final static Item AOCEAN = null;
    	public final static Item AKANERALD = null;
    	public final static Item REMOTE_CHEST = null;
    	public final static Item BACKPACK_CHEST = null;
    	public final static Item BLUEPRINT = null;
    	public final static Item DIMENSION_KEY = null;
    	public final static Item INSTANT_HOUSE = null;
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
    	public final static Block BOXED_YUQUARRY = null;
    	public final static Block BOXED_AOCEAN = null;
    	public final static Block BOXED_AKANERALD = null;
    	public final static Block LIQUID_MANA = null;
    	public final static Block SCROLL_CHEST = null;
    	public final static Block CORPOREA_CHEST = null;
    	public final static Block PETAL_WORKBENCH = null;
    	public final static Block RUNE_WORKBENCH = null;
    	public final static Block TERRA_PLATE = null;
    	public final static Block AUTO_WORKBENCH = null;
    	public final static Block ALFHEIM_CORE = null;
    	public final static Block PIXIE_ORE = null;
    	public final static Block ELEVN_ORE = null;
    	public final static Block ELEMENTIUM_ORE = null;
    	public final static Block DRAGON_ORE = null;
    	public final static Block GAIA_ORE = null;
    	public final static Block DREAM_LEAF = null;
    	public final static Block DREAM_SAPLING = null;
    	public final static Block ALF_SAND = null;
    }
    
    public static class YuzuKizuFluids {
    	//液体マナ
    	public final static Fluid LIQUID_MANA = new Fluid(
    			"liquid_mana", 
    			new ResourceLocation("yuzukizuflower:blocks/mana_water_still"), 
    			new ResourceLocation("yuzukizuflower:blocks/mana_water_flow"))
    			.setLuminosity(15);
    }
    
    /**
     * 初期化ブロック
     */
    static {
    	//enableUniversalBucketの有効化は初期化ブロックでしないと有効化されない
    	FluidRegistry.enableUniversalBucket();
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
        
        GameRegistry.registerTileEntity(YKTileManaTankExtends.class, 
					new ResourceLocation(YuzuKizuFlower.MODID, "ykte_mana_tank_ext"));
        
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
        
        GameRegistry.registerTileEntity(YKTileBoxedYuquarry.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_yuquarry"));
        
        GameRegistry.registerTileEntity(YKTileBoxedAocean.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_aocean"));
        
        GameRegistry.registerTileEntity(YKTileBoxedAkanerald.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_boxed_akanerald"));
        
        GameRegistry.registerTileEntity(YKTileScrollChest.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_scholl_chest"));
        
        GameRegistry.registerTileEntity(YKTileCorporeaChest.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_corporea_chest"));
        
        GameRegistry.registerTileEntity(YKTilePetalWorkbench.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_petal_workbench"));
        
        GameRegistry.registerTileEntity(YKTileRuneWorkbench.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_rune_workbench"));
        
        GameRegistry.registerTileEntity(YKTileTerraPlate.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_terra_plate"));

        GameRegistry.registerTileEntity(YKTileAutoWorkbench.class, 
				new ResourceLocation(YuzuKizuFlower.MODID, "ykte_auto_workbench"));
        
        //ネットワーク登録
        NetworkHandler.init();
        
        //ディメンション初期化
        DimensionHandler.init();
        
        //LootTable追加
        LootTableList.register(new ResourceLocation(YuzuKizuFlower.MODID, "chests/alfheim_chest"));
        
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	//botaniaのレシピを追加
    	BotaniaRecipes.init();
    	
    	//GUIの登録
    	NetworkRegistry.INSTANCE.registerGuiHandler(YuzuKizuFlower.INSTANCE, new YKGuiHandler());
    	
    	//液体マナを生成
    	MinecraftForge.TERRAIN_GEN_BUS.register(PopulateChunkEventHandler.class);
    	
    	//キーバインディング設定
    	proxy.registerKeyBinding();
    	
    	//Layerを設定
    	proxy.initLayerRenderer();
    	
    	//鉱石生成イベント
    	MinecraftForge.ORE_GEN_BUS.register(OreGenHandler.class);
    	
    	//エレメンチウムインゴット
    	GameRegistry.addSmelting(YuzuKizuBlocks.ELEMENTIUM_ORE, 
    			new ItemStack(ModItems.manaResource, 1, 7), 0.7f);
    	
    	//アルフガラス
    	GameRegistry.addSmelting(YuzuKizuBlocks.ALF_SAND, 
    			new ItemStack(ModBlocks.elfGlass, 1), 0.0f);
    	
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
        
        // 箱入りユクァーリー
        event.getRegistry().register(
                new YKBlockBoxedYuquarry()
                .setRegistryName(MODID, "boxed_yuquarry")
                .setUnlocalizedName("boxed_yuquarry")
        );
        
        // 箱入りアオーシャン
        event.getRegistry().register(
                new YKBlockBoxedAocean()
                .setRegistryName(MODID, "boxed_aocean")
                .setUnlocalizedName("boxed_aocean")
        );
        
        // 箱入りアカネラルド
        event.getRegistry().register(
                new YKBlockBoxedAkanerald()
                .setRegistryName(MODID, "boxed_akanerald")
                .setUnlocalizedName("boxed_akanerald")
        );
        
        //流体登録
        FluidRegistry.registerFluid(YuzuKizuFluids.LIQUID_MANA);
    	
        //汎用バケツの登録処理
        FluidRegistry.addBucketForFluid(YuzuKizuFluids.LIQUID_MANA);
        
        // 液体マナ
        event.getRegistry().register(
                new BlockFluidClassic(YuzuKizuFluids.LIQUID_MANA, Material.WATER)
                .setRegistryName(MODID, "liquid_mana")
                .setUnlocalizedName("liquid_mana")
                .setCreativeTab(YuzuKizuCreativeTab)
        );
        
        // スクロールチェスト
        event.getRegistry().register(
                new YKBlockScrollChest()
                .setRegistryName(MODID, "scroll_chest")
                .setUnlocalizedName("scroll_chest")
        );
        
        // コーポリアチェスト
        event.getRegistry().register(
                new YKBlockCorporeaChest()
                .setRegistryName(MODID, "corporea_chest")
                .setUnlocalizedName("corporea_chest")
        );
        
        // 花びら作業台
        event.getRegistry().register(
                new YKBlockPetalWorkbench()
                .setRegistryName(MODID, "petal_workbench")
                .setUnlocalizedName("petal_workbench")
        );
        
        // ルーン作業台
        event.getRegistry().register(
                new YKBlockRuneWorkbench()
                .setRegistryName(MODID, "rune_workbench")
                .setUnlocalizedName("rune_workbench")
        );
        
        // テラプレート
        event.getRegistry().register(
                new YKBlockTerraPlate()
                .setRegistryName(MODID, "terra_plate")
                .setUnlocalizedName("terra_plate")
        );
        
        // 自動作業台
        event.getRegistry().register(
                new YKBlockAutoWorkbench()
                .setRegistryName(MODID, "auto_workbench")
                .setUnlocalizedName("auto_workbench")
        );
        
        // アルフヘイムコア
        event.getRegistry().register(
                new YKBlockAlfheimCore()
                .setRegistryName(MODID, "alfheim_core")
                .setUnlocalizedName("alfheim_core")
        );
        
        // ピクシィ鉱石
        event.getRegistry().register(
                new YKBlockOre(YKBlockOre.OreType.PIXIE)
                .setRegistryName(MODID, "pixie_ore")
                .setUnlocalizedName("pixie_ore")
        );
        
        // エレブン鉱石
        event.getRegistry().register(
                new YKBlockOre(YKBlockOre.OreType.ELVEN)
                .setRegistryName(MODID, "elevn_ore")
                .setUnlocalizedName("elevn_ore")
        );
        
        // エレメンチウム鉱石
        event.getRegistry().register(
                new YKBlockOre(YKBlockOre.OreType.ELEMENTIUM)
                .setRegistryName(MODID, "elementium_ore")
                .setUnlocalizedName("elementium_ore")
        );
        
        // ドラゴン鉱石
        event.getRegistry().register(
                new YKBlockOre(YKBlockOre.OreType.DRAGON)
                .setRegistryName(MODID, "dragon_ore")
                .setUnlocalizedName("dragon_ore")
        );
        
        // ガイア鉱石
        event.getRegistry().register(
                new YKBlockOre(YKBlockOre.OreType.GAIA)
                .setRegistryName(MODID, "gaia_ore")
                .setUnlocalizedName("gaia_ore")
        );
        
        // ドリームリーフ
        event.getRegistry().register(
                new YKBlockDreamLeaf()
                .setRegistryName(MODID, "dream_leaf")
                .setUnlocalizedName("dream_leaf")
        );
        
        // ドリームウッドの苗木
        event.getRegistry().register(
                new YKBlockDreamSapling()
                .setRegistryName(MODID, "dream_sapling")
                .setUnlocalizedName("dream_sapling")
        );
        
        // アルフサンド
        event.getRegistry().register(
                new YKBlockAlfSand()
                .setRegistryName(MODID, "alf_sand")
                .setUnlocalizedName("alf_sand")
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
    	
    	// アカリカルチャー
    	event.getRegistry().register(new YKItemBase()
    			.setRegistryName(MODID, "akariculture")
    			.setUnlocalizedName("akariculture")
    	);
    	
    	// 箱入りアカリカルチャー
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_AKARICULTURE)
    			.setRegistryName(MODID, "boxed_akariculture")
    	);
    	
    	// ユクァーリー
    	event.getRegistry().register(new YKItemBase()
    			.setRegistryName(MODID, "yuquarry")
    			.setUnlocalizedName("yuquarry")
    	);
    	
    	// 箱入りユクァーリー
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_YUQUARRY)
    			.setRegistryName(MODID, "boxed_yuquarry")
    	);
    	
    	// アオーシャン
    	event.getRegistry().register(new YKItemBase()
    			.setRegistryName(MODID, "aocean")
    			.setUnlocalizedName("aocean")
    	);
    	
    	// 箱入りアオーシャン
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_AOCEAN)
    			.setRegistryName(MODID, "boxed_aocean")
    	);
    	
    	// アカネラルド
    	event.getRegistry().register(new YKItemBase()
    			.setRegistryName(MODID, "akanerald")
    			.setUnlocalizedName("akanerald")
    	);
    	
    	// 箱入りアカネラルド
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.BOXED_AKANERALD)
    			.setRegistryName(MODID, "boxed_akanerald")
    	);
    	
    	// 液体マナ
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.LIQUID_MANA)
    			.setRegistryName(MODID, "liquid_mana")
    	);

    	// コーポリアチェスト
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.CORPOREA_CHEST)
    			.setRegistryName(MODID, "corporea_chest")
    	);
    	
    	// スクロールチェスト
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.SCROLL_CHEST)
    			.setRegistryName(MODID, "scroll_chest")
    	);
    	
    	// リモートチェスト
    	event.getRegistry().register(new YKItemRemoteChest()
    			.setRegistryName(MODID, "remote_chest")
    			.setUnlocalizedName("remote_chest")
    	);
    	
    	// 花びら作業台
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.PETAL_WORKBENCH)
    			.setRegistryName(MODID, "petal_workbench")
    	);
    	
    	// ルーン作業台
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.RUNE_WORKBENCH)
    			.setRegistryName(MODID, "rune_workbench")
    	);
    	
    	// テラプレート
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.TERRA_PLATE)
    			.setRegistryName(MODID, "terra_plate")
    	);
    	
    	// バックパックチェスト
    	event.getRegistry().register(new YKItemBackpackChest()
    			.setRegistryName(MODID, "backpack_chest")
    			.setUnlocalizedName("backpack_chest")
    	);
    	
    	// ブループリント
    	event.getRegistry().register(new YKItemBlueprint()
    			.setRegistryName(MODID, "blueprint")
    			.setUnlocalizedName("blueprint")
    	);
    	
    	// テラプレート
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.AUTO_WORKBENCH)
    			.setRegistryName(MODID, "auto_workbench")
    	);
    	
    	// ディメンションキー
    	event.getRegistry().register(new YKItemDimensionKey()
    			.setRegistryName(MODID, "dimension_key")
    			.setUnlocalizedName("dimension_key")
    	);
    	
    	// アルフヘイムコア
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.ALFHEIM_CORE)
    			.setRegistryName(MODID, "alfheim_core")
    	);
    	
    	//ピクシィ鉱石
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.PIXIE_ORE)
    			.setRegistryName(MODID, "pixie_ore")
    	);
    	
    	//ピクシィ鉱石
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.ELEVN_ORE)
    			.setRegistryName(MODID, "elevn_ore")
    	);
    	
    	//エレメンチウム鉱石
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.ELEMENTIUM_ORE)
    			.setRegistryName(MODID, "elementium_ore")
    	);
    	
    	//ドラゴン鉱石
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.DRAGON_ORE)
    			.setRegistryName(MODID, "dragon_ore")
    	);
    	
    	//ガイア鉱石
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.GAIA_ORE)
    			.setRegistryName(MODID, "gaia_ore")
    	);
    	
    	//インスタントハウス
    	event.getRegistry().register(new YKItemInstantHouse()
    			.setRegistryName(MODID, "instant_house")
    			.setUnlocalizedName("instant_house")
    	);
    	
    	//ドリームリーフ
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.DREAM_LEAF)
    			.setRegistryName(MODID, "dream_leaf")
    	);
    	
    	//ドリームウッドの苗木
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.DREAM_SAPLING)
    			.setRegistryName(MODID, "dream_sapling")
    	);
    	
    	//アルフサンド
    	event.getRegistry().register(new ItemBlock(YuzuKizuBlocks.ALF_SAND)
    			.setRegistryName(MODID, "alf_sand")
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
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.FLOWER_BOX), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.FLOWER_BOX.getRegistryName(), "inventory"));
    	
    	// 箱入りピュアデイジー
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_PURE_DAISY), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_PURE_DAISY.getRegistryName(), "inventory"));

    	// 箱入りエンドフレイム
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_ENDOFLAME), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_ENDOFLAME.getRegistryName(), "inventory"));
    	
    	// マナタンク
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.MANA_TANK), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.MANA_TANK.getRegistryName(), "inventory"));
    	
    	// 箱入りラナンカーパス
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_RANNUNCARPUS), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_RANNUNCARPUS.getRegistryName(), "inventory"));
    	
    	// 箱入りジェイディッド・アマランサス
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_JADED_AMARANTHUS), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_JADED_AMARANTHUS.getRegistryName(), "inventory"));
    	
    	/*
    	// 箱入りエンドフレイム
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.MANASTEEL_BOXED_ENDOFLAME, 0,
    			new ModelResourceLocation(YuzuKizuItems.MANASTEEL_BOXED_ENDOFLAME.getRegistryName(), "inventory"));
		*/
    	
    	// 箱入りオアキド
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_ORECHID), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_ORECHID.getRegistryName(), "inventory"));
    	
    	// 箱入りガーマリリス
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_GOURMARYLLIS), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_GOURMARYLLIS.getRegistryName(), "inventory"));
    	
    	// 箱入りアカリカルチャー
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_AKARICULTURE), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_AKARICULTURE.getRegistryName(), "inventory"));
    	
    	// アカリカルチャー
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.AKARICULTURE, 0,
    			new ModelResourceLocation(YuzuKizuItems.AKARICULTURE.getRegistryName(), "inventory"));
    	
    	// 箱入りユクァーリー
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_YUQUARRY), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_YUQUARRY.getRegistryName(), "inventory"));
    	
    	// ユクァーリー
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.YUQUARRY, 0,
    			new ModelResourceLocation(YuzuKizuItems.YUQUARRY.getRegistryName(), "inventory"));
    	
    	// 箱入りアオーシャン
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_AOCEAN), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_AOCEAN.getRegistryName(), "inventory"));
    	
    	// アオーシャン
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.AOCEAN, 0,
    			new ModelResourceLocation(YuzuKizuItems.AOCEAN.getRegistryName(), "inventory"));
    	
    	// 箱入りアカネラルド
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.BOXED_AKANERALD), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.BOXED_AKANERALD.getRegistryName(), "inventory"));
    	
    	// アカネラルド
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.AKANERALD, 0,
    			new ModelResourceLocation(YuzuKizuItems.AKANERALD.getRegistryName(), "inventory"));
    	    	
    	//ブロック描画用StateMapper
    	ModelLoader.setCustomStateMapper(YuzuKizuBlocks.LIQUID_MANA, new StateMapperBase() {
    	    @Override
    	    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
    	        return new ModelResourceLocation("yuzukizuflower:liquid_mana", "fluid");
    	    }
    	});
    	
    	//アイテム描画用ItemMeshDefinition
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(YuzuKizuBlocks.LIQUID_MANA), new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
            	return new ModelResourceLocation("yuzukizuflower:liquid_mana", "fluid");
            }
        });
    	
    	//マナタンク
    	ClientRegistry.bindTileEntitySpecialRenderer(YKTileManaTank.class, new YKTileManaTankSpRenderer());
    	
        // スクロールチェスト
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.SCROLL_CHEST), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.SCROLL_CHEST.getRegistryName(), "inventory"));
    	
    	
    	// リモートチェスト
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.REMOTE_CHEST, 0,
    			new ModelResourceLocation(YuzuKizuItems.REMOTE_CHEST.getRegistryName(), "inventory"));
    	
    	// コーポリアチェスト
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.CORPOREA_CHEST), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.CORPOREA_CHEST.getRegistryName(), "inventory"));
    	
    	//スクロールチェスト
    	ClientRegistry.bindTileEntitySpecialRenderer(YKTileScrollChest.class, new YKTileScrollChestSpRenderer());

    	//コーポリアチェスト
    	ClientRegistry.bindTileEntitySpecialRenderer(YKTileCorporeaChest.class, new YKTileCorporeaChestSpRenderer());
    	
    	// 花びら作業台
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.PETAL_WORKBENCH), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.PETAL_WORKBENCH.getRegistryName(), "inventory"));
    	
    	// ルーン作業台
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.RUNE_WORKBENCH), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.RUNE_WORKBENCH.getRegistryName(), "inventory"));

    	// テラプレート
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.TERRA_PLATE), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.TERRA_PLATE.getRegistryName(), "inventory"));
    	
    	// バックパックチェスト
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BACKPACK_CHEST, 0,
    			new ModelResourceLocation(YuzuKizuItems.BACKPACK_CHEST.getRegistryName(), "inventory"));
    	
    	// ブループリント
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.BLUEPRINT, 0,
    			new ModelResourceLocation(YuzuKizuItems.BLUEPRINT.getRegistryName(), "inventory"));
    	
    	// テラプレート
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.AUTO_WORKBENCH), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.AUTO_WORKBENCH.getRegistryName(), "inventory"));
    	
    	// ディメンションキー
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.DIMENSION_KEY, 0,
    			new ModelResourceLocation(YuzuKizuItems.DIMENSION_KEY.getRegistryName(), "inventory"));

    	// アルフヘイムコア
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.ALFHEIM_CORE), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.ALFHEIM_CORE.getRegistryName(), "inventory"));
    	
    	// ピクシィ鉱石
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.PIXIE_ORE), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.PIXIE_ORE.getRegistryName(), "inventory"));
    	
    	// エレブン鉱石
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.ELEVN_ORE), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.ELEVN_ORE.getRegistryName(), "inventory"));
    	
    	// エレメンチウム鉱石
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.ELEMENTIUM_ORE), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.ELEMENTIUM_ORE.getRegistryName(), "inventory"));
    
    	// ドラゴン鉱石
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.DRAGON_ORE), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.DRAGON_ORE.getRegistryName(), "inventory"));

    	// ガイア鉱石
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.GAIA_ORE), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.GAIA_ORE.getRegistryName(), "inventory"));
    	
    	// インスタントハウス
    	ModelLoader.setCustomModelResourceLocation(YuzuKizuItems.INSTANT_HOUSE, 0,
    			new ModelResourceLocation(YuzuKizuItems.INSTANT_HOUSE.getRegistryName(), "inventory"));
    	
    	// ドリームリーフ
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.DREAM_LEAF), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.DREAM_LEAF.getRegistryName(), "inventory"));
    	
    	// ドリームウッドの苗木
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.DREAM_SAPLING), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.DREAM_SAPLING.getRegistryName(), "inventory"));
    	
    	// アルフサンド
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(YuzuKizuBlocks.ALF_SAND), 0,
    			new ModelResourceLocation(YuzuKizuBlocks.ALF_SAND.getRegistryName(), "inventory"));
    }
    
    /**
     * Biomeを登録するイベント
     */
    @SubscribeEvent
    protected static void registerBiomes(RegistryEvent.Register<Biome> event) {
    	event.getRegistry().register(new BiomeManaPlains());
    }
    
    @SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
    	
    	for (String key : RecipeBoxedFlower.flowerMap.keySet()) {
    		//レシピの追加
        	event.getRegistry().register(new RecipeBoxedFlower(key));
    	}
    }
}
