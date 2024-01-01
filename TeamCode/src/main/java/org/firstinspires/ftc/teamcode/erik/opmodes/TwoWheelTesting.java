package org.firstinspires.ftc.teamcode.erik.opmodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.erik.ErikCenterstageRobot;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;


@TeleOp(name="Two Wheel Testing", group="Erik CenterStage")
public final class TwoWheelTesting extends LinearOpMode {
    private final int READ_PERIOD = 1;
    private String myZone = "Center" ;


    // -------------- TensorFlow Object Detection Setup ------------------
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private static final String TFOD_MODEL_ASSET = "model_with_metadata.tflite";
    private static final String[] LABELS = {
            "UFO", "???"
    };
    // The variable to store our instance of the TensorFlow Object Detection processor.
    private TfodProcessor tfod;
    // The variable to store our instance of the vision portal.
    private VisionPortal visionPortal;
    // --------------- End TFOD setup ---------------------------


    @Override
    public void runOpMode() throws InterruptedException {
        ErikCenterstageRobot robot = new ErikCenterstageRobot(this) ;
        MecanumDrive drive = new MecanumDrive(hardwareMap,
                new Pose2d(-34, 58, Math.toRadians(90)));

        robot.gripperState = ErikCenterstageRobot.GripperState.INTAKE ;
        robot.update();

        initTfod();

        waitForStart();
        // ================= OpMode Started ======================================

        robot.gripAndStore() ;
        int myZone = tfodDetect() ;


        switch (myZone) {
            case 2: // Right
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(-42, 30), Math.toRadians(-120))
                            .setReversed(false)
                            .splineTo(new Vector2d(-42, 42), Math.toRadians(180))
                            .build());
                break ;
            case 0: // Left
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(-27, 30), Math.toRadians(-60))
                            .setReversed(false)
                            .splineTo(new Vector2d(-42, 42), Math.toRadians(180))
                            .build());
                break ;
            default: // Center, default
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(-34, 30), Math.toRadians(-90))
                            .setReversed(false)
                            .splineTo(new Vector2d(-42, 42), Math.toRadians(180))
                            .build());
                break ;
        }



        /*

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

        */


        while(this.opModeIsActive()) {

        }
    } // End of OpMode



    /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

                // With the following lines commented out, the default TfodProcessor Builder
                // will load the default model for the season. To define a custom model to load,
                // choose one of the following:
                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                .setModelAssetName(TFOD_MODEL_ASSET)
                //.setModelFileName(TFOD_MODEL_FILE)


                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                .setModelLabels(LABELS)
                .setIsModelTensorFlow2(true)
                .setIsModelQuantized(true)
                .setModelInputSize(300)
                .setModelAspectRatio(16.0 / 9.0)

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        //tfod.setMinResultConfidence(0.85f);
        tfod.setMinResultConfidence(0.65f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }   // end method initTfod()

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());
        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;
            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop
    }   // end method telemetryTfod()

    private int tfodDetect() {
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        if (currentRecognitions.size()>0) {
            double x = currentRecognitions.get(0).getLeft() + currentRecognitions.get(0).getRight() / 2 ;
            if (x>300) return 0 ;
            else if (x<100) return 2 ;
        }
        return 1 ;
    }
}
