package com.github.iunius118.type18gunsight.client;


import com.github.iunius118.type18gunsight.Type18GunSight;
import com.github.iunius118.type18gunsight.client.ballisticcomputer.BallisticComputerSystem;
import com.github.iunius118.type18gunsight.config.GunSightConfig;
import com.github.iunius118.type18gunsight.item.GunSightItem;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler {
    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.ClientTickEvent.Phase.END) {
            return;
        }

        World world = Minecraft.getInstance().world;
        Type18GunSight.ballisticComputerSystem.updateTrackerAndComputer(world);
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        World world = Minecraft.getInstance().world;
        Type18GunSight.ballisticComputerSystem.updateIndicator(world, event.getPartialTicks());
    }

    @SubscribeEvent
    public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR && mc.getRenderManager().options != null && mc.getRenderManager().options.thirdPersonView < 1) {

            if (!(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof GunSightItem) && !(mc.player.getHeldItem(Hand.OFF_HAND).getItem() instanceof GunSightItem)) {
                return;
            }

            BallisticComputerSystem bcs = Type18GunSight.ballisticComputerSystem;

            // For debug
            /*
            if (bcs.computer.isValid()) {
                mc.player.rotationYaw = bcs.computer.rotationYaw;
                mc.player.rotationPitch = bcs.computer.rotationPitch;
            }
            //*/

            Vec3d vec3Target = bcs.indicator.getTargetScreenPos();
            Vec3d vec3Marker = bcs.indicator.getTargetFutureScreenPos();

            if (vec3Target == null && vec3Marker == null) {
                return;
            }

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexBuffer = tessellator.getBuffer();
            double markerSize = 4.0D;

            GlStateManager.disableDepthTest();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture();
            GlStateManager.color4f(
                    (float) GunSightConfig.marker_color.colorRed,
                    (float) GunSightConfig.marker_color.colorGreen,
                    (float) GunSightConfig.marker_color.colorBlue,
                    (float) GunSightConfig.marker_color.colorAlpha);
            GlStateManager.lineWidth(1.0F);

            if (vec3Target != null) {
                double x = vec3Target.x;
                double y = vec3Target.y;
                vertexBuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
                vertexBuffer.pos(x - markerSize, y - markerSize, 0.0D).endVertex();
                vertexBuffer.pos(x + markerSize, y - markerSize, 0.0D).endVertex();
                vertexBuffer.pos(x + markerSize, y + markerSize, 0.0D).endVertex();
                vertexBuffer.pos(x - markerSize, y + markerSize, 0.0D).endVertex();
                tessellator.draw();
            }

            if (vec3Marker != null) {
                double x = vec3Marker.x;
                double y = vec3Marker.y;
                vertexBuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
                vertexBuffer.pos(x, y - markerSize, 0.0D).endVertex();
                vertexBuffer.pos(x + markerSize, y, 0.0D).endVertex();
                vertexBuffer.pos(x, y + markerSize, 0.0D).endVertex();
                vertexBuffer.pos(x - markerSize, y, 0.0D).endVertex();
                tessellator.draw();
            }

            GlStateManager.enableTexture();
            GlStateManager.enableDepthTest();
        }
    }
}
