package com.piratesee.crystalline.datagen;

import com.piratesee.crystalline.AwsomeMod;
import com.piratesee.crystalline.datagen.client.ModBlockStateProvider;
import com.piratesee.crystalline.datagen.client.ModItemModelProvider;
import com.piratesee.crystalline.datagen.server.ModBlockTagsProvider;
import com.piratesee.crystalline.datagen.server.ModItemTagsProvider;
import com.piratesee.crystalline.datagen.server.ModLootTableProvider;
import com.piratesee.crystalline.datagen.server.ModRecipeProvider;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AwsomeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {
	private DataGeneration() {}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();
		
		generator.addProvider(event.includeClient(), new ModBlockStateProvider(generator, helper));
		generator.addProvider(event.includeClient(), new ModItemModelProvider(generator, helper));
		
		generator.addProvider(event.includeServer(), new ModRecipeProvider(generator));
		
		ModBlockTagsProvider blockTags = new ModBlockTagsProvider(generator, helper);
		
		generator.addProvider(event.includeServer(), new ModRecipeProvider(generator));
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new ModItemTagsProvider(generator, blockTags, helper));
		generator.addProvider(event.includeServer(), new ModLootTableProvider(generator));
		
		
		
	}
}
