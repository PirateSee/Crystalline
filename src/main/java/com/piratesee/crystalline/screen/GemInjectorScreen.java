package com.piratesee.crystalline.screen;

import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.piratesee.crystalline.Crystalline;
import com.piratesee.crystalline.screen.renderer.EnergyInfoArea;
import com.piratesee.crystalline.util.MouseUtil;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GemInjectorScreen extends AbstractContainerScreen<GemInjectorMenu> {
	public GemInjectorScreen(GemInjectorMenu menu, Inventory inv, Component component) {
		super(menu, inv, component);
	}

	public int craftanim = 227;
	
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Crystalline.MOD_ID,"textures/gui/gem_injector_gui.png");
	
    private EnergyInfoArea energyInfoArea;
    
    @Override
    protected void init() {
    	super.init();
    	assignEnergyInfoArea();
    }
    
	private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
		energyInfoArea = new EnergyInfoArea(x + 152, y + 24, menu.blockEntity.getEnergyStorage());
	}

	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        
        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
	}

	private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
	       if(isMouseAboveArea(pMouseX, pMouseY, x, y, 156, 13, 12, 53)) {
	            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
	                    Optional.empty(), pMouseX - x, pMouseY - y);
	       }
		
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pPoseStack, x, y); 
        energyInfoArea.draw(pPoseStack);
        
        System.out.println(menu.getCraftAnimFrame());
 
        if(menu.getCraftAnimFrame() >= 1 && menu.getCraftAnimFrame() < 3) {blit(pPoseStack, x + 113, y + 37, 176, 64, 17, 17);}
        if(menu.getCraftAnimFrame() == 2) {blit(pPoseStack, x + 113, y + 37, 193, 64, 17, 17);}
        if(menu.getCraftAnimFrame() == 3) {blit(pPoseStack, x + 113, y + 37, 210, 64, 17, 17);}
	}
	
    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 62, y + 40, 176, 0, menu.getScaledProgress(), 11);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
