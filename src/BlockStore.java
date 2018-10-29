public class BlockStore {
    private int size;
    private double[][] store;
    private String storeType;
    private int position;

    public BlockStore(int size, String storeType, int position) {
        this.size = size;
        this.storeType = storeType;
        this.position = position;

        store = new double[size][size];
    }

    public int getSize() {
        return size;
    }

    public double[][] getStore() {
        return store;
    }

    public String getStoreType() {
        return storeType;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        String msg = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                msg += store[i][j] + " ";
            }
            msg += "\n";
        }
        return msg;
    }
}
