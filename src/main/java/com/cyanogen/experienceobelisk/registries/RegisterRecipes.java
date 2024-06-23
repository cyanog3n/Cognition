package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.recipe.LaserTransfiguratorRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterRecipes{

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExperienceObelisk.MOD_ID);

    public static final RegistryObject<RecipeSerializer<LaserTransfiguratorRecipe>> LASER_TRANSFIGURATOR_SERIALIZER =
            SERIALIZERS.register("laser_transfiguration", () -> LaserTransfiguratorRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
