package marineboy308.mod.objects.items;

import java.util.List;

import marineboy308.mod.Main;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.util.interfaces.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemUpgrade extends Item implements IHasModel {
	
	private String tooltip;
	private TextFormatting color;

	public ItemUpgrade(String name, String tooltip, TextFormatting color) {
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.rusticrefiningtab);
		
		this.tooltip = tooltip;
		this.color = color;
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(this.color + this.tooltip);
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	

	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
