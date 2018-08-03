package marineboy308.mod.util.compatibility.jei.material_filter;

import java.awt.Color;
import java.util.List;

import marineboy308.mod.recipes.FilterRecipes;
import marineboy308.mod.util.compatibility.jei.JEICompatibility;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public class FilterRecipe implements IRecipeWrapper{

	private final ItemStack input;
	private final List<ItemStack> outputs;
	
	public FilterRecipe(ItemStack input, List<ItemStack> outputs) {
		this.input = input;
		this.outputs = outputs;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, input);
		ingredients.setOutputs(ItemStack.class, outputs);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		FilterRecipes recipes = FilterRecipes.instance();
		float experience = recipes.getFilteringExperience(outputs.get(0));
		
		if (experience > 0) {
			String experienceString = JEICompatibility.translateToLocalFormatted("gui.jei.category.smelting.experience", experience);
			FontRenderer renderer = minecraft.fontRenderer;
			int stringWidth = renderer.getStringWidth(experienceString);
			renderer.drawString(experienceString, recipeWidth - stringWidth, 0, Color.GRAY.getRGB());
		}
	}
}
