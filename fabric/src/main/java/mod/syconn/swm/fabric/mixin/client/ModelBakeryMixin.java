package mod.syconn.swm.fabric.mixin.client;

import mod.syconn.swm.utils.generic.FileUtil;
import mod.syconn.swm.utils.interfaces.ISpecialRenderer;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow
    protected abstract void loadTopLevel(ModelResourceLocation location);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadTopLevel(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V", ordinal = 3))
    public void addCustomModels(BlockColors blockColors, ProfilerFiller profilerFiller, Map<ResourceLocation, BlockModel> modelResources, Map<ResourceLocation, List<ModelBakery.LoadedJson>> blockStateResources, CallbackInfo ci) {
        profilerFiller.popPush("special_renders");
        var files = new ArrayList<ResourceLocation>();
        ISpecialRenderer.SPECIAL_RENDER_FOLDER.forEach(path -> FileUtil.scanFilesInDirectory(path, ISpecialRenderer::itemModelPath, files));
        files.addAll(ISpecialRenderer.SPECIAL_RENDERS);
        files.forEach(v -> this.loadTopLevel((ModelResourceLocation) v));
    }
}
