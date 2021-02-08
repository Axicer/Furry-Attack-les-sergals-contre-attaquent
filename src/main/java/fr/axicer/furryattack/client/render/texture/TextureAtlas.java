package fr.axicer.furryattack.client.render.texture;

public class TextureAtlas {

    public static TextureAtlas loadAtlas(String path, int xCellCount, int yCellCount, int wrapMode, int filterMode){
        Texture texture = Texture.loadTexture(path, wrapMode, filterMode);
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

    public float getRatioX(){
        return 1f/getCountX();
    }

    public float getRatioY(){
        return 1f/getCountY();
    }
}
