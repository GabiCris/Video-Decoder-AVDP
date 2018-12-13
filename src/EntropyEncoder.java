import java.util.ArrayList;
import java.util.List;

public class EntropyEncoder {

    private List<BlockStore> encodedY;
    private List<BlockStore> encodedU;
    private List<BlockStore> encodedV;

    private List<EntropyBlock> outputEncoded = new ArrayList<>();

    public EntropyEncoder(List<BlockStore> encodedY, List<BlockStore> encodedU, List<BlockStore> encodedV) {
        this.encodedY = encodedY;
        this.encodedU = encodedU;
        this.encodedV = encodedV;
        entropyEncoding();
    }

    public int getSize(int ampl) {
        if (ampl == 0) return 0;
        if (ampl == -1 || ampl == 1) return 1;
        if (ampl == -3 || ampl == -2 || ampl == 3 || ampl == 2) return 2;
        if (ampl >= -7 && ampl <= 7) return 3;
        if (ampl >= -15 && ampl <= 15) return 4;
        if (ampl >= -31 && ampl <= 31) return 5;
        if (ampl >= -63 && ampl <= 63) return 6;
        if (ampl >= -127 && ampl <= 127) return 7;
        if (ampl >= -255 && ampl <= 255) return 8;
        if (ampl >= -511 && ampl <= 511) return 9;
        if (ampl >= -1023 && ampl <= 1023) return 10;
        return 0;
    }

    // TODO: ZIG ZAG PARSE
    private List<Double> zigZagParse(BlockStore b) {
        ArrayList<Double> parsedList = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            if (i % 2 == 1) {
                // jos stanga
                int x = i < 8 ? 0 : i - 7;
                int y = i < 8 ? i : 7;
                while (x < 8 && y >= 0) {
                    parsedList.add(b.getStore()[x++][y--]);
                }
            } else {
                // sus dreapta
                int x = i < 8 ? i : 7;
                int y = i < 8 ? 0 : i - 7;
                while (x >= 0 && y < 8) {
                    parsedList.add(b.getStore()[x--][y++]);
                   }
            }
        }

        return parsedList;
    }

    private void entropyEncoding() {
        for (int blockNo = 0; blockNo < encodedY.size(); blockNo++) {
            encodeBlock(encodedY.get(blockNo));
            encodeBlock(encodedU.get(blockNo));
            encodeBlock(encodedV.get(blockNo));
        }
    }

    private void encodeBlock(BlockStore b) {
        List<Double> zagParse = zigZagParse(b);
        outputEncoded.add(new EntropyBlock(-1, getSize(zagParse.get(0).intValue()), zagParse.get(0).intValue()));

        int count = zagParse.size() - 1;
        while (zagParse.get(count).intValue() == 0 && count > 0) {
            count--;
        }

        int zeroCount = 0;
        for (int i = 1; i <= count; i++) {
            if (zagParse.get(i).intValue() == 0) {
                zeroCount++;
            }
            else {
                outputEncoded.add(new EntropyBlock(zeroCount, getSize(zagParse.get(i).intValue()), zagParse.get(i).intValue()));
                zeroCount = 0;
            }
        }

        if (count != zagParse.size() - 1) {
            outputEncoded.add(new EntropyBlock(0, 0, 0));
        }
    }

    public List<EntropyBlock> getOutputEncoded() {
        return outputEncoded;
    }
}
