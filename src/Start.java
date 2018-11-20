import java.io.IOException;

public class Start {
    public static String filename = "nt-P3.ppm";

    public static void main(String[] args) {
        PPMReader reader = new PPMReader(filename);
        Encoder encoder = new Encoder(reader);

        DCTQuant dctQuant = new DCTQuant(encoder);
        DeQuantIDCT deQuantIDCT = new DeQuantIDCT(dctQuant);
        encoder.setEncodedY(deQuantIDCT.getDecodedY());
        encoder.setEncodedU(deQuantIDCT.getDecodedU());
        encoder.setEncodedV(deQuantIDCT.getDecodedV());

        Decoder decoder = new Decoder(encoder);
        YUVtoPPM converter = new YUVtoPPM(decoder);

        try {
            converter.outputPPM(converter.getOutputMatrix(), "test.ppm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
