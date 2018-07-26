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

public class CondenserRecipes {

	private static final CondenserRecipes CONDENSING_BASE = new CondenserRecipes();
    private final Map<ItemStack[], ItemStack> condensingList = Maps.<ItemStack[], ItemStack>newHashMap();
    private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();

    public static CondenserRecipes instance()
    {
        return CONDENSING_BASE;
    }

    private CondenserRecipes()
    {
    	addCondensing(ItemInit.BALL_DIRT, ItemInit.BALL_DIRT, ItemInit.BALL_DIRT, ItemInit.BALL_DIRT, new ItemStack(Blocks.DIRT, 2, 0), 0.0F);
    	addCondensing(ItemInit.PEBBLE, ItemInit.PEBBLE, ItemInit.PEBBLE, ItemInit.PEBBLE, new ItemStack(Blocks.GRAVEL, 2), 0.0F);
    	addCondensing(ItemInit.PILE_SAND, ItemInit.PILE_SAND, ItemInit.PILE_SAND, ItemInit.PILE_SAND, new ItemStack(Blocks.SAND, 2, 0), 0.0F);
        addCondensingRecipeForBlock(Blocks.DIRT, Blocks.DIRT, Blocks.DIRT, Blocks.DIRT, new ItemStack(BlockInit.CONDENSED_DIRT, 1), 0.0F);
    }

    public void addCondensingRecipeForBlock(Block input1, Block input2, Block input3, Block input4, ItemStack output, float experience)
    {
        this.addCondensing(Item.getItemFromBlock(input1), Item.getItemFromBlock(input2), Item.getItemFromBlock(input3), Item.getItemFromBlock(input4), output, experience);
    }

    public void addCondensing(Item input1, Item input2, Item input3, Item input4, ItemStack output, float experience)
    {
        this.addCondensingRecipe(new ItemStack(input1, 1, 32767), new ItemStack(input2, 1, 32767), new ItemStack(input3, 1, 32767), new ItemStack(input4, 1, 32767), output, experience);
    }

    public void addCondensingRecipe(ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4, ItemStack output, float experience)
    {
    	ItemStack[] inputs = new ItemStack[] {input1,input2,input3,input4};
        if (getCondensingResult(inputs) != ItemStack.EMPTY) { net.minecraftforge.fml.common.FMLLog.log.info("Ignored condensing recipe with conflicting input: {} = {}", inputs, output); return; }
        this.condensingList.put(inputs, output);
        this.experienceList.put(output, Float.valueOf(experience));
    }

    public ItemStack getCondensingResult(ItemStack[] stacks)
    {
        for (Entry<ItemStack[], ItemStack> entry : this.condensingList.entrySet())
        {
            if (this.compareInputs(stacks, entry.getKey()))
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean compareInputs(ItemStack[] stacks1, ItemStack[] stacks2)
    {
    	boolean flag1 = false;
    	boolean flag2 = false;
    	boolean flag3 = false;
    	boolean flag4 = false;
    	
    	if(stacks2[0].getItem() == stacks1[0].getItem() && (stacks2[0].getMetadata() == 32767 || stacks2[0].getMetadata() == stacks1[0].getMetadata()))
    		flag1 = true;
    	if(stacks2[1].getItem() == stacks1[1].getItem() && (stacks2[1].getMetadata() == 32767 || stacks2[1].getMetadata() == stacks1[1].getMetadata()))
    		flag2 = true;
    	if(stacks2[2].getItem() == stacks1[2].getItem() && (stacks2[2].getMetadata() == 32767 || stacks2[2].getMetadata() == stacks1[2].getMetadata()))
    		flag3 = true;
    	if(stacks2[3].getItem() == stacks1[3].getItem() && (stacks2[3].getMetadata() == 32767 || stacks2[3].getMetadata() == stacks1[3].getMetadata()))
    		flag4 = true;
        return flag1 && flag2 && flag3 && flag4;
    }
    
    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack[], ItemStack> getCondensingList()
    {
        return this.condensingList;
    }

    public float getCondensingExperience(ItemStack stack)
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
