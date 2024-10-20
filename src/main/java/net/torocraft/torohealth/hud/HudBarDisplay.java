package net.torocraft.torohealth.hud;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import net.torocraft.torohealth.util.ValueUtils;

public class HudBarDisplay {

	private static final Identifier ICON_TEXTURES = new Identifier("textures/gui/icons.png");

	private final MinecraftClient mc;
	private final DrawableHelper gui;

	public HudBarDisplay(MinecraftClient mc, DrawableHelper gui) {
		this.mc = mc;
		this.gui = gui;
	}

	public void draw(MatrixStack matrix, LivingEntity entity) {

		// The starting offset of horizontal position.
		int xOffset = 0;
		TextRenderer textRenderer = mc.textRenderer;

		// Set render color to 0xFFFFFF and make it default 100% opaque white.
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderTexture(0, ICON_TEXTURES);

		// Enable OpenGL Blend to process colors.
		RenderSystem.enableBlend();

		HealthBarRenderer.render(matrix, entity, 63, 14, 130, false);

		String name = getEntityName(entity);
		int healthMax = MathHelper.ceil(ValueUtils.bypassLimit(entity, EntityAttributes.GENERIC_MAX_HEALTH));
		int healthCur = Math.min(MathHelper.ceil(entity.getHealth()), healthMax);
		String healthText = healthCur + "/" + healthMax;

		DrawableHelper.drawStringWithShadow(matrix, textRenderer, name, xOffset, 2, 0xffffff);

		textRenderer.drawWithShadow(matrix, name, xOffset, 2, 0xffffff);
		xOffset += textRenderer.getWidth(name) + 5;

		renderHeartIcon(matrix, xOffset, (int) 2);
		xOffset += 10;

		textRenderer.drawWithShadow(matrix, healthText, xOffset, 2, 0xe0e0e0);
		xOffset += textRenderer.getWidth(healthText) + 5;

		int armor = entity.getArmor();
		if (armor > 0) {
			renderArmorIcon(matrix, xOffset, (int) 2);
			xOffset += 10;
			textRenderer.drawWithShadow(matrix, entity.getArmor() + "", xOffset, 2, 0xe0e0e0);
		}

		RenderSystem.disableBlend();
	}

	private String getEntityName(LivingEntity entity) {
		return entity.getDisplayName().getString();
	}

	private void renderArmorIcon(MatrixStack matrix, int x, int y) {
		RenderSystem.setShaderTexture(0, ICON_TEXTURES);
		gui.drawTexture(matrix, x, y, 34, 9, 9, 9);
	}

	private void renderHeartIcon(MatrixStack matrix, int x, int y) {
		RenderSystem.setShaderTexture(0, ICON_TEXTURES);
		gui.drawTexture(matrix, x, y, 16, 0, 9, 9);
		gui.drawTexture(matrix, x, y, 16 + 36, 0, 9, 9);
	}
}