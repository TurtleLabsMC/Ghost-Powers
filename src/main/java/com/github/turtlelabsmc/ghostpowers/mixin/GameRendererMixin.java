package com.github.turtlelabsmc.ghostpowers.mixin;

import com.github.turtlelabsmc.ghostpowers.GhostPowers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private Camera camera;
    @Shadow @Final private MinecraftClient client;

    // PHASING: remove_blocks
    private HashMap<BlockPos, BlockState> savedStates = new HashMap<>();

    @Inject(at = @At(value = "HEAD"), method = "render")
    private void beforeRender(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
        if (camera.getFocusedEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.hasStatusEffect(GhostPowers.GHOST_EFFECT)) {
                //Changing these values will allow the player to disable neighboring blocks.
                //0.25F, 0.05F, 0.25F only disables the current block you're in
                Set<BlockPos> eyePositions = getEyePos(0.25F, 0.05F, 0.25F);

                Set<BlockPos> noLongerEyePositions = new HashSet<>();
                for (BlockPos p : savedStates.keySet()) {
                    if (!eyePositions.contains(p)) {
                        noLongerEyePositions.add(p);
                    }
                }
                for (BlockPos eyePosition : noLongerEyePositions) {
                    BlockState state = savedStates.get(eyePosition);
                    client.world.setBlockState(eyePosition, state);
                    savedStates.remove(eyePosition);
                }
                for (BlockPos p : eyePositions) {
                    BlockState stateAtP = client.world.getBlockState(p);
                    if (!savedStates.containsKey(p) && !client.world.isAir(p) && !(stateAtP.getBlock() instanceof FluidBlock)) {
                        savedStates.put(p, stateAtP);
                        client.world.setBlockStateWithoutNeighborUpdates(p, Blocks.AIR.getDefaultState());
                    }
                }
            } else if (savedStates.size() > 0) {
                Set<BlockPos> noLongerEyePositions = new HashSet<>(savedStates.keySet());
                for (BlockPos eyePosition : noLongerEyePositions) {
                    BlockState state = savedStates.get(eyePosition);
                    client.world.setBlockState(eyePosition, state);
                    savedStates.remove(eyePosition);
                }
            }
        }
    }

    private Set<BlockPos> getEyePos(float rangeX, float rangeY, float rangeZ) {
        Vec3d pos = camera.getFocusedEntity().getPos().add(0, camera.getFocusedEntity().getEyeHeight(camera.getFocusedEntity().getPose()), 0);
        Box cameraBox = new Box(pos, pos);
        cameraBox = cameraBox.expand(rangeX, rangeY, rangeZ);
        HashSet<BlockPos> set = new HashSet<>();
        BlockPos.stream(cameraBox).forEach(p -> set.add(p.toImmutable()));
        return set;
    }

    // PHASING
    /*@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"), method = "renderWorld")
    private void preventThirdPerson(Camera camera, BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        if (camera.getFocusedEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.hasStatusEffect(GhostPowers.GHOST_EFFECT)) {
                camera.update(area, focusedEntity, false, false, tickDelta);
            } else {
                camera.update(area, focusedEntity, thirdPerson, inverseView, tickDelta);
            }
        }
    }*/
}
