package fr.axicer.furryattack.common.map.frame;

import fr.axicer.furryattack.client.render.texture.TextureAtlas;
import fr.axicer.furryattack.util.Direction;
import org.joml.Vector2d;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
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
    private int data; // data of the block
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

    public Direction getSpawnFromOrientation() {
        return Direction.getFromOrdinal((data & 0b00000000000000000000000000000110) >>> 1);
    }

    public boolean isFrameTrigger() {
        return (data & 0b00000000000000000000000000001000) >>> 3 == 1;
    }

    public Direction getFrameToMoveOnWhenTriggered() {
        return Direction.getFromOrdinal((data & 0x00000030) >>> 4);
    }

    public boolean isSolid() {
        return (data & 0x00000040) >>> 6 == 1;
    }

    public boolean isBreakable() {
        return (data & 0x00000080) >>> 7 == 1;
    }

    public FrameBlockType getFrameType() {
        return FrameBlockType.getTypeFromOrdinal((data & 0x0000FF00) >>> 8);
    }

    public void setBlockType(FrameBlockType type){
        this.data = (data & 0xFFFF00FF) | (type.ordinal() << 8);
    }

    public int[] getPosition() {
        return position;
    }

    public boolean isNeighbor(Direction orientation, FrameBlockType type) {
        return Optional.ofNullable(frame.getNeighbor(this, orientation))
                .map(f -> f.getFrameType().equals(type))
                .orElse(true);
    }

    private static final Map<Integer, Predicate<FrameBlock>> borderChecks = new HashMap<>();
    static{
        //single corner
        borderChecks.put(1, b -> b.isNeighbor(Direction.BOTTOM_LEFT, FrameBlockType.AIR));
        borderChecks.put(2, b -> b.isNeighbor(Direction.TOP_LEFT, FrameBlockType.AIR));
        borderChecks.put(3, b -> b.isNeighbor(Direction.TOP_RIGHT, FrameBlockType.AIR));
        borderChecks.put(4, b -> b.isNeighbor(Direction.BOTTOM_RIGHT, FrameBlockType.AIR));
        //single border
        borderChecks.put(5, b -> b.isNeighbor(Direction.BOTTOM, FrameBlockType.AIR));
        borderChecks.put(6, b -> b.isNeighbor(Direction.LEFT, FrameBlockType.AIR));
        borderChecks.put(7, b -> b.isNeighbor(Direction.TOP, FrameBlockType.AIR));
        borderChecks.put(8, b -> b.isNeighbor(Direction.RIGHT, FrameBlockType.AIR));
        //2 borders opposite
        borderChecks.put(9, b -> Stream.of(Direction.LEFT, Direction.RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(10, b -> Stream.of(Direction.TOP, Direction.BOTTOM).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        //one border one corner left
        borderChecks.put(11, b -> Stream.of(Direction.BOTTOM, Direction.TOP_LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(12, b -> Stream.of(Direction.LEFT, Direction.TOP_RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(13, b -> Stream.of(Direction.TOP, Direction.BOTTOM_RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(14, b -> Stream.of(Direction.RIGHT, Direction.BOTTOM_LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        //one border one corner right
        borderChecks.put(15, b -> Stream.of(Direction.BOTTOM, Direction.TOP_RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(16, b -> Stream.of(Direction.LEFT, Direction.BOTTOM_RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(17, b -> Stream.of(Direction.TOP, Direction.BOTTOM_LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(18, b -> Stream.of(Direction.RIGHT, Direction.TOP_LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        //one border both corner
        borderChecks.put(19, b -> Stream.of(Direction.BOTTOM, Direction.TOP_RIGHT, Direction.TOP_LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)) &&
                !b.isNeighbor(Direction.TOP, FrameBlockType.AIR));//check for opposite being air to avoid mismatch with full opposite border
        borderChecks.put(20, b -> Stream.of(Direction.LEFT, Direction.BOTTOM_RIGHT, Direction.TOP_RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)) &&
                !b.isNeighbor(Direction.RIGHT, FrameBlockType.AIR));//check for opposite being air to avoid mismatch with full opposite border
        borderChecks.put(21, b -> Stream.of(Direction.TOP, Direction.BOTTOM_LEFT, Direction.BOTTOM_RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)) &&
                !b.isNeighbor(Direction.BOTTOM, FrameBlockType.AIR));//check for opposite being air to avoid mismatch with full opposite border
        borderChecks.put(22, b -> Stream.of(Direction.RIGHT, Direction.TOP_LEFT, Direction.BOTTOM_LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)) &&
                !b.isNeighbor(Direction.LEFT, FrameBlockType.AIR));//check for opposite being air to avoid mismatch with full opposite border
        //2 borders corners
        borderChecks.put(23, b -> Stream.of(Direction.RIGHT, Direction.BOTTOM).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(24, b -> Stream.of(Direction.BOTTOM, Direction.LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(25, b -> Stream.of(Direction.LEFT, Direction.TOP).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(26, b -> Stream.of(Direction.TOP, Direction.RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        //2 borders corner + opposite corner
        borderChecks.put(27, b -> Stream.of(Direction.RIGHT, Direction.BOTTOM, Direction.TOP_LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(28, b -> Stream.of(Direction.BOTTOM, Direction.LEFT, Direction.TOP_RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(29, b -> Stream.of(Direction.LEFT, Direction.TOP, Direction.BOTTOM_RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(30, b -> Stream.of(Direction.TOP, Direction.RIGHT, Direction.BOTTOM_LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        //3 borders
        borderChecks.put(31, b -> Stream.of(Direction.RIGHT, Direction.BOTTOM, Direction.LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(32, b -> Stream.of(Direction.BOTTOM, Direction.LEFT, Direction.TOP).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(33, b -> Stream.of(Direction.LEFT, Direction.TOP, Direction.RIGHT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        borderChecks.put(34, b -> Stream.of(Direction.TOP, Direction.RIGHT, Direction.BOTTOM).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
        //4 borders
        borderChecks.put(35, b -> Stream.of(Direction.RIGHT, Direction.BOTTOM, Direction.TOP, Direction.LEFT).allMatch(dir -> b.isNeighbor(dir, FrameBlockType.AIR)));
    }

    public Vector2d getBorderTexCoords(TextureAtlas atlas) {
        var x = new AtomicInteger(0);
        borderChecks.entrySet().stream()
                .filter(entry -> entry.getValue().test(this))
                .max(Comparator.comparingInt(Map.Entry::getKey))
                .ifPresent(entry -> x.set(entry.getKey()));
        int y = getFrameType().equals(FrameBlockType.AIR) ? 0 : 1;

        //TODO add more borders

        return new Vector2d(x.get() * atlas.getRatioX(), y * atlas.getRatioY());
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

    public void addQuadFloats(FloatBuffer buffer, int x, int y) {
        buffer.put(x * frame.blockWidth);
        buffer.put(y * frame.blockHeight);
        buffer.put(x * frame.blockWidth);
        buffer.put(y * frame.blockHeight + frame.blockHeight);
        buffer.put(x * frame.blockWidth + frame.blockWidth);
        buffer.put(y * frame.blockHeight + frame.blockHeight);

        buffer.put(x * frame.blockWidth + frame.blockWidth);
        buffer.put(y * frame.blockHeight + frame.blockHeight);
        buffer.put(x * frame.blockWidth + frame.blockWidth);
        buffer.put(y * frame.blockHeight);
        buffer.put(x * frame.blockWidth);
        buffer.put(y * frame.blockHeight);
    }

    public void addTextureFloats(DoubleBuffer buffer, Vector2d start, double ratioX, double ratioY) {
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
    public String toString() {
        return getFrameType().ordinal()+"";
    }
}
