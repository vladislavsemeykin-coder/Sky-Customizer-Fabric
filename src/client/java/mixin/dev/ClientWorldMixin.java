package dev.skycustomizer.mixin;

import dev.skycustomizer.SkyCustomizerClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Inject(method = "getSkyColor(Lnet/minecraft/util/math/Vec3d;F)I", at = @At("RETURN"), cancellable = true)
    private void skycustomizer$color(Vec3d pos, float delta, CallbackInfoReturnable<Integer> cir) {
        int color = SkyCustomizerClient.getSkyColor();
        if (color != -1) cir.setReturnValue(color);
    }

    @Inject(method = "getStarBrightness(F)F", at = @At("RETURN"), cancellable = true)
    private void skycustomizer$stars(float delta, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(SkyCustomizerClient.starBrightness(cir.getReturnValue()));
    }
}
