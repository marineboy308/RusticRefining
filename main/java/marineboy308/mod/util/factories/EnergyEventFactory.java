package marineboy308.mod.util.factories;

import javax.annotation.Nonnull;

import marineboy308.mod.objects.items.ItemBattery;
import marineboy308.mod.objects.items.ItemFuel;
import marineboy308.mod.util.events.EnergyDrainTimeEvent;
import marineboy308.mod.util.events.EnergyStoredEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class EnergyEventFactory {

	public static int getItemDrainTime(@Nonnull ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        if ((item instanceof ItemFuel ? (ItemFuel)item : null) != null) {
	        int drainTime = ((ItemFuel)item).getEnergyDrainTime(itemStack);
	        EnergyDrainTimeEvent event = new EnergyDrainTimeEvent(itemStack, drainTime);
	        MinecraftForge.EVENT_BUS.post(event);
	        if (event.getDrainTime() < 0)
	        {
	            int energyValue = 0;
	            return energyValue;
	        }
	        return event.getDrainTime();
        }
        return 0;
    }
	
	public static int getItemEnergyStored(@Nonnull ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        if ((item instanceof ItemFuel ? (ItemFuel)item : null) != null) {
	        int energy = ((ItemFuel)item).getItemEnergy(itemStack);
	        EnergyStoredEvent event = new EnergyStoredEvent(itemStack, energy);
	        MinecraftForge.EVENT_BUS.post(event);
	        if (event.getStoredEnergy() < 0)
	        {
	            int energyValue = 0;
	            return energyValue;
	        }
	        return event.getStoredEnergy();
        } else if ((item instanceof ItemBattery ? (ItemBattery)item : null) != null) {
	        int energy = ((ItemBattery)item).getItemEnergy(itemStack);
	        EnergyStoredEvent event = new EnergyStoredEvent(itemStack, energy);
	        MinecraftForge.EVENT_BUS.post(event);
	        if (event.getStoredEnergy() < 0)
	        {
	            int energyValue = 0;
	            return energyValue;
	        }
	        return event.getStoredEnergy();
        }
        return 0;
    }
}
