package firis.yuzukizuflower.common.recipe;

import java.util.HashMap;
import java.util.Map;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.botania.BotaniaHelper;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * 箱入りお花のレシピ
 * @author computer
 *
 */
public class RecipeBoxedFlower extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@SuppressWarnings("serial")
	public static Map<String, Block> flowerMap = new HashMap<String, Block>(){
		{
			put("puredaisy", YuzuKizuFlower.YuzuKizuBlocks.BOXED_PURE_DAISY);
			put("endoflame", YuzuKizuFlower.YuzuKizuBlocks.BOXED_ENDOFLAME);
			put("rannuncarpus", YuzuKizuFlower.YuzuKizuBlocks.BOXED_RANNUNCARPUS);
			put("jadedAmaranthus", YuzuKizuFlower.YuzuKizuBlocks.BOXED_JADED_AMARANTHUS);
			put("orechid", YuzuKizuFlower.YuzuKizuBlocks.BOXED_ORECHID);
			put("orechidIgnem", YuzuKizuFlower.YuzuKizuBlocks.BOXED_ORECHID);
			put("gourmaryllis", YuzuKizuFlower.YuzuKizuBlocks.BOXED_GOURMARYLLIS);
			put("kekimurus", YuzuKizuFlower.YuzuKizuBlocks.BOXED_KEKIMURUS);
			put("entropinnyum", YuzuKizuFlower.YuzuKizuBlocks.BOXED_ENTROPINNYUM);
			put("clayconia", YuzuKizuFlower.YuzuKizuBlocks.BOXED_CLAYCONIA);
			put("loonium", YuzuKizuFlower.YuzuKizuBlocks.BOXED_LOONIUM);
		}
	};
	
	/**
	 * コンストラクタ
	 * @param key
	 */
	public RecipeBoxedFlower(String flowerType) {

		this.setRegistryName(new ResourceLocation(YuzuKizuFlower.MODID, "RecipeBoxedFlower_" + flowerType));
		this.flowerType = flowerType;
	}
	public String flowerType = "";
	
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		
		boolean special_flower = false;
		boolean flower_box = false;
		
		//SpecialFlower
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			
			ItemStack stack = inv.getStackInSlot(i);
			
			//SpecialFlower検索
			if (BotaniaHelper.isSpecialFlower(stack, this.flowerType)) {
				special_flower = true;
				continue;
			}
			
			//FlowerBox検索
			if (!stack.isEmpty()
					&& stack.getItem().getRegistryName().equals(new ResourceLocation("yuzukizuflower:flower_box"))) {
				flower_box = true;
				continue;
			}
			
			//それ以外のアイテム
			if (!stack.isEmpty()) {
				return false;
			}
		}
		
		if (special_flower && flower_box) {
			return true;
		}
		
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return new ItemStack(RecipeBoxedFlower.flowerMap.get(this.flowerType));
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
	
	
}
