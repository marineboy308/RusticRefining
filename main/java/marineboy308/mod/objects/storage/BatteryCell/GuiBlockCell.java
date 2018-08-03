package marineboy308.mod.objects.storage.BatteryCell;

import java.util.Arrays;

import marineboy308.mod.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBlockCell extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/gui/cell_battery.png");
	private final InventoryPlayer player;
	private final TileEntityBlockCell tileentity;
	
	public GuiBlockCell(InventoryPlayer player, TileEntityBlockCell tileentity) {
		super(new ContainerBlockCell(player, tileentity));
		this.player = player;
		this.tileentity = tileentity;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String tilename = this.tileentity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(tilename, (this.xSize /2 - this.fontRenderer.getStringWidth(tilename) / 2), 8, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		int j = this.getEnergyScaled(17,0,1);
		this.drawTexturedModalRect(this.guiLeft + 58, this.guiTop + 17 + 16 - j, 176, 16 - j, 13, j + 1);
		int i = this.getEnergyScaled(17,2,3);
		this.drawTexturedModalRect(this.guiLeft + 107, this.guiTop + 17 + 16 - i, 176, 16 - i, 13, i + 1);
		int k = this.getEnergyScaled(17,4,5);
		this.drawTexturedModalRect(this.guiLeft + 58, this.guiTop + 35 + 16 - k, 176, 16 - k, 13, k + 1);
		int l = this.getEnergyScaled(17,6,7);
		this.drawTexturedModalRect(this.guiLeft + 107, this.guiTop + 35 + 16 - l, 176, 16 - l, 13, l + 1);
		int h = this.getEnergyScaled(17,8,9);
		this.drawTexturedModalRect(this.guiLeft + 58, this.guiTop + 53 + 16 - h, 176, 16 - h, 13, h + 1);
		int g = this.getEnergyScaled(17,10,11);
		this.drawTexturedModalRect(this.guiLeft + 107, this.guiTop + 53 + 16 - g, 176, 16 - g, 13, g + 1);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        if (this.isPointInRegion(58, 17, 11, 15, mouseX, mouseY)) this.drawHoveringText(Arrays.asList(this.tileentity.getField(0) + "/" + this.tileentity.getField(1) + "RE"), mouseX, mouseY,this.mc.fontRenderer);
        if (this.isPointInRegion(107, 17, 11, 15, mouseX, mouseY)) this.drawHoveringText(Arrays.asList(this.tileentity.getField(2) + "/" + this.tileentity.getField(3) + "RE"), mouseX, mouseY,this.mc.fontRenderer);
        if (this.isPointInRegion(58, 35, 11, 15, mouseX, mouseY)) this.drawHoveringText(Arrays.asList(this.tileentity.getField(4) + "/" + this.tileentity.getField(5) + "RE"), mouseX, mouseY,this.mc.fontRenderer);
        if (this.isPointInRegion(107, 35, 11, 15, mouseX, mouseY)) this.drawHoveringText(Arrays.asList(this.tileentity.getField(6) + "/" + this.tileentity.getField(7) + "RE"), mouseX, mouseY,this.mc.fontRenderer);
        if (this.isPointInRegion(58, 53, 11, 15, mouseX, mouseY)) this.drawHoveringText(Arrays.asList(this.tileentity.getField(8) + "/" + this.tileentity.getField(9) + "RE"), mouseX, mouseY,this.mc.fontRenderer);
        if (this.isPointInRegion(107, 53, 11, 15, mouseX, mouseY)) this.drawHoveringText(Arrays.asList(this.tileentity.getField(10) + "/" + this.tileentity.getField(11) + "RE"), mouseX, mouseY,this.mc.fontRenderer);
    }
	
	protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        int i = this.guiLeft;
        int j = this.guiTop;
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }
	
	private int getEnergyScaled(int pixels, int field, int fieldmax) {
		int i = this.tileentity.getField(field);
		int j = this.tileentity.getField(fieldmax);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
}
