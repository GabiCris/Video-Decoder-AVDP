public class Decoder {

    private Encoder encoder;

    private double[][] Y;
    private double[][] U;
    private double[][] V;

    public Decoder(Encoder encoder) {
        this.encoder = encoder;
        Y = new double[encoder.getPpmReader().getHeight()][encoder.getPpmReader().getWidth()];
        U = new double[encoder.getPpmReader().getHeight()][encoder.getPpmReader().getWidth()];
        V = new double[encoder.getPpmReader().getHeight()][encoder.getPpmReader().getWidth()];
        decodeY();
        decodeU();
        decodeV();
    }

    public void decodeY() {
        int line = 0;
        int column = 0;
        for(BlockStore blockStore: encoder.getEncodedY()) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Y[line][column] = blockStore.getStore()[i][j];
                    column++;
                    if (column % 8 == 0) {
                        line++;
                        column = 0;
                    }
                }
            }
            line -= 8;
            column += 8;
            if (column == encoder.getPpmReader().getWidth()-1) {
                column = 0;
                line += 8;
            }
        }
    }

    public void decodeU() {
        int line = 0;
        int column = 0;
        for(BlockStore blockStore: encoder.getEncodedU()) {
            BlockStore store = resizeBlock(blockStore, 0, "U");
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    U[line][column] = store.getStore()[i][j];
                    column++;
                    if (column % 8 == 0) {
                        line++;
                        column = 0;
                    }
                }
            }
            line -= 8;
            column += 8;
            if (column == encoder.getPpmReader().getWidth()) {
                column = 0;
                line += 8;
            }
        }
    }

    public void decodeV() {
        int line = 0;
        int column = 0;
        for(BlockStore blockStore: encoder.getEncodedV()) {
            BlockStore store = resizeBlock(blockStore, 0, "V");
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    V[line][column] = store.getStore()[i][j];
                    column++;
                    if (column % 8 == 0) {
                        line++;
                        column = 0;
                    }
                }
            }
            line -= 8;
            column += 8;
            if (column == encoder.getPpmReader().getWidth()) {
                column = 0;
                line += 8;
            }
        }
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
        //System.out.println(toResize);
        //System.out.println(sampledStore);
        return sampledStore;
    }

    public double[][] getY() {
        return Y;
    }

    public double[][] getU() {
        return U;
    }

    public double[][] getV() {
        return V;
    }
}
