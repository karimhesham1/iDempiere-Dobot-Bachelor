package org.adoxx.dobot_magician.service.response;

import java.io.Serializable;


/**
 * Serializable indicates that the class can be converted to a bite stream and vice versa.
 * This is quite useful for saving data or sending it between different interfaces.
 * The roboter arm can execute commands and return results.
 */
public class DobotResponse implements Serializable {
  private static final long serialVersionUID = 1L;
  String pythonScript;
  String dobotResponse;
  String log;

  
  /**
   * @return String of the command as python script
   */
  public String getPythonScript() {
    return pythonScript;
  }

  
  /**
   * @return String of the answer of the robot
   */
  public String getDobotResponse() {
    return dobotResponse;
  }

  
  /**
   * @return String of the logging text
   */
  public String getLog() {
    return log;
  }

  
  /**
   * @param pythonScript as String
   */
  public void setPythonScript(String pythonScript) {
    this.pythonScript = pythonScript;
  }

  
  /**
   * @param dobotResponse as String
   */
  public void setDobotResponse(String dobotResponse) {
    this.dobotResponse = dobotResponse;
  }

  
  /**
   * @param log as String
   */
  public void setLog(String log) {
    this.log = log;
  }

}
