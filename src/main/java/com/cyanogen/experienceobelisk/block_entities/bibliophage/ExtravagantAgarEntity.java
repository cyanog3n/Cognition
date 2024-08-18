package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ExtravagantAgarEntity extends AbstractAgarEntity {

    public ExtravagantAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.EXTRAVAGANT_AGAR_BE.get(), pos, state);
    }

}
