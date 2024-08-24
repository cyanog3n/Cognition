package com.cyanogen.experienceobelisk.block.bibliophage;

import com.cyanogen.experienceobelisk.block_entities.bibliophage.InsightfulAgarEntity;
import com.cyanogen.experienceobelisk.block_entities.bibliophage.NutrientAgarEntity;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class InsightfulAgarBlock extends HalfTransparentBlock implements EntityBlock {

    public InsightfulAgarBlock() {
        super(Properties.copy(Blocks.SLIME_BLOCK)
                .noOcclusion()
                .emissiveRendering((state,getter,pos)->true)
                .isViewBlocking((state,getter,pos)->false));
    }

    //-----BLOCK ENTITY-----//

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == RegisterBlockEntities.INSIGHTFUL_AGAR_BE.get() ? InsightfulAgarEntity::tick : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.INSIGHTFUL_AGAR_BE.get().create(pos, state);
    }
}
