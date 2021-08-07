package com.github.turtlelabsmc.ghostpowers.entity.effect;

import com.github.turtlelabsmc.ghostpowers.GhostPowers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;

public class GhostStatusEffect extends StatusEffect {
    public GhostStatusEffect() {super(StatusEffectType.BENEFICIAL, 0xFFFFFF);}

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.world.playSound(null, player.getBlockPos(), GhostPowers.REAPERS_BELL_RING, SoundCategory.PLAYERS, 1f, 1f);
            //Todo: Add to Player Ghost Logic
            player.noClip = true;
            System.out.println("noClip Check");
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.world.playSound(null, player.getBlockPos(), GhostPowers.REAPERS_BELL_RING, SoundCategory.PLAYERS, 1f, 1f);
            //Todo: Remove Ghost Logic from Player
            player.noClip = false;
        }
    }
}
