package com.piratesee.crystalline;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class SocketGems {

	@SuppressWarnings("unused")
	public static int[] socketGemTester(ItemStack stack) {
		
		//ResourceLocation jsonFile = new ResourceLocation(Crystalline.MOD_ID, "gemStats/" + ForgeRegistries.ITEMS.getKey(stack.getItem()) );
		
		BufferedReader reader = null;
		
		//1. resource location
		String itemPath = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().replace(':', '_');
		ResourceLocation loc = new ResourceLocation(Crystalline.MOD_ID, "gemStats/" + itemPath + ".json");
		System.out.println(loc.toString());
		
		//2. ???
		
		//3. parsed json
		
		return parsedJson;
	}
	
}
