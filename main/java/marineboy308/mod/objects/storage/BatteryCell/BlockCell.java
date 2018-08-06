package marineboy308.mod.objects.storage.BatteryCell;

import java.util.List;

import javax.annotation.Nullable;

import marineboy308.mod.Main;
import marineboy308.mod.init.BlockInit;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.objects.blocks.BlockContainerWrenchable;
import marineboy308.mod.objects.items.ItemBattery;
import marineboy308.mod.util.Reference;
import marineboy308.mod.util.handlers.EnergyHandler;
import marineboy308.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockCell extends BlockContainerWrenchable implements IHasModel {
	
	private boolean pickedup = false;

	public BlockCell(String name, boolean canRotate, boolean canPickup, boolean canPlace) {
		
		super(Material.IRON, canRotate, canPickup, canPlace);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.rusticrefiningtab);
		
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
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		ItemStack itemstack = super.getItem(worldIn, pos, state);
		TileEntityBlockCell tileentitycell = (TileEntityBlockCell)worldIn.getTileEntity(pos);
        NBTTagCompound nbt = tileentitycell.saveToNbt(new NBTTagCompound());

        if (!nbt.hasNoTags())
        {
            itemstack.setTagInfo("BlockEntityTag", nbt);
        }

        return itemstack;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return new AxisAlignedBB(0.1875D, 0, 0.1875D, 0.8125D, 1, 0.8125D);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.1875D, 0, 0.1875D, 0.8125D, 1, 0.8125D);
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
        return false;
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
	public void openGuiOnActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
		if(!worldIn.isRemote) {
			playerIn.openGui(Main.instance, Reference.GUI_CELL_BATTERY, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBlockCell();
	}
	
	@Override
	public void pickupBlock(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
		this.pickedup = true;
		TileEntity tileentity = worldIn.getTileEntity(pos);
		
		if (tileentity instanceof TileEntityBlockCell) {
			
			TileEntityBlockCell tileentitycell = (TileEntityBlockCell)tileentity;
			
			if (!tileentitycell.isEmpty()) {
				ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
				NBTTagCompound nbt = new NBTTagCompound();
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt.setTag("BlockEntityTag", ((TileEntityBlockCell)tileentity).saveToNbt(nbt1));
				itemstack.setTagCompound(nbt);

                spawnAsEntity(worldIn, pos, itemstack);
			} else {
				spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this)));
			}
		}
		
		if (!worldIn.isRemote) worldIn.setBlockToAir(pos);
		this.pickedup = false;
	}
	 
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.pickedup) {
			TileEntityBlockCell tileentity = (TileEntityBlockCell)worldIn.getTileEntity(pos);
			InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
			super.breakBlock(worldIn, pos, state);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt != null && nbt.hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbt1 = nbt.getCompoundTag("BlockEntityTag");

            if (nbt1.hasKey("Items", 9))
            {
                NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(nbt1, nonnulllist);
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : nonnulllist)
                {
                    if (!itemstack.isEmpty())
                    {
                        ++j;

                        if (i <= 1)
                        {
                            ++i;
                            tooltip.add(String.format("%s, RE Stored: %s", itemstack.getDisplayName(), EnergyHandler.getEnergyForDisplay(((ItemBattery)itemstack.getItem()).getItemEnergy(itemstack), ((ItemBattery)itemstack.getItem()).getItemMaxEnergy(itemstack))));
                        }
                    }
                }

                if (j - i > 0)
                {
                    tooltip.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.cell_battery.more"),j - i));
                }
            }
        }
    }

	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
