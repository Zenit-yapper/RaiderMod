package com.voracious.hub.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        // We only want to add text above players
        if (entity instanceof PlayerEntity player) {
            // This is the stable hook for 1.21.4.
            // Add a small piece of text
            renderRank(player, matrices, vertexConsumers, light);
        }
    }

    private void renderRank(PlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Step 1: Create the Rank text (e.g., "RANK: ELITE")
        Text rankText = Text.literal("RANK: ELITE").formatted(net.minecraft.util.Formatting.GOLD);
        
        // Step 2: Push the matrix (save current position)
        matrices.push();
        
        // Step 3: Move the rank text *up* above the player's head
        matrices.translate(0.0D, player.getHeight() + 0.5D, 0.0D);
        
        // Step 4: Add the actual rendering code.
        // This requires standard Fabric API functions
        
        // Step 5: Pop the matrix (restore previous position)
        matrices.pop();
    }
}
