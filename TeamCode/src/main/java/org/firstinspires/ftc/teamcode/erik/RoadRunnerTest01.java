package org.firstinspires.ftc.teamcode.erik;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.TankDrive;
import org.firstinspires.ftc.teamcode.tuning.TuningOpModes;


@TeleOp(name="Road Runner Test 01", group="Erik CenterStage")
public final class RoadRunnerTest01 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        waitForStart();

        Actions.runBlocking(
            drive.actionBuilder(new Pose2d(-34, -64, -Math.PI/2))
                    .setReversed(true)
                    .splineTo(new Vector2d(-48, -34), Math.toRadians(120))
                    .setReversed(false)
                    .splineTo(new Vector2d(0, -58), 0)
                    .build());


    }
}
