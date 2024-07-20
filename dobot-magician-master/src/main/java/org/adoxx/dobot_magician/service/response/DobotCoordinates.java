package org.adoxx.dobot_magician.service.response;

import java.io.Serializable;


/**
 * Serializable indicates that the class can be converted to a bite stream and vice versa.
 * This is quite useful for saving data or sending it between different interfaces.
 * The roboter arm need coordinates which are based on the xyz coordinate system.
 * X-values: the arm moves forward or backwards
 * Y-values: the arm moves right or left
 * Z-values: the arm moves up or down
 */
public class DobotCoordinates implements Serializable {
  private static final long serialVersionUID = 1L;
  double x;
  double y;
  double z;

  
  /**
   * @return double x-coordinate
   */
  public double getX() {
    return x;
  }

  
  /**
   * @return double y-coordinate
   */
  public double getY() {
    return y;
  }

  
  /**
   * @return double z-coordinate
   */
  public double getZ() {
    return z;
  }

  
  /**
   * @param x coordinate as double
   */
  public void setX(double x) {
    this.x = x;
  }

  
  /**
   * @param y coordinate as double
   */
  public void setY(double y) {
    this.y = y;
  }

  
  /**
   * @param z coordinate as double
   */
  public void setZ(double z) {
    this.z = z;
  }
}
