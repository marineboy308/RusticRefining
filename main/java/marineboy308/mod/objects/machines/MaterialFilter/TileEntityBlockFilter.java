package marineboy308.mod.objects.machines.MaterialFilter;

import java.util.Random;

import marineboy308.mod.init.ItemInit;
import marineboy308.mod.recipes.FilterRecipes;
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

public class TileEntityBlockFilter extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] OUTPUT_SLOTS = new int[] {1, 2};
    private static final int[] INPUT_SLOTS = new int[] {0};
    
    private NonNullList<ItemStack> filterItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
    
    private int filteringTime;
    private int filterTime;
    private int totalFilterTime;
    private String filterCustomName;

    @Override
    public int getSizeInventory()
    {
        return this.filterItemStacks.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.filterItemStacks)
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
        return this.filterItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.filterItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.filterItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.filterItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.filterItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalFilterTime = this.getFilterTime();
            this.filterTime = 0;
            this.markDirty();
        }
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.filterCustomName : "container.material_filter";
    }

    @Override
    public boolean hasCustomName()
    {
        return this.filterCustomName != null && !this.filterCustomName.isEmpty();
    }

    public void setCustomInventoryName(String name)
    {
        this.filterCustomName = name;
    }

    public static void registerFixes(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBlockFilter.class, new String[] {"Items"}));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.filterItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.filterItemStacks);
        this.filteringTime = compound.getInteger("FilteringTime");
        this.filterTime = compound.getInteger("FilterTime");
        this.totalFilterTime = compound.getInteger("FilterTimeTotal");

        if (compound.hasKey("CustomName", 8))
        {
            this.filterCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("FilteringTime", (short)this.filteringTime);
        compound.setInteger("FilterTime", (short)this.filterTime);
        compound.setInteger("FilterTimeTotal", (short)this.totalFilterTime);
        ItemStackHelper.saveAllItems(compound, this.filterItemStacks);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.filterCustomName);
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

    public boolean isFiltering()
    {
        return this.filteringTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isFiltering(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    @Override
    public void update()
    {
        boolean flag = this.isFiltering();
        boolean flag1 = false;

        if (this.isFiltering()) {
            --this.filteringTime;
        }

        if (!this.world.isRemote) {
            ItemStack itemstack = this.filterItemStacks.get(0);

            if (this.isFiltering() || !itemstack.isEmpty()) {
                if (!this.isFiltering() && this.canFilter()) {
                    this.filteringTime = this.getFilterTime();

                    if (this.isFiltering()) {
                        flag1 = true;
                    }
                }

                if (this.isFiltering() && this.canFilter())
                {
                    ++this.filterTime;

                    if (this.filterTime == this.totalFilterTime)
                    {
                        this.filterTime = 0;
                        this.totalFilterTime = this.getFilterTime();
                        this.filterItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.filterTime = 0;
                }
            }
            else if (!this.isFiltering() && this.filterTime > 0)
            {
                this.filterTime = MathHelper.clamp(this.filterTime - 2, 0, this.totalFilterTime);
            }

            if (flag != this.isFiltering())
            {
                flag1 = true;
                BlockFilter.setState(this.isFiltering(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int getFilterTime()
    {
    	int time = 200;
    	Item item = this.filterItemStacks.get(3).getItem();
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
    	Item item = this.filterItemStacks.get(3).getItem();
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

    private boolean canFilter() {
        if (((ItemStack)this.filterItemStacks.get(0)).isEmpty()) {
            return false;
        } else {
            ItemStack result = FilterRecipes.instance().getFilteringResult(this.filterItemStacks.get(0));
            ItemStack chanceitem = FilterRecipes.instance().getFilteringChanceItemResult(this.filterItemStacks.get(0));

            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack output1 = this.filterItemStacks.get(1); /* Output For Result 1 */
                ItemStack output2 = this.filterItemStacks.get(2); /* Output For Result 2 [Chance Item] */
                
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
        if (this.canFilter()) {
            ItemStack input = this.filterItemStacks.get(0);
            ItemStack output1 = this.filterItemStacks.get(1);
            ItemStack output2 = this.filterItemStacks.get(2);
            ItemStack result1 = FilterRecipes.instance().getFilteringResult(this.filterItemStacks.get(0));
            ItemStack result2 = FilterRecipes.instance().getFilteringChanceItemResult(this.filterItemStacks.get(0));
            
            ItemStack result = result1.copy();
            
            float chance = FilterRecipes.instance().getFilteringChanceResult(this.filterItemStacks.get(0));
            
            Item item = this.filterItemStacks.get(3).getItem();
            
            if(UpgradeHandler.isItemUpgrade(item)) {
            	if(item == ItemInit.UPGRADE_CHANCE_1) {
            		chance = chance + (chance * 0.1F);
            	} else if(item == ItemInit.UPGRADE_CHANCE_2) {
            		chance = chance + (chance * 0.2F);
            	} else if(item == ItemInit.UPGRADE_CHANCE_3) {
            		chance = chance + (chance * 0.4F);
            	} else if(item == ItemInit.UPGRADE_CHANCE_4) {
            		chance = chance + (chance * 0.8F);
            	}
            }
            
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
	                this.filterItemStacks.set(1, result.copy());
	            } else if (output1.getItem() == result.getItem()) {
	            	output1.grow(result.getCount());
	            }
	
	            if (chancevalue < chance) {
	            	if (output2.isEmpty()) {
	            		this.filterItemStacks.set(2, result2.copy());
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
    
    public boolean isItemFilterable() {
    	ItemStack result = FilterRecipes.instance().getFilteringResult(this.filterItemStacks.get(0));
    	if (!result.isEmpty()) {
    		return true;
    	}
    	return false;
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean isItemFilterable(ItemStack stack) {
    	ItemStack result = FilterRecipes.instance().getFilteringResult(stack);
    	if (!result.isEmpty()) {
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
            return isItemFilterable(stack);
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
        return "rusticrefining:material_filter";
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
                return this.filteringTime;
            case 1:
                return this.filterTime;
            case 2:
                return this.totalFilterTime;
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
                this.filteringTime = value;
                break;
            case 1:
                this.filterTime = value;
                break;
            case 2:
                this.totalFilterTime = value;
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
        this.filterItemStacks.clear();
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
