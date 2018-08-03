package marineboy308.mod.objects.storage.BatteryCell.slots;

import marineboy308.mod.objects.storage.BatteryCell.TileEntityBlockCell;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockCell extends Slot {

	public SlotBlockCell(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return TileEntityBlockCell.isItemBattery(stack);
    }

	@Override
    public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }
}
