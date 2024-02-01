package org.firstinspires.ftc.teamcode.erik.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
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


@TeleOp(name="Auto-Combo", group="Erik CenterStage")
@Config
public final class AutoCombo extends LinearOpMode {
    private final int READ_PERIOD = 1;
    private String myZone = "Center" ;
    private ErikCenterstageRobot robot ;
    public static int DEFAULT_ZONE  = 2 ;

    // -------------- TensorFlow Object Detection Setup ------------------
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private static final String TFOD_MODEL_ASSET = "model_with_metadata.tflite";
    private static final String[] LABELS = {
            "UFO", "???"
    };
    // The variable to store our instance of the TensorFlow Object Detection processor.
    private TfodProcessor tfod;
    // The variable to store our instance of the vision portal.
    private VisionPortal visionPortal;  // Not used for my TFOD code.
    // --------------- End TFOD declarations ---------------------------

    // ------------- Autonomous Variables ---------------------
    public static boolean BLUE = true ;
    public static boolean BACKSTAGE = true ;
    public static double FRONTSTAGE_X = -36 ;
    public static double BACKSTAGE_X = 12 ;
    public static double START_Y = 62 ;
    public static double PURPLE_RELEASE_X_DELTA = 5 ;
    public static double YELLOW_RELEASE_Y_DELTA = 6 ;
    public static int colorModifier = 1 ;  // 1 = Blue, -1 = Red


    @Override
    public void runOpMode() throws InterruptedException {
        // ------------ Derived Autonomous settings ------------------
        double startX = (BACKSTAGE ? BACKSTAGE_X : FRONTSTAGE_X) ;  // Set the starting X coordinate based on back/front stage
        double purpleReleaseX = startX ;
        double purpleReleaseY = 34 ;
        double purpleReleaseAngle = -90 ;
        double yellowReleaseY = 36 ;
        colorModifier = (BLUE ? 1 : -1) ;
        TranslationalVelConstraint slow = new TranslationalVelConstraint(15) ;

        robot = new ErikCenterstageRobot(this, new Pose2d(startX, START_Y*colorModifier, Math.toRadians(90)*colorModifier)) ;

        // TODO: *** Use the drive in the robot class ***
        //MecanumDrive drive = new MecanumDrive(hardwareMap,
        //        new Pose2d(startX, START_Y, Math.toRadians(90)));

        //robot.gripperState = ErikCenterstageRobot.GripperState.STORE ;
        //robot.update();
        robot.gripAndStore() ;

        initTfod();

        waitForStart();
        robot.gripAndStore() ;

        // ================= OpMode Started ======================================

        if (this.opModeIsActive()) {

            //int myZone = tfodPropDetect() ;
            int myZone = DEFAULT_ZONE;

            switch (myZone) {
                case 2: // Right
                    purpleReleaseX = startX - PURPLE_RELEASE_X_DELTA  * colorModifier ;
                    purpleReleaseY = 36 * colorModifier ;
                    purpleReleaseAngle = -90 * colorModifier - 45 ;
                    yellowReleaseY = 36 * colorModifier - YELLOW_RELEASE_Y_DELTA  ;
                    break;
                case 0: // Left
                    purpleReleaseX = startX + PURPLE_RELEASE_X_DELTA * colorModifier ;
                    purpleReleaseY = 36 * colorModifier ;
                    purpleReleaseAngle = -90 * colorModifier + 45;
                    yellowReleaseY = 36 * colorModifier + YELLOW_RELEASE_Y_DELTA ;
                    break;
                default: // 1: Center, Default
                    purpleReleaseX = startX ;
                    purpleReleaseY = 34 * colorModifier ;
                    purpleReleaseAngle = -90 * colorModifier ;
                    yellowReleaseY = 36 * colorModifier;
                    break;
            }

            // ==================== Place Purple Pixel on spike mark =======================
            Actions.runBlocking(
                    robot.drive.actionBuilder(robot.drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(startX, 48  * colorModifier), Math.toRadians(-90) * colorModifier)
                            .splineTo(new Vector2d(purpleReleaseX, purpleReleaseY ), Math.toRadians(purpleReleaseAngle), slow)
                            .setReversed(false)
                            //.splineTo(new Vector2d(startX, 42 * colorModifier), Math.toRadians(90) * colorModifier)
                            .splineTo(new Vector2d(startX + (BACKSTAGE ? 12 : -12), 48 * colorModifier), Math.toRadians((BACKSTAGE ? 0 : 180)), slow)
                            .build());

            // ---------------------- Frontstage additional driving ---------------------
            if (!BACKSTAGE) {
                Actions.runBlocking(
                        robot.drive.actionBuilder(robot.drive.pose)
                                .splineTo(new Vector2d(startX-22, 34 * colorModifier), Math.toRadians(-90) * colorModifier)
                                .splineTo(new Vector2d(startX, 12 * colorModifier), Math.toRadians(0) )
                                .splineTo(new Vector2d(24, 13 * colorModifier), Math.toRadians(0) )
                                .build());
            }

            robot.armRaise(1);
            // ===================== Place Yellow Pixel on backdrop =======================
            Actions.runBlocking(
                    robot.drive.actionBuilder(robot.drive.pose)
                            .splineTo(new Vector2d(45, yellowReleaseY ), Math.toRadians(0), slow)
                            .build());

            robot.releaseAndDrop();
            // ===================== Leave backdrop and park ==============================
            Actions.runBlocking(
                    robot.drive.actionBuilder(robot.drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(36, (BACKSTAGE ? 48 : 24) * colorModifier), Math.toRadians(BACKSTAGE ? 90 : -90) * colorModifier)
                            .splineTo(new Vector2d(54, (BACKSTAGE ? 60 : 13) * colorModifier), Math.toRadians(0))
                            .build());
        }

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
        tfod.setMinResultConfidence(0.75f);

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

    private int tfodPropDetect() {
        double highScore = .70 ;
        double highX = 310 ;
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        for (Recognition recognition : currentRecognitions) {
            double width = (recognition.getRight() - recognition.getLeft()) ;
            double height = (recognition.getTop() - recognition.getBottom()) ;
            if (width<120 && height<120 && recognition.getConfidence()>highScore) {
                highX = (recognition.getLeft() + recognition.getRight()) / 2 ;
            }
        }
        if (highX<200) return 0 ;
        else if (highX>400) return 2 ;
        if ( currentRecognitions.size() >0) return 1 ;
        else return DEFAULT_ZONE ;
    }
}
