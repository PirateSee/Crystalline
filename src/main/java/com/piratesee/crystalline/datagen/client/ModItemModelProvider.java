package com.piratesee.crystalline.datagen.client;

import com.piratesee.crystalline.Crystalline;
import com.piratesee.crystalline.init.BlockInit;
import com.piratesee.crystalline.init.ItemInit;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItemModelProvider extends ItemModelProvider {
	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, Crystalline.MOD_ID, helper);
		// TODO Auto-generated constructor stub
	}

	protected void simpleBlockItem(Item item) {
		getBuilder(ForgeRegistries.ITEMS.getKey(item).toString()).parent(getExistingFile(modLoc("block/" + ForgeRegistries.ITEMS.getKey(item).getPath())));
	}
	
	protected void oneLayerItem(Item item, ResourceLocation texture) {
		ResourceLocation itemTexture = new ResourceLocation(texture.getNamespace(), "item/" + texture.getPath());
		if (existingFileHelper.exists(itemTexture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
			getBuilder(ForgeRegistries.ITEMS.getKey(item).getPath()).parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", itemTexture);
		} else {
			System.out.println("Texture for " + ForgeRegistries.ITEMS.getKey(item).toString() + " not present at " + itemTexture.toString());
		}
	}
	
	protected void oneLayerItem(Item item) {
		oneLayerItem(item, ForgeRegistries.ITEMS.getKey(item));
	}
	
	@Override
	protected void registerModels() {
		//block items
		simpleBlockItem(BlockInit.PROTOTYPE.get().asItem());
		simpleBlockItem(BlockInit.DIAMONDMETAL_BLOCK.get().asItem());
		simpleBlockItem(BlockInit.CUT_DIAMONDMETAL.get().asItem());
		simpleBlockItem(BlockInit.DIAMONDMETAL_PILLAR.get().asItem());
		
		//reg items
		oneLayerItem(ItemInit.THE_THING.get());
		oneLayerItem(ItemInit.DIAMONDMETAL.get());
		oneLayerItem(ItemInit.DIAMONDMETAL_NUGGET.get());
		
	}

}
