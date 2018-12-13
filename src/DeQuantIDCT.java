import java.util.ArrayList;
import java.util.List;

public class DeQuantIDCT {

    private DCTQuant dctQuant;

    private List<BlockStore> decodedY = new ArrayList<>();
    private List<BlockStore> decodedU = new ArrayList<>();
    private List<BlockStore> decodedV = new ArrayList<>();


    public DeQuantIDCT(DCTQuant dctQuant) {
        this.dctQuant = dctQuant;
        transformBlocks();
    }

    private void transformBlocks(){
        for (BlockStore b: dctQuant.getQuantizedY()) {
            decodedY.add(inverseDct(deQuantizeBlock(b)));
        }
        for (BlockStore b: dctQuant.getQuantizedU()) {
            decodedU.add(inverseDct(deQuantizeBlock(b)));
        }
        for (BlockStore b: dctQuant.getQuantizedV()) {
            decodedV.add(inverseDct(deQuantizeBlock(b)));
        }
    }

    private BlockStore deQuantizeBlock(BlockStore b) {
        BlockStore newBlock = new BlockStore(8,"", 0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBlock.getStore()[i][j] = b.getStore()[i][j] * dctQuant.getQuantizationMatrix()[i][j];
            }
        }
        return newBlock;
    }

    private BlockStore inverseDct(BlockStore b) {
        BlockStore transformedBlock = new BlockStore(8, "", 0);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int sum = 0;
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        sum += alpha(k) * alpha(l) * b.getStore()[k][l] * Math.cos(((2*i+1)*k*Math.PI)/16) *
                                Math.cos(((2*j+1)*l*Math.PI)/16);
                    }
                }
                transformedBlock.getStore()[i][j] = 0.25 * sum + 128;
            }
        }
        return transformedBlock;
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

    private double alpha(int value) {
        return value > 0 ? 1 : (1 / Math.sqrt(2.0));
    }

    public List<BlockStore> getDecodedY() {
        return decodedY;
    }

    public List<BlockStore> getDecodedU() {
        return decodedU;
    }

    public List<BlockStore> getDecodedV() {
        return decodedV;
    }
}
