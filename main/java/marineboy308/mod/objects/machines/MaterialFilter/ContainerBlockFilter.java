	package marineboy308.mod.objects.machines.MaterialFilter;

import marineboy308.mod.objects.machines.MaterialFilter.slots.SlotBlockFilterFuel;
import marineboy308.mod.objects.machines.MaterialFilter.slots.SlotBlockFilterInput;
import marineboy308.mod.objects.machines.MaterialFilter.slots.SlotBlockFilterOutput;
import marineboy308.mod.objects.machines.MaterialFilter.slots.SlotBlockFilterUpgrades;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBlockFilter extends Container {

	private final IInventory tileBlock;
	
    private int filterTime;
    private int totalFilterTime;
    private int filteringTime;
    
    private int energy;
    private int maxEnergy;
    
    private int inventorysize = 5;

    public ContainerBlockFilter(InventoryPlayer playerInventory, IInventory inventory)
    {
        this.tileBlock = inventory;
        this.addSlotToContainer(new SlotBlockFilterInput(inventory, 0, 44, 8));
        this.addSlotToContainer(new SlotBlockFilterOutput(playerInventory.player,inventory, 1, 98, 31));
        this.addSlotToContainer(new SlotBlockFilterOutput(playerInventory.player,inventory, 2, 44, 53));
        this.addSlotToContainer(new SlotBlockFilterUpgrades(inventory, 3, 116, 62));
        this.addSlotToContainer(new SlotBlockFilterFuel(inventory, 4, 152, 62));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
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

            if (this.filterTime != this.tileBlock.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileBlock.getField(1));
            }

            if (this.filteringTime != this.tileBlock.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileBlock.getField(0));
            }

            if (this.totalFilterTime != this.tileBlock.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileBlock.getField(2));
            }
            
            if (this.energy != this.tileBlock.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileBlock.getField(3));
            }
            
            if (this.maxEnergy != this.tileBlock.getField(4))
            {
                icontainerlistener.sendWindowProperty(this, 4, this.tileBlock.getField(4));
            }
        }

        this.filterTime = this.tileBlock.getField(1);
        this.filteringTime = this.tileBlock.getField(0);
        this.totalFilterTime = this.tileBlock.getField(2);
        this.energy = this.tileBlock.getField(3);
        this.maxEnergy = this.tileBlock.getField(4);
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
