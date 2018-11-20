import java.util.ArrayList;
import java.util.List;

public class DeQuantIDCT {

    private DCTQuant dctQuant;

    private List<BlockStore> decodedY = new ArrayList<>();
    private List<BlockStore> decodedU = new ArrayList<>();
    private List<BlockStore> decodedV = new ArrayList<>();


    public DeQuantIDCT(DCTQuant dctQuant) {
        this.dctQuant = dctQuant;
    }

    private void transformBlocks(){
        for (BlockStore b: dctQuant.getQuantizedY()) {
            decodedY.add(inverseDct(deQuantizeBlock(b)));
        }
        for (BlockStore b: dctQuant.getQuantizedU()) {
            decodedU.add(sampleBlock(inverseDct(deQuantizeBlock(b)), 0, ""));
        }
        for (BlockStore b: dctQuant.getQuantizedV()) {
            decodedV.add(sampleBlock(inverseDct(deQuantizeBlock(b)), 0, ""));
        }
    }

    private BlockStore deQuantizeBlock(BlockStore b) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                b.getStore()[i][j] = b.getStore()[i][j] * dctQuant.getQuantizationMatrix()[i][j];
            }
        }
        return b;
    }

    private BlockStore inverseDct(BlockStore b) {
        BlockStore transformedBlock = new BlockStore(8, "", 0);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int sum = 0;
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        double alphaK = i == 0 ? 1/Math.sqrt(2) : 1;
                        double alphaL = j == 0 ? 1/Math.sqrt(2) : 1;
                        sum += alphaK * alphaL * b.getStore()[k][l] * Math.cos((2*i+1)*k*Math.PI/16) *
                                Math.cos((2*j+1)*l*Math.PI/16);
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
