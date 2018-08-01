package marineboy308.mod.objects.machines.MaterialFilter;

import java.util.Random;

import marineboy308.mod.init.ItemInit;
import marineboy308.mod.objects.items.ItemBattery;
import marineboy308.mod.objects.items.ItemFuel;
import marineboy308.mod.recipes.FilterRecipes;
import marineboy308.mod.util.factories.EnergyEventFactory;
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
    
    private NonNullList<ItemStack> tileEntityItemStacks = NonNullList.<ItemStack>withSize(5, ItemStack.EMPTY);
    
    private int bakingTime;
    private int bakeTime;
    private int totalBakeTime;
    private String customName;
    
    private int energy;
    private int maxEnergy = 30000;
    private int drainWait;

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
        ItemStack itemstack = this.tileEntityItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.tileEntityItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalBakeTime = this.getBakeTime();
            this.bakeTime = 0;
            this.markDirty();
        }
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.material_filter";
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
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBlockFilter.class, new String[] {"Items"}));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.tileEntityItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.tileEntityItemStacks);
        this.bakingTime = compound.getInteger("FilteringTime");
        this.bakeTime = compound.getInteger("FilterTime");
        this.totalBakeTime = compound.getInteger("FilterTimeTotal");
        this.energy = compound.getInteger("Energy");
        
        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("FilteringTime", (short)this.bakingTime);
        compound.setInteger("FilterTime", (short)this.bakeTime);
        compound.setInteger("FilterTimeTotal", (short)this.totalBakeTime);
        compound.setInteger("Energy", (short)this.energy);
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
        return 64;
    }

    public boolean isBaking()
    {
        return this.bakingTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBaking(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    @Override
    public void update()
    {
        boolean flag = this.isBaking();
        boolean flag1 = false;

        if (this.isBaking()) {
            --this.bakingTime;
        }

        if (!this.world.isRemote) {
        	ItemStack fuel = this.tileEntityItemStacks.get(4);
        	
        	this.energy = MathHelper.clamp(this.energy, 0, this.maxEnergy);
            
            if (!fuel.isEmpty()) {
            	if (isItemFuel(fuel) && fuel.getItem() instanceof ItemFuel) {
            		int storedEnergy = getItemEnergyStored(fuel);
            		if (storedEnergy < 0) storedEnergy = 0;
            		if (this.energy + storedEnergy <= this.maxEnergy) {
    	        		if (drainWait == 0) {
    		        		int drainTime = EnergyEventFactory.getItemDrainTime(fuel);
    		        		drainWait += drainTime;
    		        	} else {
    		        		--this.drainWait;
    		        		if (this.drainWait == 0) {
    		        			this.energy = this.energy + storedEnergy;
    		        			fuel.shrink(1);
    		        			flag1 = true;
    		        		}
    		        	}
            		}
            	} else if (isItemFuel(fuel) && fuel.getItem() instanceof ItemBattery) {
            		int storedEnergy = getItemEnergyStored(fuel);
            		if (storedEnergy < 0) {
            			storedEnergy = 0;
            			((ItemBattery)fuel.getItem()).setItemEnergy(fuel,0);
            		}
            		else {
            			if (this.energy + ((ItemBattery)fuel.getItem()).getEnergyDrainAmount(fuel) <= this.maxEnergy && ((ItemBattery)fuel.getItem()).getItemEnergy(fuel) - ((ItemBattery)fuel.getItem()).getEnergyDrainAmount(fuel) >= 0) {
            				this.energy = this.energy + ((ItemBattery)fuel.getItem()).getEnergyDrainAmount(fuel);
            				((ItemBattery)fuel.getItem()).setItemEnergy(fuel,((ItemBattery)fuel.getItem()).getItemEnergy(fuel) - ((ItemBattery)fuel.getItem()).getEnergyDrainAmount(fuel));
            				flag1 = true;
            			} else if (this.energy + ((ItemBattery)fuel.getItem()).getEnergyDrainAmount(fuel) <= this.maxEnergy && ((ItemBattery)fuel.getItem()).iscreative) {
            				this.energy = this.energy + ((ItemBattery)fuel.getItem()).getEnergyDrainAmount(fuel);
            				flag1 = true;
            			}
            		}
            	} else {
            		drainWait = 0;
            	}
            } else {
            	drainWait = 0;
            }
        	
            ItemStack itemstack = this.tileEntityItemStacks.get(0);

            if (this.isBaking() || !itemstack.isEmpty()) {
                if (!this.isBaking() && this.canBake()) {
                    this.bakingTime = this.getBakeTime();

                    if (this.isBaking()) {
                        flag1 = true;
                    }
                }

                if (this.isBaking() && this.canBake())
                {
                    ++this.bakeTime;

                    if (this.bakeTime == this.totalBakeTime)
                    {
                        this.bakeTime = 0;
                        this.totalBakeTime = this.getBakeTime();
                        this.bakeItem();
                        flag1 = true;
                    }	
                }
                else
                {
                    this.bakeTime = 0;
                }
            }
            else if (!this.isBaking() && this.bakeTime > 0)
            {
                this.bakeTime = MathHelper.clamp(this.bakeTime - 2, 0, this.totalBakeTime);
            }

            if (flag != this.isBaking())
            {
                flag1 = true;
                BlockFilter.setState(this.isBaking(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int getBakeTime()
    {
    	int time = 200;
    	Item item = this.tileEntityItemStacks.get(3).getItem();
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
    
    public int getEnergyNeeded() {
    	return 1000;
    }
    
    public int upgradeOutput(int amount) {
    	Item item = this.tileEntityItemStacks.get(3).getItem();
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

    private boolean canBake() {
    	if (this.energy < getEnergyNeeded()) return false;
        if (((ItemStack)this.tileEntityItemStacks.get(0)).isEmpty()) {
            return false;
        } else {
            ItemStack result = FilterRecipes.instance().getFilteringResult(this.tileEntityItemStacks.get(0));
            ItemStack chanceitem = FilterRecipes.instance().getFilteringChanceItemResult(this.tileEntityItemStacks.get(0));

            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack output1 = this.tileEntityItemStacks.get(1); /* Output For Result 1 */
                ItemStack output2 = this.tileEntityItemStacks.get(2); /* Output For Result 2 [Chance Item] */
                
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

    public void bakeItem() {
        if (this.canBake()) {
            ItemStack input = this.tileEntityItemStacks.get(0);
            ItemStack output1 = this.tileEntityItemStacks.get(1);
            ItemStack output2 = this.tileEntityItemStacks.get(2);
            ItemStack result1 = FilterRecipes.instance().getFilteringResult(this.tileEntityItemStacks.get(0));
            ItemStack result2 = FilterRecipes.instance().getFilteringChanceItemResult(this.tileEntityItemStacks.get(0));
            
            ItemStack result = result1.copy();
            
            float chance = FilterRecipes.instance().getFilteringChanceResult(this.tileEntityItemStacks.get(0));
            
            Item item = this.tileEntityItemStacks.get(3).getItem();
            
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
	                this.tileEntityItemStacks.set(1, result.copy());
	            } else if (output1.getItem() == result.getItem()) {
	            	output1.grow(result.getCount());
	            }
	
	            if (chancevalue < chance) {
	            	if (output2.isEmpty()) {
	            		this.tileEntityItemStacks.set(2, result2.copy());
	            	} else if (output2.getItem() == result2.getItem()) {
	            		output2.grow(result2.getCount());
	            	}
	            }
	            input.shrink(1);
	            this.energy = this.energy - getEnergyNeeded();
            }
        }
    }
    
    public static int getItemEnergyStored(ItemStack stack) {
    	if (stack.isEmpty()) {
            return 0;
        } else {
        	int energy = EnergyEventFactory.getItemEnergyStored(stack);
            if (energy >= 0) return energy;
            return 0;
        }
    }
    
    public static boolean isItemFuel(ItemStack stack)
    {
        return getItemEnergyStored(stack) > 0;
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
    
    public boolean isItemBakeable() {
    	ItemStack result = FilterRecipes.instance().getFilteringResult(this.tileEntityItemStacks.get(0));
    	if (!result.isEmpty()) {
    		return true;
    	}
    	return false;
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean isItemBakeable(ItemStack stack) {
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
            return isItemBakeable(stack);
        }
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
        if (direction == EnumFacing.DOWN && (index != 0 && index != 4 && index != 5))
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
                return this.bakingTime;
            case 1:
                return this.bakeTime;
            case 2:
                return this.totalBakeTime;
            case 3:
            	return this.energy;
            case 4:
            	return this.maxEnergy;
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
                this.bakingTime = value;
                break;
            case 1:
                this.bakeTime = value;
                break;
            case 2:
                this.totalBakeTime = value;
            case 3:
                this.energy = value;
                break;
            case 4:
                this.maxEnergy = value;
        }
    }

    @Override
    public int getFieldCount()
    {
        return 5;
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
