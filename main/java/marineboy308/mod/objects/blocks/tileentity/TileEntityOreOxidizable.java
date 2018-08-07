package marineboy308.mod.objects.blocks.tileentity;

import marineboy308.mod.init.BlockInit;
import marineboy308.mod.objects.blocks.BlockOreOxidizable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityOreOxidizable extends TileEntity implements ITickable {

    private int oxidizeTime;
    private boolean oxidized;
    
    

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.oxidizeTime = compound.getInteger("OxidizeTime");
        this.oxidized = compound.getBoolean("Oxidized");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("OxidizeTime", this.oxidizeTime);
        compound.setBoolean("Oxidized", this.oxidized);

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
    public void update()
    {
    	boolean flag = false;

        if (!this.world.isRemote) {
        	IBlockState blockstate = this.world.getBlockState(pos);
        	Block block = blockstate.getBlock();
        	
        	if (block == BlockInit.ORE_COPPER_OXIDIZED && !this.oxidized) {
        		this.oxidized = true;
        		flag = true;
        	} else if (this.oxidizeTime >= 12000) {
        		BlockOreOxidizable.oxidize(this.world, pos);
        		this.oxidizeTime = 0;
        		this.oxidized = true;
        		flag = true;
        	}
        	
        	if (!this.oxidized) {
        		Block blockup = this.world.getBlockState(pos.up()).getBlock();
        		Block blockdown = this.world.getBlockState(pos.down()).getBlock();
        		Block blocknorth = this.world.getBlockState(pos.north()).getBlock();
        		Block blocksouth = this.world.getBlockState(pos.south()).getBlock();
        		Block blockeast = this.world.getBlockState(pos.east()).getBlock();
        		Block blockwest = this.world.getBlockState(pos.west()).getBlock();
        		
        		if (blockup == Blocks.AIR) {
        			++this.oxidizeTime;
        			flag = true;
        		}
        		if (blockdown == Blocks.AIR) {
        			++this.oxidizeTime;
        			flag = true;
        		}
        		if (blocknorth == Blocks.AIR) {
        			++this.oxidizeTime;
        			flag = true;
        		}
        		if (blocksouth == Blocks.AIR) {
        			++this.oxidizeTime;
        			flag = true;
        		}
        		if (blockeast == Blocks.AIR) {
        			++this.oxidizeTime;
        			flag = true;
        		}
        		if (blockwest == Blocks.AIR) {
        			++this.oxidizeTime;
        			flag = true;
        		}
        	}
        }
        
        if (flag) {
        	this.markDirty();
        }
    }
}
