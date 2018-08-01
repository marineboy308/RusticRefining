package marineboy308.mod.objects.machines.MaterialFilter.slots;

import marineboy308.mod.objects.machines.MaterialFilter.TileEntityBlockFilter;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockFilterFuel extends Slot {

	public SlotBlockFilterFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return TileEntityBlockFilter.isItemFuel(stack);
    }

	@Override
    public int getItemStackLimit(ItemStack stack)
    {
        return super.getItemStackLimit(stack);
    }
}
