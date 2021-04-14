package fr.axicer.furryattack.common.map.layer;

import fr.axicer.furryattack.common.map.frame.FrameBlockType;
import fr.axicer.furryattack.util.OpenSimplexNoise;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A layer is a specific format for this game it's composed of
 * -> a byte= version
 * -> a byte width
 * -> a byte height
 * -> each pixels as integer
 */
public class Layer {
    public static final int CURRENT_VERSION = 42;

    int version, width, height;
    int[] data;

    private Layer(int version, int width, int height, int[] data) {
        this.version = version;
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVersion() {
        return version;
    }

    public int[] getData() {
        return data;
    }

    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final int pix = data[j + i * width];
                image.setRGB(j, i, pix);
            }
        }
        return image;
    }

    public void write(OutputStream stream) throws IOException {
        final AtomicInteger registryInc = new AtomicInteger(0);
        Map<Integer, Integer> registry = new HashMap<>();
        LinkedList<Instruction<Integer, Integer>> instructions = new LinkedList<>();
        final AtomicInteger lastID = new AtomicInteger(0);
        for(int i = 0 ; i < width * height ; i++){
            int val = data[i];
            registry.entrySet().stream()
                    .filter(entry -> entry.getValue() == val)
                    .findFirst()
                    .ifPresentOrElse(
                            entry -> {
                                if(lastID.get() == entry.getKey()){
                                    //ajouter +1 a la précédente instruction
                                    instructions.getLast().count += 1;
                                }else{
                                    //changer instruction et nbr à 1
                                    instructions.add(new Instruction<>(entry.getKey(), 1));
                                    //reset lastID
                                    lastID.set(entry.getKey());
                                }
                            },
                            () -> {
                                //enregistrer nouveau blockID
                                int ID = registryInc.getAndIncrement();
                                registry.put(ID, val);
                                //changer instruction et nbr à 1
                                instructions.add(new Instruction<>(ID, 1));
                                //reset lastID
                                lastID.set(ID);
                            }
                    );
        }

        ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + 2 + 4 + 4 + //version, width, height, registry size, instruction size
                registry.size() * (2 + 4) + //registry
                instructions.size() * (2 + 2)); //instructions

        buffer.put((byte) CURRENT_VERSION);
        buffer.putShort((short)width);
        buffer.putShort((short)height);
        buffer.putInt(registry.size());
        buffer.putInt(instructions.size());

        for (Map.Entry<Integer, Integer> entry : registry.entrySet()){
            buffer.putShort(entry.getKey().shortValue());
            buffer.putInt(entry.getValue());
        }

        for(Instruction<Integer, Integer> inst : instructions){
            buffer.putShort(inst.registryID.shortValue());
            buffer.putShort(inst.count.shortValue());
        }

        stream.write(buffer.array());
    }

    public static Layer parse(InputStream stream) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(stream.readAllBytes());
            int version = buffer.get();
            if (version != CURRENT_VERSION) {
                System.err.printf("Found version %d for layer parsing instead of current version (%d)", version, CURRENT_VERSION);
                return null;
            }
            int width = buffer.get();
            int height = buffer.get();

            int registrySize = buffer.getInt();
            int instructionCount = buffer.getInt();

            Map<Integer, Integer> registry = new HashMap<>();
            for(int i = 0 ; i < registrySize ; i++){
                int ID = buffer.getShort();
                int block = buffer.getInt();
                registry.put(ID, block);
            }

            List<Integer> data = new LinkedList<>();
            for(int i = 0 ; i < instructionCount ; i++){
                int ID = buffer.getShort();
                int count = buffer.getShort();
                int block = registry.get(ID);
                for(int j = 0 ; j < count ; j++){
                    data.add(block);
                }
            }

            return new Layer(version, width, height, data.stream().mapToInt(i->i).toArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Layer getStoneFullMap() {
        int height = 54;
        int width = (int) (height*(16.0/9.0));

        OpenSimplexNoise noise = new OpenSimplexNoise(0);
        var octave = 20f;
        ByteBuffer contentBuffer = ByteBuffer.allocate(width * height * 4);
        for (int i = 0; i < width * height; i++) {
            double w = (i%width)/octave;
            double h = (i/(float)width)/octave;
            if (noise.eval(w, h) > 0.01) {
                contentBuffer.put(new byte[]{0, 0, (byte) FrameBlockType.COBBLESTONE.ordinal(), 0b01000000});
            } else {
                contentBuffer.put(new byte[]{0, 0, (byte) FrameBlockType.AIR.ordinal(), 0b00000000});
            }
        }
        contentBuffer.clear();

        final var intBuffer = contentBuffer.asIntBuffer();
        int[] data = new int[intBuffer.remaining()];
        intBuffer.get(data);

        return new Layer(CURRENT_VERSION, width, height, data);
    }

    private static class Instruction<A, B>{

        A registryID;
        B count;

        public Instruction(A registryID, B count) {
            this.registryID = registryID;
            this.count = count;
        }
    }
}