package org.adoxx.dobot_magician.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.adoxx.dobot_magician.DobotMagician;
import org.adoxx.dobot_magician.service.response.DobotCoordinates;
import org.adoxx.dobot_magician.service.response.DobotResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.swagger.annotations.Api;


/**
 * The general operations resource is accessed with /operation. This class provides various general
 * operations like moving, turning or getting a position.
 */
@Path("/operation")
@Api(tags = {"dobot Operation"})
public class DobotMagicianOperation {

	
  /**
   * The logger makes sure that asked information is written in a file. This eases the debugging
   * process and allows to check the output values.
   */
  private Logger log = LogManager.getLogger(DobotMagicianOperation.class);


  /**
   * The operation moves the arm to the position in a 3 dimensional space.
   * x-value: moves the arm forward or backward
   * y-value: moves the arm left or right
   * z-value: moves the arm up or down.
   * 
   * @param x as integer
   * @param y as intger
   * @param z as integer
   * @return {@link DobotResponse}
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("moveToPosition")
  public DobotResponse moveToPosition(@QueryParam("x") int x, @QueryParam("y") int y,
	      @QueryParam("z") int z, @QueryParam("r") int r) {
	    log.info("command received - move to position x:" + x + " y:" + y + " z: " + z + " r: " + r);
	    return DobotMagician.moveToPosition(x, y, z, r);
	  }

  
  /**
   * The operation moves the arm to home position.
   * This means that all coordinates are zero at this point.
   * x: 0 y: 0 z: 0
   * 
   * @return {@link DobotResponse}
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("moveToHomePosition")
  public DobotResponse moveToHomePosition() {
    log.info("command received - move to home position");
    return DobotMagician.moveToHomePosition();
  }

  
  /**
   * The operation turns on the suction cup.
   * If the arm is placed over an element it is possible to pick up things (coffee cups, paper, ...).
   * 
   * @return {@link DobotResponse}
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("closeGripper")
  public DobotResponse closeGripper() {
    log.info("command received - turn suction cup on");
    return DobotMagician.closeGripper();
  }

  
  /**
   * The operation turns off the suction cup.
   * In order to let go of the picked up element, the suction mode can be turned off.
   * If the arm is placed only a little bit above the floor it is lain down, otherwise the element falls down.
   * 
   * @return {@link DobotResponse}
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("turnOffSuctionCup")
  public DobotResponse turnOffSuctionCup() {
    log.info("command received - turn suction cup off");
    return DobotMagician.turnSuctionCupOff();
  }
  
  /**
   * The operation releases the Gripper.
   * In order to let go of the picked up element, the suction mode can be turned off.
   * If the arm is placed only a little bit above the floor it is lain down, otherwise the element falls down.
   * 
   * @return {@link DobotResponse}
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("releaseGripper")
  public DobotResponse releaseGripper() {
    log.info("command received - release gripper");
    return DobotMagician.releaseGripper();
  }
  
  /**
   * The operation clears Alerts.
   * If the arm is placed over an element it is possible to pick up things (coffee cups, paper, ...).
   * 
   * @return {@link DobotResponse}
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("clearAlerts")
  public DobotResponse clearAlerts() {
    log.info("command received - turn suction cup on");
    return DobotMagician.clearAlerts();
  }

  
  /**
   * The operation is for getting the actual coordinates, where the robotic arm is located at the moment.
   * 
   * @return {@link DobotResponse}
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("getPosition")
  public DobotCoordinates getPosition() {
    log.info("command received - get position of arm");
    return DobotMagician.getArmPosition();
  }
}
