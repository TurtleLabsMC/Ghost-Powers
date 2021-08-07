package com.github.turtlelabsmc.ghostpowers.mixin;

import com.github.turtlelabsmc.ghostpowers.GhostPowers;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("deprecation")
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow
    public abstract Block getBlock();

    @Shadow
    protected abstract BlockState asBlockState();

    @Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", cancellable = true)
    private void phaseThroughBlocks(net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> info) {
        VoxelShape blockShape = getBlock().getCollisionShape(asBlockState(), world, pos, context);
        if(!blockShape.isEmpty() && context instanceof EntityShapeContext) {
            EntityShapeContext esc = (EntityShapeContext)context;
            if(esc.getEntity().isPresent()) {
                Entity entity = esc.getEntity().get();
                boolean isAbove = isAbove(entity, blockShape, pos, false);
                if (entity instanceof LivingEntity livingEntity){
                    if (livingEntity.hasStatusEffect(GhostPowers.GHOST_EFFECT)){
                        info.setReturnValue(VoxelShapes.empty());
                    }
                }
            }
        }
    }

    @Unique
    private boolean isAbove(Entity entity, VoxelShape shape, BlockPos pos, boolean defaultValue) {
        return entity.getY() > (double) pos.getY() + shape.getMax(Direction.Axis.Y) - (entity.isOnGround() ? 8.05 / 16.0 : 0.0015);
    }
}
