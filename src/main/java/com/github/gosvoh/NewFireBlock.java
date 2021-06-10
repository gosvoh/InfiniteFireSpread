package com.github.gosvoh;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class NewFireBlock extends FireBlock {

    public NewFireBlock(Properties builder) {
        super(builder);
    }

    private static int getTickCooldown(Random rand) {
        return 30 + rand.nextInt(10);
    }
    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        worldIn.getPendingBlockTicks().scheduleTick(pos, this, getTickCooldown(worldIn.rand));
        if (worldIn.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            if (!state.isValidPosition(worldIn, pos)) {
                worldIn.removeBlock(pos, false);
            }
            BlockState blockstate = worldIn.getBlockState(pos.down());
            boolean flag = blockstate.isFireSource(worldIn, pos, Direction.UP);
            int i = state.get(AGE);
            if (!flag && worldIn.isRaining() && this.canDie(worldIn, pos) && rand.nextFloat() < 0.2F + (float)i * 0.03F) {
                worldIn.removeBlock(pos, false);
                return;
            }
            if (i < 15) {
                state = state.with(AGE, i + 1);
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }
}
