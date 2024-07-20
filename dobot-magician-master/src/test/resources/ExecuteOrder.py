import threading
import DobotDllType as dType

CON_STR = {
    dType.DobotConnect.DobotConnect_NoError:  "DobotConnect_NoError",
    dType.DobotConnect.DobotConnect_NotFound: "DobotConnect_NotFound",
    dType.DobotConnect.DobotConnect_Occupied: "DobotConnect_Occupied"}
    
#Load Dll
api = dType.load()

#Import Variables
#from Coordinates import xL, yL, xP, yP
xL = 200
yL = 200
xP = 150
yP = 200
i = 0

#Connect Dobot
state = dType.ConnectDobot(api, "", 115200)[0]
print("Connect status:",CON_STR[state])


if (state == dType.DobotConnect.DobotConnect_NoError):

    #Clean Command Queued
    dType.SetQueuedCmdClear(api)
    print("Command Queue cleared")
    
    #Async Motion Params Setting
    dType.SetHOMEParams(api, 250, 0, 50, 0, isQueued = 1)
    dType.SetPTPJointParams(api, 200, 200, 200, 200, 200, 200, 200, 200, isQueued = 1)
    dType.SetPTPCommonParams(api, 100, 100, isQueued = 1)
    print("Motion Parameters Set")
    
    #Clean Command Queued
    #dType.SetQueuedCmdClear(api)
    #print("Command Queue cleared")


    #Command for movement
    #for i in range (0,1):
    dType.SetEndEffectorParamsEx(api, 59.7, 0, 0, isQueued = 1)
    dType.SetPTPCmd(api, dType.PTPMode.PTPMOVLXYZMode, 240, 100, 60, 0, isQueued = 1)
    dType.SetPTPCmd(api, dType.PTPMode.PTPMOVLXYZMode, xL, yL, -20, 0, isQueued = 1)
    dType.SetPTPCmd(api, dType.PTPMode.PTPMOVLXYZMode, xL, yL, -70, 0, isQueued = 1)
    dType.SetEndEffectorSuctionCupEx(api, 1, 1, isQueued = 1)
    dType.SetPTPCmd(api, dType.PTPMode.PTPMOVLXYZMode, xL, yL, -20, 0, isQueued = 1)
    dType.SetPTPCmd(api, dType.PTPMode.PTPMOVLXYZMode, xP, yP, -20, 0, isQueued = 1)
    lastIndex = dType.SetEndEffectorSuctionCupEx(api, 0, 1, isQueued = 1)[0]
    print("Command received")
    #i == 1
    dType.SetQueuedCmdStartExec(api)

    while lastIndex > dType.GetQueuedCmdCurrentIndex(api)[0]:
        dType.dSleep(100)

    dType.SetQueuedCmdStopExec(api)


#Disconnect Dobot
dType.DisconnectDobot(api)



