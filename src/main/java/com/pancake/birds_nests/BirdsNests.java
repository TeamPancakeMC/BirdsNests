package com.pancake.birds_nests;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BirdsNests.MOD_ID)
public class BirdsNests {
    public static final String MOD_ID = "birds_nests";
    private static final Logger LOGGER = LogUtils.getLogger();
    public BirdsNests() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
