package org.firstinspires.ftc.teamcode.erik;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder ;
import com.acmerobotics.roadrunner.TimeTrajectory ;
import com.acmerobotics.roadrunner.Trajectory ;


import org.firstinspires.ftc.teamcode.MecanumDrive ;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;


@TeleOp(name="Road Runner Test 03", group="Erik CenterStage")
public final class RoadRunnerTest03 extends LinearOpMode {
    //ErikCenterstageRobot robot = new ErikCenterstageRobot(this) ;

    @Override
    public void runOpMode() throws InterruptedException {
        ErikCenterstageRobot robot = new ErikCenterstageRobot(this) ;
        robot.update() ;
        MecanumDrive drive = new MecanumDrive(hardwareMap,
                new Pose2d(-34, 62, Math.PI/2));



        waitForStart();


        Action autoSeguence01 = drive.actionBuilder(drive.pose)
                .setReversed(true)
                .splineTo(new Vector2d(-44, 36), Math.toRadians(-120))
                .setReversed(false)
                .splineTo(new Vector2d(-20, 58), Math.toRadians(0))
                .build() ;


        Actions.runBlocking( new SequentialAction(
            autoSeguence01,
            new ParallelAction(
                    raiseArm(),
                    drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(13, 36), Math.toRadians(0))
                            .build())
                )
        ) ;

        robot.armLower();
    }

    public Action raiseArm() {
        return new Action() {
            @Override
            public boolean run(TelemetryPacket packet) {
                //robot.armRaise() ;
                return false;
            }
        } ;
    }



}
