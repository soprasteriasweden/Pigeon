package se.soprasteria.automatedtesting.webdriver.batchsnipper;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

/**
 *  This class defines a specialized panel for displaying a captured image.
 */

public class ImageArea extends JPanel
{
   /**
    *  Stroke-defined outline of selection rectangle.
    */

   private BasicStroke bs;

   /**
    *  A gradient paint is used to create a distinctive-looking selection
    *  rectangle outline.
    */

   private GradientPaint gp;

   /**
    *  Displayed image's Image object, which is actually a BufferedImage.
    */

   private Image image;

   /**
    *  Mouse coordinates when mouse button pressed.
    */

   private int srcx, srcy;

   /**
    *  Latest mouse coordinates during drag operation.
    */

   private int destx, desty;

   /**
    *  Location and extents of selection rectangle.
    */

   private Rectangle rectSelection;

   /**
    *  Construct an ImageArea component.
    */

   public ImageArea ()
   {
      // Create a selection Rectangle. It's better to create one Rectangle
      // here than a Rectangle each time paintComponent() is called, to reduce
      // unnecessary object creation.

      rectSelection = new Rectangle ();

      // Define the stroke for drawing selection rectangle outline.

      bs = new BasicStroke (5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                            0, new float [] { 12, 12 }, 0);

      // Define the gradient paint for coloring selection rectangle outline.

      gp = new GradientPaint (0.0f, 0.0f, Color.red, 1.0f, 1.0f, Color.white,
                              true);

      // Install a mouse listener that sets things up for a selection drag.

      MouseListener ml;
      ml = new MouseAdapter ()
           {
               public void mousePressed (MouseEvent e)
               {
                  // When you start Capture, there is no captured image.
                  // Therefore, it makes no sense to try and select a subimage.
                  // This is the reason for the if (image == null) test.

                  if (image == null)
                      return;

                  destx = srcx = e.getX ();
                  desty = srcy = e.getY ();

                  repaint ();
               }
           };
      addMouseListener (ml);

      // Install a mouse motion listener to update the selection rectangle
      // during drag operations.

      MouseMotionListener mml;
      mml = new MouseMotionAdapter ()
            {
                public void mouseDragged (MouseEvent e)
                {
                   // When you start Capture, there is no captured image.
                   // Therefore, it makes no sense to try and select a
                   // subimage. This is the reason for the if (image == null)
                   // test.

                   if (image == null)
                       return;

                   destx = e.getX ();
                   desty = e.getY ();

                   repaint (); 
                }
            };
      addMouseMotionListener (mml);
   }

   /**
    *  Crop the image to the dimensions of the selection rectangle.
    *
    *  @return true if cropping succeeded
    */

   public boolean crop ()
   {
      // There is nothing to crop if the selection rectangle is only a single
      // point.

      if (srcx == destx && srcy == desty)
          return true;

      // Assume success.

      boolean succeeded = true;

      // Compute upper-left and lower-right coordinates for selection rectangle
      // corners.

      int x1 = (srcx < destx) ? srcx : destx;
      int y1 = (srcy < desty) ? srcy : desty;

      int x2 = (srcx > destx) ? srcx : destx;
      int y2 = (srcy > desty) ? srcy : desty;

      // Compute width and height of selection rectangle.

      int width = (x2-x1)+1;
      int height = (y2-y1)+1;

      // Create a buffer to hold cropped image.

      BufferedImage biCrop = new BufferedImage (width, height,
                                                BufferedImage.TYPE_INT_RGB);
      Graphics2D g2d = biCrop.createGraphics ();

      // Perform the crop operation.

      try
      {
          BufferedImage bi = (BufferedImage) image;
          BufferedImage bi2 = bi.getSubimage (x1, y1, width, height);
          g2d.drawImage (bi2, null, 0, 0);
      }
      catch (RasterFormatException e)
      {
         succeeded = false;
      }

      g2d.dispose ();

      if (succeeded)
          setImage (biCrop); // Implicitly remove selection rectangle.
      else
      {
          // Prepare to remove selection rectangle.

          srcx = destx;
          srcy = desty;

          // Explicitly remove selection rectangle.

          repaint ();
      }

      return succeeded;
   }

   /**
    *  Return the current image.
    *
    *  @return Image reference to current image
    */

   public Image getImage ()
   {
      return image;
   }

   /**
    *  Repaint the ImageArea with the current image's pixels.
    *
    *  @param g graphics context
    */

   public void paintComponent (Graphics g)
   {
      // Repaint the component's background.

      super.paintComponent (g);

      // If an image has been defined, draw that image using the Component
      // layer of this ImageArea object as the ImageObserver.

      if (image != null)
          g.drawImage (image, 0, 0, this);

      // Draw the selection rectangle if present.

      if (srcx != destx || srcy != desty)
      {
          // Compute upper-left and lower-right coordinates for selection
          // rectangle corners.

          int x1 = (srcx < destx) ? srcx : destx;
          int y1 = (srcy < desty) ? srcy : desty;

          int x2 = (srcx > destx) ? srcx : destx;
          int y2 = (srcy > desty) ? srcy : desty;

          // Establish selection rectangle origin.

          rectSelection.x = x1;
          rectSelection.y = y1;

          // Establish selection rectangle extents.

          rectSelection.width = (x2-x1)+1;
          rectSelection.height = (y2-y1)+1;

          // Draw selection rectangle.

          Graphics2D g2d = (Graphics2D) g;
          g2d.setStroke (bs);
          g2d.setPaint (gp);
          g2d.draw (rectSelection);
      }
   }

   /**
    *  Establish a new image and update the display.
    *
    *  @param image new image's Image reference
    */

   public void setImage (Image image)
   {
      // Save the image for later repaint.

      this.image = image;

      // Set this panel's preferred size to the image's size, to influence the
      // display of scrollbars.
 
      setPreferredSize (new Dimension (image.getWidth (this),
                                       image.getHeight (this)));

      // Present scrollbars as necessary.

      revalidate ();

      // Prepare to remove any selection rectangle.

      srcx = destx;
      srcy = desty;

      // Update the image displayed on the panel.

      repaint ();
   }
}
