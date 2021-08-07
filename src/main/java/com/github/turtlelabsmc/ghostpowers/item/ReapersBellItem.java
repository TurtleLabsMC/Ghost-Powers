package com.github.turtlelabsmc.ghostpowers.item;

import com.github.turtlelabsmc.ghostpowers.GhostPowers;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ReapersBellItem extends Item {
    public ReapersBellItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.hasStatusEffect(GhostPowers.GHOST_EFFECT)) {
            user.addStatusEffect(new StatusEffectInstance(GhostPowers.GHOST_EFFECT, Integer.MAX_VALUE, 0, false, false, true));
        }else
            user.removeStatusEffect(GhostPowers.GHOST_EFFECT);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

}
