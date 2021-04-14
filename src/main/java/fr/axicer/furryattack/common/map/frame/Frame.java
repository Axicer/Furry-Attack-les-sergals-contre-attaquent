package fr.axicer.furryattack.common.map.frame;

import fr.axicer.furryattack.client.control.handler.KeyboardHandler;
import fr.axicer.furryattack.client.render.FrameModel;
import fr.axicer.furryattack.client.render.Loader;
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
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class Frame implements Renderable, Updatable, Removable, EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Frame.class);

    private static FrameShader shader;
    private FrameBlock[][] blocks; //all blocks
    private FrameModel model;
    private Background background;
    private TextureAtlas borderAtlas;
    private TextureAtlas decorationAtlas;
    private TextureAtlas textureAtlas;

    private Layer layer;
    protected float blockWidth, blockHeight;

    private boolean readyToUpdate = false;

    public Frame(Layer layer) {
        this.layer = layer;
        this.blockWidth = 1.0f / layer.getWidth();
        this.blockHeight = 1.0f / layer.getHeight();
        loadModel();
    }

    public void loadModel() {
        blocks = new FrameBlock[layer.getHeight()][layer.getWidth()];
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                blocks[y][x] = new FrameBlock(this, layer.getData()[y * layer.getWidth() + x], x, y);
            }
        }

        if (shader == null) shader = new FrameShader();
        if (background == null) background = Background.loadBackground("/img/background.png", GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        if (borderAtlas == null) borderAtlas = TextureAtlas.loadAtlas("/img/atlas/border_atlas.png", 36, 36, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        if (decorationAtlas == null) decorationAtlas = TextureAtlas.loadAtlas("/img/atlas/decoration_atlas.png", 20, 20, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        if (textureAtlas == null) textureAtlas = TextureAtlas.loadAtlas("/img/atlas/texture_atlas.png", 20, 20, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);

        //load vertices
        var vertexBuffer = BufferUtils.createFloatBuffer(layer.getWidth() * layer.getHeight() * 2 * 2 * 3);
        var borderTexBuffer = BufferUtils.createDoubleBuffer(layer.getWidth() * layer.getHeight() * 2 * 2 * 3);
        var decorationTexBuffer = BufferUtils.createDoubleBuffer(layer.getWidth() * layer.getHeight() * 2 * 2 * 3);
        var texBuffer = BufferUtils.createDoubleBuffer(layer.getWidth() * layer.getHeight() * 2 * 2 * 3);
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                final FrameBlock block = blocks[y][x];
                block.addQuadFloats(vertexBuffer, x, y);
                block.addTextureFloats(borderTexBuffer, block.getBorderTexCoords(borderAtlas), borderAtlas.getRatioX(), borderAtlas.getRatioY());
                block.addTextureFloats(decorationTexBuffer, block.getDecorationTexCoord(decorationAtlas), decorationAtlas.getRatioX(), decorationAtlas.getRatioY());
                block.addTextureFloats(texBuffer, block.getTexCoord(textureAtlas), textureAtlas.getRatioX(), textureAtlas.getRatioY());
            }
        }
        vertexBuffer.flip();
        decorationTexBuffer.flip();
        borderTexBuffer.flip();
        texBuffer.flip();

        if(model != null){
            model.updateAll(vertexBuffer, decorationTexBuffer, borderTexBuffer, texBuffer);
        }else{
            model = Loader.loadFrameToVAO(vertexBuffer, decorationTexBuffer, borderTexBuffer, texBuffer);
        }

        readyToUpdate = true;
    }

    public Layer getLayer() {
        return layer;
    }

    public TextureAtlas getBorderAtlas() {
        return borderAtlas;
    }

    public TextureAtlas getDecorationAtlas() {
        return decorationAtlas;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public void setBlock(int x, int y, FrameBlock block){
        if (x < 0 || x >= layer.getWidth()) return;
        if (y < 0 || y >= layer.getHeight()) return;
        blocks[y][x] = block;
        model.update(x, y, this);
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
        if(!readyToUpdate)return;
        int x = (int) (Math.random()*layer.getWidth());
        int y = (int) (Math.random()*layer.getHeight());

        final var block = getBlock(x, y);
        if(block.getFrameType().equals(FrameBlockType.COBBLESTONE)){
            block.setBlockType(FrameBlockType.AIR);
        }else{
            block.setBlockType(FrameBlockType.COBBLESTONE);
        }
        setBlock(x, y, block);
    }
}
