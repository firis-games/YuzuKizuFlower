package firis.yuzukizuflower.common.item;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.botania.ManaRecipe;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedBrewery;
import firis.yuzukizuflower.common.tileentity.YKTilePetalWorkbench;
import firis.yuzukizuflower.common.tileentity.YKTileRuneWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class YKItemBlueprint extends Item {

	public YKItemBlueprint() {
		super();
		
		//初期化
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		this.setMaxStackSize(1);
		
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return stack.hasTagCompound();
    }
	
	/**
     * Called when a Block is right-clicked with this Item
     */
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		//ブループリント以外は何もしない
		ItemStack stack = player.getHeldItem(hand);
		if (stack.isEmpty() || !(stack.getItem() instanceof YKItemBlueprint)) {
			return EnumActionResult.PASS;
		}
		
		//TileEntityを取得
		TileEntity tile = worldIn.getTileEntity(pos);
		
		//設計図データ
		NBTTagCompound blueprint = null;
		
		//花びら作業台の場合
		if (tile instanceof YKTilePetalWorkbench) {
			blueprint = createNbtPetalBluprint((YKTilePetalWorkbench) tile);
		}
		//ルーンの作業台の場合
		else if (tile instanceof YKTileRuneWorkbench) {
			blueprint = createNbtRuneBluprint((YKTileRuneWorkbench) tile);
		}
		//箱入り醸造台の場合
		else if (tile instanceof YKTileBoxedBrewery) {
			blueprint = createNbtBreweryBluprint((YKTileBoxedBrewery) tile);
		}
		
		if (blueprint != null) {
			//設計図情報を書き込み
			stack.setTagCompound(blueprint);
			return EnumActionResult.SUCCESS;
		}
		
        return EnumActionResult.PASS;
    }
	
	/**
	 * 花びら作業台を参照してレシピを取得する
	 * @param tile
	 */
	protected NBTTagCompound createNbtPetalBluprint(YKTilePetalWorkbench tile) {
		
		ItemStackHandler handler = tile.inventory;
		NBTTagCompound recipeNbt = null;
		
		//種を参照する
		ItemStack seed = handler.getStackInSlot(16);
		if (!BotaniaHelper.recipesPetal.isSeed(seed)) {
			return recipeNbt;
		}

		//レシピを参照する
		List<ItemStack> recipeList = new ArrayList<ItemStack>();
		for (int slot = 0; slot < 16; slot++) {
			if (!handler.getStackInSlot(slot).isEmpty()) {
				recipeList.add(handler.getStackInSlot(slot));
			}
		}
		//レシピが有効の場合
		ItemStack resultStack = BotaniaHelper.recipesPetal.getMatchesRecipe(recipeList);
		if (resultStack.isEmpty()) {
			return recipeNbt;
		}
		
		//結果を元に検索すると複数当たるパターンとかを考慮しないといけないので
		//素材の一覧を保存する
		//レシピ保存用のNBTを生成する
		recipeNbt = new NBTTagCompound();
		
		NBTTagList recipeNbtTagList = new NBTTagList();
		//レシピ
		for (int i = 0; i < recipeList.size(); i++) {
			ItemStack stack = recipeList.get(i).copy();
			stack.setCount(1);
			NBTTagCompound itemTag = new NBTTagCompound();
			stack.writeToNBT(itemTag);
			itemTag.setInteger("Slot", i);
			recipeNbtTagList.appendTag(itemTag);
		}
		recipeNbt.setTag("recipe", recipeNbtTagList);
		
		//種も追加する
		NBTTagCompound itemTag = new NBTTagCompound();
		ItemStack stack = seed.copy();
		stack.setCount(1);
		stack.writeToNBT(itemTag);
		recipeNbt.setTag("catalyst", itemTag);
		
		//レシピのタイプ
		recipeNbt.setString("recipetype", "petal");
		
		//表示用
		NBTTagCompound nameTag= new NBTTagCompound();
		nameTag.setString("Name", resultStack.getDisplayName() + " Blueprint");
		recipeNbt.setTag("display", nameTag);
		return recipeNbt;
		
	}
	
	/**
	 * ルーンの作業台を参照してレシピを取得する
	 * @param tile
	 */
	protected NBTTagCompound createNbtRuneBluprint(YKTileRuneWorkbench tile) {
		
		ItemStackHandler handler = tile.inventory;
		NBTTagCompound recipeNbt = null;
		
		//種を参照する
		ItemStack cataylst = handler.getStackInSlot(16);
		if (!BotaniaHelper.recipesRuneAltar.isStone(cataylst)) {
			return recipeNbt;
		}

		//レシピを参照する
		List<ItemStack> recipeList = new ArrayList<ItemStack>();
		for (int slot = 0; slot < 16; slot++) {
			if (!handler.getStackInSlot(slot).isEmpty()) {
				recipeList.add(handler.getStackInSlot(slot));
			}
		}
		//レシピが有効の場合
		ManaRecipe resultRecipe = BotaniaHelper.recipesRuneAltar.getMatchesRecipe(recipeList);
		if (resultRecipe == null) {
			return recipeNbt;
		}
		
		//結果を元に検索すると複数当たるパターンとかを考慮しないといけないので
		//素材の一覧を保存する
		//レシピ保存用のNBTを生成する
		recipeNbt = new NBTTagCompound();
		
		NBTTagList recipeNbtTagList = new NBTTagList();
		//レシピ
		for (int i = 0; i < recipeList.size(); i++) {
			ItemStack stack = recipeList.get(i).copy();
			stack.setCount(1);
			NBTTagCompound itemTag = new NBTTagCompound();
			stack.writeToNBT(itemTag);
			itemTag.setInteger("Slot", i);
			recipeNbtTagList.appendTag(itemTag);
		}
		recipeNbt.setTag("recipe", recipeNbtTagList);
		
		//種も追加する
		NBTTagCompound itemTag = new NBTTagCompound();
		ItemStack stack = cataylst.copy();
		stack.setCount(1);
		stack.writeToNBT(itemTag);
		recipeNbt.setTag("catalyst", itemTag);
		
		//レシピのタイプ
		recipeNbt.setString("recipetype", "rune");
		
		//表示用
		NBTTagCompound nameTag= new NBTTagCompound();
		nameTag.setString("Name", resultRecipe.getOutputItemStack().getDisplayName() + " Blueprint" );
		recipeNbt.setTag("display", nameTag);
		return recipeNbt;
	}
	
	/**
	 * 箱入り醸造台を参照してレシピを取得する
	 * @param tile
	 */
	protected NBTTagCompound createNbtBreweryBluprint(YKTileBoxedBrewery tile) {
		
		ItemStackHandler handler = tile.inventory;
		NBTTagCompound recipeNbt = null;
		
		//触媒を参照する
		ItemStack cataylst = handler.getStackInSlot(16);
		if (!BotaniaHelper.recipesBrewery.isCatalyst(cataylst)) {
			return recipeNbt;
		}

		//レシピを参照する
		List<ItemStack> recipeList = new ArrayList<ItemStack>();
		for (int slot = 0; slot < 16; slot++) {
			if (!handler.getStackInSlot(slot).isEmpty()) {
				recipeList.add(handler.getStackInSlot(slot));
			}
		}
		//レシピが有効の場合
		ManaRecipe resultRecipe = BotaniaHelper.recipesBrewery.getMatchesRecipe(recipeList, cataylst);
		if (resultRecipe == null) {
			return recipeNbt;
		}
		
		//結果を元に検索すると複数当たるパターンとかを考慮しないといけないので
		//素材の一覧を保存する
		//レシピ保存用のNBTを生成する
		recipeNbt = new NBTTagCompound();
		
		NBTTagList recipeNbtTagList = new NBTTagList();
		//レシピ
		for (int i = 0; i < recipeList.size(); i++) {
			ItemStack stack = recipeList.get(i).copy();
			stack.setCount(1);
			NBTTagCompound itemTag = new NBTTagCompound();
			stack.writeToNBT(itemTag);
			itemTag.setInteger("Slot", i);
			recipeNbtTagList.appendTag(itemTag);
		}
		recipeNbt.setTag("recipe", recipeNbtTagList);
		
		//触媒も追加する
		NBTTagCompound itemTag = new NBTTagCompound();
		ItemStack stack = cataylst.copy();
		stack.setCount(1);
		stack.writeToNBT(itemTag);
		recipeNbt.setTag("catalyst", itemTag);
		
		//レシピのタイプ
		recipeNbt.setString("recipetype", "brewery");
		
		//表示用
		NBTTagCompound nameTag= new NBTTagCompound();
		nameTag.setString("Name", resultRecipe.getOutputItemStack().getDisplayName() + " Blueprint" );
		recipeNbt.setTag("display", nameTag);
		return recipeNbt;
	}
}
