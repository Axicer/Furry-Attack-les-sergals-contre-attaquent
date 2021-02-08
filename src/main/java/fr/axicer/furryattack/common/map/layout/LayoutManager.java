package fr.axicer.furryattack.common.map.layout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LayoutManager {

    private static List<Layout> loadedLayouts;

    public static void loadLayouts() {
        loadedLayouts = new LinkedList<>();
        loadedLayouts.add(loadLayout("none/0.png", false, false, false, false));
        loadedLayouts.add(loadLayout("none/1.png", false, false, false, false));
        loadedLayouts.add(loadLayout("none/2.png", false, false, false, false));
        loadedLayouts.add(loadLayout("none/3.png", false, false, false, false));
        loadedLayouts.add(loadLayout("none/4.png", false, false, false, false));
        loadedLayouts.add(loadLayout("none/5.png", false, false, false, false));

        loadedLayouts.add(loadLayout("bottom/0.png", false, false, false, true));
        loadedLayouts.add(loadLayout("bottom/1.png", false, false, false, true));
        loadedLayouts.add(loadLayout("bottom/2.png", false, false, false, true));
        loadedLayouts.add(loadLayout("bottom/3.png", false, false, false, true));
        loadedLayouts.add(loadLayout("bottom/4.png", false, false, false, true));
        loadedLayouts.add(loadLayout("bottom/5.png", false, false, false, true));

        loadedLayouts.add(loadLayout("bottom_left/0.png", true, false, false, true));
        loadedLayouts.add(loadLayout("bottom_left/1.png", true, false, false, true));
        loadedLayouts.add(loadLayout("bottom_left/2.png", true, false, false, true));
        loadedLayouts.add(loadLayout("bottom_left/3.png", true, false, false, true));
        loadedLayouts.add(loadLayout("bottom_left/4.png", true, false, false, true));
        loadedLayouts.add(loadLayout("bottom_left/5.png", true, false, false, true));

        loadedLayouts.add(loadLayout("bottom_right/0.png", false, true, false, true));
        loadedLayouts.add(loadLayout("bottom_right/1.png", false, true, false, true));
        loadedLayouts.add(loadLayout("bottom_right/2.png", false, true, false, true));
        loadedLayouts.add(loadLayout("bottom_right/3.png", false, true, false, true));
        loadedLayouts.add(loadLayout("bottom_right/4.png", false, true, false, true));
        loadedLayouts.add(loadLayout("bottom_right/5.png", false, true, false, true));

        loadedLayouts.add(loadLayout("bottom_top/0.png", false, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top/1.png", false, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top/2.png", false, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top/3.png", false, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top/4.png", false, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top/5.png", false, false, true, true));

        loadedLayouts.add(loadLayout("bottom_top_left/0.png", true, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top_left/1.png", true, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top_left/2.png", true, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top_left/3.png", true, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top_left/4.png", true, false, true, true));
        loadedLayouts.add(loadLayout("bottom_top_left/5.png", true, false, true, true));

        loadedLayouts.add(loadLayout("bottom_top_right/0.png", false, true, true, true));
        loadedLayouts.add(loadLayout("bottom_top_right/1.png", false, true, true, true));
        loadedLayouts.add(loadLayout("bottom_top_right/2.png", false, true, true, true));
        loadedLayouts.add(loadLayout("bottom_top_right/3.png", false, true, true, true));
        loadedLayouts.add(loadLayout("bottom_top_right/4.png", false, true, true, true));
        loadedLayouts.add(loadLayout("bottom_top_right/5.png", false, true, true, true));

        loadedLayouts.add(loadLayout("left/0.png", true, false, false, false));
        loadedLayouts.add(loadLayout("left/1.png", true, false, false, false));
        loadedLayouts.add(loadLayout("left/2.png", true, false, false, false));
        loadedLayouts.add(loadLayout("left/3.png", true, false, false, false));
        loadedLayouts.add(loadLayout("left/4.png", true, false, false, false));
        loadedLayouts.add(loadLayout("left/5.png", true, false, false, false));

        loadedLayouts.add(loadLayout("left_right/0.png", true, true, false, false));
        loadedLayouts.add(loadLayout("left_right/1.png", true, true, false, false));
        loadedLayouts.add(loadLayout("left_right/2.png", true, true, false, false));
        loadedLayouts.add(loadLayout("left_right/3.png", true, true, false, false));
        loadedLayouts.add(loadLayout("left_right/4.png", true, true, false, false));
        loadedLayouts.add(loadLayout("left_right/5.png", true, true, false, false));

        loadedLayouts.add(loadLayout("left_right_bottom/0.png", true, true, false, true));
        loadedLayouts.add(loadLayout("left_right_bottom/1.png", true, true, false, true));
        loadedLayouts.add(loadLayout("left_right_bottom/2.png", true, true, false, true));
        loadedLayouts.add(loadLayout("left_right_bottom/3.png", true, true, false, true));
        loadedLayouts.add(loadLayout("left_right_bottom/4.png", true, true, false, true));
        loadedLayouts.add(loadLayout("left_right_bottom/5.png", true, true, false, true));

        loadedLayouts.add(loadLayout("left_right_top/0.png", true, true, true, false));
        loadedLayouts.add(loadLayout("left_right_top/1.png", true, true, true, false));
        loadedLayouts.add(loadLayout("left_right_top/2.png", true, true, true, false));
        loadedLayouts.add(loadLayout("left_right_top/3.png", true, true, true, false));
        loadedLayouts.add(loadLayout("left_right_top/4.png", true, true, true, false));
        loadedLayouts.add(loadLayout("left_right_top/5.png", true, true, true, false));

        loadedLayouts.add(loadLayout("right/0.png", false, true, false, false));
        loadedLayouts.add(loadLayout("right/1.png", false, true, false, false));
        loadedLayouts.add(loadLayout("right/2.png", false, true, false, false));
        loadedLayouts.add(loadLayout("right/3.png", false, true, false, false));
        loadedLayouts.add(loadLayout("right/4.png", false, true, false, false));
        loadedLayouts.add(loadLayout("right/5.png", false, true, false, false));

        loadedLayouts.add(loadLayout("top/0.png", false, false, true, false));
        loadedLayouts.add(loadLayout("top/1.png", false, false, true, false));
        loadedLayouts.add(loadLayout("top/2.png", false, false, true, false));
        loadedLayouts.add(loadLayout("top/3.png", false, false, true, false));
        loadedLayouts.add(loadLayout("top/4.png", false, false, true, false));
        loadedLayouts.add(loadLayout("top/5.png", false, false, true, false));

        loadedLayouts.add(loadLayout("top_left/0.png", true, false, true, false));
        loadedLayouts.add(loadLayout("top_left/1.png", true, false, true, false));
        loadedLayouts.add(loadLayout("top_left/2.png", true, false, true, false));
        loadedLayouts.add(loadLayout("top_left/3.png", true, false, true, false));
        loadedLayouts.add(loadLayout("top_left/4.png", true, false, true, false));
        loadedLayouts.add(loadLayout("top_left/5.png", true, false, true, false));

        loadedLayouts.add(loadLayout("top_right/0.png", false, true, true, false));
        loadedLayouts.add(loadLayout("top_right/1.png", false, true, true, false));
        loadedLayouts.add(loadLayout("top_right/2.png", false, true, true, false));
        loadedLayouts.add(loadLayout("top_right/3.png", false, true, true, false));
        loadedLayouts.add(loadLayout("top_right/4.png", false, true, true, false));
        loadedLayouts.add(loadLayout("top_right/5.png", false, true, true, false));

        loadedLayouts.add(loadLayout("all/0.png", true, true, true, true));
        loadedLayouts.add(loadLayout("all/1.png", true, true, true, true));
        loadedLayouts.add(loadLayout("all/2.png", true, true, true, true));
        loadedLayouts.add(loadLayout("all/3.png", true, true, true, true));
        loadedLayouts.add(loadLayout("all/4.png", true, true, true, true));
        loadedLayouts.add(loadLayout("all/5.png", true, true, true, true));
    }

    public static Layout getRandomLayout(boolean left, boolean right, boolean top, boolean bottom) {
        final List<Layout> matchingLayouts = loadedLayouts.stream()
                .filter(layout -> left == layout.isLeft())
                .filter(layout -> right == layout.isRight())
                .filter(layout -> top == layout.isTop())
                .filter(layout -> bottom == layout.isBottom())
                .collect(Collectors.toList());
        return matchingLayouts.stream()
                .skip(new Random().nextInt(matchingLayouts.size()))
                .findFirst()
                .orElse(null);
    }

    private static Layout loadLayout(String filename, boolean left, boolean right, boolean top, boolean bottom) {
        try (InputStream is = LayoutManager.class.getResourceAsStream("/layout/" + filename)) {
            BufferedImage bi = ImageIO.read(is);
            int[][] pixels = convertTo2D(bi);
            return new Layout(pixels, left, right, top, bottom);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int[][] convertTo2D(BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[height - row - 1][col] = image.getRGB(col, row);
            }
        }

        return result;
    }

}
