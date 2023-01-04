package com.piratesee.crystalline.datagen.client;

import com.piratesee.crystalline.AwsomeMod;
import com.piratesee.crystalline.init.BlockInit;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper helper) {
		super(gen, AwsomeMod.MOD_ID, helper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		simpleBlock(BlockInit.PROTOTYPE.get());
	}
	
}
