package org.firstinspires.ftc.teamcode.erik;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;


@TeleOp(name="Road Runner Test 04", group="Erik CenterStage")
public final class RoadRunnerTest04 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ErikCenterstageRobot robot = new ErikCenterstageRobot(this) ;
        MecanumDrive drive = new MecanumDrive(hardwareMap,
                new Pose2d(-34, 62, Math.PI/2));

        robot.gripAndStore();

        waitForStart();

        Actions.runBlocking(
            drive.actionBuilder(drive.pose)
                .setReversed(true)
                .splineTo(new Vector2d(-44, 36), Math.toRadians(-120))
                .setReversed(false)
                .splineTo(new Vector2d(-20, 58), Math.toRadians(0))
                .build());

        robot.armRaise() ;

        Actions.runBlocking(
            drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(13, 36), Math.toRadians(0))
                .waitSeconds(2)
                .build());

        robot.releaseAndDrop();

        while(this.opModeIsActive()) {}
    }


}
