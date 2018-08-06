package marineboy308.mod.objects.blocks;

import marineboy308.mod.objects.items.ItemWrench;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockContainerWrenchable extends BlockContainer {

	protected boolean canRotate;
	protected boolean canPickup;
	protected boolean canPlace;
	
	public BlockContainerWrenchable(Material materialIn, boolean canRotate, boolean canPickup, boolean canPlace) {
		super(materialIn);
		this.canRotate = canRotate;
		this.canPickup = canPickup;
		this.canPlace = canPlace;
	}
	
	/**
     * called to set the blockState of the block when rotating.
     */
	public void setBlockStateForRotation(World worldIn, BlockPos pos, IBlockState state, EnumFacing facing)
	{
	}
	
	/**
     * called to open gui for a valid tileentity when clicked without wrench.
     */
	public void openGuiOnActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn)
	{
	}
	
	/**
     * @return whether the block can be rotated.
     */
	public boolean canRotateBlock(World worldIn, BlockPos pos) {
		return this.canRotate;
	}
	
	/**
     * @return whether the block can be picked up.
     */
	public boolean canPickupBlock(World worldIn, BlockPos pos) {
		return this.canPickup;
	}
	
	/**
     * @return whether the block can be placed.
     */
	public boolean canPlaceBlock(World worldIn, BlockPos pos) {
		return this.canPlace;
	}
	
	/**
     * Called serverside when this block is picked up, but before the Tile Entity is updated
     */
	public void pickupBlock(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
		Block block = worldIn.getBlockState(pos).getBlock();
		if (!worldIn.isRemote) {
			worldIn.setBlockToAir(pos);
			if (!playerIn.isCreative()) InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(block,1));
		}
	}

	/**
     * rotate/pickup block.
     */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack handitem = playerIn.getHeldItem(hand);
		if (handitem.getItem() instanceof ItemWrench) {
			handitem.damageItem(1, playerIn);
			
			NBTTagCompound nbt;
			if (handitem.hasTagCompound()) {
				nbt = handitem.getTagCompound();
			} else {
				nbt = new NBTTagCompound();
			}
			
			if (!nbt.hasKey("Mode")) {
				nbt.setInteger("Mode", 0);
			}
			handitem.setTagCompound(nbt);
			
			if (nbt.getInteger("Mode") == 0) {
				if (((BlockContainerWrenchable)worldIn.getBlockState(pos).getBlock()).canRotateBlock(worldIn, pos)) {
					setBlockStateForRotation(worldIn, pos, state, facing);
					SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, playerIn);
	                worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), soundtype.getHitSound(), SoundCategory.BLOCKS, soundtype.getVolume(), soundtype.getPitch(), false);
					return true;
				}
				return false;
			} else if (nbt.getInteger("Mode") == 1) {
				if (((BlockContainerWrenchable)worldIn.getBlockState(pos).getBlock()).canPickupBlock(worldIn, pos)) {
					pickupBlock(worldIn, pos, state, playerIn);
					return true;
				}
				return false;
			} else {
				return false;
			}
		} else {
			openGuiOnActivated(worldIn, pos, state, playerIn);
			return true;
		}
	}

	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}
}
