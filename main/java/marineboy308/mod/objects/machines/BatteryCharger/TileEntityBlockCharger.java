package marineboy308.mod.objects.machines.BatteryCharger;

import marineboy308.mod.init.ItemInit;
import marineboy308.mod.objects.items.ItemBattery;
import marineboy308.mod.objects.items.ItemFuel;
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

public class TileEntityBlockCharger extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] OUTPUT_SLOTS = new int[] {};
    private static final int[] INPUT_SLOTS = new int[] {0};
    
    private NonNullList<ItemStack> tileEntityItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    
    private int charging;
    private int chargeWait;
    private int totalChargeWait;
    private String customName;
    
    private int energy;
    private int maxEnergy = 100000;
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
        this.tileEntityItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.charger_battery";
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
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBlockCharger.class, new String[] {"Items"}));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.tileEntityItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.tileEntityItemStacks);
        this.charging = compound.getInteger("Charging");
        this.chargeWait = compound.getInteger("ChargeWait");
        this.totalChargeWait = compound.getInteger("TotalChargeWait");
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
        compound.setInteger("Charging", (short)this.charging);
        compound.setInteger("ChargeWait", (short)this.chargeWait);
        compound.setInteger("TotalChargeWait", (short)this.totalChargeWait);
        compound.setInteger("Energy", this.energy);
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

    public boolean isCharging()
    {
        return this.charging == 1;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isCharging(IInventory inventory)
    {
        return inventory.getField(0) == 1;
    }

    @Override
    public void update()
    {
    	boolean flag = false;
        boolean flag1 = false;
        
        if (isCharging()) {
        	flag = true;
        }

        if (!this.world.isRemote) {
        	ItemStack fuel = this.tileEntityItemStacks.get(2);
        	
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
            	} else if (isItemBattery(fuel)) {
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
            		this.drainWait = 0;
            	}
            } else {
            	this.drainWait = 0;
            }
            
            ItemStack battery = this.tileEntityItemStacks.get(0);
            
            if (!battery.isEmpty()) {
	            if (isItemBattery(battery)) {
	            	if (isCharging()) {
	            		if (this.energy - 10 >= 0 && ((ItemBattery)battery.getItem()).getItemEnergy(battery) + 10 <= ((ItemBattery)battery.getItem()).getItemMaxEnergy(battery)) {
	            			--this.chargeWait;
	            			if (this.chargeWait == 0) {
	            				this.energy = this.energy - 10;
	            				((ItemBattery)battery.getItem()).setItemEnergy(battery,((ItemBattery)battery.getItem()).getItemEnergy(battery) + 10);
	            				flag1 = true;
	            				this.chargeWait = getBakeTime();
	            				this.totalChargeWait = this.chargeWait;
	            				this.charging = 1;
	            			}
	            		} else {
	            			this.chargeWait = 0;
	            			this.charging = 0;
	            		}
	            	} else {
	            		if (this.energy - 10 >= 0 && ((ItemBattery)battery.getItem()).getItemEnergy(battery) + 10 <= ((ItemBattery)battery.getItem()).getItemMaxEnergy(battery)) {
	            			this.chargeWait = getBakeTime();
	            			this.totalChargeWait = this.chargeWait;
            				this.charging = 1;
	            		} else {
	            			this.chargeWait = 0;
	            			this.charging = 0;
	            		}
	            	}
	            } else {
	            	this.chargeWait = 0;
	            	this.charging = 0;
	            }
            } else {
            	this.chargeWait = 0;
            	this.charging = 0;
            }
            
            if (flag != this.isCharging())
            {
                flag1 = true;
                BlockCharger.setState(this.isCharging(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int getBakeTime()
    {
    	int time = 20;
    	Item item = this.tileEntityItemStacks.get(1).getItem();
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
        if (index != 0) {
            return false;
        } else {
            return isItemBattery(stack);
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
        if (direction == EnumFacing.DOWN && (index != 1 && index != 2))
        {
            return true;
        }

        return false;
    }

    @Override
    public String getGuiID()
    {
        return "rusticrefining:charger_battery";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerBlockCharger(playerInventory, this);
    }

    @Override
    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.charging;
            case 1:
            	return this.chargeWait;
            case 2:
            	return this.totalChargeWait;
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
                this.charging = value;
                break;
            case 1:
                this.chargeWait = value;
                break;
            case 2:
                this.totalChargeWait = value;

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
