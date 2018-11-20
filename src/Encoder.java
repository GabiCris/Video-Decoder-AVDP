import java.util.ArrayList;
import java.util.List;

public class Encoder {
    private PPMReader ppmReader;

    private List<BlockStore> encodedY = new ArrayList<>();
    private List<BlockStore> encodedU = new ArrayList<>();
    private List<BlockStore> encodedV = new ArrayList<>();

    public Encoder(PPMReader ppmReader) {
        this.ppmReader = ppmReader;
        encodeY();
        encodeU();
        encodeV();
    }

    public void encodeY() {
        int iterations = ppmReader.getWidth() * ppmReader.getHeight() / 64;
        int line = 0;
        int column = 0;
        for(int position = 0; position < iterations && line != ppmReader.getHeight(); position++) {
            BlockStore store = new BlockStore(8, "Y", position);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    store.getStore()[i][j] = ppmReader.getY()[line][column];
                    column++;
                    if (column % 8 == 0) {
                        line++;
                        column -= 8;
                    }
                }
            }
            line -= 8;
            column += 8;
            encodedY.add(store);
            if (column == ppmReader.getWidth()) {
                column = 0;
                line += 8;
            }
        }
    }

    private void encodeU() {
        int iterations = ppmReader.getWidth() * ppmReader.getHeight() / 64;
        int line = 0;
        int column = 0;
        for(int position = 0; position < iterations && line != ppmReader.getHeight(); position++) {
            BlockStore store = new BlockStore(8, "U", position);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    store.getStore()[i][j] = ppmReader.getU()[line][column];
                    column++;
                    if (column % 8 == 0) {
                        line++;
                        column -= 8;
                    }
                }
            }
            line -= 8;
            column += 8;

            encodedU.add(sampleBlock(store, position, "U"));
            if (column == ppmReader.getWidth()) {
                column = 0;
                line += 8;
            }
        }
    }

    public void encodeV() {
        int iterations = ppmReader.getWidth() * ppmReader.getHeight() / 64;
        int line = 0;
        int column = 0;
        for(int position = 0; position < iterations && line != ppmReader.getHeight(); position++) {
            BlockStore store = new BlockStore(8, "V", position);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    store.getStore()[i][j] = ppmReader.getV()[line][column];
                    column++;
                    if (column % 8 == 0) {
                        line++;
                        column -= 8;
                    }
                }
            }
            line -= 8;
            column += 8;

            encodedV.add(sampleBlock(store, position, "V"));
            if (column == ppmReader.getWidth()) {
                column = 0;
                line += 8;
            }
        }
    }

    public BlockStore sampleBlock(BlockStore toSample, int position, String type) {
        BlockStore sampledStore = new BlockStore(4, type, position);
        int line = 0;
        int column =  0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double aux = (toSample.getStore()[line][column] + toSample.getStore()[line][column + 1]
                        + toSample.getStore()[line + 1][column] + toSample.getStore()[line + 1][column + 1]) / 4.0;
                sampledStore.getStore()[i][j] = aux;
                column += 2;
            }
            line += 2;
            column = 0;
        }
        return sampledStore;
    }

    public List<BlockStore> getEncodedY() {
        return encodedY;
    }

    public List<BlockStore> getEncodedU() {
        return encodedU;
    }

    public List<BlockStore> getEncodedV() {
        return encodedV;
    }

    public PPMReader getPpmReader() {
        return ppmReader;
    }

    public void setEncodedY(List<BlockStore> encodedY) {
        this.encodedY = encodedY;
    }

    public void setEncodedU(List<BlockStore> encodedU) {
        this.encodedU = encodedU;
    }

    public void setEncodedV(List<BlockStore> encodedV) {
        this.encodedV = encodedV;
    }
}
