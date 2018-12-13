
import java.util.ArrayList;
import java.util.List;

public class DCTQuant {
    private Encoder encoder;

    private List<BlockStore> quantizedY = new ArrayList<>();
    private List<BlockStore> quantizedU = new ArrayList<>();
    private List<BlockStore> quantizedV = new ArrayList<>();

    private int[][] quantizationMatrix = {
            {6, 4, 4, 6, 10, 16, 20, 24},
            {5, 5, 6, 8, 10, 23, 24, 22},
            {6, 5, 6, 10, 16, 23, 28, 22},
            {6, 7, 9, 12, 20, 35, 32, 25},
            {7, 9, 15, 22, 27, 44, 41, 31},
            {10, 14, 22, 26, 32, 42, 45, 37},
            {20, 26, 31, 35, 41, 48, 48, 40},
            {29, 37, 38, 39, 45, 40, 41, 40}
    };

    public DCTQuant(Encoder encoder) {
        this.encoder = encoder;
        transformBlocks();
    }

    private void transformBlocks() {
        for (BlockStore b: encoder.getEncodedY()) {
            BlockStore tr = discreteCosineTr(subtractBlock(b));
            quantizedY.add(quantizeBlock(tr));
        }
        for (BlockStore b: encoder.getEncodedU()) {
            BlockStore tr = discreteCosineTr(subtractBlock(resizeBlock(b, 0, "")));
            quantizedU.add(quantizeBlock(tr));
        }
        for (BlockStore b: encoder.getEncodedV()) {
            BlockStore tr = discreteCosineTr(subtractBlock(resizeBlock(b, 0, "")));
            quantizedV.add(quantizeBlock(tr));
        }
    }

    private BlockStore discreteCosineTr(BlockStore b) {
        BlockStore transformedBlock = new BlockStore(8, "", 0);

        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {
                int sum = 0;
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        sum += b.getStore()[x][y] * Math.cos(((2*x+1)*u*Math.PI)/16) *
                                Math.cos(((2*y+1)*v*Math.PI)/16);
                    }
                }
                transformedBlock.getStore()[u][v] = 0.25 * alpha(u) * alpha(v) * sum;
            }
        }
        return transformedBlock;
    }

    private BlockStore quantizeBlock(BlockStore b) {
        BlockStore newBlock = new BlockStore(8,"", 0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
               // newBlock.getStore()[i][j] = Math.floor(b.getStore()[i][j] / quantizationMatrix[i][j]);
                newBlock.getStore()[i][j] = b.getStore()[i][j] / quantizationMatrix[i][j];
            }
        }
        return newBlock;
    }

    public BlockStore resizeBlock(BlockStore toResize, int position, String type) {
        BlockStore sampledStore = new BlockStore(8, type, position);
        int line = 0;
        int column = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double value = toResize.getStore()[i][j];
                sampledStore.getStore()[line][column] = value;
                sampledStore.getStore()[line][column+1] = value;
                sampledStore.getStore()[line+1][column] = value;
                sampledStore.getStore()[line+1][column+1] = value;
                column += 2;
            }
            line += 2;
            column = 0;
        }

        return sampledStore;
    }

    private double alpha(int value) {
        return value > 0 ? 1 : (1 / Math.sqrt(2.0));
    }

    private BlockStore subtractBlock(BlockStore b) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                b.getStore()[i][j] -= 128;
            }
        }
        return b;
    }

    public List<BlockStore> getQuantizedY() {
        return quantizedY;
    }

    public List<BlockStore> getQuantizedU() {
        return quantizedU;
    }

    public List<BlockStore> getQuantizedV() {
        return quantizedV;
    }

    public int[][] getQuantizationMatrix() {
        return quantizationMatrix;
    }

    public void setQuantizedY(List<BlockStore> quantizedY) {
        this.quantizedY = quantizedY;
    }

    public void setQuantizedU(List<BlockStore> quantizedU) {
        this.quantizedU = quantizedU;
    }

    public void setQuantizedV(List<BlockStore> quantizedV) {
        this.quantizedV = quantizedV;
    }
}
