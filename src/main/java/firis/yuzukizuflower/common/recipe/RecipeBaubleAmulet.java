package firis.yuzukizuflower.common.recipe;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.item.YKItemBaseBaubleAmulet;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * お守りの変更レシピ
 * @author computer
 *
 */
public class RecipeBaubleAmulet extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	
	/**
	 * コンストラクタ
	 * @param key
	 */
	public RecipeBaubleAmulet() {

		this.setRegistryName(new ResourceLocation(YuzuKizuFlower.MODID, "RecipeBaubleAmulet"));
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		
		boolean amulet = false;
		
		//お守り検索
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			//お守り検索
			if (!stack.isEmpty() && stack.getItem() instanceof YKItemBaseBaubleAmulet) {
				amulet = true;
			}
			//それ以外のアイテム
			else if (!stack.isEmpty()) {
				return false;
			}
		}
		
		if (amulet) {
			return true;
		}
		
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		//お守り検索
		ItemStack amulet = null;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			//お守り検索
			if (!stack.isEmpty() && stack.getItem() instanceof YKItemBaseBaubleAmulet) {
				amulet = stack.copy();
				break;
			}
		}
		//アミュレットのNBTを設定
		if (!amulet.hasTagCompound()) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("amulet", false);
			amulet.setTagCompound(nbt);
		} else {
			NBTTagCompound nbt = amulet.getTagCompound();
			if (nbt.hasKey("amulet")) {
				nbt.setBoolean("amulet", !nbt.getBoolean("amulet"));
			} else {
				nbt.setBoolean("amulet", false);
			}
			amulet.setTagCompound(nbt);
		}
		return amulet;
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
