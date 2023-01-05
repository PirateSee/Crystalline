package com.piratesee.crystalline.datagen.client;

import com.piratesee.crystalline.Crystalline;
import com.piratesee.crystalline.init.BlockInit;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper helper) {
		super(gen, Crystalline.MOD_ID, helper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		simpleBlock(BlockInit.PROTOTYPE.get());
		simpleBlock(BlockInit.DIAMONDMETAL_BLOCK.get());
		simpleBlock(BlockInit.CUT_DIAMONDMETAL.get());
		simpleBlock(BlockInit.DIAMONDMETAL_PILLAR.get());
	}
	
}
