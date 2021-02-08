package fr.axicer.furryattack.common.map.frame;

import fr.axicer.furryattack.client.render.shader.Shader;
import org.lwjgl.opengl.GL20;

public class FrameShader extends Shader {

    public FrameShader() {
        super("frame/vertex.glsl", null, "frame/fragment.glsl", true, false);
        GL20.glBindAttribLocation(getProgram(), 0, "vertices");
        GL20.glBindAttribLocation(getProgram(), 1, "stoneTexCoord");
        GL20.glBindAttribLocation(getProgram(), 2, "borderTexCoord");
        GL20.glBindAttribLocation(getProgram(), 3, "decorationTexCoord");
        linkAndValidate();
    }
}
