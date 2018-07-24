package marineboy308.mod.objects.machines.Condenser.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockCondenserInput extends Slot {

	public SlotBlockCondenserInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
    public boolean isItemValid(ItemStack stack)
    {
        /*return TileEntityBlockCondenser.canItemBeCondensed(input1, input2, input3, input4);*/
		return true;
    }

	@Override
    public int getItemStackLimit(ItemStack stack)
    {
        return super.getItemStackLimit(stack);
    }
}
