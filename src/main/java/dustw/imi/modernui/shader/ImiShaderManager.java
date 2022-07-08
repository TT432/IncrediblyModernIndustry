package dustw.imi.modernui.shader;

import dustw.imi.Imi;
import icyllis.modernui.ModernUI;
import icyllis.modernui.opengl.GLProgram;
import icyllis.modernui.opengl.ShaderManager;

import javax.annotation.Nonnull;

/**
 * @author DustW
 **/
public class ImiShaderManager {
    public static final GLProgram CUBIC_BEZIER = new GLProgram();

    static {
        ShaderManager.getInstance().addListener(ImiShaderManager::onLoadShaders);
    }

    private static void onLoadShaders(@Nonnull ShaderManager manager) {
        int posColor = manager.getShard(ModernUI.ID, "pos_color.vert");

        int quadBezier = manager.getShard(Imi.MOD_ID, "cubic_bezier.frag");

        manager.create(CUBIC_BEZIER, posColor, quadBezier);
    }
}
