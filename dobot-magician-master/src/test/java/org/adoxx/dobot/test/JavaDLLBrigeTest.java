package org.adoxx.dobot.test;

import java.util.Timer;
import java.util.TimerTask;
import org.adoxx.dobot.dll.DobotDll;
import org.adoxx.dobot.dll.DobotDll.DobotResult;
import org.adoxx.dobot.dll.DobotDll.EndEffectorParams;
import org.adoxx.dobot.dll.DobotDll.JOGCmd;
import org.adoxx.dobot.dll.DobotDll.JOGCommonParams;
import org.adoxx.dobot.dll.DobotDll.JOGCoordinateParams;
import org.adoxx.dobot.dll.DobotDll.JOGJointParams;
import org.adoxx.dobot.dll.DobotDll.PTPCoordinateParams;
import org.adoxx.dobot.dll.DobotDll.PTPJointParams;
import org.adoxx.dobot.dll.DobotDll.PTPJumpParams;
import org.adoxx.dobot.dll.DobotDll.Pose;
import com.sun.jna.ptr.IntByReference;


/**
 * NOT WORKING - CRASHES JAVA - asked for support at DOBOT forum
 */
// tip: The demo must import Jna library, inner DobotDemo folder of this project
public class JavaDLLBrigeTest {



  public static void main(String[] args) {
    try {
      System.setSecurityManager(null);

      JavaDLLBrigeTest app = new JavaDLLBrigeTest();
      app.Start();

      IntByReference ib = new IntByReference();

      JOGCmd test = new JOGCmd();
      test.isJoint = 1;
      while (true) {
        try {
          test.cmd = 1;
          DobotDll.instance.SetJOGCmd(test, false, ib);
          Thread.sleep(500);
          test.cmd = 0;
          DobotDll.instance.SetJOGCmd(test, false, ib);
          Thread.sleep(2000);
          test.cmd = 2;
          DobotDll.instance.SetJOGCmd(test, false, ib);
          Thread.sleep(500);
          test.cmd = 0;
          DobotDll.instance.SetJOGCmd(test, false, ib);
          Thread.sleep(2000);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      /*
       * while(true) { try{ PTPCmd ptpCmd = new PTPCmd(); ptpCmd.ptpMode = 0; ptpCmd.x = 260;
       * ptpCmd.y = 0; ptpCmd.z = 50; ptpCmd.r = 0; DobotDll.instance.SetPTPCmd(ptpCmd, true, ib);
       * //Thread.sleep(200);
       * 
       * ptpCmd.ptpMode = 0; ptpCmd.x = 220; ptpCmd.y = 0; ptpCmd.z = 80; ptpCmd.r = 0;
       * DobotDll.instance.SetPTPCmd(ptpCmd, true, ib); //Thread.sleep(200);
       * 
       * } catch (Exception e) { e.printStackTrace(); } }
       */
      // DobotDll.instance.DisconnectDobot();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void Start() {

    DobotResult ret = DobotResult.values()[DobotDll.instance.ConnectDobot((char) 0, 115200)];
    // ��ʼ����
    if (ret == DobotResult.DobotConnect_NotFound || ret == DobotResult.DobotConnect_Occupied) {
      Msg("Connect error, code:" + ret.name());
      return;
    }
    Msg("connect success code:" + ret.name());

    StartDobot();

    StartGetStatus();
  }

  /*
   * (non-Java-doc)
   * 
   * @see java.lang.Object#Object()
   */
  public JavaDLLBrigeTest() {
    super();
  }

  private void StartDobot() {
    IntByReference ib = new IntByReference();
    EndEffectorParams endEffectorParams = new EndEffectorParams();
    endEffectorParams.xBias = 71.6f;
    endEffectorParams.yBias = 0;
    endEffectorParams.zBias = 0;
    DobotDll.instance.SetEndEffectorParams(endEffectorParams, false, ib);
    JOGJointParams jogJointParams = new JOGJointParams();
    for (int i = 0; i < 4; i++) {
      jogJointParams.velocity[i] = 200;
      jogJointParams.acceleration[i] = 200;
    }
    DobotDll.instance.SetJOGJointParams(jogJointParams, false, ib);

    JOGCoordinateParams jogCoordinateParams = new JOGCoordinateParams();
    for (int i = 0; i < 4; i++) {
      jogCoordinateParams.velocity[i] = 200;
      jogCoordinateParams.acceleration[i] = 200;
    }
    DobotDll.instance.SetJOGCoordinateParams(jogCoordinateParams, false, ib);

    JOGCommonParams jogCommonParams = new JOGCommonParams();
    jogCommonParams.velocityRatio = 50;
    jogCommonParams.accelerationRatio = 50;
    DobotDll.instance.SetJOGCommonParams(jogCommonParams, false, ib);

    PTPJointParams ptpJointParams = new PTPJointParams();
    for (int i = 0; i < 4; i++) {
      ptpJointParams.velocity[i] = 200;
      ptpJointParams.acceleration[i] = 200;
    }
    DobotDll.instance.SetPTPJointParams(ptpJointParams, false, ib);

    PTPCoordinateParams ptpCoordinateParams = new PTPCoordinateParams();
    ptpCoordinateParams.xyzVelocity = 200;
    ptpCoordinateParams.xyzAcceleration = 200;
    ptpCoordinateParams.rVelocity = 200;
    ptpCoordinateParams.rAcceleration = 200;
    DobotDll.instance.SetPTPCoordinateParams(ptpCoordinateParams, false, ib);

    PTPJumpParams ptpJumpParams = new PTPJumpParams();
    ptpJumpParams.jumpHeight = 20;
    ptpJumpParams.zLimit = 180;
    DobotDll.instance.SetPTPJumpParams(ptpJumpParams, false, ib);

    DobotDll.instance.SetCmdTimeout(3000);
    DobotDll.instance.SetQueuedCmdClear();
    DobotDll.instance.SetQueuedCmdStartExec();
  }

  private void StartGetStatus() {
    Timer timerPos = new Timer();
    timerPos.schedule(new TimerTask() {
      public void run() {
        Pose pose = new Pose();
        DobotDll.instance.GetPose(pose);

        Msg("joint1Angle=" + pose.jointAngle[0] + "  " + "joint2Angle=" + pose.jointAngle[1] + "  "
            + "joint3Angle=" + pose.jointAngle[2] + "  " + "joint4Angle=" + pose.jointAngle[3]
            + "  " + "x=" + pose.x + "  " + "y=" + pose.y + "  " + "z=" + pose.z + "  " + "r="
            + pose.r + "  ");
      }
    }, 100, 500);//
  }

  private void Msg(String string) {
    System.out.println(string);
  }
}
