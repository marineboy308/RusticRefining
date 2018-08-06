package marineboy308.mod.objects.storage.BatteryCell;

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
        
        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.tileEntityItemStacks);
        
        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }
    
    public void loadFromNbt(NBTTagCompound compound)
    {
        this.tileEntityItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (compound.hasKey("Items", 9)) {
            ItemStackHelper.loadAllItems(compound, this.tileEntityItemStacks);
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound saveToNbt(NBTTagCompound compound)
    {
        ItemStackHelper.saveAllItems(compound, this.tileEntityItemStacks, false);

        if (this.hasCustomName()) {
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
	        		this.batteryCharge0 = ((ItemBattery)b0.getItem()).getItemEnergy(b0);
	        		this.batteryMaxCharge0 = ((ItemBattery)b0.getItem()).getItemMaxEnergy(b0);
	        		flag = true;
	        	}
        	} else {
        		this.batteryCharge0 = 0;
        		this.batteryMaxCharge0 = 0;
        		flag = true;
        	}
        	
        	if (!b1.isEmpty()) {
	        	if (b1.getItem() instanceof ItemBattery) {
	        		this.batteryCharge1 = ((ItemBattery)b1.getItem()).getItemEnergy(b1);
	        		this.batteryMaxCharge1 = ((ItemBattery)b1.getItem()).getItemMaxEnergy(b1);
	        		flag = true;
	        	}
        	} else {
        		this.batteryCharge1 = 0;
        		this.batteryMaxCharge1 = 0;
        		flag = true;
        	}
        	
        	if (!b2.isEmpty()) {
	        	if (b2.getItem() instanceof ItemBattery) {
	        		this.batteryCharge2 = ((ItemBattery)b2.getItem()).getItemEnergy(b2);
	        		this.batteryMaxCharge2 = ((ItemBattery)b2.getItem()).getItemMaxEnergy(b2);
	        		flag = true;
	        	}
        	} else {
        		this.batteryCharge2 = 0;
        		this.batteryMaxCharge2 = 0;
        		flag = true;
        	}
        	
        	if (!b3.isEmpty()) {
	        	if (b3.getItem() instanceof ItemBattery) {
	        		this.batteryCharge3 = ((ItemBattery)b3.getItem()).getItemEnergy(b3);
	        		this.batteryMaxCharge3 = ((ItemBattery)b3.getItem()).getItemMaxEnergy(b3);
	        		flag = true;
	        	}
        	} else {
        		this.batteryCharge3 = 0;
        		this.batteryMaxCharge3 = 0;
        		flag = true;
        	}
        	
        	if (!b4.isEmpty()) {
	        	if (b4.getItem() instanceof ItemBattery) {
	        		this.batteryCharge4 = ((ItemBattery)b4.getItem()).getItemEnergy(b4);
	        		this.batteryMaxCharge4 = ((ItemBattery)b4.getItem()).getItemMaxEnergy(b4);
	        		flag = true;
	        	}
        	} else {
        		this.batteryCharge4 = 0;
        		this.batteryMaxCharge4 = 0;
        		flag = true;
        	}
        	
        	if (!b5.isEmpty()) {
	        	if (b5.getItem() instanceof ItemBattery) {
	        		this.batteryCharge5 = ((ItemBattery)b5.getItem()).getItemEnergy(b5);
	        		this.batteryMaxCharge5 = ((ItemBattery)b5.getItem()).getItemMaxEnergy(b5);
	        		flag = true;
	        	}
        	} else {
        		this.batteryCharge5 = 0;
        		this.batteryMaxCharge5 = 0;
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
                return this.batteryCharge0;
            case 1:
            	return this.batteryMaxCharge0;
            case 2:
                return this.batteryCharge1;
            case 3:
            	return this.batteryMaxCharge1;
            case 4:
                return this.batteryCharge2;
            case 5:
            	return this.batteryMaxCharge2;
            case 6:
                return this.batteryCharge3;
            case 7:
            	return this.batteryMaxCharge3;
            case 8:
                return this.batteryCharge4;
            case 9:
            	return this.batteryMaxCharge4;
            case 10:
                return this.batteryCharge5;
            case 11:
            	return this.batteryMaxCharge5;
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
                this.batteryCharge0 = value;
                break;
            case 1:
                this.batteryMaxCharge0 = value;
                break;
            case 2:
                this.batteryCharge1 = value;
                break;
            case 3:
                this.batteryMaxCharge1 = value;
                break;
            case 4:
                this.batteryCharge2 = value;
                break;
            case 5:
                this.batteryMaxCharge2 = value;
                break;
            case 6:
                this.batteryCharge3 = value;
                break;
            case 7:
                this.batteryMaxCharge3 = value;
                break;
            case 8:
                this.batteryCharge4 = value;
                break;
            case 9:
                this.batteryMaxCharge4 = value;
                break;
            case 10:
                this.batteryCharge5 = value;
                break;
            case 11:
                this.batteryMaxCharge5 = value;
                break;
        }
    }

    @Override
    public int getFieldCount()
    {
        return 0;//12;
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
