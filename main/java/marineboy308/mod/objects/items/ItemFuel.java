package marineboy308.mod.objects.items;

import java.util.List;

import marineboy308.mod.Main;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.util.interfaces.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemFuel extends ItemEnergy implements IHasModel {
	
	private int burntime;
	private int energy;
	private int draintime;

	public ItemFuel(String name, int burntime, int energy, int draintime) {

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.rusticrefiningtab);
		
		this.burntime = burntime;
		this.energy = energy;
		this.draintime = draintime;
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack) {
		return this.burntime;
	}
	
	@Override
	public int getItemEnergy(ItemStack itemStack) {
		return this.energy;
	}
	
	@Override
	public int getEnergyDrainTime(ItemStack itemStack) {
		return this.draintime;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.DARK_RED + "Energy: " + TextFormatting.WHITE + this.energy + "RE");
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
