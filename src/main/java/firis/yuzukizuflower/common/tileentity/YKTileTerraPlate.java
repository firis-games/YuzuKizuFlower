package firis.yuzukizuflower.common.tileentity;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import firis.yuzukizuflower.common.botania.ManaRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.Botania;

/**
 * テラプレート処理
 * @author computer
 *
 */
public class YKTileTerraPlate extends YKTileBaseBoxedFuncFlower {

	public YKTileTerraPlate() {

		this.maxMana = 500000;
		
		//汎用レシピ変換は利用しない
		this.funcFlowerRecipes = null;
		
		this.randomRecipe = false;
		
		//inputスロット
		this.inputSlotIndex = IntStream.rangeClosed(0, 2).boxed().collect(Collectors.toList());
		
		//outputスロット
		this.outputSlotIndex = IntStream.rangeClosed(3, 3).boxed().collect(Collectors.toList());
		
		//workスロット
		this.workSlotIndex = IntStream.rangeClosed(4, 6).boxed().collect(Collectors.toList());
		
	}
	
	@Override
	public int getSizeInventory() {
		return 7;
	}
	
	/**
	 * マナ変換レシピを取得する
	 */
	@Override
	public ManaRecipe getManaRecipe() {
		
		List<ItemStack> stackList = getStackInputSlotList();
		
		boolean ingot = false;
		boolean pearl = false;
		boolean diamond = false;
		
		ResourceLocation rl = new ResourceLocation("botania", "manaresource");
		
		for (ItemStack stack : stackList) {
			//マナインゴット
			if (!stack.isEmpty() 
					&& stack.getItem().getRegistryName().equals(rl)
					&& stack.getMetadata() == 0) {
				ingot = true;
			} else if (!stack.isEmpty() 
					&& stack.getItem().getRegistryName().equals(rl)
					&& stack.getMetadata() == 1) {
				pearl = true;
			} else if (!stack.isEmpty() 
					&& stack.getItem().getRegistryName().equals(rl)
					&& stack.getMetadata() == 2) {
				diamond = true;
			}
		}
		
		//マナレシピを作成
		ManaRecipe recipe = null;
		if (ingot && pearl && diamond) {
			recipe = getTerraSteelRecipe();
		}

		//レシピの確認
		return recipe;
	}
	
	/**
	 * recipeに設定された入力側のItemStackをWorkスロットへ移動する
	 */
	@Override
	public void shrinkStackInputSlotToWorkSlot(ManaRecipe recipe) {
		
		List<ItemStack> stackList = getStackInputSlotList();
		
		for (int i = 0; i < this.workSlotIndex.size(); i++) {
			
			ItemStack stack = stackList.get(i).copy();
			stack.setCount(1);
			
			//inputからworkSlotへ設定
			this.setInventorySlotContents(workSlotIndex.get(i), stack);
			
		}
		//inputから1つ減らす
		for (int i = 0; i < this.inputSlotIndex.size(); i++) {
			this.getStackInSlot(i).shrink(1);
		}
	}

	/**
	 * レシピの出力結果を取得する
	 */
	@Override
	public ItemStack getRecipesResult() {
		ManaRecipe recipe = getTerraSteelRecipe();
		return recipe.getOutputItemStack();		
	}
	
	@Override
	public boolean isItemValidRecipesForInputSlot(ItemStack stack) {
		
		ResourceLocation rl = new ResourceLocation("botania", "manaresource");
		if (!stack.isEmpty() 
				&& stack.getItem().getRegistryName().equals(rl)
				&& (stack.getMetadata() == 0 || stack.getMetadata() == 1 || stack.getMetadata() == 2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * テラスチールのレシピを取得
	 * @return
	 */
	private ManaRecipe getTerraSteelRecipe() {
		return new ManaRecipe(
				ItemStack.EMPTY.copy(), 
				new ItemStack(Item.getByNameOrId(new ResourceLocation("botania", "manaresource").toString()), 1, 4), 
				500000, 100);
	}
	
	/**
	 * お花が稼動状態か判断する
	 * @return
	 */
	@Override
	public boolean isActive() {
		
		//レッドストーン入力がある場合は停止状態
		if(isRedStonePower()) {
			return false;
		}
		
		//timerが0の場合は停止状態
		if (this.timer == 0) {
			return false;
		}
		
		//Manaが0の場合は停止状態
		if (this.mana == 0) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * ランダムでパーティクルを表示する
	 */
	@Override
	public void clientSpawnParticle() {
		//クライアントの場合
		if(this.getWorld().isRemote) {
			
			double particleChance = 0.95F;
			
			Color color = Color.GREEN;
			
			if(Math.random() > particleChance) {
				//マナプールと同じパーティクル
				//マナプールと同じパーティクル
				Botania.proxy.wispFX(
						pos.getX() + 0.3 + Math.random() * 0.5, 
						pos.getY() + 0.4 + Math.random() * 0.25, 
						pos.getZ() + Math.random(), 
						color.getRed() / 255F, 
						color.getGreen() / 255F, 
						color.getBlue() / 255F, 
						(float) Math.random() / 4F, 
						(float) -Math.random() / 120F, 
						1F);
			}
		}
	}
}