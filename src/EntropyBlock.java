public class EntropyBlock {
    int runelength;
    int size;
    int amplitude;

    public EntropyBlock(int runelength, int size, int amplitude) {
        this.runelength = runelength;
        this.size = size;
        this.amplitude = amplitude;
    }

    public int getRunelength() {
        return runelength;
    }

    public boolean isEndBlock() {
        return runelength == 0 && size == 0 && amplitude == 0;
    }

    public boolean isBeginBlock() {
        return runelength == -1;
    }

    public void setRunelength(int runelength) {
        this.runelength = runelength;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude;
    }

    @Override
    public String toString() {
        return "EntropyBlock{" +
                "runelength=" + runelength +
                ", size=" + size +
                ", amplitude=" + amplitude +
                '}';
    }
}
