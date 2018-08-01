package marineboy308.mod.objects.blocks;

import marineboy308.mod.objects.items.ItemWrench;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWrenchable extends Block {

	protected boolean canRotate;
	protected boolean canPickup;
	
	public BlockWrenchable(Material materialIn, boolean canRotate, boolean canPickup) {
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
     * rotate/pickup block.
     */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack handitem = playerIn.getHeldItem(hand);
		if (handitem.getItem() instanceof ItemWrench) {
			handitem.damageItem(1, playerIn);
			if (playerIn.isSneaking()) {
				breakBlock(worldIn, pos, state);
				return true;
			} else {
				setBlockStateForRotation(worldIn, pos, state, facing);
				return true;
			}
		} else {
			return true;
		}
	}
}
