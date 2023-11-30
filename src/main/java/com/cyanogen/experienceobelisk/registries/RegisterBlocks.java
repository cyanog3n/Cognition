package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class RegisterBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ExperienceObelisk.MOD_ID);

    //-----FUNCTIONAL BLOCKS-----//

    public static final RegistryObject<Block> EXPERIENCE_OBELISK = BLOCKS.register("experience_obelisk", ExperienceObeliskBlock::new);
    public static final RegistryObject<Block> EXPERIENCE_FOUNTAIN = BLOCKS.register("experience_fountain", ExperienceFountainBlock::new);
    public static final RegistryObject<Block> PRECISION_DISPELLER = BLOCKS.register("precision_dispeller", PrecisionDispellerBlock::new);

    //-----DECORATIVE / CRAFTING-----//

    public static final RegistryObject<Block> COGNITIVE_ALLOY_BLOCK = registerBlock("cognitive_alloy_block", CognitiveAlloyBlock::new, RegisterCreativeTab.EXPERIENCE_OBELISK_TAB.get());
    public static final RegistryObject<Block> COGNITIVE_CRYSTAL_BLOCK = registerBlock("cognitive_crystal_block", CognitiveCrystalBlock::new, RegisterCreativeTab.EXPERIENCE_OBELISK_TAB.get());

    //-----FLUID BLOCKS-----//

    public static final RegistryObject<LiquidBlock> COGNITIUM = BLOCKS.register("cognitium",
            () -> new LiquidBlock(RegisterFluids.COGNITIUM_FLOWING, BlockBehaviour.Properties.of().liquid()
                    .lightLevel(value -> 10)
                    .emissiveRendering((p_61036_, p_61037_, p_61038_) -> true)
            ));


    //----utility methods to register block and block items at once-----//

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return RegisterItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

}
