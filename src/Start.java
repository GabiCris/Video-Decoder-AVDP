import java.io.IOException;

public class Start {
    public static String filename = "nt-P3.ppm";

    public static void main(String[] args) {
        PPMReader reader = new PPMReader(filename);
        Encoder encoder = new Encoder(reader);

        DCTQuant dctQuant = new DCTQuant(encoder);


        EntropyEncoder entropyEncoder = new EntropyEncoder(dctQuant.getQuantizedY(), dctQuant.getQuantizedU(),
                dctQuant.getQuantizedV());
        EntropyDecoder entropyDecoder = new EntropyDecoder(entropyEncoder.getOutputEncoded());


        dctQuant.setQuantizedY(entropyDecoder.getDecodedY());
        dctQuant.setQuantizedU(entropyDecoder.getDecodedU());
        dctQuant.setQuantizedV(entropyDecoder.getDecodedV());
        DeQuantIDCT deQuantIDCT = new DeQuantIDCT(dctQuant);

        encoder.setEncodedY(deQuantIDCT.getDecodedY());
        encoder.setEncodedU(deQuantIDCT.getDecodedU());
        encoder.setEncodedV(deQuantIDCT.getDecodedV());

//        encoder.setEncodedY(entropyDecoder.getDecodedY());
//        encoder.setEncodedU(entropyDecoder.getDecodedU());
//        encoder.setEncodedV(entropyDecoder.getDecodedV());

        Decoder decoder = new Decoder(encoder);
        YUVtoPPM converter = new YUVtoPPM(decoder);

        try {
            converter.outputPPM(converter.getOutputMatrix(), "test.ppm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
