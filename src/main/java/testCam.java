import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.leptonica.global.leptonica;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.tesseract.TessBaseAPI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class testCam extends JFrame {
    private JButton btnOpenCam, btnTakePhoto, btnSend, btnCloseCam;
    private JTextField entryText;
    private CanvasFrame canvasFrame;
    private OpenCVFrameGrabber grabber;
    private IplImage currentFrame;

    public testCam() {
        setTitle("testCam");
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
            while (canvasFrame.isVisible()) {
                try {
                    org.bytedeco.javacv.Frame frame = grabber.grab();
                    if (frame == null) {
                        System.out.println("No se pudo obtener un frame.");
                        break;
                    }
                    canvasFrame.showImage(frame);
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
        try {
            TessBaseAPI tessBaseAPI = new TessBaseAPI();
            if (tessBaseAPI.Init("lib/Tess4J/tessdata", "eng", 3) != 0) {
                System.out.println("Error al inicializar Tesseract.");
                return;
            }

            tessBaseAPI.SetPageSegMode(3);

            if (currentFrame == null) {
                System.out.println("currentFrame es null. No se puede procesar.");
                return;
            }

            org.bytedeco.leptonica.PIX pixImage = iplImageToPIX(currentFrame);
            if (pixImage == null) {
                System.out.println("No se pudo convertir IplImage a PIX.");
                return;
            }

            // guardar foto
            // saveImage(currentFrame);

            tessBaseAPI.SetImage(pixImage);
            tessBaseAPI.SetVariable("tessedit_char_whitelist", "0123456789+-=*/^()%&|~");

            BytePointer bytePointer = tessBaseAPI.GetUTF8Text();
            if (bytePointer == null) {
                System.out.println("Tesseract no devolvió texto.");
                tessBaseAPI.End();
                return;
            }

            String text = bytePointer.getString();
            if (text == null || text.isEmpty()) {
                System.out.println("El texto devuelto por Tesseract está vacío o es nulo.");
                JOptionPane.showMessageDialog(null, "No se leyó nada. Intente de nuevo.");
                tessBaseAPI.End();
                bytePointer.close();
                return;
            } else { // Si Tesseract devuelve un texto
                if (canvasFrame != null) {
                    canvasFrame.dispose(); // Cerrar la ventana de la cámara
                    canvasFrame = null;  // Resetear el objeto canvasFrame
                    grabber = null;     // Resetear el objeto grabber
                }
            }

            System.out.println("Texto obtenido: " + text);
            entryText.setText(text);
            tessBaseAPI.End();
            bytePointer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Excepción capturada: " + e.getMessage());
        }
    }


    private void saveImage(IplImage img) {
        File directory = new File("photos");
        if (!directory.exists()){
            directory.mkdir();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = "photos/captured_image_" + timeStamp + ".png";
        org.bytedeco.opencv.global.opencv_imgcodecs.cvSaveImage(path, img);
    }


    private PIX matToPix(Mat mat) {
        BytePointer bp = new BytePointer();
        org.bytedeco.opencv.global.opencv_imgcodecs.imencode(".png", mat, bp);
        byte[] byteArray = bp.getStringBytes();
        PIX pix = leptonica.pixReadMem(byteArray, byteArray.length);
        bp.close();
        return pix;
    }

    private PIX iplImageToPIX(IplImage src) {
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        Mat mat = converterToMat.convertToMat(new OpenCVFrameConverter.ToIplImage().convert(src));

        return matToPix(mat);
    }

    private void sendText() {
        System.out.println(entryText.getText());
    }


    public void iniciar(){

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            testCam screen = new testCam();
            screen.setVisible(true);
        });
    }
}