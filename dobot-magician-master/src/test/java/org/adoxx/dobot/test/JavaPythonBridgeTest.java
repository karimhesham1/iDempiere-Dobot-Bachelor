package org.adoxx.dobot.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JavaPythonBridgeTest {

  public static void main(String[] args) {

    try {
      String prg = "import sys\nprint int(sys.argv[1])+int(sys.argv[2])\n";
      BufferedWriter out = new BufferedWriter(new FileWriter("test1.py"));
      out.write(prg);
      out.close();
      int number1 = 10;
      int number2 = 32;
      Process p = Runtime.getRuntime().exec("python test1.py " + number1 + " " + number2);

      BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
      int ret = new Integer(in.readLine()).intValue();
      System.out.println("value is : " + ret);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
