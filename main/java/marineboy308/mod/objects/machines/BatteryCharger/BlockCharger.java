package marineboy308.mod.objects.machines.BatteryCharger;

import java.util.Random;

import marineboy308.mod.Main;
import marineboy308.mod.init.BlockInit;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.objects.blocks.BlockContainerWrenchable;
import marineboy308.mod.util.Reference;
import marineboy308.mod.util.interfaces.IHasModel;
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
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCharger extends BlockContainerWrenchable implements IHasModel {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool CHARGING = PropertyBool.create("charging");

	public BlockCharger(String name, boolean canRotate, boolean canPickup, boolean canPlace) {
		
		super(Material.IRON, canRotate, canPickup, canPlace);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.rusticrefiningtab);
		
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CHARGING, Boolean.valueOf(false)));
		
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
        return Item.getItemFromBlock(BlockInit.CHARGER_BATTERY);
    }
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(BlockInit.CHARGER_BATTERY);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
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
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }
	
	@Override
	public void setBlockStateForRotation(World worldIn, BlockPos pos, IBlockState state, EnumFacing facing) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		
		EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
		
		switch (enumfacing)
        {
            case NORTH:
            	worldIn.setBlockState(pos, BlockInit.CHARGER_BATTERY.getDefaultState().withProperty(FACING, EnumFacing.EAST).withProperty(CHARGING, state.getValue(CHARGING)), 3);
                break;
            case EAST:
            	worldIn.setBlockState(pos, BlockInit.CHARGER_BATTERY.getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(CHARGING, state.getValue(CHARGING)), 3);
                break;
            case SOUTH:
            	worldIn.setBlockState(pos, BlockInit.CHARGER_BATTERY.getDefaultState().withProperty(FACING, EnumFacing.WEST).withProperty(CHARGING, state.getValue(CHARGING)), 3);
                break;
            case WEST:
            	worldIn.setBlockState(pos, BlockInit.CHARGER_BATTERY.getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(CHARGING, state.getValue(CHARGING)), 3);
            	break;
            default:
            	break;
        }
		
		 if (tileentity != null) {
	        tileentity.validate();
	        worldIn.setTileEntity(pos, tileentity);
	     }
	}
	
	@Override
	public void openGuiOnActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
		if(!worldIn.isRemote) {
			playerIn.openGui(Main.instance, Reference.GUI_CHARGER_BATTERY, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	public static void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (active) {
            worldIn.setBlockState(pos, BlockInit.CHARGER_BATTERY.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(CHARGING, true), 3);
        } else {
            worldIn.setBlockState(pos, BlockInit.CHARGER_BATTERY.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(CHARGING, false), 3);
        }

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBlockCharger();
	}
	
	public static boolean isCharging(int meta) {
        return (meta & 8) != 8;
    }
	
	public static EnumFacing getFacing(int meta) {
        return EnumFacing.getFront(meta & 7);
    }
	
	@Override
	 protected BlockStateContainer createBlockState() {
		 return new BlockStateContainer(this, new IProperty[] {FACING,CHARGING});
	 }

	 @Override
	 public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | ((EnumFacing)state.getValue(FACING)).getIndex();
		
		if(!((Boolean)state.getValue(CHARGING)).booleanValue()) {
			i |= 8;
		}
		
	 	return i;
	 }

	 @Override
	 public IBlockState getStateFromMeta(int meta) {
	 	return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(CHARGING, Boolean.valueOf(isCharging(meta)));
	 }

	 @Override
	 public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		 worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()).withProperty(CHARGING, Boolean.valueOf(false)), 2);
	 }
	 
	 @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		 TileEntityBlockCharger tileentity = (TileEntityBlockCharger)worldIn.getTileEntity(pos);
		 InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
		 super.breakBlock(worldIn, pos, state);
	}

	 @Override
	 public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		 return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(CHARGING, Boolean.valueOf(false));
	 }

	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
