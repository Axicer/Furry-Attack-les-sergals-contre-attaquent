package fr.axicer.furryattack.client.render.texture;

public class TextureAtlas {

    public static TextureAtlas loadAtlas(String path, int xCellCount, int yCellCount, int wrapMode, int filterMode){
        return loadAtlas(path, xCellCount, yCellCount, wrapMode, wrapMode, filterMode, filterMode);
    }

    public static TextureAtlas loadAtlas(String path, int xCellCount, int yCellCount, int wrapModeS, int wrapModeT, int filterModeMIN, int filterModeMAG){
        Texture texture = Texture.loadTexture(path, wrapModeS, wrapModeT, filterModeMIN, filterModeMAG);
        return new TextureAtlas(texture, xCellCount, yCellCount);
    }

    private final Texture texture;
    private final int countX;
    private final int countY;

    public TextureAtlas(Texture texture, int countX, int countY) {
        this.texture = texture;
        this.countX = countX;
        this.countY = countY;
    }

    public int getCountX() {
        return countX;
    }

    public int getCountY() {
        return countY;
    }

    public Texture getTexture() {
        return texture;
    }

    public double getRatioX(){
        return 1D/(double)getCountX();
    }

    public double getRatioY(){
        return 1D/(double)getCountY();
    }
}
