package com.voracious.hub.mixin;

import com.voracious.hub.api.TierAPI;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<S extends EntityRenderState> {
    
    @Inject(method = "render", at = @At("HEAD"))
    public void onRender(S state, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (state instanceof PlayerEntityRenderState ps) {
            // In 1.21.10+, PlayerEntityRenderState should contain the player's name and nametag state
            // ps.name is typically the raw username or display name
            String username = ps.name != null ? ps.name.getString() : null;
            if (username == null) return;

            String rank = TierAPI.getRank(username);
            
            // If rank is not cached, fetch it asynchronously
            if (rank == null) {
                TierAPI.fetch(username);
                return;
            }

            // Skip if still loading or errored
            if (rank.equals("...") || rank.equals("ERROR")) return;

            // Define the prefix
            String prefixStr = "§b§l[" + rank + "] §r";

            // Inject the prefix into the customName field which controls the nametag display
            if (ps.customName != null) {
                if (!ps.customName.getString().startsWith(prefixStr)) {
                    ps.customName = Text.literal(prefixStr).append(ps.customName);
                }
            } else if (ps.name != null) {
                // If customName is null, we can set it to the name with the prefix
                if (!ps.name.getString().startsWith(prefixStr)) {
                    ps.customName = Text.literal(prefixStr).append(ps.name);
                }
            }
        }
    }
}
