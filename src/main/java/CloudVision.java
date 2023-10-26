import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CloudVision extends JFrame {
    private JButton btnOpenCam, btnSend;
    private JTextField entryText;

    public CloudVision() {
        setTitle("CloudVision");
        setSize(900, 600);
        setLayout(new BorderLayout());

        btnOpenCam = new JButton("Open Camera");
        btnOpenCam.addActionListener(e -> {
            CameraFrame cameraFrame = new CameraFrame(entryText);
            cameraFrame.setVisible(true);
        });

        btnSend = new JButton("Send");
        btnSend.addActionListener(e -> sendText());

        entryText = new JTextField(20);

        // Panel central con FlowLayout para organizar los elementos horizontalmente
        JPanel centerPanel = new JPanel();
        centerPanel.add(btnOpenCam);
        centerPanel.add(entryText);
        centerPanel.add(btnSend);

        // Agrega el panel central a la ventana principal
        add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

class CameraFrame extends JFrame {
    private JButton btnTakePhoto;
    private JLabel cameraLabel;
    private OpenCVFrameGrabber grabber;
    private IplImage currentFrame;
    private final Lock grabberLock = new ReentrantLock();
    private JTextField entryText;
    private volatile boolean isRunning = true;
    private Thread cameraThread;
    private JButton btnCloseCamera;


    public CameraFrame(JTextField entryText) {
        this.entryText = entryText;

        setTitle("Camera");
        setSize(900, 600);
        setLayout(new BorderLayout());

        cameraLabel = new JLabel();
        add(cameraLabel, BorderLayout.CENTER);

        btnTakePhoto = new JButton("Take Photo");
        btnTakePhoto.addActionListener(e -> captureAndProcess());

        // Inicialización del botón "Close Camera"
        btnCloseCamera = new JButton("Close Camera");
        btnCloseCamera.addActionListener(e -> {
            closeAndDispose(); // Método que cierra y libera la cámara y termina la ventana
        });

        JPanel southPanel = new JPanel(); // Panel para contener ambos botones
        southPanel.add(btnTakePhoto);
        southPanel.add(btnCloseCamera);
        add(southPanel, BorderLayout.SOUTH);
        startCamera();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRunning = false;
                if (cameraThread != null) {
                    try {
                        cameraThread.join(); // Asegúrate de que el thread de la cámara termine antes de proceder.
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                closeCamera(); // Crea este método
            }
        });


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    private void startCamera() {
        grabberLock.lock();
        try {
            if (grabber == null) {
                grabber = new OpenCVFrameGrabber(0);
                grabber.setImageWidth(832);
                grabber.setImageHeight(468);
                grabber.start();
            }

            Java2DFrameConverter converter = new Java2DFrameConverter();
            cameraThread = new Thread(() -> {
                try {
                    while (isRunning) {
                        org.bytedeco.javacv.Frame frame = grabber.grab();
                        if (frame == null) break;
                        currentFrame = new OpenCVFrameConverter.ToIplImage().convert(frame);
                        SwingUtilities.invokeLater(() -> {
                            ImageIcon icon = new ImageIcon(converter.getBufferedImage(frame));
                            cameraLabel.setIcon(icon);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            cameraThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            grabberLock.unlock();
        }
    }

    private void captureAndProcess() {
        if (currentFrame != null) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String formattedNow = now.format(formatter);

            File photosDir = new File("photos");
            if (!photosDir.exists()) {
                photosDir.mkdir();
            }

            String savedImagePath = "photos/" + formattedNow + ".png";
            opencv_imgcodecs.cvSaveImage(savedImagePath, currentFrame);

            try {
                String extractedText = OCR.detectDocument(savedImagePath);  // Aquí asumo que tienes un método detectDocument en una clase OCR.
                entryText.setText(extractedText);
            } catch (Exception e) {
                e.printStackTrace();
            }

            File file = new File(savedImagePath);
            file.delete();
            closeAndDispose();
        } else {
            System.out.println("No image to process.");
        }
    }

    private void closeCamera() {
        grabberLock.lock();
        try {
            if (grabber != null) {
                grabber.stop();
                grabber.release();
                grabber = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            grabberLock.unlock();
        }
    }

    private void closeAndDispose() {
        isRunning = false;
        if (cameraThread != null) {
            try {
                cameraThread.join(); // Asegúrate de que el thread de la cámara termine antes de proceder.
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        closeCamera();
        dispose(); // Cierra la ventana
    }
}