package org.adoxx.dobot_magician;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.adoxx.dobot_magician.service.response.DobotCoordinates;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;




/**
 * The Webcam should take snapshots of the robotic environment. As the Microsoft camera does not
 * work with Linux very well, a kind of workaround is build up. In this Java code a picture file is
 * generated and a picture taking thread is started on the raspberry. The result picture is then
 * given back to this code.
 */
public class WebCam {

  /**
   * The logger makes sure that asked information is written in a file. This eases the debugging
   * process and allows to check the output values.
   */
  static final Logger log = LogManager.getLogger(WebCam.class);


  /**
   * An image in the buffer is created. This is done by forwarding the functionality invocation to
   * an additional method (takeSnapshotFile).
   * @return picture file or null
   */
  public static BufferedImage takeSnapshot(/*
                                            * int brightness, int contrast, int saturation, int
                                            * white_balance_temp, int sharpness, int
                                            * backlight_compensation, int pan_absolute, int
                                            * tilt_absolute, int zoom
                                            */) {

    try {
      File f = File.createTempFile("webcam_", ".jpg");
      log.info("creating image at " + f.getAbsolutePath());
      return ImageIO
          .read(takeSnapshotFile(/*
                                  * brightness, contrast, saturation, white_balance_temp, sharpness,
                                  * backlight_compensation, pan_absolute, tilt_absolute, zoom
                                  */));
    } catch (IOException e) {
      log.error(e.fillInStackTrace());
    }
    return null;
  }


  /**
   * This method invokes the the camera and sets the needed parameters. Then the picture taking
   * process is executed by starting the program directly on the raspberry and calling the services.
   * 
  
   * @return picture file or null
   */
  public static File takeSnapshotFile(/*
                                       * int brightness, int contrast, int saturation, int
                                       * white_balance_temp, int sharpness, int
                                       * backlight_compensation, int pan_absolute, int
                                       * tilt_absolute, int zoom
                                       */) {

    /**
     * brightness=80 via service call contrast=5 via service call saturation=103 via service call
     * white_balance_temperature_auto=0 white_balance_temperature=5000 via service call sharpness=25
     * via service call backlight_compensation=5 via service call exposure_auto=1 -> manual
     * exposure_absolute=20 pan_absolute=0 via service call tilt_absolute=0 via service call
     * focus_auto=1 zoom_absolute=0 via service call
     */
    // fswebcam -r 1280x720 -S 20 -F 50 out1.jpg -p MJPEG -D 2

    /*
     * executeNativeOperation("v4l2-ctl -c exposure_auto=1");
     * executeNativeOperation("v4l2-ctl -c white_balance_temperature_auto=0");
     * executeNativeOperation("v4l2-ctl -c focus_auto=1");
     * 
     * executeNativeOperation("v4l2-ctl -c brightness=" + brightness);
     * executeNativeOperation("v4l2-ctl -c contrast=" + contrast);
     * executeNativeOperation("v4l2-ctl -c saturation=" + saturation);
     * executeNativeOperation("v4l2-ctl -c white_balance_temperature=" + white_balance_temp);
     * executeNativeOperation("v4l2-ctl -c sharpness=" + sharpness);
     * executeNativeOperation("v4l2-ctl -c backlight_compensation=" + backlight_compensation);
     * executeNativeOperation("v4l2-ctl -c pan_absolute=" + pan_absolute);
     * executeNativeOperation("v4l2-ctl -c tilt_absolute=" + tilt_absolute);
     * executeNativeOperation("v4l2-ctl -c zoom_absolute=" + zoom);
     */
    // move arm out of sight
    DobotCoordinates currentPosition = DobotMagician.getArmPosition();
    // lift first only to 50
    DobotMagician.moveToPosition(currentPosition.getX(), currentPosition.getY(), 50);
    // move to home
    DobotMagician.moveToPosition(250, 0, 50);
    // move to side position
    DobotMagician.moveToPosition(-6, -256, 52);
    try {
      File f = File.createTempFile("webcam_", ".jpg");
      log.info("creating image at " + f.getAbsolutePath());
      executeNativeOperation("fswebcam -r 1280x720 -S 20 -F 50 -p MJPEG " + f.getAbsolutePath());
      // move to originalposition
      DobotMagician.moveToPosition(currentPosition.getX(), currentPosition.getY(),
          currentPosition.getZ());
      return f;


    } catch (IOException e) {
      log.error(e.fillInStackTrace());
    }
    return null;
  }


  /**
   * Services on the raspberry are called via this method. The operation is read with a buffered
   * reader. Also error are read and the result is returned as string.
   * 
   * @param operation as String
   * @return input buffer as String or null
   */
  private static String executeNativeOperation(String operation) {
    try {
      StringBuffer iBuffer = new StringBuffer();
      StringBuffer eBuffer = new StringBuffer();

      // FIXME: make envp configurable
      // FIXME: make python3 path configurable
      String line;
      Process p = Runtime.getRuntime().exec(operation);
      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));

      while ((line = bri.readLine()) != null) {
        System.out.println(line);
        iBuffer.append(line);
      }

      bri.close();

      while ((line = bre.readLine()) != null) {
        System.out.println(line);
        eBuffer.append(line);
      }

      bre.close();
      p.waitFor();
      System.out.println("Done.");
      return iBuffer.toString();
    } catch (Exception err) {
      err.printStackTrace();
    }
    return null;
  }


  /**
   * The properties of the Dobot are given back. They are looked up in the properties config file.
   * 
   * @param key as String
   * @return property value
   */
  public static String readConfig(String key) {
    Properties prop = new Properties();
    InputStream input = null;

    try {
      ClassLoader classLoader = DobotMagician.class.getClassLoader();
      File config = new File(classLoader.getResource("config.properties").getFile());
      input = new FileInputStream(config);

      // load a properties file
      prop.load(input);

      // get the property value and print it out
      return prop.getProperty(key);

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }


  /**
   * A thread with Python is used to detect objects in images. The image input is looked through by
   * using a program running on the raspberry.
   * 
   * @param pythonFile as String
   * @param inputImage as String
   * @return image
   */
  public static BufferedImage executeObjectDetectionPython(String pythonFile, String inputImage) {

    try {
      File outputImage = File.createTempFile("detection_", ".jpg");
      executePython(getAbsoluteFilePath("object_detection_burger.py"),
          inputImage + " " + outputImage);
      System.out.println("Done.");
      return ImageIO.read(outputImage);
    } catch (Exception err) {
      err.printStackTrace();
    }
    return null;
  }

  // FIXME: generic helper methods to be established.
  private static String executePython(String pythonFile, String parameters) {
    String pythonPath = readConfig("python_path");
    String envSetting = readConfig("env_setting");
    try {
      StringBuffer iBuffer = new StringBuffer();
      StringBuffer eBuffer = new StringBuffer();

      // FIXME: make envp configurable
      // FIXME: make python3 path configurable
      String line;
      String[] envp = {envSetting};
      Process p = Runtime.getRuntime().exec(pythonPath + " " + pythonFile + " " + parameters, envp);
      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));

      while ((line = bri.readLine()) != null) {
        System.out.println(line);
        iBuffer.append(line);
      }
      bri.close();
      while ((line = bre.readLine()) != null) {
        System.out.println(line);
        eBuffer.append(line);
      }
      bre.close();
      p.waitFor();
      System.out.println("Done.");
      return iBuffer.toString();
    } catch (

    Exception err) {
      err.printStackTrace();
    }
    return null;
  }


  /**
   * This method reads the absolute file path by inserting a filename.
   * 
   * @param fileName as String
   * @return absolute file path as String
   */
  private static String getAbsoluteFilePath(String fileName) {
    // Get file from resources folder
    ClassLoader classLoader = DobotMagician.class.getClassLoader();
    File file = new File(classLoader.getResource(fileName).getFile());
    return file.getAbsolutePath();
  }

}
