package marineboy308.mod.objects.machines.BatteryCharger;

import java.util.Arrays;

import marineboy308.mod.util.Reference;
import marineboy308.mod.util.handlers.EnergyHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBlockCharger extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/gui/charger_battery.png");
	private final InventoryPlayer player;
	private final TileEntityBlockCharger tileentity;
	
	public GuiBlockCharger(InventoryPlayer player, TileEntityBlockCharger tileentity) {
		super(new ContainerBlockCharger(player, tileentity));
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
		
		if (TileEntityBlockCharger.isCharging(tileentity)) {
			int j = this.getChargingLeftScaled(17);
			this.drawTexturedModalRect(this.guiLeft + 81, this.guiTop + 51 + 16 - j, 176, 16 - j, 13, j + 1);
		}
		int i = this.getEnergyStoredScaled(48);
		this.drawTexturedModalRect(this.guiLeft + 152, this.guiTop + 8 + 48 - i, 176, 17 + 48 - i, 16, i);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        if (this.isPointInRegion(152, 8, 16, 48, mouseX, mouseY)) this.drawHoveringText(Arrays.asList(EnergyHandler.getEnergyForDisplay(this.tileentity.getField(3), this.tileentity.getField(4))), mouseX, mouseY,this.mc.fontRenderer);
    }
	
	protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        int i = this.guiLeft;
        int j = this.guiTop;
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }
	
	private int getChargingLeftScaled(int pixels) {
		int t = this.tileentity.getField(0);
		int i = this.tileentity.getField(1);
		int j = this.tileentity.getField(2);
		return j != 0 && i != 0 && t == 1 ? i * pixels / j : 0;
	}
	
	private int getEnergyStoredScaled(int pixels) {
		int k = this.tileentity.getField(3);
		int l = this.tileentity.getField(4);
		return l != 0 && k != 0 ? k * pixels / l : 0;
	}
}
