package com.piratesee.crystalline.datagen.server;

import java.util.function.Consumer;

import com.piratesee.crystalline.Crystalline;
import com.piratesee.crystalline.init.BlockInit;
import com.piratesee.crystalline.init.ItemInit;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
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
		
		ShapedRecipeBuilder.shaped(BlockInit.DIAMONDMETAL_BLOCK.get().asItem())
		.define('a', ItemInit.DIAMONDMETAL.get())
		.pattern("aaa").pattern("aaa").pattern("aaa")
		.unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(ItemInit.DIAMONDMETAL.get()).getPath(), has(ItemInit.DIAMONDMETAL.get()))
		.save(consumer, new ResourceLocation(Crystalline.MOD_ID, "crafting/diamondmetal_block"));
		
		ShapelessRecipeBuilder.shapeless(ItemInit.DIAMONDMETAL.get(), 9)
		.requires(BlockInit.DIAMONDMETAL_BLOCK.get().asItem())
		.unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(ItemInit.DIAMONDMETAL.get()).getPath(), has(ItemInit.DIAMONDMETAL.get()))
		.save(consumer, new ResourceLocation(Crystalline.MOD_ID, "crafting/diamondmetal_from_block"));
		
		ShapelessRecipeBuilder.shapeless(ItemInit.DIAMONDMETAL_NUGGET.get(), 9)
		.requires(ItemInit.DIAMONDMETAL.get())
		.unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(ItemInit.DIAMONDMETAL.get()).getPath(), has(ItemInit.DIAMONDMETAL.get()))
		.save(consumer, new ResourceLocation(Crystalline.MOD_ID, "crafting/diamondmetal_nugget"));
		
		ShapedRecipeBuilder.shaped(ItemInit.DIAMONDMETAL.get())
		.define('a', ItemInit.DIAMONDMETAL_NUGGET.get())
		.pattern("aaa").pattern("aaa").pattern("aaa")
		.unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(ItemInit.DIAMONDMETAL.get()).getPath(), has(ItemInit.DIAMONDMETAL.get()))
		.save(consumer, new ResourceLocation(Crystalline.MOD_ID, "crafting/diamondmetal_from_nugget"));
	}
	
}
