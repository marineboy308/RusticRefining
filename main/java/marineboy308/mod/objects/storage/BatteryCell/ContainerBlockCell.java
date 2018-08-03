	package marineboy308.mod.objects.storage.BatteryCell;

import marineboy308.mod.objects.storage.BatteryCell.slots.SlotBlockCell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBlockCell extends Container {

	private final IInventory tileBlock;
	
	private int batteryCharge0;
    private int batteryMaxCharge0;
    private int batteryCharge1;
    private int batteryMaxCharge1;
    private int batteryCharge2;
    private int batteryMaxCharge2;
    private int batteryCharge3;
    private int batteryMaxCharge3;
    private int batteryCharge4;
    private int batteryMaxCharge4;
    private int batteryCharge5;
    private int batteryMaxCharge5;
    
    private int inventorysize = 6;

    public ContainerBlockCell(InventoryPlayer playerInventory, IInventory inventory)
    {
        this.tileBlock = inventory;
        this.addSlotToContainer(new SlotBlockCell(inventory, 0, 71, 22));
        this.addSlotToContainer(new SlotBlockCell(inventory, 1, 89, 22));
        this.addSlotToContainer(new SlotBlockCell(inventory, 2, 71, 40));
        this.addSlotToContainer(new SlotBlockCell(inventory, 3, 89, 40));
        this.addSlotToContainer(new SlotBlockCell(inventory, 4, 71, 58));
        this.addSlotToContainer(new SlotBlockCell(inventory, 5, 89, 58));

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

            if (this.batteryCharge0 != this.tileBlock.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileBlock.getField(0));
            }
            if (this.batteryMaxCharge0 != this.tileBlock.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileBlock.getField(1));
            }
            
            if (this.batteryCharge1 != this.tileBlock.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileBlock.getField(2));
            }
            if (this.batteryMaxCharge1 != this.tileBlock.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileBlock.getField(3));
            }
            
            if (this.batteryCharge2 != this.tileBlock.getField(4))
            {
                icontainerlistener.sendWindowProperty(this, 4, this.tileBlock.getField(4));
            }
            if (this.batteryMaxCharge2 != this.tileBlock.getField(5))
            {
                icontainerlistener.sendWindowProperty(this, 5, this.tileBlock.getField(5));
            }
            
            if (this.batteryCharge3 != this.tileBlock.getField(6))
            {
                icontainerlistener.sendWindowProperty(this, 6, this.tileBlock.getField(6));
            }
            if (this.batteryMaxCharge3 != this.tileBlock.getField(7))
            {
                icontainerlistener.sendWindowProperty(this, 7, this.tileBlock.getField(7));
            }
            
            if (this.batteryCharge4 != this.tileBlock.getField(8))
            {
                icontainerlistener.sendWindowProperty(this, 8, this.tileBlock.getField(8));
            }
            if (this.batteryMaxCharge4 != this.tileBlock.getField(9))
            {
                icontainerlistener.sendWindowProperty(this, 9, this.tileBlock.getField(9));
            }
            
            if (this.batteryCharge5 != this.tileBlock.getField(10))
            {
                icontainerlistener.sendWindowProperty(this, 10, this.tileBlock.getField(10));
            }
            if (this.batteryMaxCharge5 != this.tileBlock.getField(11))
            {
                icontainerlistener.sendWindowProperty(this, 11, this.tileBlock.getField(11));
            }
        }

        this.batteryCharge0 = this.tileBlock.getField(0);
        this.batteryCharge1 = this.tileBlock.getField(2);
        this.batteryCharge2 = this.tileBlock.getField(4);
        this.batteryCharge3 = this.tileBlock.getField(6);
        this.batteryCharge4 = this.tileBlock.getField(8);
        this.batteryCharge5 = this.tileBlock.getField(10);
        this.batteryMaxCharge0 = this.tileBlock.getField(1);
        this.batteryMaxCharge1 = this.tileBlock.getField(3);
        this.batteryMaxCharge2 = this.tileBlock.getField(5);
        this.batteryMaxCharge3 = this.tileBlock.getField(7);
        this.batteryMaxCharge4 = this.tileBlock.getField(9);
        this.batteryMaxCharge5 = this.tileBlock.getField(11);
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
