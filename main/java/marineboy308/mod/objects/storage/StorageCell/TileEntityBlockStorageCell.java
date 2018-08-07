package marineboy308.mod.objects.storage.StorageCell;

import marineboy308.mod.objects.storage.BatteryCell.ContainerBlockCell;
import marineboy308.mod.util.handlers.UpgradeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBlockStorageCell extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] OUTPUT_SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
														 10,11,12,13,14,15,16,17,18,19,
														 20,21,22,23,24,25,26,27,28,29,
														 30,31,32,33,34,35,36,37,38,39};
    private static final int[] INPUT_SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    													10,11,12,13,14,15,16,17,18,19,
    													20,21,22,23,24,25,26,27,28,29,
    													30,31,32,33,34,35,36,37,38,39};
    
    private NonNullList<ItemStack> tileEntityItemStacks = NonNullList.<ItemStack>withSize(43, ItemStack.EMPTY);
    
    private int storagelevel;
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
        return this.hasCustomName() ? this.customName : "container.cell_storage";
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
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBlockStorageCell.class, new String[] {"Items"}));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.tileEntityItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.tileEntityItemStacks);
        this.storagelevel = compound.getInteger("StorageLevel");
        
        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("StorageLevel", this.storagelevel);
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
        	ItemStack u0 = this.tileEntityItemStacks.get(40);
        	ItemStack u1 = this.tileEntityItemStacks.get(41);
        	ItemStack u2 = this.tileEntityItemStacks.get(42);
        	
        	int templevel = 0;
        	
        	if (!u0.isEmpty()) {
        		if (UpgradeHandler.isItemStorageUpgrade(u0.getItem())) {
        			templevel += 1;
        		}
        	}
        	if (!u1.isEmpty()) {
        		if (UpgradeHandler.isItemStorageUpgrade(u1.getItem())) {
        			templevel += 1;
        		}
        	}
        	if (!u2.isEmpty()) {
        		if (UpgradeHandler.isItemStorageUpgrade(u2.getItem())) {
        			templevel += 1;
        		}
        	}
        	
        	this.storagelevel = templevel;
        	templevel = 0;
        	flag = true;
        }
        
        if (flag) {
        	this.markDirty();
        }
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
    
    @SideOnly(Side.CLIENT)
    public static int getStorageLevel(IInventory inventory) {
    	return inventory.getField(0);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
    	if (index < 40) {
    		return true;
    	}
        return UpgradeHandler.isItemStorageUpgrade(stack.getItem());
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
        return "rusticrefining:cell_storage";
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
                return this.storagelevel;
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
                this.storagelevel = value;
                break;
        }
    }

    @Override
    public int getFieldCount()
    {
        return 1;
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
