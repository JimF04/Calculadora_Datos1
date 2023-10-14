import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class OCR {

    public static ImageAnnotatorClient getVisionClient() throws IOException {
        // Ruta al archivo JSON de clave de cuenta de servicio
        String keyPath = "extra/application_default_credentials.json";

        // Carga las credenciales desde el archivo JSON
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));

        // Crea el cliente de Vision API usando las credenciales
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(() -> credentials).build();
        return ImageAnnotatorClient.create(settings);
    }

    public static byte[] readImage(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public static String detectDocument(String path) throws Exception {
        try (ImageAnnotatorClient client = getVisionClient()) {
            ByteString content = ByteString.copyFrom(readImage(path));
            Image image = Image.newBuilder().setContent(content).build();
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(List.of(
                    AnnotateImageRequest.newBuilder()
                            .addFeatures(Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION))
                            .setImage(image)
                            .build()));
            return processResponse(response);
        }
    }

    public static String processResponse(BatchAnnotateImagesResponse response) throws Exception {
        AnnotateImageResponse annotateImageResponse = response.getResponses(0);
        if (annotateImageResponse.hasError()) {
            throw new Exception(annotateImageResponse.getError().getMessage() +
                    "\nPara más información sobre mensajes de error, revisa: " +
                    "https://cloud.google.com/apis/design/errors");
        }

        TextAnnotation textAnnotation = annotateImageResponse.getFullTextAnnotation();
        StringBuilder extractedText = new StringBuilder();
        for (Page page : textAnnotation.getPagesList()) {
            for (Block block : page.getBlocksList()) {
                for (Paragraph paragraph : block.getParagraphsList()) {
                    for (Word word : paragraph.getWordsList()) {
                        StringBuilder wordText = new StringBuilder();
                        for (Symbol symbol : word.getSymbolsList()) {
                            wordText.append(symbol.getText());
                        }
                        extractedText.append(" ").append(wordText.toString());
                    }
                }
            }
        }
        return extractedText.toString().trim();
    }
}
