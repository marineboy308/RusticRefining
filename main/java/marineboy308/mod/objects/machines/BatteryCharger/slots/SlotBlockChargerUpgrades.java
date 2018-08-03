package marineboy308.mod.objects.machines.BatteryCharger.slots;

import marineboy308.mod.util.handlers.UpgradeHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockChargerUpgrades extends Slot {

	public SlotBlockChargerUpgrades(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
    {
        return UpgradeHandler.isItemUpgrade(stack.getItem());
    }

	@Override
    public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }
}