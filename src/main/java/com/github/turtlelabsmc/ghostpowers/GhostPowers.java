package com.github.turtlelabsmc.ghostpowers;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GhostPowers implements ModInitializer {
    public static final String MODID = "ghostpowers";
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info(MODID + " wurde geladen!");
    }
}
