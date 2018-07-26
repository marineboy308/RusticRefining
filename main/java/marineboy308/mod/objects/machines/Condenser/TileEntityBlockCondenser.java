package marineboy308.mod.objects.machines.Condenser;

import java.util.Random;

import marineboy308.mod.init.ItemInit;
import marineboy308.mod.recipes.CondenserRecipes;
import marineboy308.mod.util.handlers.UpgradeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBlockCondenser extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] OUTPUT_SLOTS = new int[] {4};
    private static final int[] INPUT_SLOTS = new int[] {0, 1, 2, 3};
    
    private NonNullList<ItemStack> condenserItemStacks = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
    
    private int level;
    
    private int condensingTime;
    private int condenseTime;
    private int totalCondensingTime;
    private String condenserCustomName;

    @Override
    public int getSizeInventory()
    {
        return this.condenserItemStacks.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.condenserItemStacks)
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
        return this.condenserItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.condenserItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.condenserItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.condenserItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.condenserItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalCondensingTime = this.getCondenseTime();
            this.condenseTime = 0;
            this.markDirty();
        }
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.condenserCustomName : "container.condenser";
    }

    @Override
    public boolean hasCustomName()
    {
        return this.condenserCustomName != null && !this.condenserCustomName.isEmpty();
    }

    public void setCustomInventoryName(String name)
    {
        this.condenserCustomName = name;
    }

    public static void registerFixes(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBlockCondenser.class, new String[] {"Items"}));
    }
    
    public static int getLevel(IInventory inventory) {
    	return inventory.getField(3);
    }
    
    public static void setLevel(IInventory inventory) {
    	inventory.setField(3, inventory.getField(3)+1);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.condenserItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.condenserItemStacks);
        this.condensingTime = compound.getInteger("CondensingTime");
        this.condenseTime = compound.getInteger("CondenseTime");
        this.totalCondensingTime = compound.getInteger("CondensingTimeTotal");
        this.level = compound.getInteger("Level");

        if (compound.hasKey("CustomName", 8))
        {
            this.condenserCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("CondensingTime", (short)this.condensingTime);
        compound.setInteger("CondenseTime", (short)this.condenseTime);
        compound.setInteger("CondensingTimeTotal", (short)this.totalCondensingTime);
        compound.setInteger("Level", (short)this.level);
        ItemStackHelper.saveAllItems(compound, this.condenserItemStacks);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.condenserCustomName);
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
        return 64;
    }

    public boolean isCondensing()
    {
        return this.condensingTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isCondensing(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    @Override
    public void update()
    {
        boolean flag = this.isCondensing();
        boolean flag1 = false;

        if (this.isCondensing()) {
            --this.condensingTime;
        }

        if (!this.world.isRemote) {
            ItemStack input1 = this.condenserItemStacks.get(0);
            ItemStack input2 = this.condenserItemStacks.get(1);
            ItemStack input3 = this.condenserItemStacks.get(2);
            ItemStack input4 = this.condenserItemStacks.get(3);

            if (this.isCondensing() || !input1.isEmpty() && !input2.isEmpty() && !input3.isEmpty() && !input4.isEmpty()) {
                if (!this.isCondensing() && this.canCondense()) {
                    this.condensingTime = this.getCondenseTime();

                    if (this.isCondensing()) {
                        flag1 = true;
                    }
                }

                if (this.isCondensing() && this.canCondense())
                {
                    ++this.condenseTime;

                    if (this.condenseTime == this.totalCondensingTime)
                    {
                        this.condenseTime = 0;
                        this.totalCondensingTime = this.getCondenseTime();
                        this.condenseItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.condenseTime = 0;
                }
            }
            else if (!this.isCondensing() && this.condenseTime > 0)
            {
                this.condenseTime = MathHelper.clamp(this.condenseTime - 2, 0, this.totalCondensingTime);
            }

            if (flag != this.isCondensing())
            {
                flag1 = true;
                BlockCondenser.setState(this.isCondensing(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int getCondenseTime()
    {
    	int time = 600;
    	Item item = this.condenserItemStacks.get(5).getItem();
    	if(UpgradeHandler.isItemUpgrade(item)) {
    		if(item == ItemInit.UPGRADE_SPEED_1) {
        		return (int)((float)time * 0.75);
        	} else if(item == ItemInit.UPGRADE_SPEED_2) {
        		return (int)((float)time * 0.5);
        	} else if(item == ItemInit.UPGRADE_SPEED_3) {
        		return (int)((float)time * 0.25);
        	} else {
        		return time;
        	}
    	}
        return time;
    }
    
    public int upgradeOutput(int amount) {
    	Item item = this.condenserItemStacks.get(5).getItem();
    	if(UpgradeHandler.isItemUpgrade(item)) {
    		if(item == ItemInit.UPGRADE_DOUBLE_1) {
        		return amount + (int)((float)amount * 0.3);
        	} else if(item == ItemInit.UPGRADE_DOUBLE_2) {
        		return amount + (int)((float)amount * 0.6);
        	} else if(item == ItemInit.UPGRADE_DOUBLE_3) {
        		return amount + (int)((float)amount * 0.9);
        	} else {
        		return amount;
        	}
    	}
    	return amount;
    }

    private boolean canCondense() {
        if (((ItemStack)this.condenserItemStacks.get(0)).isEmpty()) {
            return false;
        } else {
        	ItemStack[] stacks = new ItemStack[] {this.condenserItemStacks.get(0),this.condenserItemStacks.get(1),this.condenserItemStacks.get(2),this.condenserItemStacks.get(3)};
            ItemStack result = CondenserRecipes.instance().getCondensingResult(stacks);

            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack output = this.condenserItemStacks.get(4);
                
                if (!output.isEmpty() && output.getItem() != result.getItem()) {
                	return false;
            	} else if (output.isEmpty() || output.isItemEqual(result)) {
                    return true;
                } else if (output.getCount() + result.getCount() <= this.getInventoryStackLimit() && output.getCount() + result.getCount() <= output.getMaxStackSize()) {       
                    return true;
                } else {
                    return output.getCount() + result.getCount() <= result.getMaxStackSize();
                }
            }
        }
    }

    public void condenseItem() {
        if (this.canCondense()) {
            ItemStack input1 = this.condenserItemStacks.get(0);
            ItemStack input2 = this.condenserItemStacks.get(1);
            ItemStack input3 = this.condenserItemStacks.get(2);
            ItemStack input4 = this.condenserItemStacks.get(3);
            ItemStack output = this.condenserItemStacks.get(4);
            ItemStack[] inputs = new ItemStack[] {input1,input2,input3,input4};
        	ItemStack result1 = CondenserRecipes.instance().getCondensingResult(inputs);
            
            ItemStack result = result1.copy();
            
            Random rand = new Random();
            
            int resultamount = rand.nextInt(upgradeOutput(result.getCount()));
            
            if(resultamount == 0) {
            	resultamount = 1;
            }
            
            result.setCount(resultamount);
            
            if (output.getCount() + result.getCount() <= this.getInventoryStackLimit() && 
            		output.getCount() + result.getCount() <= output.getMaxStackSize()) {
            	
            	if (output.isEmpty()) {
	                this.condenserItemStacks.set(4, result.copy());
	            } else if (output.getItem() == result.getItem()) {
	            	output.grow(result.getCount());
	            }

	            input1.shrink(1);
	            input2.shrink(1);
	            input3.shrink(1);
	            input4.shrink(1);
            }
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
    
    public boolean canItemBeCondensed() {
    	ItemStack[] inputs = new ItemStack[] {this.condenserItemStacks.get(0),this.condenserItemStacks.get(1),this.condenserItemStacks.get(2),this.condenserItemStacks.get(3)};
    	ItemStack result = CondenserRecipes.instance().getCondensingResult(inputs);
    	if (!result.isEmpty()) {
    		return true;
    	}
    	return false;
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean canItemBeCondensed(ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4) {
    	ItemStack[] inputs = new ItemStack[] {input1,input2,input3,input4};
    	ItemStack result = CondenserRecipes.instance().getCondensingResult(inputs);
    	if (!result.isEmpty()) {
    		return true;
    	}
    	return false;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
    	Item item = this.condenserItemStacks.get(5).getItem();
        if (index == 4) {
            return false;
        } else if(UpgradeHandler.isItemUpgrade(item)) {
        	return true;
        } else {
            return canItemBeCondensed();
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.DOWN) {
            return OUTPUT_SLOTS;
        } else if (side == EnumFacing.WEST) {
        	return INPUT_SLOTS;
        } else {
            return new int[] {};
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
        if (direction == EnumFacing.DOWN && (index != 0 && index != 4))
        {
            return true;
        }

        return false;
    }

    @Override
    public String getGuiID()
    {
        return "rusticrefining:condenser";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerBlockCondenser(playerInventory, this);
    }

    @Override
    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.condensingTime;
            case 1:
                return this.condenseTime;
            case 2:
                return this.totalCondensingTime;
            case 3:
            	return this.level;
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
                this.condensingTime = value;
                break;
            case 1:
                this.condenseTime = value;
                break;
            case 2:
                this.totalCondensingTime = value;
            case 3:
            	this.level = value;
        }
    }

    @Override
    public int getFieldCount()
    {
        return 4;
    }

    @Override
    public void clear()
    {
        this.condenserItemStacks.clear();
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
