package com.voracious.hub.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) {
            renderRank(player, matrices, vertexConsumers, light);
        }
    }

    private void renderRank(PlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Text rankText = Text.literal("RANK: ELITE").formatted(Formatting.GOLD);
        matrices.push();
        // Shift position up so it doesn't overlap the name.
        matrices.translate(0.0D, player.getHeight() + 0.5D, 0.0D);
        matrices.pop();
    }
}
