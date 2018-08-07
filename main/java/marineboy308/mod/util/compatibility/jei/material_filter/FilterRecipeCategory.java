package marineboy308.mod.util.compatibility.jei.material_filter;

import marineboy308.mod.util.Reference;
import marineboy308.mod.util.compatibility.jei.RecipeCategories;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;

public class FilterRecipeCategory extends AbstractFilterRecipeCategory<FilterRecipe> {

	private final IDrawable background;
	private final String name;
	
	public FilterRecipeCategory(IGuiHelper helper) {
		super(helper);
		background = helper.createDrawable(TEXTURES, 40, 4, 131, 77);
		name = "Material Filter";
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		animatedEnergy.draw(minecraft, 112, 4);
		animatedArrow.draw(minecraft, 22, 28);
		animatedFilter.draw(minecraft, 2, 25);
	}
	
	@Override
	public String getTitle() {
		return name;
	}
	
	@Override
	public String getModName() {
		return Reference.NAME;
	}
	
	@Override
	public String getUid() {
		return RecipeCategories.FILTER;
	}
	
	public void setRecipe(IRecipeLayout recipeLayout, FilterRecipe recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(input, true, 3, 3);
		stacks.init(output1, false, 57, 26);
		stacks.init(output2, false, 3, 48);
		stacks.init(fuel, true, 111, 57);
		stacks.set(ingredients);
	};
}
