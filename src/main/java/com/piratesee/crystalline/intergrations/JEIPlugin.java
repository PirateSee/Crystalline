package com.piratesee.crystalline.intergrations;

import java.util.List;
import java.util.Objects;

import com.piratesee.crystalline.AwsomeMod;
import com.piratesee.crystalline.recipe.GemInfusingStationRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static RecipeType<GemInfusingStationRecipe> INFUSION_TYPE =
            new RecipeType<>(GemInfusingStationRecipeCategory.UID, GemInfusingStationRecipe.class);
	
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(AwsomeMod.MOD_ID, "jei_plugin");
	}

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                GemInfusingStationRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<GemInfusingStationRecipe> recipesInfusing = rm.getAllRecipesFor(GemInfusingStationRecipe.Type.INSTANCE);
        registration.addRecipes(INFUSION_TYPE, recipesInfusing);
    }
	
}
