package mod.syconn.swm.mixin;

import mod.syconn.swm.utils.Constants;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

  @Inject(method = "init", at = @At("HEAD"))
  public void initMixinExample(CallbackInfo ci) {

    String baseString = "Hello from %LOADER% on %MINECRAFT%!";

    /// https://stonecutter.kikugie.dev/stonecutter/guide/comments

    //? if fabric {
    baseString = baseString.replace("%LOADER%", "Fabric Loader");
     //?} else if neoforge {
    /*baseString = baseString.replace("%LOADER%", "NeoForge");
    *///?} else {
    /*baseString = baseString.replace("%LOADER%", "Legacy Forge");
     *///?}

    //? if 1.21.1 {
    /*baseString = baseString.replace("%MINECRAFT%", "1.21.1");
    *///?} else if 26.1.2 {
    /*baseString = baseString.replace("%MINECRAFT%", "26.1.2");
     *///?} else {
    baseString = baseString.replace("%MINECRAFT%", "1.20.1");
     //?}

    Constants.LOGGER.info(baseString);
  }
}
