package mod.syconn.swm.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public class HologramRenderer extends PlayerRenderer {

    public HologramRenderer(EntityRendererProvider.Context context, boolean useSlimModel) {
        super(context, useSlimModel);
    }
}
