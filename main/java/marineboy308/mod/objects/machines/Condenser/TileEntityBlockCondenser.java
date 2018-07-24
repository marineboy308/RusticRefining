package marineboy308.mod.objects.machines.Condenser;

import java.util.Random;

import marineboy308.mod.init.ItemInit;
import marineboy308.mod.objects.machines.MaterialFilter.BlockFilter;
import marineboy308.mod.objects.machines.MaterialFilter.ContainerBlockFilter;
import marineboy308.mod.recipes.CondenserRecipes;
import marineboy308.mod.recipes.FilterRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

    public static void registerFixesFilter(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBlockCondenser.class, new String[] {"Items"}));
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

        if (compound.hasKey("CustomName", 8))
        {
            this.condenserCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("FilteringTime", (short)this.condensingTime);
        compound.setInteger("FilterTime", (short)this.condenseTime);
        compound.setInteger("FilterTimeTotal", (short)this.totalCondensingTime);
        ItemStackHelper.saveAllItems(compound, this.condenserItemStacks);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.condenserCustomName);
        }

        return compound;
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
            ItemStack itemstack = this.condenserItemStacks.get(0);

            if (this.isCondensing() || !itemstack.isEmpty()) {
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
                        this.filterItem();
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
                BlockFilter.setState(this.isCondensing(), this.world, this.pos);
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
    	Item item = this.condenserItemStacks.get(3).getItem();
    	if(isItemUpgrade()) {
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
    	Item item = this.condenserItemStacks.get(3).getItem();
    	if(isItemUpgrade()) {
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
            ItemStack result = FilterRecipes.instance().getFilteringResult(this.condenserItemStacks.get(0));
            ItemStack chanceitem = FilterRecipes.instance().getFilteringChanceItemResult(this.condenserItemStacks.get(0));

            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack output1 = this.condenserItemStacks.get(1); /* Output For Result 1 */
                ItemStack output2 = this.condenserItemStacks.get(2); /* Output For Result 2 [Chance Item] */
                
                if (!output1.isEmpty() && output1.getItem() != result.getItem() || !output2.isEmpty() && output2.getItem() != chanceitem.getItem()) {
                	return false;
            	} else if ((output1.isEmpty() && output2.isEmpty()) || (output1.isEmpty() && output2.isItemEqual(chanceitem)) || (output1.isItemEqual(result) && output2.isEmpty())) {
                    return true;
                } else if (output1.getCount() + result.getCount() <= this.getInventoryStackLimit() && output1.getCount() + result.getCount() <= output1.getMaxStackSize() && output2.getCount() + result.getCount() <= this.getInventoryStackLimit() && output2.getCount() + result.getCount() <= output2.getMaxStackSize()) {       
                    return true;
                } else {
                    return output1.getCount() + result.getCount() <= result.getMaxStackSize() && output2.getCount() + result.getCount() <= result.getMaxStackSize();
                }
            }
        }
    }

    public void filterItem() {
        if (this.canCondense()) {
            ItemStack input = this.condenserItemStacks.get(0);
            ItemStack output1 = this.condenserItemStacks.get(1);
            ItemStack output2 = this.condenserItemStacks.get(2);
            ItemStack result1 = FilterRecipes.instance().getFilteringResult(this.condenserItemStacks.get(0));
            ItemStack result2 = FilterRecipes.instance().getFilteringChanceItemResult(this.condenserItemStacks.get(0));
            
            ItemStack result = result1.copy();
            
            float chance = FilterRecipes.instance().getFilteringChanceResult(this.condenserItemStacks.get(0));
            
            if (chance > 1.0F) {
            	chance = 1.0F;
            } else if (chance < 0.0F) {
            	chance = 0.0F;
            }
            
            Random rand = new Random();
            
            float chancevalue = rand.nextFloat();
            int resultamount = rand.nextInt(upgradeOutput(result.getCount()+1));
            
            if(resultamount == 0) {
            	resultamount = 1;
            }
            
            result.setCount(resultamount);
            
            if (chancevalue < chance ? output1.getCount() + result.getCount() <= this.getInventoryStackLimit() && 
            		output1.getCount() + result.getCount() <= output1.getMaxStackSize() && 
            		output2.getCount() + result2.getCount() <= this.getInventoryStackLimit() && 
            		output2.getCount() + result2.getCount() <= output2.getMaxStackSize() : 
            		output1.getCount() + result.getCount() <= this.getInventoryStackLimit() && 
            		output1.getCount() + result.getCount() <= output1.getMaxStackSize()) {
            	
            	if (output1.isEmpty()) {
	                this.condenserItemStacks.set(1, result.copy());
	            } else if (output1.getItem() == result.getItem()) {
	            	output1.grow(result.getCount());
	            }
	
	            if (chancevalue < chance) {
	            	if (output2.isEmpty()) {
	            		this.condenserItemStacks.set(2, result2.copy());
	            	} else if (output2.getItem() == result2.getItem()) {
	            		output2.grow(result2.getCount());
	            	}
	            }
	            input.shrink(1);
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
    
    public boolean isItemUpgrade() {
    	Item item = this.condenserItemStacks.get(3).getItem();
    	if(item == ItemInit.UPGRADE_SPEED_1) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_SPEED_2) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_SPEED_3) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_DOUBLE_1) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_DOUBLE_2) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_DOUBLE_3) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_CHANCE_1) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_CHANCE_2) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_CHANCE_3) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_CHANCE_4) {
    		return true;
    	}
    	return false;
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean isItemUpgrade(ItemStack stack) {
    	Item item = stack.getItem();
    	if(item == ItemInit.UPGRADE_SPEED_1) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_SPEED_2) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_SPEED_3) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_DOUBLE_1) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_DOUBLE_2) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_DOUBLE_3) {
    		return true;
    	}
    	return false;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if (index != 0 && index != 4) {
            return false;
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
        return new ContainerBlockFilter(playerInventory, this);
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
        }
    }

    @Override
    public int getFieldCount()
    {
        return 3;
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
