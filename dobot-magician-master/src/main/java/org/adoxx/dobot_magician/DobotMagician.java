package org.adoxx.dobot_magician;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Scanner;
import org.adoxx.dobot_magician.service.response.DobotCoordinates;
import org.adoxx.dobot_magician.service.response.DobotResponse;

/**
 * This class is the main class for the robot arm. It offers various methods like moving the arm. As
 * the java dll is not working, Python scripts are used to execute the operations on the robot.
 */
public class DobotMagician {

  /**
   * The coordinates are inserted as integer numbers and afterwards the Python script responsible
   * for moving the arm is invoked with the parameters. A DobotResponse (consisting of pythonScript,
   * dobotResponse and log) is returned.
   * 
   * @param x as integer
   * @param y as integer
   * @param z as integer
   * @return DobotResponse
   */
	public static DobotResponse moveToPosition(int x, int y, int z, int r) {
	    DobotResponse response = new DobotResponse();
	    response.setPythonScript(getFileContent("moveToPosition.py"));
	    String execResult =
	        executePython(getAbsoluteFilePath("moveToPosition.py"), x + " " + y + " " + z + " " + r + " ");
	    response.setLog(execResult);
	    System.out.println(execResult);
	    return response;
	  }


  /**
   * In order to execute any operation on the robot, a Python file is used as an interface. The
   * execution is started by using the pythonPath from the config file, the relevant pythonFile and
   * some parameters.
   * 
   * @param pythonFile as String
   * @param parameters as String
   * @return input buffer as String
   */
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


  /**
   * This method gives back the file content by inserting a filename. By using String builder and
   * scanner, the output is combined out of the given file.
   * 
   * @param fileName as String
   * @return file content as String
   */
  private static String getFileContent(String fileName) {

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


  /**
   * In order to move the robot arm to the home position, the suitable Python script is loaded and
   * executed. A DobotResponse (consisting of pythonScript, dobotResponse and log) is returned.
   * 
   * @return response as DobotResponse type
   */
  public static DobotResponse moveToHomePosition() {
    DobotResponse response = new DobotResponse();
    response.setPythonScript(getFileContent("goHome.py"));
    String execResult = executePython(getAbsoluteFilePath("goHome.py"), "");
    response.setLog(execResult);
    System.out.println(response);
    return response;
  }


  /**
   * In order to turn on the suction cup, the suitable Python script is loaded and executed. A
   * DobotResponse (consisting of pythonScript, dobotResponse and log) is returned.
   * 
   * @return response as DobotResponse type
   */
  public static DobotResponse closeGripper() {
    DobotResponse response = new DobotResponse();
    response.setPythonScript(getFileContent("closeGripper.py"));
    String execResult = executePython(getAbsoluteFilePath("closeGripper.py"), "");
    response.setLog(execResult);
    System.out.println(response);
    return response;
  }


  /**
   * In order to turn off the suction cup, the suitable Python script is loaded and executed. A
   * DobotResponse (consisting of pythonScript, dobotResponse and log) is returned.
   * 
   * @return response as DobotResponse type
   */
  public static DobotResponse turnSuctionCupOff() {
    DobotResponse response = new DobotResponse();
    response.setPythonScript(getFileContent("controlSuctionOff.py"));
    String execResult = executePython(getAbsoluteFilePath("controlSuctionOff.py"), "");
    response.setLog(execResult);
    System.out.println(response);
    return response;
  }
  
  /**
   * In order to release gripper, the suitable Python script is loaded and executed. A
   * DobotResponse (consisting of pythonScript, dobotResponse and log) is returned.
   * 
   * @return response as DobotResponse type
   */
  public static DobotResponse releaseGripper() {
    DobotResponse response = new DobotResponse();
    response.setPythonScript(getFileContent("releaseGripper.py"));
    String execResult = executePython(getAbsoluteFilePath("releaseGripper.py"), "");
    response.setLog(execResult);
    System.out.println(response);
    return response;
  }
  
  /**
   * In order to clear alerts, the suitable Python script is loaded and executed. A
   * DobotResponse (consisting of pythonScript, dobotResponse and log) is returned.
   * 
   * @return response as DobotResponse type
   */
  public static DobotResponse clearAlerts() {
    DobotResponse response = new DobotResponse();
    response.setPythonScript(getFileContent("clearAlerts.py"));
    String execResult = executePython(getAbsoluteFilePath("clearAlerts.py"), "");
    response.setLog(execResult);
    System.out.println(response);
    return response;
  }


  /**
   * In order to get the coordinates of the arm, the suitable Python script is loaded and executed.
   * The result (array of type double) is parsed in order to get a result of type DobotCoordinates.
   * 
   * @return coordinates as DobotCoordinates type
   */
  public static DobotCoordinates getArmPosition() {
    DobotCoordinates coordinates = new DobotCoordinates();
    String execResult = executePython(getAbsoluteFilePath("getPosition.py"), "");
    double[] value = parseCoordinates(execResult);
    coordinates.setX(value[0]);
    coordinates.setY(value[1]);
    coordinates.setZ(value[2]);
    return coordinates;
  }


  /**
   * The input String contains coordinates that are parsed with this method in order to get a double
   * array.
   * 
   * @param input as String
   * @return coordinates as array of type double
   */
  private static double[] parseCoordinates(String input) {
    int indexOfOpenBracket = input.indexOf("[");
    int indexOfLastBracket = input.lastIndexOf("]");

    String[] array = input.substring(indexOfOpenBracket + 1, indexOfLastBracket).split(",");
    double[] coordinates = new double[array.length];
    for (int i = 0; i < array.length; i++) {
      String array_element = array[i].trim();
      double value = Double.parseDouble(array_element);
      coordinates[i] = value;
    }
    return coordinates;
  }


  /**
   * The properties of the Dobot are given back. They are looked up in the properties config file.
   * 
   * @param key as String
   * @return value of property or null
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


  public static void moveToPosition(double x, double y, double z) {
    int xInt = (int) Math.round(x);
    int yInt = (int) Math.round(y);
    int zInt = (int) Math.round(z);
    moveToPosition(xInt, yInt, zInt);
  }
}
