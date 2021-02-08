package fr.axicer.furryattack.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TextureAtlas {

    public static final int ATLAS_COUNT_X = 16;
    public static final int ATLAS_COUNT_Y = 16;
    public static final float ATLAS_RATIO_X = 1f/ATLAS_COUNT_X;
    public static final float ATLAS_RATIO_Y = 1f/ATLAS_COUNT_Y;

    private static Texture atlas;

    public static Texture getAtlas(){
        if(atlas == null)atlas = Texture.loadTexture("/img/atlas.png", GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
        return atlas;
    }


}
