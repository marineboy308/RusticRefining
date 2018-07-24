package marineboy308.mod.objects.machines.MaterialFilter.slots;

import marineboy308.mod.objects.machines.MaterialFilter.TileEntityBlockFilter;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockFilterInput extends Slot {

	public SlotBlockFilterInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return TileEntityBlockFilter.isItemFilterable(stack);
    }

	@Override
    public int getItemStackLimit(ItemStack stack)
    {
        return super.getItemStackLimit(stack);
    }
}
