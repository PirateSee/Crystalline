package com.piratesee.crystalline.recipe;

import com.piratesee.crystalline.Crystalline;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Crystalline.MOD_ID);
	
    public static final RegistryObject<RecipeSerializer<GemInfusingStationRecipe>> GEM_INFUSING_SERIALIZER =
            SERIALIZERS.register("gem_infusing", () -> GemInfusingStationRecipe.Serializer.INSTANCE);
    
    public static final RegistryObject<RecipeSerializer<GemInjectingRecipe>> GEM_INJECTING_SERIALIZER =
            SERIALIZERS.register("gem_injecting", () -> GemInjectingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
