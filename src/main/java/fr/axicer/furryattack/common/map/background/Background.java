package fr.axicer.furryattack.common.map.background;

import fr.axicer.furryattack.client.render.Loader;
import fr.axicer.furryattack.client.render.RawModel;
import fr.axicer.furryattack.client.render.texture.Texture;
import fr.axicer.furryattack.common.entity.Removable;
import fr.axicer.furryattack.common.entity.Renderable;
import fr.axicer.furryattack.common.entity.Updatable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;
import java.util.Map;

public class Background implements Updatable, Renderable, Removable {

    private static final Map<String, Background> loadedBackgrounds = new HashMap<>();

    public static Background loadBackground(String path, int wrapMode, int filterMode){
        if(loadedBackgrounds.get(path) != null){
            return loadedBackgrounds.get(path);
        }else{
            Texture tex = Texture.loadTexture(path, wrapMode, filterMode);
            float[] vertices = {
                    -1f, 1f, -1f,-1f,  1f,-1f,
                    1f,-1f,  1f, 1f, -1f, 1f
            };
            float[] texCoord = {
                    0f,1f, 0f,0f, 1f,0f,
                    1f,0f, 1f,1f, 0f,1f
            };
            RawModel model = Loader.loadToVAO(vertices, 2, texCoord);
            if(shader == null)shader = new BackgroundShader();
            final Background background = new Background(model, tex);
            loadedBackgrounds.put(path, background);
            return background;
        }
    }

    public static void cleanUp(){
        loadedBackgrounds.values().forEach(Background::remove);
        loadedBackgrounds.clear();
    }

    private static BackgroundShader shader;

    private final RawModel model;
    private final Texture texture;

    public Background(RawModel model, Texture texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void remove() {
        model.destroy();
        texture.delete();
    }

    @Override
    public void render() {
        shader.bind();
        shader.setUniformInt("tex", 0);
        texture.bind(0);
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        Texture.unbind();
        shader.unbind();
    }

    @Override
    public void update() {

    }
}
