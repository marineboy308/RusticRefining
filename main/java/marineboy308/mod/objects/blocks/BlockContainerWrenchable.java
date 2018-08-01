package marineboy308.mod.objects.blocks;

import marineboy308.mod.objects.items.ItemWrench;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockContainerWrenchable extends BlockContainer {

	protected boolean canRotate;
	protected boolean canPickup;
	
	public BlockContainerWrenchable(Material materialIn, boolean canRotate, boolean canPickup) {
		super(materialIn);
		this.canRotate = canRotate;
		this.canPickup = canPickup;
	}
	
	/**
     * called to set the blockState of the block when rotating.
     */
	public void setBlockStateForRotation(World worldIn, BlockPos pos, IBlockState state, EnumFacing facing)
	{
	}
	
	/**
     * called to drop items in a valid tileentity when picked up.
     */
	public void dropItemsOnPickup(World worldIn, BlockPos pos, IBlockState state)
	{
	}
	
	/**
     * called to open gui for a valid tileentity when clicked without wrench.
     */
	public void openGuiOnActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn)
	{
	}

	/**
     * rotate/pickup block.
     */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack handitem = playerIn.getHeldItem(hand);
		if (handitem.getItem() instanceof ItemWrench) {
			handitem.damageItem(1, playerIn);
			if (playerIn.isSneaking()) {
				System.out.println("Sneaking");
				dropItemsOnPickup(worldIn, pos, state);
				breakBlock(worldIn, pos, state);
				return true;
			} else {
				setBlockStateForRotation(worldIn, pos, state, facing);
				return true;
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
