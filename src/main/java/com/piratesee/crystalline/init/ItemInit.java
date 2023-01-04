package com.piratesee.crystalline.init;

import com.google.common.base.Supplier;
import com.piratesee.crystalline.Crystalline;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Crystalline.MOD_ID);
	
	public static final RegistryObject<Item> THE_THING = register("the_thing", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<Item> DIAMONDMETAL = register("diamondmetal", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
		return ITEMS.register(name, item);
	}
}
