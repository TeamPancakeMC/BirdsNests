package com.pancake.birds_nests.mixin;

import com.pancake.birds_nests.LeavesDecayingEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {
    public LeavesBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }
    @Shadow @Final public static BooleanProperty PERSISTENT;
    @Shadow @Final public static IntegerProperty DISTANCE;

    /**
     * @author XiaoHuNao
     * @reason Add event for leaves decaying
     */
    @Overwrite
    public void randomTick(BlockState p_54451_, ServerLevel p_54452_, BlockPos p_54453_, Random p_54454_) {
        if (!p_54451_.getValue(PERSISTENT) && p_54451_.getValue(DISTANCE) == 7) {
            List<ItemStack> loot = getDrops(p_54451_, p_54452_, p_54453_, null);
            LeavesDecayingEvent leavesDecayingEvent = new LeavesDecayingEvent(p_54452_, p_54453_, p_54451_, loot);
            MinecraftForge.EVENT_BUS.post(leavesDecayingEvent);
            if (leavesDecayingEvent.isCanceled()) return;
            List<ItemStack> eventLoot = leavesDecayingEvent.getLoot();
            for (ItemStack stack : eventLoot) {
                popResource(p_54452_, p_54453_, stack);
            }
            p_54451_.spawnAfterBreak((ServerLevel)p_54452_, p_54453_, ItemStack.EMPTY);
            p_54452_.removeBlock(p_54453_, false);
        }
    }
}
