package fr.axicer.furryattack.common.map.frame;

import fr.axicer.furryattack.client.render.TextureAtlas;
import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FrameBlock {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrameBlock.class);

    private final FrameBlock[] neighbor;
    private final boolean solid;

    public FrameBlock(boolean solid) {
        this.neighbor = new FrameBlock[4];
        this.solid = solid;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isNeighborSolid(FrameOrientation orientation){
        var n = neighbor[orientation.ordinal()];
        return n == null || n.isSolid();
    }

    public void setNeighbor(FrameBlock block, FrameOrientation orientation){
        neighbor[orientation.ordinal()] = block;
    }

    private final List<Integer> bottomIndex = Arrays.asList(3, 7, 8, 10, 11, 12, 13, 15);
    private final List<Integer> topIndex = Arrays.asList(1, 5, 6, 9, 10, 12, 13, 15);
    private final List<Integer> leftIndex = Arrays.asList(2, 6, 7, 9, 10, 11, 13, 14);
    private final List<Integer> rightIndex = Arrays.asList(4, 5, 8, 9, 11, 12, 13, 14);

    public Vector2f getTextureCoords(){
        List<Integer> possible = new ArrayList<>(15);
        IntStream.rangeClosed(1,15).forEach(possible::add);

        if(!isNeighborSolid(FrameOrientation.TOP)){
            possible = possible.stream()
                    .filter(topIndex::contains)
                    .collect(Collectors.toList());
        }else{
            possible.removeAll(topIndex);
        }
        if(!isNeighborSolid(FrameOrientation.RIGHT)){
            possible = possible.stream()
                    .filter(rightIndex::contains)
                    .collect(Collectors.toList());
        }else{
            possible.removeAll(rightIndex);
        }
        if(!isNeighborSolid(FrameOrientation.BOTTOM)){
            possible = possible.stream()
                    .filter(bottomIndex::contains)
                    .collect(Collectors.toList());
        }else{
            possible.removeAll(bottomIndex);
        }
        if(!isNeighborSolid(FrameOrientation.LEFT)){
            possible = possible.stream()
                    .filter(leftIndex::contains)
                    .collect(Collectors.toList());
        }else{
            possible.removeAll(leftIndex);
        }

        float x = possible.stream().findFirst().orElse(0);

        return new Vector2f(x/(float)TextureAtlas.ATLAS_COUNT_X,0f);
    }

    public enum FrameOrientation{
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;
    }
}
