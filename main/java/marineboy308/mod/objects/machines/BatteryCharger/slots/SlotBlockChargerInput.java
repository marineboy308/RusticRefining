package marineboy308.mod.objects.machines.BatteryCharger.slots;

import marineboy308.mod.objects.machines.BatteryCharger.TileEntityBlockCharger;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockChargerInput extends Slot {

	public SlotBlockChargerInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return TileEntityBlockCharger.isItemBattery(stack);
    }

	@Override
    public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }
}
