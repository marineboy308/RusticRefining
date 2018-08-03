package marineboy308.mod.objects.storage.BatteryCell;

import java.util.Arrays;

import marineboy308.mod.objects.items.ItemBattery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityBlockCell extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] OUTPUT_SLOTS = new int[] {};
    private static final int[] INPUT_SLOTS = new int[] {0,1,2,3,4,5};
    
    private NonNullList<ItemStack> tileEntityItemStacks = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
    
    private int[] batteryCharges = new int[] {0,0,0,0,0,0};
    private int[] batteryMaxCharges = new int[] {0,0,0,0,0,0};
    private String customName;

    @Override
    public int getSizeInventory()
    {
        return this.tileEntityItemStacks.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.tileEntityItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.tileEntityItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.tileEntityItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.tileEntityItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.tileEntityItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.cell_battery";
    }

    @Override
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomInventoryName(String name)
    {
        this.customName = name;
    }

    public static void registerFixes(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBlockCell.class, new String[] {"Items"}));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.tileEntityItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.tileEntityItemStacks);
        this.batteryCharges = compound.getIntArray("Charges");
        this.batteryMaxCharges = compound.getIntArray("MaxCharges");
        
        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setIntArray("Charges", this.batteryCharges);
        compound.setIntArray("MaxCharges", this.batteryMaxCharges);
        ItemStackHelper.saveAllItems(compound, this.tileEntityItemStacks);
        
        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        int metadata = getBlockMetadata();
        return new SPacketUpdateTileEntity(pos, metadata, nbt);
    }
 
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }
 
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return nbt;
    }
 
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }
 
    @Override
    public NBTTagCompound getTileData() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return nbt;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public void update()
    {
    	boolean flag = false;

        if (!this.world.isRemote) {
        	ItemStack b0 = this.tileEntityItemStacks.get(0);
        	ItemStack b1 = this.tileEntityItemStacks.get(1);
        	ItemStack b2 = this.tileEntityItemStacks.get(2);
        	ItemStack b3 = this.tileEntityItemStacks.get(3);
        	ItemStack b4 = this.tileEntityItemStacks.get(4);
        	ItemStack b5 = this.tileEntityItemStacks.get(5);
        	
        	if (!b0.isEmpty()) {
	        	if (b0.getItem() instanceof ItemBattery) {
	        		Arrays.fill(this.batteryCharges, 0, 0, ((ItemBattery)b0.getItem()).getItemEnergy(b0));
	        		Arrays.fill(this.batteryMaxCharges, 0, 0, ((ItemBattery)b0.getItem()).getItemMaxEnergy(b0));
	        		flag = true;
	        	}
        	} else {
        		Arrays.fill(this.batteryCharges, 0, 0, 0);
        		Arrays.fill(this.batteryMaxCharges, 0, 0, 0);
        		flag = true;
        	}
        	
        	if (!b1.isEmpty()) {
	        	if (b1.getItem() instanceof ItemBattery) {
	        		Arrays.fill(this.batteryCharges, 1, 1, ((ItemBattery)b1.getItem()).getItemEnergy(b1));
	        		Arrays.fill(this.batteryMaxCharges, 1, 1, ((ItemBattery)b1.getItem()).getItemMaxEnergy(b1));
	        		flag = true;
	        	}
        	} else {
        		Arrays.fill(this.batteryCharges, 1, 1, 0);
        		Arrays.fill(this.batteryMaxCharges, 1, 1, 0);;
        		flag = true;
        	}
        	
        	if (!b2.isEmpty()) {
	        	if (b2.getItem() instanceof ItemBattery) {
	        		Arrays.fill(this.batteryCharges, 2, 2, ((ItemBattery)b2.getItem()).getItemEnergy(b2));
	        		Arrays.fill(this.batteryMaxCharges, 2, 2, ((ItemBattery)b2.getItem()).getItemMaxEnergy(b2));
	        		flag = true;
	        	}
        	} else {
        		Arrays.fill(this.batteryCharges, 2, 2, 0);
        		Arrays.fill(this.batteryMaxCharges, 2, 2, 0);
        		flag = true;
        	}
        	
        	if (!b3.isEmpty()) {
	        	if (b3.getItem() instanceof ItemBattery) {
	        		Arrays.fill(this.batteryCharges, 3, 3, ((ItemBattery)b3.getItem()).getItemEnergy(b3));
	        		Arrays.fill(this.batteryMaxCharges, 3, 3, ((ItemBattery)b3.getItem()).getItemMaxEnergy(b3));
	        		flag = true;
	        	}
        	} else {
        		Arrays.fill(this.batteryCharges, 3, 3, 0);
        		Arrays.fill(this.batteryMaxCharges, 3, 3, 0);
        		flag = true;
        	}
        	
        	if (!b4.isEmpty()) {
	        	if (b4.getItem() instanceof ItemBattery) {
	        		Arrays.fill(this.batteryCharges, 4, 4, ((ItemBattery)b4.getItem()).getItemEnergy(b4));
	        		Arrays.fill(this.batteryMaxCharges, 4, 4, ((ItemBattery)b4.getItem()).getItemMaxEnergy(b4));
	        		flag = true;
	        	}
        	} else {
        		Arrays.fill(this.batteryCharges, 4, 4, 0);
        		Arrays.fill(this.batteryMaxCharges, 4, 4, 0);
        		flag = true;
        	}
        	
        	if (!b5.isEmpty()) {
	        	if (b5.getItem() instanceof ItemBattery) {
	        		Arrays.fill(this.batteryCharges, 5, 5, ((ItemBattery)b5.getItem()).getItemEnergy(b5));
	        		Arrays.fill(this.batteryMaxCharges, 5, 5, ((ItemBattery)b5.getItem()).getItemMaxEnergy(b5));
	        		flag = true;
	        	}
        	} else {
        		Arrays.fill(this.batteryCharges, 5, 5, 0);
        		Arrays.fill(this.batteryMaxCharges, 5, 5, 0);
        		flag = true;
        	}
        }
        
        if (flag) {
        	this.markDirty();
        }
    }
    
    public static boolean isItemBattery(ItemStack stack)
    {
        return stack.getItem() instanceof ItemBattery;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return isItemBattery(stack);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.DOWN) {
            return OUTPUT_SLOTS;
        } else {
        	return INPUT_SLOTS;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }
    
    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return false;
    }

    @Override
    public String getGuiID()
    {
        return "rusticrefining:cell_battery";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerBlockCell(playerInventory, this);
    }

    @Override
    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.batteryCharges[0];
            case 1:
            	return this.batteryMaxCharges[0];
            case 2:
                return this.batteryCharges[1];
            case 3:
            	return this.batteryMaxCharges[1];
            case 4:
                return this.batteryCharges[2];
            case 5:
            	return this.batteryMaxCharges[2];
            case 6:
                return this.batteryCharges[3];
            case 7:
            	return this.batteryMaxCharges[3];
            case 8:
                return this.batteryCharges[4];
            case 9:
            	return this.batteryMaxCharges[4];
            case 10:
                return this.batteryCharges[5];
            case 11:
            	return this.batteryMaxCharges[5];
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.batteryCharges[0] = value;
                break;
            case 1:
                this.batteryMaxCharges[0] = value;
                break;
            case 2:
                this.batteryCharges[1] = value;
                break;
            case 3:
                this.batteryMaxCharges[1] = value;
                break;
            case 4:
                this.batteryCharges[2] = value;
                break;
            case 5:
                this.batteryMaxCharges[2] = value;
                break;
            case 6:
                this.batteryCharges[3] = value;
                break;
            case 7:
                this.batteryMaxCharges[3] = value;
                break;
            case 8:
                this.batteryCharges[4] = value;
                break;
            case 9:
                this.batteryMaxCharges[4] = value;
                break;
            case 10:
                this.batteryCharges[5] = value;
                break;
            case 11:
                this.batteryMaxCharges[5] = value;
                break;
        }
    }

    @Override
    public int getFieldCount()
    {
        return 12;
    }

    @Override
    public void clear()
    {
        this.tileEntityItemStacks.clear();
    }

    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;
        return super.getCapability(capability, facing);
    }
}
