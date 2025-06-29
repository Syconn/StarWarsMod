package mod.syconn.swm.fabric.datagen;

import mod.syconn.swm.core.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class DatapackProvider extends RegistriesDatapackGenerator {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, DatapackProvider::bootstrapDamageType);

    public DatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries.thenApply(DatapackProvider::constructRegistries));
    }

    private static HolderLookup.Provider constructRegistries(HolderLookup.Provider original) {
        return DatapackProvider.BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }

    private static void bootstrapDamageType(BootstapContext<DamageType> context) {
        context.register(ModTags.LIGHTSABER_DAMAGE, new DamageType("lightsaber", 0.1F));
        context.register(ModTags.BLASTER_DAMAGE, new DamageType("blaster", 0.1F));
    }
}
