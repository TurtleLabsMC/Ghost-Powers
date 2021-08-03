package com.github.turtlelabsmc.ghostpowers;

import com.github.turtlelabsmc.ghostpowers.entity.effect.GhostStatusEffect;
import com.github.turtlelabsmc.ghostpowers.item.ReapersBellItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GhostPowers implements ModInitializer {
    public static final String MODID = "ghostpowers";
    private static final Logger LOGGER = LogManager.getLogger();

    //Status Effects
    public static final StatusEffect GHOST_EFFECT = new GhostStatusEffect();

    //Items
    public static final ReapersBellItem BELL_ITEM = new ReapersBellItem(new FabricItemSettings().maxCount(1).fireproof().group(ItemGroup.COMBAT));

    //Sounds
    public static final SoundEvent REAPERS_BELL_RING = new SoundEvent(new Identifier(MODID, "reapers_bell_ring"));
    @Override
    public void onInitialize() {
        LOGGER.info(MODID + " wurde geladen!");

        //Register Status Effects
        Registry.register(Registry.STATUS_EFFECT, new Identifier(MODID, "ghost"), GHOST_EFFECT);

        //Register Items
        Registry.register(Registry.ITEM, new Identifier(MODID, "reapers_bell"), BELL_ITEM);

        //Register Sounds
        Registry.register(Registry.SOUND_EVENT, new Identifier(MODID, "reapers_bell_ring"), REAPERS_BELL_RING);
    }

}
