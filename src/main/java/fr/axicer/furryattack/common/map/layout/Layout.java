package fr.axicer.furryattack.common.map.layout;

public class Layout {

    private final int[][] pixels;
    private final boolean top;
    private final boolean bottom;
    private final boolean left;
    private final boolean right;

    public Layout(int[][] pixels, boolean left, boolean right, boolean top, boolean bottom) {
        this.pixels = pixels;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isBottom() {
        return bottom;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }
}
