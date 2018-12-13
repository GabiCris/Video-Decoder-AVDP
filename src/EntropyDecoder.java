import java.util.ArrayList;
import java.util.List;

public class EntropyDecoder {

    private List<BlockStore> decodedY = new ArrayList<>();
    private List<BlockStore> decodedU = new ArrayList<>();
    private List<BlockStore> decodedV = new ArrayList<>();

    private List<EntropyBlock> encodedList;

    public EntropyDecoder(List<EntropyBlock> enccodedList) {
        this.encodedList = enccodedList;
        entropyDecode();
        System.out.println();
    }

    private void entropyDecode() {
        ArrayList<Double> entropyList = new ArrayList<>();
        String type = "Y";
        for (EntropyBlock e: encodedList) {
            if (e.isEndBlock()) {
                while(entropyList.size() < 64) {
                    entropyList.add(0.0);
                }
            }
            else if (e.isBeginBlock() && !entropyList.isEmpty()) {
                switch (type) {
                    case "Y":
                        decodedY.add(convertToBlock(entropyList));
                        type = "U";
                        break;
                    case "U":
                        decodedU.add(convertToBlock(entropyList));
                        type = "V";
                        break;
                    case "V":
                        decodedV.add(convertToBlock(entropyList));
                        type = "Y";
                        break;
                }
                entropyList.clear();
                entropyList.add(e.amplitude + 0.0);
            }
            else {
                for (int z = 0; z < e.runelength; z++) {
                    entropyList.add(0.0);
                }
                entropyList.add(e.amplitude + 0.0);
            }
        }

        // add last block  in V
        decodedV.add(convertToBlock(entropyList));
    }

    // TODO: Reverse ZIG ZAG
    private BlockStore convertToBlock(List<Double> encodedList) {
        if (encodedList.size() != 64) {
            System.err.println("ENCODED LIST size not 64");
        }
        BlockStore b = new BlockStore(8,"",0);
        int pos = 0;

        for (int i = 0; i < 15; i++) {
            if (i % 2 == 1) {
                // down left
                int x = i < 8 ? 0 : i - 7;
                int y = i < 8 ? i : 8 - 1;
                while (x < 8 && y >= 0) {
                    b.getStore()[x++][y--] = encodedList.get(pos);
                    pos++;
                }
            } else {
                // up right
                int x = i < 8 ? i : 7;
                int y = i < 8 ? 0 : i - 7;
                while (x >= 0 && y < 8) {
                    b.getStore()[x--][y++] = encodedList.get(pos);
                    pos++;
                }
            }
        }
        return b;
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
