package marineboy308.mod.objects.blocks;

import marineboy308.mod.Main;
import marineboy308.mod.init.BlockInit;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.objects.blocks.tileentity.TileEntityOreOxidizable;
import marineboy308.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOreOxidizable extends BlockContainer implements IHasModel {

	public BlockOreOxidizable(String name, Material material, SoundType soundtype, float hardness, float resistance) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.rusticrefiningtab);
		
		setSoundType(soundtype);
		setHardness(hardness);
		setResistance(resistance);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	public static void oxidize(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();

        if (block == BlockInit.ORE_COPPER) worldIn.setBlockState(pos, BlockInit.ORE_COPPER_OXIDIZED.getDefaultState(), 3);
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityOreOxidizable();
	}
	
	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
