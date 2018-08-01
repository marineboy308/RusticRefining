package marineboy308.mod.util.events;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EnergyStoredEvent extends Event {

	@Nonnull
    private final ItemStack itemStack;
    private int energy;
    
    public EnergyStoredEvent(@Nonnull ItemStack itemStack, int energy)
    {
        this.itemStack = itemStack;
        this.energy = energy;
    }

    /**
     * Get the ItemStack "fuel" in question.
     */
    @Nonnull
    public ItemStack getItemStack()
    {
        return itemStack;
    }

    /**
     * Set the stored energy for the given ItemStack.
     */
    public void setStoredEnergy(int energy)
    {
        this.energy = energy;
        setCanceled(true);
    }

    /**
     * The resulting value of this event, the stored energy for the ItemStack.
     */
    public int getStoredEnergy()
    {
        return energy;
    }
}
