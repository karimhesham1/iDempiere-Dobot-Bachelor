package org.adoxx.dobot.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import org.adoxx.dobot_magician.DobotMagician;

public class JavaPythonBridgeOut {

  public static void main(String[] args) {


    try {
      // python script to run

      System.out.println(getAbsoluteFilePath("moveToPosition.py"));
      System.out.println(getFile("moveToPosition.py"));

      String pythonFile = getAbsoluteFilePath("moveToPosition.py");

      int number1 = 10;
      int number2 = 32;
      String line;

      // Map<String, String> env = new HashMap<String, String>();
      // env.put("DYLD_LIBRARY_PATH",
      // "/Users/wilfridutz/Documents/GitHub/dobot-magician/DobotDLLs/Mac/DobotDll/");

      String[] envp = {
          "DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH:/Users/wilfridutz/Documents/GitHub/dobot-magician/DobotDLLs/Mac/DobotDll/"};
      Process p = Runtime.getRuntime()
          .exec("/usr/local/bin/python3 " + pythonFile + " " + number1 + " " + number2, envp);
      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));



      while ((line = bri.readLine()) != null) {
        System.out.println(line);
      }
      bri.close();
      while ((line = bre.readLine()) != null) {
        System.out.println(line);
      }
      bre.close();
      p.waitFor();
      System.out.println("Done.");
    } catch (Exception err) {
      err.printStackTrace();
    }

  }

  private static String getAbsoluteFilePath(String fileName) {
    // Get file from resources folder
    ClassLoader classLoader = DobotMagician.class.getClassLoader();
    File file = new File(classLoader.getResource(fileName).getFile());
    return file.getAbsolutePath();
  }


  private static String getFile(String fileName) {

    StringBuilder result = new StringBuilder("");

    // Get file from resources folder
    ClassLoader classLoader = DobotMagician.class.getClassLoader();
    File file = new File(classLoader.getResource(fileName).getFile());
    System.out.println(file.getAbsolutePath());

    try (Scanner scanner = new Scanner(file)) {

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        result.append(line).append("\n");
      }

      scanner.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return result.toString();

  }
}

