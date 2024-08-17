package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractAgarEntity extends AbstractInfectiveEntity{

    public AbstractAgarEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    double infectivity = 0.05;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof NutrientAgarEntity agar){

            if(level.getGameTime() % 20 == 0 && Math.random() <= agar.infectivity){
                agar.infectAdjacent(level, pos);
            }

        }
    }

}
