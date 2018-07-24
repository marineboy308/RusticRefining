package marineboy308.mod.recipes;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import marineboy308.mod.init.BlockInit;
import marineboy308.mod.init.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FilterRecipes {

	private static final FilterRecipes FILTERING_BASE = new FilterRecipes();
    private final Map<ItemStack, ItemStack> filteringList = Maps.<ItemStack, ItemStack>newHashMap();
    private final Map<ItemStack, ItemStack> filteringChanceItemList = Maps.<ItemStack, ItemStack>newHashMap();
    private final Map<ItemStack, Float> filteringChanceList = Maps.<ItemStack, Float>newHashMap();
    private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();

    public static FilterRecipes instance()
    {
        return FILTERING_BASE;
    }

    private FilterRecipes()
    {
        addFilteringRecipeForBlock(Blocks.DIRT, new ItemStack(ItemInit.BALL_DIRT, 4), new ItemStack(ItemInit.SHARD_GOLD, 1), 0.25F, 0.0F);
        addFilteringRecipeForBlock(Blocks.GRAVEL, new ItemStack(ItemInit.PEBBLE, 4), new ItemStack(ItemInit.SHARD_IRON, 1), 0.3F, 0.0F);
        addFilteringRecipeForBlock(Blocks.SAND, new ItemStack(ItemInit.PILE_SAND, 2), new ItemStack(ItemInit.SHARD_DIAMOND, 1), 0.05F, 0.0F);
        addFilteringRecipeForBlock(BlockInit.CONDENSED_DIRT, new ItemStack(Blocks.DIRT, 4), new ItemStack(ItemInit.SHARD_GOLD, 4), 0.5F, 0.0F);
    }

    public void addFilteringRecipeForBlock(Block input, ItemStack output1, ItemStack output2, float chance, float experience)
    {
        this.addFiltering(Item.getItemFromBlock(input), output1, output2, chance, experience);
    }

    public void addFiltering(Item input, ItemStack output1, ItemStack output2, float chance, float experience)
    {
        this.addFilteringRecipe(new ItemStack(input, 1, 32767), output1, output2, chance, experience);
    }

    public void addFilteringRecipe(ItemStack input, ItemStack output1, ItemStack output2, float chance, float experience)
    {
        if (getFilteringResult(input) != ItemStack.EMPTY) { net.minecraftforge.fml.common.FMLLog.log.info("Ignored filtering recipe with conflicting input: {} = {}", input, output1); return; }
        this.filteringList.put(input, output1);
        this.filteringChanceItemList.put(input, output2);
        this.filteringChanceList.put(input, Float.valueOf(chance));
        this.experienceList.put(output1, Float.valueOf(experience));
    }

    public ItemStack getFilteringResult(ItemStack stack)
    {
        for (Entry<ItemStack, ItemStack> entry : this.filteringList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }
    
    public ItemStack getFilteringChanceItemResult(ItemStack stack)
    {
        for (Entry<ItemStack, ItemStack> entry : this.filteringChanceItemList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }
    
    public float getFilteringChanceResult(ItemStack stack)
    {
        for (Entry<ItemStack, Float> entry : this.filteringChanceList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return ((Float)entry.getValue()).floatValue();
            }
        }

        return 0.0F;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getFilteringList()
    {
        return this.filteringList;
    }
    
    public Map<ItemStack, ItemStack> getFilteringChanceItemList()
    {
        return this.filteringChanceItemList;
    }

    public float getFilteringExperience(ItemStack stack)
    {
        float ret = stack.getItem().getSmeltingExperience(stack);
        if (ret != -1) return ret;

        for (Entry<ItemStack, Float> entry : this.experienceList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return ((Float)entry.getValue()).floatValue();
            }
        }

        return 0.0F;
    }
}
