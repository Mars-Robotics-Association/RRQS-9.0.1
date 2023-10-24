package org.firstinspires.ftc.teamcode.erik;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;


@TeleOp(name="Road Runner Test 02", group="Erik CenterStage")
public final class RoadRunnerTest02 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ErikCenterstageRobot robot = new ErikCenterstageRobot(this) ;
        robot.update() ;
        MecanumDrive drive = new MecanumDrive(hardwareMap,
                new Pose2d(-34, 62, Math.PI/2));

        waitForStart();

        Actions.runBlocking(
            drive.actionBuilder(drive.pose)
                    .setReversed(true)
                    .splineTo(new Vector2d(-44, 36), Math.toRadians(-120))
                    .setReversed(false)
                    .splineTo(new Vector2d(-20, 58), Math.toRadians(0))
                    .splineTo(new Vector2d(13, 36), Math.toRadians(0))
                    .build());

    }


}
