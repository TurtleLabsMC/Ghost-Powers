package com.github.turtlelabsmc.ghostpowers.entity.effect;

import com.github.turtlelabsmc.ghostpowers.GhostPowers;
import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.PlayerAbility;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.jmx.Server;

public class GhostStatusEffect extends StatusEffect {
    private static final AbilitySource GHOST_EFFECT = Pal.getAbilitySource(new Identifier(GhostPowers.MODID, "ghost_effect_flight"));
    private double startingY;

    public GhostStatusEffect() {
        super(StatusEffectType.BENEFICIAL, 0xFFFFFF);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            startingY = player.getY();
            player.world.playSound(null, player.getBlockPos(), GhostPowers.REAPERS_BELL_RING, SoundCategory.PLAYERS, 1f, 1f);
            player.noClip = true;
            if (!player.world.isClient()) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                if(serverPlayer.interactionManager.isSurvivalLike()) {
                    Pal.grantAbility(player, VanillaAbilities.ALLOW_FLYING, GHOST_EFFECT);
                    Pal.grantAbility(player, VanillaAbilities.FLYING, GHOST_EFFECT);
                }
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            double y = player.getY();
            System.out.println(((y - startingY) >= -3 && (y - startingY) <= 3));
            if (((y - startingY) >= -3 && (y - startingY) <= 3))
                player.setVelocity(player.getVelocity().getX(), 0, player.getVelocity().getZ());
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.world.playSound(null, player.getBlockPos(), GhostPowers.REAPERS_BELL_RING, SoundCategory.PLAYERS, 1f, 1f);
            player.noClip = false;
            if (!player.world.isClient()) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                if(serverPlayer.interactionManager.isSurvivalLike()) {
                    Pal.grantAbility(player, VanillaAbilities.ALLOW_FLYING, GHOST_EFFECT);
                    Pal.grantAbility(player, VanillaAbilities.FLYING, GHOST_EFFECT);
                }
            }
        }
    }
}
