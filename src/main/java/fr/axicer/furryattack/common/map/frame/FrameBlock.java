package fr.axicer.furryattack.common.map.frame;

import fr.axicer.furryattack.client.render.texture.TextureAtlas;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FrameBlock {

    /**
     * data is a 32-bit list of properties defined as:
     * 00000000000000000000000000000001 -> is a spawn block
     * 00000000000000000000000000000110 -> previous frame origin (when player is going from the left -> Orientation.LEFT.ordinal())
     * 00000000000000000000000000001000 -> is a frame trigger
     * 00000000000000000000000000110000 -> the frame to get when triggered (when Orientation.LEFT.ordinal() -> move to left frame relative from this frame)
     * 00000000000000000000000001000000 -> is solid
     * 00000000000000000000000010000000 -> is breakable
     * 00000000000000001111111100000000 -> block type
     * other are to define
     */
    private final int data; // data of the block
    private final Frame frame;
    private final int[] position; // position of the block as 0 = x and 1 = y

    public FrameBlock(Frame frame, int data, int posX, int posY) {
        this.frame = frame;
        this.position = new int[]{posX, posY};
        this.data = data;
    }

    public boolean isSpawnBlock() {
        return (data & 0b00000000000000000000000000000001) == 1;
    }

    public FrameOrientation getSpawnFromOrientation() {
        return FrameOrientation.getFromOrdinal((data & 0b00000000000000000000000000000110) >>> 1);
    }

    public boolean isFrameTrigger() {
        return (data & 0b00000000000000000000000000001000) >>> 3 == 1;
    }

    public FrameOrientation getFrameToMoveOnWhenTriggered() {
        return FrameOrientation.getFromOrdinal((data & 0b00000000000000000000000000110000) >>> 4);
    }

    public boolean isSolid() {
        return (data & 0b00000000000000000000000001000000) >>> 6 == 1;
    }

    public boolean isBreakable() {
        return (data & 0b00000000000000000000000010000000) >>> 7 == 1;
    }

    public FrameBlockType getFrameType() {
        return FrameBlockType.getTypeFromOrdinal((data & 0b00000000000000001111111100000000) >>> 8);
    }

    public int[] getPosition() {
        return position;
    }

    public boolean isNeighborSolid(FrameOrientation orientation) {
        return Optional.ofNullable(frame.getNeighbor(this, orientation))
                .map(FrameBlock::isSolid)
                .orElse(false);
    }

//    private final List<Integer> bottomIndex = Arrays.asList(3, 7, 8, 10, 11, 12, 13, 15);
//    private final List<Integer> topIndex = Arrays.asList(1, 5, 6, 9, 10, 12, 13, 15);
//    private final List<Integer> leftIndex = Arrays.asList(2, 6, 7, 9, 10, 11, 13, 14);
//    private final List<Integer> rightIndex = Arrays.asList(4, 5, 8, 9, 11, 12, 13, 14);

    public Vector2d getBorderTexCoords(TextureAtlas atlas) {
//        List<Integer> possible = new ArrayList<>(15);
//        IntStream.rangeClosed(1, 15).forEach(possible::add);
//
//        if (!isNeighborSolid(FrameOrientation.TOP)) {
//            possible = possible.stream()
//                    .filter(topIndex::contains)
//                    .collect(Collectors.toList());
//        } else {
//            possible.removeAll(topIndex);
//        }
//        if (!isNeighborSolid(FrameOrientation.RIGHT)) {
//            possible = possible.stream()
//                    .filter(rightIndex::contains)
//                    .collect(Collectors.toList());
//        } else {
//            possible.removeAll(rightIndex);
//        }
//        if (!isNeighborSolid(FrameOrientation.BOTTOM)) {
//            possible = possible.stream()
//                    .filter(bottomIndex::contains)
//                    .collect(Collectors.toList());
//        } else {
//            possible.removeAll(bottomIndex);
//        }
//        if (!isNeighborSolid(FrameOrientation.LEFT)) {
//            possible = possible.stream()
//                    .filter(leftIndex::contains)
//                    .collect(Collectors.toList());
//        } else {
//            possible.removeAll(leftIndex);
//        }
//
//        AtomicInteger x = new AtomicInteger(possible.stream().findFirst().orElse(0));
//
//        //check for single corner
//        if (x.get() == 0) {
//            Optional.ofNullable(frame.getNeighbor(this, FrameOrientation.BOTTOM))
//                    .ifPresent(bottom -> {
//                        if (!bottom.isNeighborSolid(FrameOrientation.RIGHT)) {
//                            x.set(18);
//                        } else if (!bottom.isNeighborSolid(FrameOrientation.LEFT)) {
//                            x.set(17);
//                        }
//                    });
//            Optional.ofNullable(frame.getNeighbor(this, FrameOrientation.TOP))
//                    .ifPresent(top -> {
//                        if (!top.isNeighborSolid(FrameOrientation.RIGHT)) {
//                            x.set(19);
//                        } else if (!top.isNeighborSolid(FrameOrientation.LEFT)) {
//                            x.set(16);
//                        }
//                    });
//        }
//
//        int y;
//        if (isNeighborSolid(FrameOrientation.BOTTOM)) {
//            y = 0;
//        } else {
//            y = 1;
//        }
//
//        //TODO add more borders
//
//        return new Vector2f(x.get() * atlas.getRatioX(), y * atlas.getRatioY());

        return new Vector2d(0D, 0D);
    }

    public Vector2d getDecorationTexCoord(TextureAtlas atlas) {
        final var range = getFrameType().getDecorationAtlasRange();
        double x = (range.x + Math.floor(Math.random() * (range.z - range.x + 1D))) * atlas.getRatioX();
        double y = (range.y + Math.floor(Math.random() * (range.w - range.y + 1D))) * atlas.getRatioY();

        return new Vector2d(x, y);
    }

    public Vector2d getTexCoord(TextureAtlas atlas) {
        final var range = getFrameType().getTextureAtlasRange();
        double x = (range.x + Math.floor(Math.random() * (range.z - range.x + 1))) * atlas.getRatioX();
        double y = (range.y + Math.floor(Math.random() * (range.w - range.y + 1))) * atlas.getRatioY();

        return new Vector2d(x, y);
    }

    public enum FrameOrientation {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;

        public static FrameOrientation getFromOrdinal(int ordinal) {
            return Stream.of(values())
                    .filter(orientation -> orientation.ordinal() == ordinal)
                    .findFirst()
                    .orElse(null);
        }
    }
}
