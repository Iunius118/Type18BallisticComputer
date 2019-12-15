package com.github.iunius118.type18gunsight;


import com.github.iunius118.type18gunsight.client.ClientEventHandler;
import com.github.iunius118.type18gunsight.client.ballisticcomputer.BallisticComputerSystem;
import com.github.iunius118.type18gunsight.config.GunSightConfig;
import com.github.iunius118.type18gunsight.item.GunSightItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Type18GunSight.MOD_ID)
public class Type18GunSight {
    public static final String MOD_ID = "type18gunsight";
    public static final String MOD_NAME = "Type 18 Ballistic Computer";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static BallisticComputerSystem ballisticComputerSystem;

    @ObjectHolder(MOD_ID)
    public static class ITEMS {
        public static final Item gun_sight = null;
    }

    public Type18GunSight() {
        // Register lifecycle event listeners
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::initClient);

        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, GunSightConfig.clientSpec);
    }

    public void initClient(final FMLClientSetupEvent event) {
        ballisticComputerSystem = new BallisticComputerSystem();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new GunSightItem(new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName("gun_sight"));
        }
    }
}
