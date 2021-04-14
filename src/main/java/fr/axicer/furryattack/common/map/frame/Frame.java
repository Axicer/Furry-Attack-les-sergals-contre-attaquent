package fr.axicer.furryattack.common.map.frame;

import fr.axicer.furryattack.client.render.Loader;
import fr.axicer.furryattack.client.render.RawModel;
import fr.axicer.furryattack.client.render.texture.Texture;
import fr.axicer.furryattack.client.render.texture.TextureAtlas;
import fr.axicer.furryattack.common.entity.Removable;
import fr.axicer.furryattack.common.entity.Renderable;
import fr.axicer.furryattack.common.entity.Updatable;
import fr.axicer.furryattack.common.events.EventListener;
import fr.axicer.furryattack.common.map.background.Background;
import fr.axicer.furryattack.common.map.layer.Layer;
import fr.axicer.furryattack.util.Direction;
import org.joml.Vector2d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class Frame implements Renderable, Updatable, Removable, EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Frame.class);

    private static FrameShader shader;
    private FrameBlock[][] blocks; //all blocks
    private RawModel model;
    private Background background;
    private TextureAtlas borderAtlas;
    private TextureAtlas decorationAtlas;
    private TextureAtlas textureAtlas;

    private final Layer layer;
    private final float blockWidth, blockHeight;

    public Frame(Layer layer) {
        this.layer = layer;
        this.blockWidth = 1.0f / layer.getWidth();
        this.blockHeight = 1.0f / layer.getHeight();
    }

    public void loadModel() {
        if (blocks == null) {
            LOGGER.error("Attempted to load frame model but frame is not generated yet.");
            return;
        }
        if (shader == null) shader = new FrameShader();
        background = Background.loadBackground("/img/background.png", GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        borderAtlas = TextureAtlas.loadAtlas("/img/atlas/border_atlas.png", 36, 36, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        decorationAtlas = TextureAtlas.loadAtlas("/img/atlas/decoration_atlas.png", 20, 20, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        textureAtlas = TextureAtlas.loadAtlas("/img/atlas/texture_atlas.png", 20, 20, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);

        //load vertices
        var vertexBuffer = FloatBuffer.allocate(layer.getWidth() * layer.getHeight() * 2 * 2 * 3);
        var borderTexBuffer = DoubleBuffer.allocate(layer.getWidth() * layer.getHeight() * 2 * 2 * 3);
        var decorationTexBuffer = DoubleBuffer.allocate(layer.getWidth() * layer.getHeight() * 2 * 2 * 3);
        var texBuffer = DoubleBuffer.allocate(layer.getWidth() * layer.getHeight() * 2 * 2 * 3);
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                final FrameBlock block = blocks[y][x];
                addQuadFloats(vertexBuffer, x, y);
                addTextureFloats(borderTexBuffer, block.getBorderTexCoords(borderAtlas), borderAtlas.getRatioX(), borderAtlas.getRatioY());
                addTextureFloats(decorationTexBuffer, block.getDecorationTexCoord(decorationAtlas), decorationAtlas.getRatioX(), decorationAtlas.getRatioY());
                addTextureFloats(texBuffer, block.getTexCoord(textureAtlas), textureAtlas.getRatioX(), textureAtlas.getRatioY());
            }
        }

        model = Loader.loadFrameToVAO(vertexBuffer.array(), 2, decorationTexBuffer.array(), borderTexBuffer.array(), texBuffer.array());
    }

    public void generate() {
        blocks = new FrameBlock[layer.getHeight()][layer.getWidth()];

        //generate blocks
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                blocks[y][x] = new FrameBlock(this, layer.getData()[y * layer.getWidth() + x], x, y);
            }
        }

    }

    public FrameBlock getBlock(int x, int y) {
        if (x < 0 || x >= layer.getWidth()) return null;
        if (y < 0 || y >= layer.getHeight()) return null;
        return blocks[y][x];
    }

    public FrameBlock getNeighbor(FrameBlock current, Direction orientation) {
        int x = current.getPosition()[0];
        int y = current.getPosition()[1];
        return switch (orientation) {
            case TOP -> getBlock(x, y - 1);
            case BOTTOM -> getBlock(x, y + 1);
            case RIGHT -> getBlock(x + 1, y);
            case LEFT -> getBlock(x - 1, y);
            case TOP_LEFT -> getBlock(x - 1, y - 1);
            case TOP_RIGHT -> getBlock(x + 1, y - 1);
            case BOTTOM_LEFT -> getBlock(x - 1, y + 1);
            case BOTTOM_RIGHT -> getBlock(x + 1, y + 1);
        };
    }

    private void addQuadFloats(FloatBuffer buffer, int x, int y) {
        buffer.put(x * blockWidth);
        buffer.put(y * blockHeight);
        buffer.put(x * blockWidth);
        buffer.put(y * blockHeight + blockHeight);
        buffer.put(x * blockWidth + blockWidth);
        buffer.put(y * blockHeight + blockHeight);

        buffer.put(x * blockWidth + blockWidth);
        buffer.put(y * blockHeight + blockHeight);
        buffer.put(x * blockWidth + blockWidth);
        buffer.put(y * blockHeight);
        buffer.put(x * blockWidth);
        buffer.put(y * blockHeight);
    }

    private void addTextureFloats(DoubleBuffer buffer, Vector2d start, double ratioX, double ratioY) {
        buffer.put(start.x);
        buffer.put(start.y);
        buffer.put(start.x);
        buffer.put(start.y + ratioY);
        buffer.put(start.x + ratioX);
        buffer.put(start.y + ratioY);

        buffer.put(start.x + ratioX);
        buffer.put(start.y + ratioY);
        buffer.put(start.x + ratioX);
        buffer.put(start.y);
        buffer.put(start.x);
        buffer.put(start.y);
    }

    @Override
    public void remove() {
        model.destroy();
        textureAtlas.getTexture().delete();
        borderAtlas.getTexture().delete();
        decorationAtlas.getTexture().delete();
    }

    @Override
    public void render() {
        background.render();
        shader.bind();
        shader.setUniformInt("texAtlas", 0);
        textureAtlas.getTexture().bind(0);
        shader.setUniformInt("borderAtlas", 1);
        borderAtlas.getTexture().bind(1);
        shader.setUniformInt("decorationAtlas", 2);
        decorationAtlas.getTexture().bind(2);
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
        Texture.unbind();
        shader.unbind();
    }

    @Override
    public void update() {
    }
}
