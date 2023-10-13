import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.global.opencv_imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

public class CloudVision extends JFrame {
    private JButton btnOpenCam, btnTakePhoto, btnSend, btnCloseCam;
    private JTextField entryText;
    private CanvasFrame canvasFrame;
    private OpenCVFrameGrabber grabber;
    private IplImage currentFrame;

    public CloudVision() {
        setTitle("CloudVision");
        setSize(500, 500);
        setLayout(new FlowLayout());

        btnOpenCam = new JButton("Open Camera");
        btnOpenCam.addActionListener(e -> startCamera());

        btnTakePhoto = new JButton("Take Photo");
        btnTakePhoto.addActionListener(e -> captureAndProcess());

        btnSend = new JButton("Send");
        btnSend.addActionListener(e -> sendText());

        btnCloseCam = new JButton("Close Camera");
        btnCloseCam.addActionListener(e -> closeCamera());

        entryText = new JTextField(20);

        add(btnOpenCam);
        add(btnTakePhoto);
        add(btnSend);
        add(btnCloseCam);
        add(entryText);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void closeCamera() { // <-- Método para cerrar la cámara
        if (canvasFrame != null) {
            canvasFrame.dispose();
            canvasFrame = null;
        }
        if (grabber != null) {
            try {
                grabber.stop();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al detener grabber.");
            }
            grabber = null;
        }
    }

    private void startCamera() {
        // Si grabber ya existe, reinícialo.
        if (grabber != null) {
            try {
                grabber.restart();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al reiniciar grabber.");
                return;
            }
        } else {
            grabber = new OpenCVFrameGrabber(0);
            grabber.setImageWidth(832);  // Ejemplo de ancho
            grabber.setImageHeight(468);  // Ejemplo de alto
            try {
                grabber.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al iniciar grabber.");
                return;
            }
        }

        // Si canvasFrame ya existe, reinícialo.
        if (canvasFrame != null) {
            canvasFrame.dispose();
            canvasFrame = null;
        }
        canvasFrame = new CanvasFrame("Camera", 1);
        canvasFrame.setCanvasSize(832, 468);
        canvasFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        new Thread(() -> {
            while (canvasFrame != null && canvasFrame.isVisible()) {
                try {
                    org.bytedeco.javacv.Frame frame = grabber.grab();
                    if (frame == null) {
                        System.out.println("No se pudo obtener un frame.");
                        break;
                    }
                    if (canvasFrame != null) {  // Asegurándonos de que canvasFrame no es null antes de usarlo
                        canvasFrame.showImage(frame);
                    }
                    currentFrame = new OpenCVFrameConverter.ToIplImage().convert(frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                grabber.stop();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al detener grabber.");
            }
        }).start();
    }

    private void captureAndProcess() {
        if (currentFrame != null) {
            // Obtiene la fecha y hora actuales y las formatea para ser utilizadas en un nombre de archivo
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String formattedNow = now.format(formatter);

            // Crea la carpeta 'photos' si no existe
            File photosDir = new File("photos");
            if (!photosDir.exists()) {
                photosDir.mkdir();
            }

            // Salva la imagen actual
            String savedImagePath = "photos/" + formattedNow + ".png";

            opencv_imgcodecs.cvSaveImage(savedImagePath, currentFrame);

            // Llama al OCR y actualiza entryText con el texto obtenido
            try {
                String extractedText = OCR.detectDocument(savedImagePath);
                entryText.setText(extractedText);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e.getMessage());
            }

            // Borra la imagen guardada
            File file = new File(savedImagePath);
            file.delete();

        } else {
            System.out.println("No image to process.");
        }
    }

    private void sendText() {
        System.out.println(entryText.getText());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CloudVision screen = new CloudVision();
            screen.setVisible(true);
        });
    }
}