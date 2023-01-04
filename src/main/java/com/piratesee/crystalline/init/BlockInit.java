package com.piratesee.crystalline.init;

import java.util.function.Function;

import com.google.common.base.Supplier;
import com.piratesee.crystalline.Crystalline;
import com.piratesee.crystalline.block.custom.GemInfusingStationBlock;
import com.piratesee.crystalline.block.custom.GemInjectorBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Crystalline.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = ItemInit.ITEMS;
	
	public static final RegistryObject<Block> PROTOTYPE = register("prototype", () -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	
	public static final RegistryObject<Block> GEM_INFUSING_STATION = register("gem_infusing_station", () -> new GemInfusingStationBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	
	public static final RegistryObject<Block> GEM_INJECTOR = register("gem_injector", () -> new GemInjectorBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	
	private static <T extends Block> RegistryObject<T> registerBlock(final String name, final Supplier<? extends T> block) {
		return BLOCKS.register(name, block);
	}
	
	private static <T extends Block> RegistryObject<T> register(final String name, final Supplier<? extends T> block,
			Function<RegistryObject<T>, Supplier<? extends Item>> item) {
		RegistryObject<T> obj = registerBlock(name, block);
		ITEMS.register(name, item.apply(obj));
		return obj;
	}
}
