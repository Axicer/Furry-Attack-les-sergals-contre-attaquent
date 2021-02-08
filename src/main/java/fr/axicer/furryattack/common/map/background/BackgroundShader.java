package fr.axicer.furryattack.common.map.background;

import fr.axicer.furryattack.client.render.shader.Shader;
import org.lwjgl.opengl.GL20;

public class BackgroundShader extends Shader {
    public BackgroundShader() {
        super("background/vertex.glsl", null, "background/fragment.glsl", true, false);
        GL20.glBindAttribLocation(getProgram(), 0, "vertices");
        GL20.glBindAttribLocation(getProgram(), 1, "texCoords");
        linkAndValidate();
    }
}
