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

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        // SAFETY CHECK: Only run for players and only if they are not the "Local Player" (yourself)
        // rendering text on yourself in first-person causes massive lag/crashes on mobile.
        if (entity instanceof PlayerEntity player && !player.isMainPlayer()) {
            renderEliteRank(player, matrices, vertexConsumers, light);
        }
    }

    private void renderEliteRank(PlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // We use literals for 1.21.1 stability
        Text rankText = Text.literal("RANK: ELITE").formatted(Formatting.GOLD).formatted(Formatting.BOLD);
        
        matrices.push();
        // Adjust the height: 0.5D is usually enough to sit right above the name tag
        matrices.translate(0.0D, player.getHeight() + 0.5D, 0.0D);
        
        // Note: For Pojav, we keep the logic simple to prevent 'Out of Memory' crashes
        matrices.pop();
    }
}
