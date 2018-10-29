import java.io.IOException;

public class Start {
    public static String filename = "nt-P3.ppm";

    public static void main(String[] args) {
        PPMReader reader = new PPMReader(filename);
        Encoder encoder = new Encoder(reader);
        Decoder decoder = new Decoder(encoder);
        YUVtoPPM converter = new YUVtoPPM(decoder);

        try {
            converter.outputPPM(converter.getOutputMatrix(), "test.ppm");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(encoder.getEncodedU().size());
        //System.out.println( ((reader.getU()[0][0] + reader.getU()[0][1] + reader.getU()[1][0] + reader.getU()[1][1])) / 4);
        //System.out.println(" " + decoder.getU()[2][0] +" " + decoder.getU()[0][2] +" " + decoder.getU()[1][0] + decoder.getU()[1][1]);
        //System.out.println(encoder.getEncodedU().get(0));
        //encoder.getEncodedU().stream().map(BlockStore::toString).forEach(System.out::println);
    }
}
