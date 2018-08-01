package marineboy308.mod.objects.items;

import marineboy308.mod.Main;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.util.interfaces.IHasModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWrench extends Item implements IHasModel {

	protected ToolMaterial toolMaterial;
	
	public ItemWrench(String name, ToolMaterial material) {
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.rusticrefiningtab);
		
		this.toolMaterial = material;
		this.setMaxDamage(material.getMaxUses());
		
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public int getItemStackLimit() {
		return 1;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(1, attacker);
        return true;
    }
	
	public String getMaterialName()
    {
        return this.toolMaterial.toString();
    }

	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
