package com.piratesee.crystalline.event;

import com.piratesee.crystalline.Crystalline;
import com.piratesee.crystalline.block.entity.ModBlockEntities;
import com.piratesee.crystalline.block.entity.renderer.GemInjectorBlockEntityRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    /*@Mod.EventBusSubscriber(modid = Crystalline.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

    }*/

    @Mod.EventBusSubscriber(modid = Crystalline.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.GEM_INJECTOR.get(),
                    GemInjectorBlockEntityRenderer::new);
        }
    }
	
}
