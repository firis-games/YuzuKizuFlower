package firis.yuzukizuflower.common.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import baubles.api.IBauble;
import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class YKItemBaseBaubleAmulet extends Item implements IBauble {
	
	/**
	 * コンストラクタ
	 */
	public YKItemBaseBaubleAmulet() {
		super();
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		this.setMaxStackSize(1);
	}
	
	/**
	 * 装備時Multimap設定追加
	 */
	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			getBaubleAttribute(attributes, itemstack);
			player.getAttributeMap().applyAttributeModifiers(attributes);
		}
	}

	/**
	 * 装備解除時Multimap設定削除
	 */
	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			getBaubleAttribute(attributes, itemstack);
			player.getAttributeMap().removeAttributeModifiers(attributes);
		}
	}
	
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		
		//playerのhashcodeは毎回変更されるようなので制御が必要
		//player.hashCode()
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			getBaubleAttribute(attributes, itemstack);
			player.getAttributeMap().applyAttributeModifiers(attributes);
		}
	}
	
	/**
	 * BaublesのMultimapを設定する
	 * @param attributes
	 * @param stack
	 */
	protected abstract void getBaubleAttribute(Multimap<String, AttributeModifier> attributes, ItemStack stack);
	
	
	@Override
	public abstract void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn);
	
}
