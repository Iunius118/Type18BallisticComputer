package com.github.iunius118.type18gunsight.client;

import com.github.iunius118.type18gunsight.Type18GunSight;
import com.github.iunius118.type18gunsight.client.ballisticcomputer.BallisticComputerSystem;
import com.github.iunius118.type18gunsight.config.GunSightConfig;
import com.github.iunius118.type18gunsight.item.GunSightItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler {
    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Type18GunSight.MOD_ID)) {
            ConfigManager.sync(Type18GunSight.MOD_ID, Config.Type.INSTANCE);
        }
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(Type18GunSight.ITEMS.gun_sight, 0, new ModelResourceLocation(Type18GunSight.ITEMS.gun_sight.getRegistryName(), "inventory"));
    }

    @SubscribeEvent
    public void onClientTickEvent(ClientTickEvent event) {
        if (event.phase == ClientTickEvent.Phase.END) {
            return;
        }

        World world = Minecraft.getMinecraft().world;
        Type18GunSight.ballisticComputerSystem.updateTrackerAndComputer(world);
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        World world = Minecraft.getMinecraft().world;
        Type18GunSight.ballisticComputerSystem.updateIndicator(world, event.getPartialTicks());
    }

    @SubscribeEvent
    public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.getType() == ElementType.HOTBAR && mc.getRenderManager().options != null && mc.getRenderManager().options.thirdPersonView < 1) {

            if (!(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof GunSightItem) && !(mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof GunSightItem)) {
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

            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.color(
                    (float) GunSightConfig.marker_color.colorRed,
                    (float) GunSightConfig.marker_color.colorGreen,
                    (float) GunSightConfig.marker_color.colorBlue,
                    (float) GunSightConfig.marker_color.colorAlpha);
            GlStateManager.glLineWidth(1.0F);

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

            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
        }
    }
}
