package com.github.iunius118.type18gunsight;

import com.github.iunius118.type18gunsight.client.ClientEventHandler;
import com.github.iunius118.type18gunsight.client.ballisticcomputer.BallisticComputerSystem;
import com.github.iunius118.type18gunsight.config.GunSightConfig;
import com.github.iunius118.type18gunsight.item.GunSightItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import org.apache.logging.log4j.Logger;

@Mod(   modid = Type18GunSight.MOD_ID,
        name = Type18GunSight.MOD_NAME,
        version = Type18GunSight.MOD_VERSION,
        guiFactory = "com.github.iunius118.type18gunsight.client.gui.ConfigGuiFactory")
@EventBusSubscriber
public class Type18GunSight {
    public static final String MOD_ID = "type18gunsight";
    public static final String MOD_NAME = "Type 18 Ballistic Computer";
    public static final String MOD_VERSION = "1.12.2-1.0.0.0";

    public static final GunSightConfig CONFIG = new GunSightConfig();
    public static Logger logger;

    public static BallisticComputerSystem ballisticComputerSystem;

    @ObjectHolder(MOD_ID)
    public static class ITEMS {
        public static final Item gun_sight = null;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        if (event.getSide().isClient()) {
            ballisticComputerSystem = new BallisticComputerSystem();
            MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new GunSightItem().setRegistryName("gun_sight").setTranslationKey(MOD_ID + ".gun_sight").setCreativeTab(CreativeTabs.COMBAT));
    }
}
