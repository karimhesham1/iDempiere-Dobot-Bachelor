package org.adoxx.dobot.test;

public class PositionParser {

  public static void main(String[] args) {
    String input =
        "[250.0025634765625, 0.0, 49.777687072753906, 0.0, 0.0, 27.010074615478516, 28.6575984954834, 0.0]";
    int indexOfOpenBracket = input.indexOf("[");
    int indexOfLastBracket = input.lastIndexOf("]");


    String[] array = input.substring(indexOfOpenBracket + 1, indexOfLastBracket).split(",");
    double[] coordinates = new double[array.length];
    for (int i = 0; i < array.length; i++) {
      String array_element = array[i].trim();
      double value = Double.parseDouble(array_element);
      coordinates[i] = value;
      System.out.println(value);
    }



  }

}
