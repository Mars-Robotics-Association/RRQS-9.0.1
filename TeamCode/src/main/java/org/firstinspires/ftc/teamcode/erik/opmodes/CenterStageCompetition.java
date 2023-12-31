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

package org.firstinspires.ftc.teamcode.erik.opmodes;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.erik.ErikCenterstageRobot;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 */
@TeleOp(name="CenterStage Competition", group="Erik CenterStage")
public class CenterStageCompetition extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private ErikCenterstageRobot robot ;
    private GamepadEx controller ;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robot = new ErikCenterstageRobot(this) ;
        controller = new GamepadEx(gamepad1);   // Instantiate the gamepad

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
        runtime.reset();
        robot.updatePayload();
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        robot.drive.setDrivePowers(new PoseVelocity2d( new Vector2d( -gamepad1.left_stick_y, -gamepad1.left_stick_x ), -gamepad1.right_stick_x ));
        controller.readButtons();
        if (controller.wasJustReleased(GamepadKeys.Button.DPAD_UP))  robot.gripAndGo() ;
        else if (controller.wasJustReleased(GamepadKeys.Button.DPAD_DOWN))  robot.releaseAndDrop() ;
        else if (controller.wasJustReleased(GamepadKeys.Button.DPAD_RIGHT))  robot.gripperState = ErikCenterstageRobot.GripperState.INTAKE ;
        else if (controller.wasJustReleased(GamepadKeys.Button.DPAD_LEFT))  robot.gripperState = ErikCenterstageRobot.GripperState.STORE ;
        else if (controller.wasJustReleased(GamepadKeys.Button.A)) {  }
        else if (controller.wasJustReleased(GamepadKeys.Button.LEFT_BUMPER))  robot.gripOpen() ;
        else if (controller.wasJustReleased(GamepadKeys.Button.RIGHT_BUMPER))  robot.gripClose() ;

        robot.update();
        telemetry.addData("Status", "Run Time: " + runtime.toString()); // Show telemetry
    }


    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

}
