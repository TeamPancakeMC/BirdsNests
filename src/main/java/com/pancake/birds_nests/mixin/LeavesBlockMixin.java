package com.pancake.birds_nests.mixin;

import com.pancake.birds_nests.LeavesDecayingEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {
    public LeavesBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Shadow
    protected abstract boolean decaying(BlockState p_221386_);

    /**
     * @author XiaoHuNao
     * @reason Add event for leaves decaying
     */
    @Overwrite
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (this.decaying(state)) {
            List<ItemStack> loot = getDrops(state, level, pos, null);

            LeavesDecayingEvent leavesDecayingEvent = new LeavesDecayingEvent(level, pos, state, loot);
            MinecraftForge.EVENT_BUS.post(leavesDecayingEvent);
            if (leavesDecayingEvent.isCanceled()) return;
            List<ItemStack> eventLoot = leavesDecayingEvent.getLoot();
            for (ItemStack stack : eventLoot) {
                popResource(level, pos, stack);
            }
            state.spawnAfterBreak((ServerLevel)level, pos, ItemStack.EMPTY, true);
            level.removeBlock(pos, false);
        }

    }
}
