package marineboy308.mod.objects.storage.StorageCell;

import java.util.List;

import javax.annotation.Nullable;

import marineboy308.mod.Main;
import marineboy308.mod.init.BlockInit;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.objects.blocks.BlockContainerWrenchable;
import marineboy308.mod.util.Reference;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockStorageCell extends BlockContainerWrenchable implements IHasModel {
	
	private boolean pickedup = false;

	public BlockStorageCell(String name, boolean canRotate, boolean canPickup, boolean canPlace) {
		
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
		TileEntityBlockStorageCell tileentitystoragecell = (TileEntityBlockStorageCell)worldIn.getTileEntity(pos);
        NBTTagCompound nbt = tileentitystoragecell.saveToNbt(new NBTTagCompound());

        if (!nbt.hasNoTags())
        {
            itemstack.setTagInfo("BlockEntityTag", nbt);
        }

        return itemstack;
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
			playerIn.openGui(Main.instance, Reference.GUI_CELL_STORAGE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBlockStorageCell();
	}
	
	@Override
	public void pickupBlock(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
		this.pickedup = true;
		TileEntity tileentity = worldIn.getTileEntity(pos);
		
		if (tileentity instanceof TileEntityBlockStorageCell) {
			
			TileEntityBlockStorageCell tileentitystoragecell = (TileEntityBlockStorageCell)tileentity;
			
			if (!tileentitystoragecell.isEmpty()) {
				ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
				NBTTagCompound nbt = new NBTTagCompound();
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt.setTag("BlockEntityTag", ((TileEntityBlockStorageCell)tileentity).saveToNbt(nbt1));
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
			TileEntityBlockStorageCell tileentity = (TileEntityBlockStorageCell)worldIn.getTileEntity(pos);
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
                NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(40, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(nbt1, nonnulllist);
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : nonnulllist)
                {
                    if (!itemstack.isEmpty())
                    {
                        ++j;

                        if (i <= 4)
                        {
                            ++i;
                            tooltip.add(String.format("%s x%d", itemstack.getDisplayName(), itemstack.getCount()));
                        }
                    }
                }

                if (j - i > 0)
                {
                    tooltip.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.cell_storage.more"),j - i));
                }
            }
        }
    }

	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
