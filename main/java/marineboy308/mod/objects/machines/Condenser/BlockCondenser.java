package marineboy308.mod.objects.machines.Condenser;

import java.util.Random;

import marineboy308.mod.Main;
import marineboy308.mod.init.BlockInit;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.util.Reference;
import marineboy308.mod.util.interfaces.IHasModel;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCondenser extends BlockContainer implements IHasModel {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool CONDENSING = PropertyBool.create("condensing");

	public BlockCondenser(String name) {
		
		super(Material.IRON);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.rusticrefiningtab);
		
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CONDENSING, Boolean.valueOf(false)));
		
		setSoundType(SoundType.METAL);
		setHardness(2.0F);
		setResistance(15.0F);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public boolean doesSideBlockChestOpening(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN ? true : false;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlockInit.CONDENSER);
    }
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(BlockInit.CONDENSER);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isTopSolid(IBlockState state) {
        return true;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
	
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if(!worldIn.isRemote) {
			playerIn.openGui(Main.instance, Reference.GUI_BLOCK_FILTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		
		return true;
	}
	
	public static void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (active) {
            worldIn.setBlockState(pos, BlockInit.CONDENSER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(CONDENSING, true), 3);
            worldIn.setBlockState(pos, BlockInit.CONDENSER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(CONDENSING, true), 3);
        } else {
            worldIn.setBlockState(pos, BlockInit.CONDENSER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(CONDENSING, false), 3);
            worldIn.setBlockState(pos, BlockInit.CONDENSER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(CONDENSING, false), 3);
        }

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBlockCondenser();
	}
	
	public static boolean isCondensing(int meta) {
        return (meta & 8) != 8;
    }
	
	public static EnumFacing getFacing(int meta) {
        return EnumFacing.getFront(meta & 7);
    }
	
	@Override
	 protected BlockStateContainer createBlockState() {
		 return new BlockStateContainer(this, new IProperty[] {FACING,CONDENSING});
	 }

	 @Override
	 public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | ((EnumFacing)state.getValue(FACING)).getIndex();
		
		if(!((Boolean)state.getValue(CONDENSING)).booleanValue()) {
			i |= 8;
		}
		
	 	return i;
	 }

	 @Override
	 public IBlockState getStateFromMeta(int meta) {
	 	return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(CONDENSING, Boolean.valueOf(isCondensing(meta)));
	 }

	 @Override
	 public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		 worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()).withProperty(CONDENSING, Boolean.valueOf(false)), 2);
	 }
	 
	 @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		 TileEntityBlockCondenser tileentity = (TileEntityBlockCondenser)worldIn.getTileEntity(pos);
		 InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
		 super.breakBlock(worldIn, pos, state);
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(CONDENSING, Boolean.valueOf(false));
	}

	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
