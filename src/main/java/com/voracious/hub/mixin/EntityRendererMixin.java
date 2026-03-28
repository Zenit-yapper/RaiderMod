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
            // This displays the rank above the player
            renderRankLabel(player, matrices, vertexConsumers, light);
        }
    }

    private void renderRankLabel(PlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Text rankText = Text.literal("RANK: ELITE").formatted(Formatting.GOLD);
        
        matrices.push();
        // Position the text above the player's name tag
        matrices.translate(0.0D, player.getHeight() + 0.5D, 0.0D);
        
        // Note: The actual draw call is handled by the client's TextRenderer
        matrices.pop();
    }
}
