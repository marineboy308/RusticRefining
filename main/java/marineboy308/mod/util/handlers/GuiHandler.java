package marineboy308.mod.util.handlers;

import marineboy308.mod.container.ContainerBlockFilter;
import marineboy308.mod.container.gui.GuiBlockFilter;
import marineboy308.mod.tileentity.TileEntityBlockFilter;
import marineboy308.mod.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == Reference.GUI_BLOCK_FILTER) return new ContainerBlockFilter(player.inventory, (TileEntityBlockFilter)world.getTileEntity(new BlockPos(x,y,z)));	
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == Reference.GUI_BLOCK_FILTER) return new GuiBlockFilter(player.inventory, (TileEntityBlockFilter)world.getTileEntity(new BlockPos(x,y,z)));
		return null;
	}

}
