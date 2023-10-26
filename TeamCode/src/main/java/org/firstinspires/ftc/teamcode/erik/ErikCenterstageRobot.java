package org.firstinspires.ftc.teamcode.erik;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ErikCenterstageRobot {
    private DcMotor liftMotor, armMotor ;
    private Servo gripperGrip, gripperRotate ;
    private OpMode opMode ;

    public static int LIFT_POSITION  = 0 ;  // Max = 1850
    public static int LIFT_MAX  = 1850 ;  // Max = 1850
    public static int ARM_POSITION  = 0 ;  // Max = 1660
    public static int ARM_MAX  = 1660 ;  // Max = 1660
    public static double GRIPPER_GRIP_POSITION  = 0.9 ;
    public static double GRIPPER_CLOSE  = 0.71 ;
    public static double GRIPPER_OPEN  = 0.9 ;
    public static double GRIPPER_ROTATE_POSITION  = 0.525 ;
    public static double GRIPPER_ROTATE_EXTENDED  = 0.575 ;
    public static double GRIPPER_ROTATE_INTAKE  = 0.525 ;
    public static double GRIPPER_ROTATE_UP  = 0.47 ;


    public double gripperGripPosition = 0.9 ;
    public double gripperRotatePosition = 0.525 ;
    public int armPosition = 0 ;
    public int liftPosition = 0 ;

    // These are the constants used by algorithms. You can modify them live in Dashboard.
    public static double[] GRIPPER_ROTATE_INTAKE_POS = { 0.525, 0.615 } ;  // Min: 0, Max: 1
    public static double[] GRIPPER_ROTATE_STORE_POS = { 0.47, 0.565 } ;  // Min: 0, Max: 1
    public static double[] GRIPPER_ROTATE_DELIVER_POS = { 0.488, 0.575 } ;  // Min: 0, Max: 1
    public static int[] ARM_POSITIONS = { 0, 800, 1000, 1200, 1400, 1660 } ;  // Min: 0, Max: 5
    public static int[] LIFT_POSITIONS = { 0, 800, 1000, 1200, 1400, 1850 } ;  // Min: 0, Max: 5

    public enum GripperState {STORE, INTAKE, DELIVER } ;
    public static GripperState gripperState = GripperState.INTAKE ;


    // Execute this constructor during the init phase of the opMode ============================
    public ErikCenterstageRobot(OpMode newOpMode) {
        opMode = newOpMode ;
        liftMotor = opMode.hardwareMap.get(DcMotor.class, "liftMotor") ;
        //liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setTargetPosition(0);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(1);

        armMotor = opMode.hardwareMap.get(DcMotor.class, "armMotor") ;
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE) ;
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(1);

        gripperGrip = opMode.hardwareMap.get(Servo.class, "gripperGrip") ;
        gripperRotate = opMode.hardwareMap.get(Servo.class, "gripperRotate") ;


        opMode.telemetry.addData("Robot Class", "Initialized") ;
    }

    // Execute this update one per loop in the opMode ==========================================
    public void update() {
        // I don't want to keep setting the positions when they are already on their way.
        // Therefore, I'm letting the support functions set that. We'll add things here later.
        gripperRotate.setPosition(GRIPPER_ROTATE_POSITION);

    }


    // Execute when the opMode stops (not active) ==============================================
    public void stop() {
        liftMotor.setTargetPosition(0);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setPower(0);
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setPower(0);
    }



    // Support methods =========================================================================
    public void updatePayload() {
        liftMotor.setTargetPosition(liftPosition);
        armMotor.setTargetPosition(armPosition);
        gripperGrip.setPosition(gripperGripPosition);
        gripperRotate.setPosition(gripperRotatePosition);
    }

    public void armRaise() {
        liftPosition = LIFT_MAX ;
        armPosition = ARM_MAX ;
        gripperRotatePosition = GRIPPER_ROTATE_EXTENDED ;
        updatePayload() ;
    }

    public void armLower() {
        liftPosition = 0 ;
        armPosition = 0 ;
        gripperRotatePosition = GRIPPER_ROTATE_INTAKE ;
        updatePayload() ;
    }

    public void armDash() {
        liftPosition = LIFT_POSITION ;
        armPosition = ARM_POSITION ;
        updatePayload() ;
    }

    public void gripClose() {
        gripperGripPosition = GRIPPER_CLOSE ;
        //gripperGrip.setPosition(GRIPPER_CLOSE);
        updatePayload() ;
    }

    public void gripOpen() {
        gripperGripPosition = GRIPPER_OPEN ;
        //gripperGrip.setPosition(GRIPPER_OPEN);
        updatePayload() ;
    }

    public void gripAndGo() {
        gripClose() ;
        waitForTime(0.5) ;
        armRaise() ;
        updatePayload() ;
    }

    public void gripAndStore() {
        gripClose() ;
        waitForTime(0.5) ;
        //armLower() ;
        gripperRotatePosition = GRIPPER_ROTATE_UP ;
        updatePayload() ;
    }

    public void releaseAndDrop () {
        gripOpen() ;
        waitForTime(0.5) ;
        gripperRotatePosition = GRIPPER_ROTATE_INTAKE ;
        armLower() ;
        updatePayload() ;
    }

    //Wait for a period of time (seconds)
    public void waitForTime(double time) {
        double startTime = opMode.getRuntime();
        while (IsTimeUp(startTime,time)){
            update() ;
        }
    }

    public void updateRaw() {
        // For opmodes that are used to find the min/max and positional values for the payload.
        gripperGrip.setPosition(GRIPPER_GRIP_POSITION);
        gripperRotate.setPosition(GRIPPER_ROTATE_POSITION);
        liftMotor.setTargetPosition(LIFT_POSITION);
        armMotor.setTargetPosition(ARM_POSITION);
    }

    public void updateInterlock() {
        // For opmodes that are used to find the min/max and positional values for the payload.
        gripperGrip.setPosition(GRIPPER_GRIP_POSITION);
        liftMotor.setTargetPosition(LIFT_POSITION);
        armMotor.setTargetPosition(ARM_POSITION);

        switch (gripperState) {
            case STORE:
                gripperRotate.setPosition(GRIPPER_ROTATE_STORE_POS[0] + ((double) ARM_POSITION / (double) ARM_POSITIONS[5]) * (GRIPPER_ROTATE_STORE_POS[1] - GRIPPER_ROTATE_STORE_POS[0]));
                opMode.telemetry.addData("Gripper Rotate Position:   ",
                        GRIPPER_ROTATE_STORE_POS[0] + ((double) ARM_POSITION / (double) ARM_POSITIONS[5]) * (GRIPPER_ROTATE_STORE_POS[1] - GRIPPER_ROTATE_STORE_POS[0]));
                break ;
            case DELIVER:
                gripperRotate.setPosition(GRIPPER_ROTATE_DELIVER_POS[0] + ((double) ARM_POSITION / (double) ARM_POSITIONS[5]) * (GRIPPER_ROTATE_DELIVER_POS[1] - GRIPPER_ROTATE_DELIVER_POS[0]));
                opMode.telemetry.addData("Gripper Rotate Position:   ",
                        GRIPPER_ROTATE_DELIVER_POS[0] + ((double) ARM_POSITION / (double) ARM_POSITIONS[5]) * (GRIPPER_ROTATE_DELIVER_POS[1] - GRIPPER_ROTATE_DELIVER_POS[0]));
                break ;
            default:
                gripperRotate.setPosition(GRIPPER_ROTATE_INTAKE_POS[0] + ((double) ARM_POSITION / (double) ARM_POSITIONS[5]) * (GRIPPER_ROTATE_INTAKE_POS[1] - GRIPPER_ROTATE_INTAKE_POS[0]));
                opMode.telemetry.addData("Gripper Rotate Position:   ",
                        GRIPPER_ROTATE_INTAKE_POS[0] + ((double) ARM_POSITION / (double) ARM_POSITIONS[5]) * (GRIPPER_ROTATE_INTAKE_POS[1] - GRIPPER_ROTATE_INTAKE_POS[0]));
                break ;
        }
    }

    public boolean IsTimeUp(double startTime, double runTime) { return opMode.getRuntime()<startTime+runTime ; } // From Owen
}
