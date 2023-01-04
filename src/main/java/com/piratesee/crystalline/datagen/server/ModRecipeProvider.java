package com.piratesee.crystalline.datagen.server;

import java.util.function.Consumer;

import com.piratesee.crystalline.Crystalline;
import com.piratesee.crystalline.init.BlockInit;
import com.piratesee.crystalline.init.ItemInit;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ItemInit.THE_THING.get())
		.define('a', BlockInit.PROTOTYPE.get().asItem())
		.pattern("aa").pattern("aa")
		.unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(BlockInit.PROTOTYPE.get()).getPath(), has(BlockInit.PROTOTYPE.get().asItem()))
		.save(consumer, new ResourceLocation(Crystalline.MOD_ID, "yes"));
	}
	
}
