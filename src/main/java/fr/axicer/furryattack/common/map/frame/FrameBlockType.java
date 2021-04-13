package fr.axicer.furryattack.common.map.frame;

import org.joml.Vector4f;

import java.util.stream.Stream;

public enum FrameBlockType {

    //TODO add more textures
    AIR(19,19,20,20,0,0,0,0),
    DIRT(0,0,0,0,0,0,0,0),
    STONE(0,0,0,0,0,0,0,0),
    COBBLESTONE(0,0,4,4,0,0,0,0),
    GRAVEL(0,0,0,0,0,0,0,0),
    ;

    private final Vector4f texAtlasRange, decAtlasRange;

    FrameBlockType(float texAtlas_startX, float texAtlas_startY, float texAtlas_endX, float texAtlas_endY, float decAtlas_startX, float decAtlas_startY, float decAtlas_endX, float decAtlas_endY) {
        this.texAtlasRange = new Vector4f(texAtlas_startX, texAtlas_startY, texAtlas_endX, texAtlas_endY);
        this.decAtlasRange = new Vector4f(decAtlas_startX, decAtlas_startY, decAtlas_endX, decAtlas_endY);
    }

    public static FrameBlockType getTypeFromOrdinal(int ordinal){
        return Stream.of(values())
                .filter(type -> type.ordinal() == ordinal)
                .findFirst()
                .orElse(null);
    }

    public Vector4f getTextureAtlasRange(){
        return texAtlasRange;
    }

    public Vector4f getDecorationAtlasRange(){
        return decAtlasRange;
    }

}
