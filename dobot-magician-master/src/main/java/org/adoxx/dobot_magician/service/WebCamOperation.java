package org.adoxx.dobot_magician.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.adoxx.dobot_magician.WebCam;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * The general operations resource is accessed with /webcam. This class provides various general
 * operations like webcam_still, webcam_still_preconfig, ...
 * 
 * PNG pictures are produced out of the byte arrays.
 */
/*@Path("/webcam")
@Api(tags = {"webcam Operation"}, hidden = true)*/
public class WebCamOperation {

  /**
   * The logger makes sure that asked information is written in a file. This eases the debugging
   * process and allows to check the output values.
   */
  private Logger log = LogManager.getLogger(WebCamOperation.class);


  /**
   * The operation takes a still picture with defined webcam settings. In case of missing setting,
   * default values are used.
   * 
  
   * 
   * @return Image as {@link byte} byte array
   */
  /*@GET
  @Produces("image/png")
  @Path("webcam_still")*/
  public byte[] webcam_still(/*
                              * @QueryParam("brightness") @DefaultValue("200") int brightness,
                              * 
                              * @QueryParam("contrast") @DefaultValue("5") int contrast,
                              * 
                              * @QueryParam("saturation") @DefaultValue("103") int saturation,
                              * 
                              * @QueryParam("white_balance_temp") @DefaultValue("5000") int
                              * white_balance_temp,
                              * 
                              * @QueryParam("sharpness") @DefaultValue("40") int sharpness,
                              * 
                              * @QueryParam("backlight_compensation") @DefaultValue("5") int
                              * backlight_compensation,
                              * 
                              * @QueryParam("pan_absolute") @DefaultValue("0") int pan_absolute,
                              * 
                              * @QueryParam("tilt_absolute") @DefaultValue("0") int tilt_absolute,
                              * 
                              * @QueryParam("zoom") @DefaultValue("0") int zoom
                              */) {

    log.info("command received - capture image and return");
    /*
     * brightness=80 via service call contrast=5 via service call saturation=103 via service call
     * white_balance_temperature_auto=0 white_balance_temperature=5000 via service call sharpness=25
     * via service call backlight_compensation=5 via service call exposure_auto=1 -> manual
     * exposure_absolute=20 pan_absolute=0 viat service call tilt_absolute=0 via service call
     * focus_auto=1 zoom_absolute=0 via service call
     * 
     * int brightness = 200; int contrast = 5; int saturation = 103; int white_balance_temp = 5000;
     * int sharpness = 40; int backlight_compensation = 5; int pan_absolute = 0; int tilt_absolute =
     * 0; int zoom = 0;
     */

    BufferedImage image =
        WebCam.takeSnapshot(/*
                             * brightness, contrast, saturation, white_balance_temp, sharpness,
                             * backlight_compensation, pan_absolute, tilt_absolute, zoom
                             */);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      ImageIO.write(image, "png", baos);
    } catch (IOException e) {
      log.error(e.fillInStackTrace());
    }

    byte[] imageData = baos.toByteArray();

    return imageData;
  }


  /**
   * The operation takes a still picture with default webcam settings. The detection of burger
   * components is the main focus of this method. Therefore, python object detection is used.
   * 
   * @return Image as {@link byte}
   */
  /*@GET
  @Produces("image/png")
  @Path("webcam_still_detect_burger")*/
  public byte[] webcam_still_detect_burger() {

    // FIXME: update logic to return the coordinates provide an operation to get the image
    // optionally based on the detection ID
    // [[346, 451, 240, 337, 96.70755863189697, ["Lettuce: 96%"]], [172, 270, 431, 524,
    // 97.34366536140442, ["Tomatoes: 97%"]], [519, 636, 175, 324, 98.99784326553345, ["Patty:
    // 98%"]], [448, 599, 596, 713, 99.98894929885864, ["Cheese: 99%"]], [222, 314, 716, 867,
    // 84.97864007949829, ["Bacon: 84%"]], [519, 633, 998, 1140, 99.74685311317444, ["Onions:
    // 99%"]], [408, 550, 843, 949, 99.4078516960144, ["BunT: 99%"]]]



    log.info("command received - capture image and return object detection");
    /*
     * brightness=80 via service call contrast=5 via service call saturation=103 via service call
     * white_balance_temperature_auto=0 white_balance_temperature=5000 via service call sharpness=25
     * via service call backlight_compensation=5 via service call exposure_auto=1 -> manual
     * exposure_absolute=20 pan_absolute=0 viat service call tilt_absolute=0 via service call
     * focus_auto=1 zoom_absolute=0 via service call
     */

    /*
     * int brightness = 200; int contrast = 5; int saturation = 103; int white_balance_temp = 5000;
     * int sharpness = 40; int backlight_compensation = 5; int pan_absolute = 0; int tilt_absolute =
     * 0; int zoom = 0;
     */


    // take image
    File inputImage =
        WebCam.takeSnapshotFile(/*
                                 * brightness, contrast, saturation, white_balance_temp, sharpness,
                                 * backlight_compensation, pan_absolute, tilt_absolute, zoom
                                 */);

    // run image detection
    BufferedImage image = WebCam.executeObjectDetectionPython("object_detection_burger.py",
        inputImage.getAbsolutePath());

    // read and return image
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      ImageIO.write(image, "png", baos);
    } catch (IOException e) {
      log.error(e.fillInStackTrace());
    }

    byte[] imageData = baos.toByteArray();

    return imageData;

  }

}
