
package dev.skycustomizer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public final class GalaxySkyRenderer {

    private static final Identifier TEXTURE = Identifier.of(
        "skycustomizer",
        "textures/sky/galaxy_sky.png"
    );

    private static final float RADIUS = 90.0f;
    private static final int SEGMENTS = 48;
    private static final int RINGS = 24;

    private GalaxySkyRenderer() {
    }

    public static void render(
            MatrixStack matrices,
            VertexConsumerProvider.Immediate consumers) {

        VertexConsumer buffer = consumers.getBuffer(
            RenderLayer.getEntityCutoutNoCull(TEXTURE)
        );

        for (int row = 0; row < RINGS; row++) {
            float v0 = (float) row / RINGS;
            float v1 = (float) (row + 1) / RINGS;

            for (int col = 0; col < SEGMENTS; col++) {
                float u0 = (float) col / SEGMENTS;
                float u1 = (float) (col + 1) / SEGMENTS;

                vertex(buffer, matrices, u0, v0);
                vertex(buffer, matrices, u0, v1);
                vertex(buffer, matrices, u1, v1);
                vertex(buffer, matrices, u1, v0);
            }
        }

        consumers.draw();
    }

    private static void vertex(
            VertexConsumer buffer,
            MatrixStack matrices,
            float u,
            float v) {

        double longitude = u * Math.PI * 2.0;
        double latitude = v * Math.PI;

        float x = RADIUS * (float) (
            Math.sin(latitude) * Math.cos(longitude)
        );

        float y = RADIUS * (float) Math.cos(latitude);

        float z = RADIUS * (float) (
            Math.sin(latitude) * Math.sin(longitude)
        );

        buffer.vertex(matrices.peek(), x, y, z)
            .color(0xFFFFFFFF)
            .texture(u, v)
            .overlay(OverlayTexture.DEFAULT_UV)
            .light(0x00F000F0)
            .normal(0.0f, 1.0f, 0.0f);
    }
}
