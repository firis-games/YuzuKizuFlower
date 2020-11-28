package firis.yuzukizuflower.common.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKItemBaubleAmuletArmor extends YKItemBaseBaubleAmulet {

	private static UUID uuid = UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6C");
	
	@Override
	public BaubleType getBaubleType(ItemStack paramItemStack) {
		return BaubleType.RING;
	}

	@Override
	protected void getBaubleAttribute(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
		//ベース防御力up
		attributes.put(SharedMonsterAttributes.ARMOR.getName(), 
				new AttributeModifier(uuid, "Armor modifier", 4, 0));
		
		attributes.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), 
				new AttributeModifier(uuid, "Armor toughness", 2, 0));
	}
	
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    	tooltip.add(I18n.format("attribute.name.generic.armor") + " +4");
    	tooltip.add(I18n.format("attribute.name.generic.armorToughness") + " +2");
    }
	

}
