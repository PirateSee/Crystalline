package com.piratesee.crystalline;

import com.piratesee.crystalline.block.entity.ModBlockEntities;
import com.piratesee.crystalline.init.BlockInit;
import com.piratesee.crystalline.init.ItemInit;
import com.piratesee.crystalline.networking.ModMessages;
import com.piratesee.crystalline.recipe.ModRecipes;
import com.piratesee.crystalline.screen.GemInfusingStationScreen;
import com.piratesee.crystalline.screen.GemInjectorScreen;
import com.piratesee.crystalline.screen.ModMenuTypes;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("crystalline")
public class Crystalline {

	public static final String MOD_ID = "crystalline";
	
	
	
	public Crystalline() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ItemInit.ITEMS.register(bus);
		BlockInit.BLOCKS.register(bus);
		
		ModBlockEntities.register(bus);
		ModMenuTypes.register(bus);
		
		ModRecipes.register(bus);
		
		bus.addListener(this::commonSetup);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
    public void addReloadListenerEvent(AddReloadListenerEvent event)
    {
        event.addListener(new SocketGems());
        System.out.println("listener");
    }
	
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        	
        });
        ModMessages.register();
    }
	
	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			MenuScreens.register(ModMenuTypes.GEM_INFUSING_STATION_MENU.get(), GemInfusingStationScreen::new);
			MenuScreens.register(ModMenuTypes.GEM_INJECTOR_MENU.get(), GemInjectorScreen::new);
		}
	}
}
