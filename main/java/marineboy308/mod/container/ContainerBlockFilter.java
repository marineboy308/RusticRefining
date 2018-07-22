	package marineboy308.mod.container;

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

	private final IInventory tileBlockFilter;
    private int filterTime;
    private int totalFilterTime;
    private int filteringTime;

    public ContainerBlockFilter(InventoryPlayer playerInventory, IInventory inventory)
    {
        this.tileBlockFilter = inventory;
        this.addSlotToContainer(new Slot(inventory, 0, 44, 26));
        this.addSlotToContainer(new Slot(inventory, 1, 116, 26));
        this.addSlotToContainer(new Slot(inventory, 2, 134, 26));
        this.addSlotToContainer(new Slot(inventory, 3, 8, 26));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 66 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 124));
        }
    }

    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBlockFilter);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.filterTime != this.tileBlockFilter.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileBlockFilter.getField(1));
            }

            if (this.filteringTime != this.tileBlockFilter.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileBlockFilter.getField(0));
            }

            if (this.totalFilterTime != this.tileBlockFilter.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileBlockFilter.getField(2));
            }
        }

        this.filterTime = this.tileBlockFilter.getField(1);
        this.filteringTime = this.tileBlockFilter.getField(0);
        this.totalFilterTime = this.tileBlockFilter.getField(2);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileBlockFilter.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileBlockFilter.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 3 || index == 2 || index == 1 )
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, false))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 0) {
                if (index >= 4 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 4, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
