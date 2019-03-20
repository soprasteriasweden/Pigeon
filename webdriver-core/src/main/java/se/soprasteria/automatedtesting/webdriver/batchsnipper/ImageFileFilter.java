package se.soprasteria.automatedtesting.webdriver.batchsnipper;

import java.io.*;

/**
 *  This class defines a filter that accepts directories and files ending in
 *  .jpg or .jpeg extensions.
 */

class ImageFileFilter extends javax.swing.filechooser.FileFilter
{
   /**
    *  Accept all directories and specified files.
    *
    *  @param f file being checked
    *
    *  @return true if accepted
    */

   public boolean accept (File f)
   {
      // Allow the user to select directories so that the user can navigate the
      // file system.

      if (f.isDirectory ())
          return true;

      // Allow the user to select files ending with a .jpg or a .jpeg
      // extension.

      String s = f.getName ();
      int i = s.lastIndexOf ('.');

      if (i > 0 && i < s.length ()-1)
      {
          String ext = s.substring (i+1).toLowerCase ();

          if (ext.equals ("jpg") || ext.equals ("jpeg"))
              return true;
      }

      // Nothing else can be selected.

      return false;
   }

   /**
    *  Return a description that appears on the file chooser and tells the user
    *  which files can be selected.
    *
    *  @return description
    */

   public String getDescription ()
   {
      return "Accepts .jpg and .jpeg files";
   }
}
