package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NutrientAgarEntity extends AbstractInfectiveEntity {

    public NutrientAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.NUTRIENT_AGAR_BE.get(), pos, state);
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
