package se.soprasteria.automatedtesting.webdriver.batchsnipper;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.imageio.stream.*;
import javax.swing.*;

public class Capture extends JFrame {

    private Dimension screenDimensions;
    private Rectangle screenRectangle;
    private JFileChooser fcSave;
    private ImageArea captureImageArea;
    private JScrollPane captureImageScrolling;
    Robot robot;


    public Capture(String title) {
        super(title);
        initWindowArea();
        initSaveFileChooser();
        initMenuBar();
        initWindowScrolling();
    }

    private void initRobot() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            showError(e.getMessage());
            System.exit(0);
        } catch (SecurityException e) {
            showError("Permission required to use Robot.");
            System.exit(0);
        }
    }

    private void initWindowArea() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        captureImageArea = new ImageArea();
        screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        screenRectangle = new Rectangle(screenDimensions);
    }

    private void initSaveFileChooser() {
        fcSave = new JFileChooser();
        fcSave.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fcSave.setAcceptAllFileFilterUsed(false);
        fcSave.setFileFilter(new ImageFileFilter());
    }

    private void initMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("File");

        JMenuItem mi = new JMenuItem("Save As...");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));


        mi.addActionListener(new saveActionListener());
        menu.add(mi);

        menu.addSeparator();

        mi = new JMenuItem("Exit");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                InputEvent.ALT_MASK));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(mi);

        mb.add(menu);

        menu = new JMenu("Capture");

        mi = new JMenuItem("Capture");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                InputEvent.ALT_MASK));

        mi.addActionListener(new captureActionListener());
        menu.add(mi);

        mb.add(menu);

        mi = new JMenuItem("Crop");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,
                InputEvent.ALT_MASK));

        mi.addActionListener(new cropActionListener());
        menu.add(mi);

        mb.add(menu);

        // Install these menus.

        setJMenuBar(mb);
    }

    private void initWindowScrolling() {
        // Install a scollable ImageArea component.

        getContentPane().add(captureImageScrolling = new JScrollPane(captureImageArea));

        // Size main window to half the screen's size, and center window.

        setSize(screenDimensions.width / 2, screenDimensions.height / 2);

        setLocation((screenDimensions.width - screenDimensions.width / 2) / 2,
                (screenDimensions.height - screenDimensions.height / 2) / 2);

        // Display the GUI and start the event-handling thread.

        setVisible(true);
        initRobot();
    }

    private class cropActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Crop ImageArea component and adjust the scrollbars if
            // cropping succeeds.

            if (captureImageArea.crop()) {
                captureImageScrolling.getHorizontalScrollBar().setValue(0);
                captureImageScrolling.getVerticalScrollBar().setValue(0);
            } else
                showError("Out of bounds.");
        }
    }

    private class captureActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Hide Capture's main window so that it does not appear in
            // the screen capture.

            setVisible(false);

            // Perform the screen capture.

            BufferedImage biScreen;
            biScreen = robot.createScreenCapture(screenRectangle);

            // Show Capture's main window for continued user interaction.

            setVisible(true);

            // Update ImageArea component with the new image, and adjust
            // the scrollbars.

            captureImageArea.setImage(biScreen);

            captureImageScrolling.getHorizontalScrollBar().setValue(0);
            captureImageScrolling.getVerticalScrollBar().setValue(0);
        }
    }

    private class saveActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Disallow image saving if there is no image to save.

            if (captureImageArea.getImage() == null) {
                showError("No captured image.");
                return;
            }

            // Present the "save" file chooser without any file selected.
            // If the user cancels this file chooser, exit this method.

            fcSave.setSelectedFile(null);
            if (fcSave.showSaveDialog(Capture.this) !=
                    JFileChooser.APPROVE_OPTION)
                return;

            // Obtain the selected file. Validate its extension, which
            // must be .jpg or .jpeg. If extension not present, append
            // .jpg extension.

            File file = fcSave.getSelectedFile();
            String path = file.getAbsolutePath().toLowerCase();
            if (!path.endsWith(".jpg") && !path.endsWith(".jpeg")) file = new File(path += ".jpg");

            // If the file exists, inform the user, who might not want
            // to accidentally overwrite an existing file. Exit method
            // if the user specifies that it is not okay to overwrite
            // the file.

            if (file.exists()) {
                int choice = JOptionPane.
                        showConfirmDialog(null,
                                "Overwrite file?",
                                "Capture",
                                JOptionPane.
                                        YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION)
                    return;
            }

            // If the file does not exist or the user gives permission,
            // save image to file.

            ImageWriter writer = null;
            ImageOutputStream ios = null;

            try {
                // Obtain a writer based on the jpeg format.

                Iterator iter;
                iter = ImageIO.getImageWritersByFormatName("jpeg");

                // Validate existence of writer.

                if (!iter.hasNext()) {
                    showError("Unable to save image to jpeg file type.");
                    return;
                }

                // Extract writer.

                writer = (ImageWriter) iter.next();

                // Configure writer output destination.

                ios = ImageIO.createImageOutputStream(file);
                writer.setOutput(ios);

                // Set JPEG compression quality to 95%.

                ImageWriteParam iwp = writer.getDefaultWriteParam();
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwp.setCompressionQuality(0.95f);

                // Write the image.

                writer.write(null,
                        new IIOImage((BufferedImage)
                                captureImageArea.getImage(), null, null),
                        iwp);
            } catch (IOException e2) {
                showError(e2.getMessage());
            } finally {
                try {
                    // Cleanup.

                    if (ios != null) {
                        ios.flush();
                        ios.close();
                    }

                    if (writer != null)
                        writer.dispose();
                } catch (IOException e2) {
                }
            }
        }
    }

    void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Capture",
                JOptionPane.ERROR_MESSAGE);
    }


}
