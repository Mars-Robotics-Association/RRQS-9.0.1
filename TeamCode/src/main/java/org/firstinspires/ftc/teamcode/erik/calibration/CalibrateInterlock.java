/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.erik.calibration;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.erik.ErikCenterstageRobot;

/**
 * This is an iterative OpMode used to set the values used to calculate
 * the rotational position of the gripper (the wrist).
 * The rotation depends on the arm rotational position and the
 * desired position, relative to the ground (INTAKE, STORE, DELIVER).
 */
@TeleOp(name="Calibrate Interlock", group="Erik Calibrate")
//@Disabled
@Config
public class CalibrateInterlock extends OpMode
{
    // Declare OpMode members.
    public static int LIFT_POSITION  = 0 ;  // Max = 1850
    public static int ARM_POSITION  = 0 ;  // Max = 1660
    public static double GRIPPER_GRIP_POSITION  = 0.9 ;  // Open=0.9, Close=0.71
    public static double GRIPPER_ROTATE_POSITION  = 0.525 ;
    public static ErikCenterstageRobot.GripperState GRIPPER_STATE = ErikCenterstageRobot.GripperState.INTAKE ;

    ErikCenterstageRobot robot ;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robot = new ErikCenterstageRobot(this) ;
        telemetry.addData("Status", "Initialized");
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        robot.gripperState = GRIPPER_STATE ;
        robot.updateRaw(GRIPPER_GRIP_POSITION, GRIPPER_ROTATE_POSITION, ARM_POSITION, LIFT_POSITION );
        robot.updateInterlock();
        // Show telemetry
        telemetry.addData("Status", "Running" );
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

}
