package marineboy308.mod.objects.items;

import java.util.List;

import marineboy308.mod.Main;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.util.interfaces.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemBattery extends ItemEnergy implements IHasModel {
	
	private int energy;
	private int maxEnergy;
	private int drainamount;
	public boolean iscreative;

	public ItemBattery(String name, int energy, int maxEnergy, int drainamount, boolean iscreative) {

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.rusticrefiningtab);
		
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.drainamount = drainamount;
		this.iscreative = iscreative;
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public int getItemEnergy(ItemStack itemStack) {
		NBTTagCompound nbt;
		if (itemStack.hasTagCompound()) {
			nbt = itemStack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		
		if (nbt.hasKey("Energy")) {
			return nbt.getInteger("Energy");
		} else {
			nbt.setInteger("Energy", this.energy);
			itemStack.setTagCompound(nbt);
			return this.energy;
		}
	}
	
	@Override
	public void setItemEnergy(ItemStack itemStack, int energy) {
			NBTTagCompound nbt;
			if (itemStack.hasTagCompound()) {
				nbt = itemStack.getTagCompound();
			} else {
				nbt = new NBTTagCompound();
			}
			
			if (energy > this.maxEnergy) energy = this.maxEnergy;
			else if (energy < 0) energy = 0;
			
			if (nbt.hasKey("Energy")) {
				if (!this.iscreative) nbt.setInteger("Energy", energy);
				else nbt.setInteger("Energy", this.energy);
			} else {
				if (!this.iscreative) nbt.setInteger("Energy", energy);
				else nbt.setInteger("Energy", this.energy);
			}
			itemStack.setTagCompound(nbt);
	}
	
	@Override
	public int getEnergyDrainAmount(ItemStack itemStack) {
		return this.drainamount;
	}
	
	@Override
	public int getItemStackLimit() {
		return 1;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		
		if (nbt.hasKey("Energy")) {
			if (!this.iscreative) tooltip.add(TextFormatting.DARK_RED + "Energy Stored: " + TextFormatting.WHITE + stack.getTagCompound().getInteger("Energy") + "/" + this.maxEnergy + "RE");
			else tooltip.add(TextFormatting.DARK_RED + "Energy Stored: " + TextFormatting.WHITE + "MaxRE");
		} else {
			nbt.setInteger("Energy", this.energy);
			stack.setTagCompound(nbt);
			if (!this.iscreative) tooltip.add(TextFormatting.DARK_RED + "Energy Stored: " + TextFormatting.WHITE + this.energy + "/" + this.maxEnergy + "RE");
			else tooltip.add(TextFormatting.DARK_RED + "Energy Stored: " + TextFormatting.WHITE + "MaxRE");
		}
	}
	
	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
