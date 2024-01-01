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

package org.firstinspires.ftc.teamcode.erik.templates;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch ;

import org.firstinspires.ftc.teamcode.erik.ErikCenterstageRobot;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 */
@TeleOp(name="PixyCam Test", group="Erik CenterStage")
@Disabled
public class PixyCamTest extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private ErikCenterstageRobot robot ;
    private GamepadEx controller ;

    //Declare a new I2cDeviceSync
    I2cDeviceSynch pixyCam;
    private byte[] sign1, sign2 ;


    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robot = new ErikCenterstageRobot(this) ;
        controller = new GamepadEx(gamepad1);   // Instantiate the gamepad

        //Get name from hardware config
        pixyCam = hardwareMap.i2cDeviceSynch.get("pixyCam");

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
        controller.readButtons();
        if (controller.wasJustReleased(GamepadKeys.Button.DPAD_UP)) { robot.gripAndGo(); }
        else if (controller.wasJustReleased(GamepadKeys.Button.DPAD_DOWN)) { robot.releaseAndDrop(); }
        else if (controller.wasJustReleased(GamepadKeys.Button.A)) {  }


        //Now to get data
        //Signature Query
        //This query simply asks Pixy to return the largest detected block of the specified signature. The
        //specified signature is the Query Address ­ 0x50. For example, a Query Address of 0x51 would
        //request the largest detected block of signature 1.
        //Query Address Optional Query Data Pixy Response
        //0x51 thru 0x57 none 5 bytes that describe the largest detected block within all signatures 1 thru 7. If no object is detected, all bytes will be 0.
        //Byte Description
        //0 Number of blocks that match the specified signature.
        //1 X value of center of largest detected block, ranging between 0 and 255. An x value of 255 is the far right­side of Pixy’s image.
        //2 Y value of center of largest detected block, ranging between 0 and 199. A value of 199 is the far bottom­side of Pixy’s image.
        //3 Width of largest block, ranging between 1 and 255. A width of 255 is the full width of Pixy’s image.
        //4 Height of largest block, ranging between 1 and 200. A height of 200 is the full height of Pixy’s image.
        //All of this info and more can be found at cmucam.org/attachments/1290/Pixy_LEGO_Protocol_1.0.pdf

        pixyCam.engage();
        sign1 = pixyCam.read(0x51,5);
        sign2 = pixyCam.read(0x52,5);
        //notice the 0xff&sign1[x], the 0xff& does an absolute value on the byte
        //the sign1[x] gets byte 1 from the query, see above comments for more info
        telemetry.addData("Object 1 X", 0xff&sign1[1]);
        telemetry.addData("Object 1 Y", 0xff&sign1[1]);
        telemetry.addData("Object 2 X", 0xff&sign2[1]);
        telemetry.addData("Object 2 Y", 0xff&sign2[2]);



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
