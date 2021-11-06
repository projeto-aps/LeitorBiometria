package leitorbiometria;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import org.opencv.objdetect.CascadeClassifier;
import util.TrainLBPH;

public class DeteccaoFace extends javax.swing.JFrame {

    private DaemonThread myThread = null;
    int count = 0;
    VideoCapture webSource = null;
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();
    CascadeClassifier faceDetector = new CascadeClassifier(DeteccaoFace.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
    MatOfRect faceDetections = new MatOfRect();
    int numSamples = 25, sample = 1;

    class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (webSource.grab()) {
                        try {
                            webSource.retrieve(frame);
                            Graphics g = pnlImagem.getGraphics();
                            faceDetector.detectMultiScale(frame, faceDetections);
                            for (Rect rect : faceDetections.toArray()) {
                                Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(0, 255, 0));
                                Mat face = new Mat(frame, rect);

                                if (btnCapturar.getModel().isPressed()) { //quando apertar o botão saveButton
                                    if (txtNome.getText().equals("") || txtNome.getText().equals(" ")) {
                                        JOptionPane.showMessageDialog(null, "Campo vazio");
                                    } else {
                                        if (sample <= numSamples) {
//                                        salva a imagem cortada [160,160]
//                                        nome do arquivo: idpessoa + a contagem de fotos. ex: person.10(id).6(sexta foto).jpg
                                            String cropped = "C:\\photos\\person." + txtNome.getText() + "." + sample + ".jpg";
                                            imwrite(cropped, face);

                                            //System.out.println("Foto " + amostra + " capturada\n");
//                                        counterLabel.setText(String.valueOf(sample) + "/25");
                                            sample++;
                                        }
                                        if (sample > 25) {
                                            new TrainLBPH().trainPhotos();//se a contagem for maior que 25, termina de tirar a foto, gera o arquivo
//                                            insertDatabase(); //insere os dados no banco

                                            System.out.println("File Generated");
                                            stopCamera(); // e fecha a camera
                                        }

                                    }
                                }

                            }

                            Highgui.imencode(".bmp", frame, mem);
                            Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                            BufferedImage buff = (BufferedImage) im;
                            if (g.drawImage(buff, 0, 0, getWidth(), getHeight() - 150, 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                                if (runnable == false) {
                                    System.out.println("Paused ..... ");
                                    this.wait();
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println("Error!!");
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public DeteccaoFace() {
        initComponents();
        System.out.println(DeteccaoFace.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlImagem = new javax.swing.JPanel();
        btnIniciar = new javax.swing.JButton();
        btnPausar = new javax.swing.JButton();
        btnCapturar = new javax.swing.JButton();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout pnlImagemLayout = new javax.swing.GroupLayout(pnlImagem);
        pnlImagem.setLayout(pnlImagemLayout);
        pnlImagemLayout.setHorizontalGroup(
            pnlImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );
        pnlImagemLayout.setVerticalGroup(
            pnlImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        btnPausar.setText("Pausar");
        btnPausar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPausarActionPerformed(evt);
            }
        });

        btnCapturar.setText("Capturar");
        btnCapturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapturarActionPerformed(evt);
            }
        });

        lblNome.setText("Nome:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlImagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(btnIniciar)
                        .addGap(59, 59, 59)
                        .addComponent(btnPausar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCapturar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlImagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIniciar)
                    .addComponent(btnPausar)
                    .addComponent(btnCapturar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPausarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPausarActionPerformed
        stopCamera();
    }//GEN-LAST:event_btnPausarActionPerformed

    private void stopCamera() {
        myThread.runnable = false;            // stop thread
        btnPausar.setEnabled(false);   // activate start button 
        btnIniciar.setEnabled(true);     // deactivate stop button
        webSource.release();  // stop caturing fron cam
    }

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        webSource = new VideoCapture(0); // video capture from default cam
        myThread = new DaemonThread(); //create object of threat class
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();                 //start thrad
        btnIniciar.setEnabled(false);  // deactivate start button
        btnPausar.setEnabled(true);  //  activate stop button

    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnCapturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapturarActionPerformed

    }//GEN-LAST:event_btnCapturarActionPerformed

    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DeteccaoFace().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapturar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnPausar;
    private javax.swing.JLabel lblNome;
    private javax.swing.JPanel pnlImagem;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
