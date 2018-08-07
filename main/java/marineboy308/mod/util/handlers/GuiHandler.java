package marineboy308.mod.util.handlers;

import marineboy308.mod.objects.machines.BatteryCharger.ContainerBlockCharger;
import marineboy308.mod.objects.machines.BatteryCharger.GuiBlockCharger;
import marineboy308.mod.objects.machines.BatteryCharger.TileEntityBlockCharger;
import marineboy308.mod.objects.machines.MaterialFilter.ContainerBlockFilter;
import marineboy308.mod.objects.machines.MaterialFilter.GuiBlockFilter;
import marineboy308.mod.objects.machines.MaterialFilter.TileEntityBlockFilter;
import marineboy308.mod.objects.storage.BatteryCell.ContainerBlockCell;
import marineboy308.mod.objects.storage.BatteryCell.GuiBlockCell;
import marineboy308.mod.objects.storage.BatteryCell.TileEntityBlockCell;
import marineboy308.mod.objects.storage.StorageCell.ContainerBlockStorageCell;
import marineboy308.mod.objects.storage.StorageCell.GuiBlockStorageCell;
import marineboy308.mod.objects.storage.StorageCell.TileEntityBlockStorageCell;
import marineboy308.mod.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == Reference.GUI_MATERIAL_FILTER) return new ContainerBlockFilter(player.inventory, (TileEntityBlockFilter)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_CHARGER_BATTERY) return new ContainerBlockCharger(player.inventory, (TileEntityBlockCharger)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_CELL_BATTERY) return new ContainerBlockCell(player.inventory, (TileEntityBlockCell)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_CELL_STORAGE) return new ContainerBlockStorageCell(player.inventory, (TileEntityBlockStorageCell)world.getTileEntity(new BlockPos(x,y,z)));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == Reference.GUI_MATERIAL_FILTER) return new GuiBlockFilter(player.inventory, (TileEntityBlockFilter)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_CHARGER_BATTERY) return new GuiBlockCharger(player.inventory, (TileEntityBlockCharger)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_CELL_BATTERY) return new GuiBlockCell(player.inventory, (TileEntityBlockCell)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_CELL_STORAGE) return new GuiBlockStorageCell(player.inventory, (TileEntityBlockStorageCell)world.getTileEntity(new BlockPos(x,y,z)));
		return null;
	}

}
