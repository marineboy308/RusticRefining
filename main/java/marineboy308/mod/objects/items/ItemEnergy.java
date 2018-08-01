package marineboy308.mod.objects.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemEnergy extends Item {

	/**
     * @return the energy stored for this itemStack to give to a container.
     */
    public int getItemEnergy(ItemStack itemStack)
    {
        return -1;
    }
    
    /**
     * set energy of the Item
     */
    public void setItemEnergy(ItemStack itemStack, int energy)
    {
    }
    
    /**
     * @return the time that it takes for this itemStack to give its energy to a container.
     */
    public int getEnergyDrainTime(ItemStack itemStack)
    {
        return 0;
    }
    
    /**
     * @return the amount of energy that is drained from this itemStack per tick.
     */
    public int getEnergyDrainAmount(ItemStack itemStack)
    {
        return 10;
    }
}
