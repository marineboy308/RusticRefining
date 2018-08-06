package marineboy308.mod.objects.blocks;

import marineboy308.mod.init.BlockInit;
import marineboy308.mod.objects.blocks.tileentity.TileEntityOreOxidizable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOre extends BlockBase {

	private boolean canOxidize;
	
	public BlockOre(String name, Material material, SoundType soundtype, float hardness, float resistance, boolean canOxidize) {
		super(name, material, soundtype, hardness, resistance);
		this.canOxidize = canOxidize;
	}
	
	public static void oxidize(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();

        if (block == BlockInit.ORE_COPPER) worldIn.setBlockState(pos, BlockInit.ORE_COPPER_OXIDIZED.getDefaultState(), 3);
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		if (this.canOxidize) {
			System.out.println("TileEntityOreOxidizable created for block: " + state.getBlock() + " in world: " + world);
			return new TileEntityOreOxidizable();
		}
		System.out.println("Failed to create tile entity for block: " + state.getBlock() + " in world: " + world);
		return super.createTileEntity(world, state);
	}
}
