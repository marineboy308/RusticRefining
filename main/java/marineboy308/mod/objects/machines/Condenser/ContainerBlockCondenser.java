	package marineboy308.mod.objects.machines.Condenser;

import marineboy308.mod.objects.machines.Condenser.slots.SlotBlockCondenserOutput;
import marineboy308.mod.objects.machines.Condenser.slots.SlotBlockCondenserUpgrades;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBlockCondenser extends Container {

	private final IInventory tileBlockCondenser;
	
    private int condenseTime;
    private int totalCondensingTime;
    private int condensingTime;
    
    private int inventorysize = 6;

    public ContainerBlockCondenser(InventoryPlayer playerInventory, IInventory inventory)
    {
        this.tileBlockCondenser = inventory;
        this.addSlotToContainer(new Slot(inventory, 0, 44, 25));
        this.addSlotToContainer(new Slot(inventory, 1, 44, 43));
        this.addSlotToContainer(new Slot(inventory, 2, 62, 25));
        this.addSlotToContainer(new Slot(inventory, 3, 62, 43));
        this.addSlotToContainer(new SlotBlockCondenserOutput(playerInventory.player,inventory, 4, 134, 35));
        this.addSlotToContainer(new SlotBlockCondenserUpgrades(inventory, 5, 8, 35));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 82 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 140));
        }
    }

    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBlockCondenser);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.condenseTime != this.tileBlockCondenser.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileBlockCondenser.getField(1));
            }

            if (this.condensingTime != this.tileBlockCondenser.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileBlockCondenser.getField(0));
            }

            if (this.totalCondensingTime != this.tileBlockCondenser.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileBlockCondenser.getField(2));
            }
        }

        this.condenseTime = this.tileBlockCondenser.getField(1);
        this.condensingTime = this.tileBlockCondenser.getField(0);
        this.totalCondensingTime = this.tileBlockCondenser.getField(2);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileBlockCondenser.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileBlockCondenser.isUsableByPlayer(playerIn);
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
