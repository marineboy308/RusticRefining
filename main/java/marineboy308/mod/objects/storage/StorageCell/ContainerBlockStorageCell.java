	package marineboy308.mod.objects.storage.StorageCell;

import marineboy308.mod.objects.storage.StorageCell.slots.SlotBlockStorageCell;
import marineboy308.mod.objects.storage.StorageCell.slots.SlotBlockStorageCellUpgrades;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBlockStorageCell extends Container {

	private final IInventory tileBlock;
	
	private int storagelevel;
    
    private int inventorysize = 43;

    public ContainerBlockStorageCell(InventoryPlayer playerInventory, IInventory inventory)
    {
        this.tileBlock = inventory;
    	for (int l = 0; l < 10; l++) {
    		this.addSlotToContainer(new SlotBlockStorageCell(inventory, l, 8 + l * 18, 18, 0));
    	}
    	
    	for (int h = 0; h < 10; h++) {
    		this.addSlotToContainer(new SlotBlockStorageCell(inventory, h + 10, 8 + h * 18, 36, 1));
    	}
    	
    	for (int g = 0; g < 10; g++) {
    		this.addSlotToContainer(new SlotBlockStorageCell(inventory, g + 20, 8 + g * 18, 54, 2));
    	}
    	
    	for (int m = 0; m < 10; m++) {
    		this.addSlotToContainer(new SlotBlockStorageCell(inventory, m + 30, 8 + m * 18, 72, 3));
    	}
        
        this.addSlotToContainer(new SlotBlockStorageCellUpgrades(inventory, 40, 196, 27));
        this.addSlotToContainer(new SlotBlockStorageCellUpgrades(inventory, 41, 196, 45));
        this.addSlotToContainer(new SlotBlockStorageCellUpgrades(inventory, 42, 196, 63));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 17 + j * 18, 104 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 17 + k * 18, 162));
        }
    }

    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBlock);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.storagelevel != this.tileBlock.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileBlock.getField(0));
            }
        }

        this.storagelevel = this.tileBlock.getField(0);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileBlock.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileBlock.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (index < inventorysize) {
            	// From The Container Inventory To The Player Inventory
                if (!this.mergeItemStack(current, inventorysize, inventorysize+36, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(current, previous);
            } else {
            	// From The Player Inventory To The Container Inventory
            	if (!this.mergeItemStack(current, 0, inventorysize, false)) {
                    return ItemStack.EMPTY;
                }
            	
            	slot.onSlotChange(current, previous);
            }

            if (current.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (current.getCount() == previous.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, current);
        }

        return previous;
    }
}
