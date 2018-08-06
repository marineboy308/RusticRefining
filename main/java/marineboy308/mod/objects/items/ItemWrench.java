package marineboy308.mod.objects.items;

import java.util.List;

import javax.annotation.Nonnull;

import marineboy308.mod.Main;
import marineboy308.mod.init.ItemInit;
import marineboy308.mod.objects.blocks.BlockContainerWrenchable;
import marineboy308.mod.objects.blocks.BlockWrenchable;
import marineboy308.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemWrench extends Item implements IHasModel {

	protected ToolMaterial toolMaterial;
	
	public ItemWrench(String name, ToolMaterial material) {
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.rusticrefiningtab);
		
		this.toolMaterial = material;
		this.setMaxDamage(material.getMaxUses());
		
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public int getItemStackLimit() {
		return 1;
	}
	
	public static void placeBlockInWorld(@Nonnull ItemStack itemstack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull EnumHand hand) {
		
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack handitem = player.getHeldItem(hand);
		
		/**
		 * 0: Rotate
		 * 1: Pickup
		 * 2: Place
		 */
		if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockWrenchable) && !(worldIn.getBlockState(pos).getBlock() instanceof BlockContainerWrenchable)) {
			if (player.isSneaking()) {
				NBTTagCompound nbt;
				if (handitem.hasTagCompound()) {
					nbt = handitem.getTagCompound();
				} else {
					nbt = new NBTTagCompound();
				}
				
				if (nbt.hasKey("Mode")) {
					if (nbt.getInteger("Mode") == 0) nbt.setInteger("Mode", 1);
					else if (nbt.getInteger("Mode") == 1) nbt.setInteger("Mode", 2);
					else nbt.setInteger("Mode", 0);
				} else {
					nbt.setInteger("Mode", 0);
				}
				handitem.setTagCompound(nbt);
				
				if (worldIn.isRemote) {
					if (nbt.getInteger("Mode") == 0) player.sendMessage(new TextComponentString("Changed Wrench Mode To 'Rotate'"));
					else if (nbt.getInteger("Mode") == 1) player.sendMessage(new TextComponentString("Changed Wrench Mode To 'Pickup'"));
					else if (nbt.getInteger("Mode") == 2) player.sendMessage(new TextComponentString("Changed Wrench Mode To 'Place'"));
				}
			} else {
				NBTTagCompound nbt;
				if (handitem.hasTagCompound()) {
					nbt = handitem.getTagCompound();
				} else {
					nbt = new NBTTagCompound();
				}
				
				if (nbt.hasKey("Mode")) {
					if (nbt.getInteger("Mode") == 2) {
						if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
							pos = pos.offset(facing);
						}
						for (int i = 0; i < 37; i++) {
							ItemStack stack = player.inventory.getStackInSlot(i);
							Item item = stack.getItem();
							if (!stack.isEmpty()) {
								if (Block.getBlockFromItem(item) != null) {
									Block block = Block.getBlockFromItem(item);
									if ((block instanceof BlockWrenchable ? (BlockWrenchable)block : null) != null) {
										if (((BlockWrenchable)block).canPlaceBlock(worldIn, pos)) {
											if (!worldIn.isRemote) placeBlockInWorld(stack, player, worldIn, pos, facing, hitX, hitY, hitZ, hand);
											break;
										}
									} else if ((block instanceof BlockContainerWrenchable ? (BlockContainerWrenchable)block : null) != null) {
										if (((BlockContainerWrenchable)block).canPlaceBlock(worldIn, pos)) {
											if (!worldIn.isRemote) placeBlockInWorld(stack, player, worldIn, pos, facing, hitX, hitY, hitZ, hand);
											break;
										}
									}
								}
							}
						}
					}
				} else {
					nbt.setInteger("Mode", 0);
				}
				handitem.setTagCompound(nbt);
			}
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(1, attacker);
        return true;
    }
	
	public String getMaterialName()
    {
        return this.toolMaterial.toString();
    }
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		
		if (!nbt.hasKey("Mode")) {
			nbt.setInteger("Mode", 0);
		}
		stack.setTagCompound(nbt);
		
		tooltip.add("Shift + Right Click The Ground To Change 'Mode'");
		if (nbt.getInteger("Mode") == 0) tooltip.add(TextFormatting.BLUE + "Mode: " + TextFormatting.WHITE + "Rotate");
		else if (nbt.getInteger("Mode") == 1) tooltip.add(TextFormatting.BLUE + "Mode: " + TextFormatting.WHITE + "Pickup");
		else if (nbt.getInteger("Mode") == 2) tooltip.add(TextFormatting.BLUE + "Mode: " + TextFormatting.WHITE + "Place");
	}

	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
