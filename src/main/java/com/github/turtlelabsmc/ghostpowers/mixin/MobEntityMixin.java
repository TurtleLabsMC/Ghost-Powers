package com.github.turtlelabsmc.ghostpowers.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow protected MoveControl moveControl;

    @Shadow public abstract void setAiDisabled(boolean aiDisabled);

    private static final TrackedData<Boolean> IS_POSSESSED;

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IS_POSSESSED, false);
    }

    protected MobEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        if(getPossessed()){
            setAiDisabled(true);
        }
    }

    public Boolean getPossessed() {
        return this.dataTracker.get(IS_POSSESSED);
    }

    static {
        IS_POSSESSED = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
