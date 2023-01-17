package com.piratesee.crystalline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class SocketGems extends SimpleJsonResourceReloadListener {

	//credit to ItsKillerluc#5688 from the KaupenHub discord (i was clueless and needed their help)
	
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	
    public static final Codec<Map<Either<TagKey<Item>, Item>, Integer>> codec = Codec.unboundedMap(Codec.either(TagKey.hashedCodec(Registry.ITEM_REGISTRY), Registry.ITEM.byNameCodec()), Codec.INT);
	
    private static final Map<Either<TagKey<Item>, Item>, Integer> charges = new HashMap<>();

    public static final Map<Ingredient, Integer> stats = new HashMap<>();
    
    public static boolean contains(ItemStack ingredient){
        return stats.keySet().stream().anyMatch(ingredient1 -> ingredient1.test(ingredient));
    }

    public static int getCharge(ItemStack key) {
        if (stats.keySet().stream().anyMatch(ingredient -> ingredient.test(key))){
            return stats.get(stats.keySet().stream().filter(ingredient -> ingredient.test(key)).findFirst().orElseThrow());
        }
        System.out.print("no stats");
        return 0;
    }
    
	public SocketGems() {
		super(GSON, "stats");
	}

	    @Override
	    protected void apply(@NotNull Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
	        System.out.println("applying");
	    	for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
	            var entries = entry.getValue().getAsJsonObject().getAsJsonObject("entries");
	            var values = codec.parse(JsonOps.INSTANCE, entries).get();
	            if (values.left().isPresent()) {
	                for (int size = values.left().get().size(); size > 0; size--) {
	                    values.left().get().forEach(charges::putIfAbsent);
	                }
	            }
	        }
	        
	        Iterator<Map.Entry<Ingredient, Integer>> entries = stats.entrySet().iterator();
	        while (entries.hasNext()) {
	            Map.Entry<Ingredient, Integer> entry = entries.next();
	            System.out.println("Key = " + entry.getKey().toString() + ", Value = " + entry.getValue());
	        }
	        
	        var ingredients = charges.keySet().stream().map(ele -> {
	            AtomicReference<Ingredient> toReturn = new AtomicReference<>();
	            ele.ifRight(var -> toReturn.set(Ingredient.of(var))).ifLeft(var -> toReturn.set(Ingredient.of(var)));
	            return toReturn.get();
	        }).toList();
	
	        for (int i = 0, ingredientsSize = ingredients.size(); i < ingredientsSize; i++) {
	            stats.putIfAbsent(ingredients.get(i), List.copyOf(charges.values()).get(i));
	        }
	        
	    }
	}
	
	/*@SuppressWarnings("unused")
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
	}*/
