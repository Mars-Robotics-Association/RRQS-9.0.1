package org.firstinspires.ftc.teamcode.erik.templates;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.erik.ErikCenterstageRobot;

import java.util.concurrent.TimeUnit;


@TeleOp(name="HuskyLens Auto-Red - Home", group="Erik CenterStage")
public final class AutoHuskyLens extends LinearOpMode {
    private final int READ_PERIOD = 1;
    private String myZone = "Center" ;
    private HuskyLens huskyLens;

    @Override
    public void runOpMode() throws InterruptedException {
        ErikCenterstageRobot robot = new ErikCenterstageRobot(this) ;
        MecanumDrive drive = new MecanumDrive(hardwareMap,
                new Pose2d(-34, 58, Math.PI/2));

        // HuskyLens setup stuff
        huskyLens = hardwareMap.get(HuskyLens.class, "huskyLens");
        Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);
        rateLimit.expire();
        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            telemetry.addData(">>", "Press start to continue");
        }
        //huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION) ;
        robot.gripperState = ErikCenterstageRobot.GripperState.INTAKE ;
        robot.update();

        waitForStart();

        robot.gripAndStore();

        rateLimit.reset();
        HuskyLens.Block[] blocks = huskyLens.blocks();
        for (int i = 0; i<10; i++ ) { // Try 10 times to get a reading
            if (blocks.length > 0) {
                if (blocks[0].x > 180) myZone = "Right";
                else if (blocks[0].x < 120) myZone = "Left";
                else myZone = "Center";
                break ;
            }
            blocks = huskyLens.blocks();
        }

        telemetry.addData("Block count", blocks.length);
        for (int i = 0; i < blocks.length; i++) {
            telemetry.addData("Block", blocks[i].toString());
            telemetry.addData("Zone", myZone ) ;
        }

        switch (myZone) {
            case "Right":
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(-42, 32), Math.toRadians(-120))
                            .setReversed(false)
                            .splineTo(new Vector2d(-20, 58), Math.toRadians(0))
                            .build());
                break ;
            case "Left":
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(-27, 32), Math.toRadians(-60))
                                .setReversed(false)
                                .splineTo(new Vector2d(-34, 42), Math.toRadians(80))
                                .splineTo(new Vector2d(-20, 58), Math.toRadians(0))
                                .build());
                break ;
            default:
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(-34, 32), Math.toRadians(-90))
                                .setReversed(false)
                                .splineTo(new Vector2d(-20, 58), Math.toRadians(0))
                                .build());
                break ;
        }



        robot.armRaise() ;

        Actions.runBlocking(
            drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(13, 36), Math.toRadians(0))
                .waitSeconds(0.5)
                .build());

        robot.releaseAndDrop();

        Actions.runBlocking(
            drive.actionBuilder(drive.pose)
                .setReversed(true)
                .splineTo(new Vector2d(-20, 58), Math.toRadians(-180))
                .splineToLinearHeading(new Pose2d(-46, 42, Math.toRadians(-160)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(-56, 36), Math.toRadians(-180))
                .build());




        while(this.opModeIsActive()) {}
    }


}
