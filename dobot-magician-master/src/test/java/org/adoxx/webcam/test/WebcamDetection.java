package org.adoxx.webcam.test;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import com.github.sarxos.webcam.Webcam;

public class WebcamDetection {
  public static void main(String[] args) throws IOException, InterruptedException {
    List<Webcam> webcams = Webcam.getWebcams();

    for (Iterator<Webcam> iterator = webcams.iterator(); iterator.hasNext();) {
      Webcam webcam = (Webcam) iterator.next();
      if (!webcam.getName().startsWith("FaceTime")) {
        System.out.println("Webcam: " + webcam.getName() + " dimensions " + webcam.getDevice());
        webcam.setViewSize(new Dimension(640, 480)); // set size
        webcam.open();
        TimeUnit.MILLISECONDS.sleep(2000);
        ImageIO.write(webcam.getImage(), "PNG",
            new File("hello-world" + webcam.getName() + ".png"));
        webcam.close();
      }
    }

  }
}
