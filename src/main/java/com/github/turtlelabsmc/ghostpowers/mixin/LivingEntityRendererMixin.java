package com.github.turtlelabsmc.ghostpowers.mixin;

import com.github.turtlelabsmc.ghostpowers.GhostPowers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin extends EntityRenderer<LivingEntity> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    /*renderColorChangedModel just changes the color of the PlayerEntity
    based on if it has the Ghost Effect or not*/
    @Environment(EnvType.CLIENT)
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", ordinal = 0))
    private void renderColorChangedModel(EntityModel model, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, LivingEntity player) {
        if (player.hasStatusEffect(GhostPowers.GHOST_EFFECT)){
            red = 0.2f;
            green = 0.2f;
            blue = 0.2f;
            alpha = 0.05f;
        }
        model.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}