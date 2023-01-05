package com.piratesee.crystalline.intergrations;

import com.piratesee.crystalline.Crystalline;
import com.piratesee.crystalline.init.BlockInit;
import com.piratesee.crystalline.recipe.GemInjectingRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GemInjectorRecipeCategory implements IRecipeCategory<GemInjectingRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Crystalline.MOD_ID, "gem_injecting");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Crystalline.MOD_ID, "textures/gui/gem_injector_gui.png");

    private final IDrawable background;
    private final IDrawable icon;
	
    public GemInjectorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.GEM_INJECTOR.get()));
    }
    
	@Override
	public RecipeType<GemInjectingRecipe> getRecipeType() {
		return JEIPlugin.INJECTION_TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.literal("Gem Injector");
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, GemInjectingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 16).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 58).addIngredients(recipe.getIngredients().get(1));
        
        builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 37).addItemStack(recipe.getResultItem());
	}

}
