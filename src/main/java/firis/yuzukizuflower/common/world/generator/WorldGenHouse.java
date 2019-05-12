package firis.yuzukizuflower.common.world.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class WorldGenHouse extends WorldGenerator {

	protected EnumFacing facing;
	
	public WorldGenHouse() {
		this.facing = EnumFacing.NORTH;
	}
	
	public WorldGenHouse(EnumFacing facing) {
		this.facing = facing;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		
		//assetsからテンプレートを取得
		WorldServer worldserver = (WorldServer)world;
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        Template template = templatemanager.getTemplate(null,
        		new ResourceLocation(YuzuKizuFlower.MODID, "house/house"));
		
        PlacementSettings placementsettings =  new PlacementSettings();
        
        BlockPos pos = position;
        
        //位置調整
        //北（標準）
        switch (this.facing) {
        case NORTH:
            pos = position.up().north(9).west(4);
        	break;
        case SOUTH:
        	placementsettings.setRotation(Rotation.CLOCKWISE_180);
            pos = position.up().south(9).east(4);
        	break;
        case EAST:
        	placementsettings.setRotation(Rotation.CLOCKWISE_90);
            pos = position.up().north(4).east(9);
        	break;
        case WEST:
        	placementsettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            pos = position.up().south(4).west(9);
        	break;
       	default:
        }
        
        
        //構造体設置
        template.addBlocksToWorldChunk(world, pos, placementsettings);
        
        //チェストにアイテムを仕込む
        BlockPos chestPos = position.up(2);
        
        //北（標準）
        switch (this.facing) {
        case NORTH:
        	chestPos = chestPos.north(7).west(2);
        	break;
        case SOUTH:
        	chestPos = chestPos.south(7).east(2);
        	break;
        case EAST:
        	chestPos = chestPos.north(2).east(7);
        	break;
        case WEST:
        	chestPos = chestPos.south(2).west(7);
        	break;
       	default:
        }
        
        //アイテムを追加する
        TileEntity tile = world.getTileEntity(chestPos);
        if (tile != null) {
        	IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        	if (handler != null) {
        		
        		List<ItemStack> stackList = new ArrayList<ItemStack>();
        		
        		//エンチャント
        		Enchantment sharpness = Enchantment.getEnchantmentByLocation("sharpness");
        		Enchantment efficiency = Enchantment.getEnchantmentByLocation("efficiency");
        		Enchantment unbreaking = Enchantment.getEnchantmentByLocation("unbreaking");
        		
        		ItemStack toolStack;
        		int randTool = 2;
        		//ツール
        		toolStack = new ItemStack(Items.STONE_SWORD);
        		if (world.rand.nextInt(randTool) == 0) {
	        		toolStack.addEnchantment(sharpness, 1);
	        		toolStack.addEnchantment(unbreaking, 1);
        		}
        		stackList.add(toolStack);

        		toolStack = new ItemStack(Items.STONE_PICKAXE);
        		if (world.rand.nextInt(randTool) == 0) {
	        		toolStack.addEnchantment(efficiency, 1);
	        		toolStack.addEnchantment(unbreaking, 1);
        		}
        		stackList.add(toolStack);
        		
        		toolStack = new ItemStack(Items.STONE_AXE);
        		if (world.rand.nextInt(randTool) == 0) {
	        		toolStack.addEnchantment(efficiency, 1);
	        		toolStack.addEnchantment(unbreaking, 1);
        		}
        		stackList.add(toolStack);

        		toolStack = new ItemStack(Items.STONE_SHOVEL);
        		if (world.rand.nextInt(randTool) == 0) {
	        		toolStack.addEnchantment(efficiency, 1);
	        		toolStack.addEnchantment(unbreaking, 1);
        		}
        		stackList.add(toolStack);

        		toolStack = new ItemStack(Items.STONE_HOE);
        		if (world.rand.nextInt(randTool) == 0) {
	        		toolStack.addEnchantment(unbreaking, 1);
        		}
        		stackList.add(toolStack);

        		//防具
        		stackList.add(new ItemStack(Items.LEATHER_HELMET));
        		stackList.add(new ItemStack(Items.LEATHER_CHESTPLATE));
        		stackList.add(new ItemStack(Items.LEATHER_LEGGINGS));
        		stackList.add(new ItemStack(Items.LEATHER_BOOTS));
        		
        		//食べ物
        		stackList.add(new ItemStack(Items.BREAD, 1));
        		stackList.add(new ItemStack(Items.APPLE, 1));
        		stackList.add(new ItemStack(Items.BAKED_POTATO, 1));
        		stackList.add(new ItemStack(Items.PUMPKIN_PIE, 1));
        		stackList.add(new ItemStack(Items.COOKIE, 1));
        		stackList.add(new ItemStack(Items.MUSHROOM_STEW, 1));
        		stackList.add(new ItemStack(Items.RABBIT_STEW, 1));
        		stackList.add(new ItemStack(Items.BEETROOT_SOUP, 1));
        		stackList.add(new ItemStack(Items.MILK_BUCKET, 1));

        		//チェストへアイテムを追加する
        		for (ItemStack stack : stackList) {
        			for (int slot = 0; slot < handler.getSlots(); slot++) {
                		stack = handler.insertItem(slot, stack, false);
                		if (stack.isEmpty()) {
                			break;
                		}
        			}
        		}
        	}
        }
        
		return true;
	}

}
