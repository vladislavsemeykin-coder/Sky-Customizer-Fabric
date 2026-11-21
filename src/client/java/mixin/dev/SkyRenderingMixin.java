
package dev.skycustomizer.mixin;

import dev.skycustomizer.GalaxySkyRenderer;
import dev.skycustomizer.SkyCustomizerClient;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRendering.class)
public abstract class SkyRenderingMixin {

    @Inject(
        method = "method_62307",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private void skycustomizer$galaxy(
            MatrixStack matrices,
            VertexConsumerProvider.Immediate consumers,
            float rotation,
            int moonPhase,
            float alpha,
            float starBrightness,
            Fog fog,
            CallbackInfo ci) {

        if (!SkyCustomizerClient.isGalaxySky()) {
            return;
        }

        GalaxySkyRenderer.render(matrices, consumers);
        ci.cancel();
    }
}
