package fr.axicer.furryattack.common.map.frame;

import fr.axicer.furryattack.client.render.texture.TextureAtlas;
import org.joml.Vector2f;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FrameBlock {

    private final FrameBlock[] neighbor;
    private final boolean solid;

    public FrameBlock(boolean solid) {
        this.neighbor = new FrameBlock[4];
        this.solid = solid;
    }

    public boolean isSolid() {
        return solid;
    }

    public FrameBlock getNeighbor(FrameOrientation orientation) {
        return neighbor[orientation.ordinal()];
    }

    public boolean isNeighborSolid(FrameOrientation orientation) {
        var n = getNeighbor(orientation);
        return n == null || n.isSolid();
    }

    public void setNeighbor(FrameBlock block, FrameOrientation orientation) {
        neighbor[orientation.ordinal()] = block;
    }

    private final List<Integer> bottomIndex = Arrays.asList(3, 7, 8, 10, 11, 12, 13, 15);
    private final List<Integer> topIndex = Arrays.asList(1, 5, 6, 9, 10, 12, 13, 15);
    private final List<Integer> leftIndex = Arrays.asList(2, 6, 7, 9, 10, 11, 13, 14);
    private final List<Integer> rightIndex = Arrays.asList(4, 5, 8, 9, 11, 12, 13, 14);

    public Vector2f getBorderTexCoords(TextureAtlas atlas) {
        List<Integer> possible = new ArrayList<>(15);
        IntStream.rangeClosed(1, 15).forEach(possible::add);

        if (!isNeighborSolid(FrameOrientation.TOP)) {
            possible = possible.stream()
                    .filter(topIndex::contains)
                    .collect(Collectors.toList());
        } else {
            possible.removeAll(topIndex);
        }
        if (!isNeighborSolid(FrameOrientation.RIGHT)) {
            possible = possible.stream()
                    .filter(rightIndex::contains)
                    .collect(Collectors.toList());
        } else {
            possible.removeAll(rightIndex);
        }
        if (!isNeighborSolid(FrameOrientation.BOTTOM)) {
            possible = possible.stream()
                    .filter(bottomIndex::contains)
                    .collect(Collectors.toList());
        } else {
            possible.removeAll(bottomIndex);
        }
        if (!isNeighborSolid(FrameOrientation.LEFT)) {
            possible = possible.stream()
                    .filter(leftIndex::contains)
                    .collect(Collectors.toList());
        } else {
            possible.removeAll(leftIndex);
        }

        float x = possible.stream().findFirst().orElse(0);

        //check for single corner
        if (x == 0) {
            FrameBlock bottom = getNeighbor(FrameOrientation.BOTTOM);
            if (bottom != null) {
                if (!bottom.isNeighborSolid(FrameOrientation.RIGHT)) {
                    x = 18;
                } else if (!bottom.isNeighborSolid(FrameOrientation.LEFT)) {
                    x = 17;
                }
            }
            FrameBlock top = getNeighbor(FrameOrientation.TOP);
            if (top != null) {
                if (!top.isNeighborSolid(FrameOrientation.RIGHT)) {
                    x = 19;
                } else if (!top.isNeighborSolid(FrameOrientation.LEFT)) {
                    x = 16;
                }
            }
        }

        int y;
        if(isNeighborSolid(FrameOrientation.BOTTOM)){
            y = 0;
        }else{
            y = 1;
        }

        //TODO add more decoration

        return new Vector2f(x * atlas.getRatioX(), y * atlas.getRatioY());
    }

    public Vector2f getDecorationTexCoord(TextureAtlas atlas) {
        int x = (int) Math.floor(Math.random() * atlas.getCountX());
        int y = (int) Math.floor(Math.random() * 2);

        //TODO add more decoration

        return new Vector2f(x * atlas.getRatioX(), y * atlas.getRatioY());
    }

    public Vector2f getStoneTexCoord(TextureAtlas atlas) {
        int x = (int) Math.floor(Math.random()*5);
        int y = (int) Math.floor(Math.random()*5);

        //TODO add more stone variant

        return new Vector2f(x * atlas.getRatioX(), y * atlas.getRatioY());
    }

    public enum FrameOrientation {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }
}
