package marineboy308.mod.objects.machines.MaterialFilter.slots;

import marineboy308.mod.objects.machines.MaterialFilter.TileEntityBlockFilter;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockFilterUpgrades extends Slot {

	public SlotBlockFilterUpgrades(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
    {
        return TileEntityBlockFilter.isItemUpgrade(stack);
    }

	@Override
    public int getItemStackLimit(ItemStack stack)
    {
        return super.getItemStackLimit(stack);
    }
}