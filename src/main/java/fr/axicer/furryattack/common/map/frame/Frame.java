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
import fr.axicer.furryattack.common.map.layout.Layout;
import fr.axicer.furryattack.util.NumberUtils;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * A frame from the map generated
 */
public class Frame implements Renderable, Updatable, Removable, EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Frame.class);
    public static final int FRAME_BLOCK_WIDTH = 48;
    public static final int FRAME_BLOCK_HEIGHT = 27;
    public static final float BLOCK_WIDTH = 1.0f / FRAME_BLOCK_WIDTH;
    public static final float BLOCK_HEIGHT = 1.0f / FRAME_BLOCK_HEIGHT;

    private static FrameShader shader;
    private FrameBlock[][] blocks; //all blocks
    private RawModel model;
    private final Layout layout;
    private Background background;
    private TextureAtlas borderAtlas;
    private TextureAtlas decorationAtlas;
    private TextureAtlas stoneAtlas;

    public Frame(Layout layout) {
        this.layout = layout;
    }

    public void loadModel() {
        if(blocks == null){
            LOGGER.error("Attempted to load frame model but frame is not generated yet.");
            return;
        }
        if (shader == null) shader = new FrameShader();
        background = Background.loadBackground("/img/background.png", GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        borderAtlas = TextureAtlas.loadAtlas("/img/atlas/border_atlas.png", 20, 20, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        decorationAtlas = TextureAtlas.loadAtlas("/img/atlas/decoration_atlas.png", 20, 20, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        stoneAtlas = TextureAtlas.loadAtlas("/img/atlas/stone_atlas.png", 20, 20, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);

        //load vertices
        var vertexList = new LinkedList<Float>();
        var borderTexCoords = new LinkedList<Float>();
        var decorationTexCoords = new LinkedList<Float>();
        var stoneTexCoords = new LinkedList<Float>();
        for (int y = 0; y < FRAME_BLOCK_HEIGHT; y++) {
            for (int x = 0; x < FRAME_BLOCK_WIDTH; x++) {
                if (blocks[y][x].isSolid()) {
                    addQuadFloats(vertexList, x, y);
                    Vector2f _borderTexCoords = blocks[y][x].getBorderTexCoords(borderAtlas);
                    Vector2f _decorationTexCoords = blocks[y][x].getDecorationTexCoord(decorationAtlas);
                    Vector2f _stoneTexCoords = blocks[y][x].getStoneTexCoord(stoneAtlas);
                    addTextureFloats(borderTexCoords, _borderTexCoords.x, _borderTexCoords.y, borderAtlas.getRatioX(), borderAtlas.getRatioY());
                    addTextureFloats(decorationTexCoords, _decorationTexCoords.x, _decorationTexCoords.y, decorationAtlas.getRatioX(), decorationAtlas.getRatioY());
                    addTextureFloats(stoneTexCoords, _stoneTexCoords.x, _stoneTexCoords.y, stoneAtlas.getRatioX(), stoneAtlas.getRatioY());
                }
            }
        }

        model = Loader.loadFrameToVAO(NumberUtils.toFloatArray(vertexList), 2,
                NumberUtils.toFloatArray(decorationTexCoords),
                NumberUtils.toFloatArray(borderTexCoords),
                NumberUtils.toFloatArray(stoneTexCoords));
    }

    public void generate() {
        blocks = new FrameBlock[FRAME_BLOCK_HEIGHT][FRAME_BLOCK_WIDTH];

        //generate blocks
        for (int y = 0; y < FRAME_BLOCK_HEIGHT; y++) {
            for (int x = 0; x < FRAME_BLOCK_WIDTH; x++) {
                Color pixColor;
                try{
                    pixColor = new Color(layout.getPixels()[y][x]);
                }catch (ArrayIndexOutOfBoundsException ex){
                    pixColor = Color.BLACK;
                }

                if (pixColor.equals(Color.BLACK)) {
                    //solid block
                    blocks[y][x] = new FrameBlock(true);
                } else if (pixColor.equals(Color.WHITE)) {
                    //transparent block
                    blocks[y][x] = new FrameBlock(false);
                } else {
                    LOGGER.error("pixel {} is unknown", pixColor);
                }
            }
        }
        //compute neighbors solid
        for (int y = 0; y < FRAME_BLOCK_HEIGHT; y++) {
            for (int x = 0; x < FRAME_BLOCK_WIDTH; x++) {
                var top = (y != 0) ? blocks[y - 1][x] : null;
                blocks[y][x].setNeighbor(top, FrameBlock.FrameOrientation.TOP);
                var right = (x != FRAME_BLOCK_WIDTH - 1) ? blocks[y][x + 1] : null;
                blocks[y][x].setNeighbor(right, FrameBlock.FrameOrientation.RIGHT);
                var bottom = (y != FRAME_BLOCK_HEIGHT - 1) ? blocks[y + 1][x] : null;
                blocks[y][x].setNeighbor(bottom, FrameBlock.FrameOrientation.BOTTOM);
                var left = (x != 0) ? blocks[y][x - 1] : null;
                blocks[y][x].setNeighbor(left, FrameBlock.FrameOrientation.LEFT);

            }
        }
    }

    private void addQuadFloats(List<Float> vertexList, int x, int y) {
        //bottom left triangle
        vertexList.add(x * BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT);
        vertexList.add(x * BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT + BLOCK_HEIGHT);
        vertexList.add(x * BLOCK_WIDTH + BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT + BLOCK_HEIGHT);

        //second triangle
        vertexList.add(x * BLOCK_WIDTH + BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT + BLOCK_HEIGHT);
        vertexList.add(x * BLOCK_WIDTH + BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT);
        vertexList.add(x * BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT);
    }

    private void addTextureFloats(List<Float> textureCoords, float startX, float startY, float ratioX, float ratioY) {
        float[] coord = {
                //bottom left triangle
                startX, startY + ratioY,
                startX, startY,
                startX + ratioX, startY,
                //top right triangle
                startX + ratioX, startY,
                startX + ratioX, startY + ratioY,
                startX, startY + ratioY
        };
        for (float f : coord) {
            textureCoords.add(f);
        }
    }

    @Override
    public void remove() {
        model.destroy();
        stoneAtlas.getTexture().delete();
        borderAtlas.getTexture().delete();
        decorationAtlas.getTexture().delete();
    }

    @Override
    public void render() {
        background.render();
        shader.bind();
        shader.setUniformInt("stoneAtlas", 0);
        stoneAtlas.getTexture().bind(0);
        shader.setUniformInt("borderAtlas", 1);
        borderAtlas.getTexture().bind(1);
        shader.setUniformInt("decorationAtlas", 2);
        decorationAtlas.getTexture().bind(2);
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
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
