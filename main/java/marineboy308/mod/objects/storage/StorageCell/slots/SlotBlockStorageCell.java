package marineboy308.mod.objects.storage.StorageCell.slots;

import marineboy308.mod.objects.storage.StorageCell.TileEntityBlockStorageCell;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlockStorageCell extends Slot {
	
    private int validlevel;

    public SlotBlockStorageCell(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition, int validlevel) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.validlevel = validlevel;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
    	if (validlevel >= TileEntityBlockStorageCell.getStorageLevel(this.inventory)) return true;
    	return false;
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return super.getItemStackLimit(stack);
    }
}
