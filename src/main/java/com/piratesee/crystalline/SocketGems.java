package com.piratesee.crystalline;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class SocketGems {

	public static int[] socketGemTester(ItemStack stack) {
		
		int[] placeholder = new int[3];
		
		//ResourceLocation jsonFile = new ResourceLocation(Crystalline.MOD_ID, "gemStats/" + ForgeRegistries.ITEMS.getKey(stack.getItem()) );
		
		BufferedReader reader = null;
		
		String itemPath = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().replace(':', '_');
		
		ResourceLocation loc = new ResourceLocation(Crystalline.MOD_ID, "gemStats/" + itemPath + ".json");
		
		System.out.println("src/main/resources/data/" + Crystalline.MOD_ID + "/gemStats/" + itemPath + ".json");
		
		System.out.println(reader);
		
		Gson gson = new Gson();
		
		if (reader != null) {
			List<String> stats = Arrays.asList(gson.fromJson(reader, String[].class));
			System.out.println(stats);
		}
		
		//int[] parsedStats = gson.fromJson(jsonFile, socketGemParser.class);
		
		/*if (stack.getItem() == Items.DIAMOND) {
			stats[0] = 1;
			stats[1] = 1;
			stats[2] = 1;
			System.out.println("diamond in socket");
		}
		
		if (stack.getItem() == Items.EMERALD) {
			stats[0] = 2;
			stats[1] = 1;
			stats[2] = 0;
			System.out.println("emerald in socket");
		}*/
		
		return placeholder;
	}
	
}
