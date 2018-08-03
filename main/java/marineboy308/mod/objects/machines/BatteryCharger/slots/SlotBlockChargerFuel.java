package marineboy308.mod.objects.machines.BatteryCharger.slots;

import marineboy308.mod.objects.machines.BatteryCharger.TileEntityBlockCharger;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockChargerFuel extends Slot {

	public SlotBlockChargerFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return TileEntityBlockCharger.isItemFuel(stack);
    }

	@Override
    public int getItemStackLimit(ItemStack stack)
    {
        return super.getItemStackLimit(stack);
    }
}
