package marineboy308.mod.util.events;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EnergyDrainTimeEvent extends Event {

	@Nonnull
    private final ItemStack itemStack;
    private int drainTime;
    
    public EnergyDrainTimeEvent(@Nonnull ItemStack itemStack, int drainTime)
    {
        this.itemStack = itemStack;
        this.drainTime = drainTime;
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
     * Set the energy drain time for the given ItemStack.
     */
    public void setDrainTime(int drainTime)
    {
        this.drainTime = drainTime;
        setCanceled(true);
    }

    /**
     * The resulting value of this event, the energy drain time for the ItemStack.
     */
    public int getDrainTime()
    {
        return drainTime;
    }
}
