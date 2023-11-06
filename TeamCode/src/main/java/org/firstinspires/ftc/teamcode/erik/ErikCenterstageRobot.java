package org.firstinspires.ftc.teamcode.erik;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Config
public class ErikCenterstageRobot {
    private DcMotor liftMotor, armMotor ;
    private Servo gripperGrip, gripperRotate ;
    private OpMode opMode ;
    public MecanumDrive drive ;

    public static double GRIPPER_CLOSE  = 0.67 ;
    public static double GRIPPER_OPEN  = 0.9 ;
    public static int INTAKE_OFFSET = 100 ;

    // These are the constants used by algorithms. You can modify them live in Dashboard.
    public static double[] GRIPPER_ROTATE_INTAKE_POS = { 0.525, 0.615 } ;  // Min: 0, Max: 1
    public static double[] GRIPPER_ROTATE_STORE_POS = { 0.469, 0.560 } ;  // Min: 0, Max: 1
    public static double[] GRIPPER_ROTATE_DELIVER_POS = { 0.488, 0.575 } ;  // Min: 0, Max: 1
    public static int[] ARM_POSITIONS = { 0, 800, 1000, 1200, 1400, 1660 } ;  // Min: 0, Max: 5
    public static int[] LIFT_POSITIONS = { 0, 800, 1000, 1200, 1400, 1850 } ;  // Min: 0, Max: 5

    public enum GripperState {STORE, INTAKE, DELIVER }
    public GripperState gripperState = GripperState.STORE ;

    // These represent the current target position of each component.
    public double gripperGripPosition = 0.9 ;
    public double gripperRotatePosition = 0.469 ;
    public int armPosition = 0 ;
    public int liftPosition = 0 ;
    public int deliveryLevel = 5 ;
    public int intakeLevel = 0 ;


    // Execute this constructor during the init phase of the opMode ============================
    public ErikCenterstageRobot(OpMode newOpMode) {
        opMode = newOpMode ;
        drive = new MecanumDrive(opMode.hardwareMap, new Pose2d(0, 0, 0)) ;
        liftMotor = opMode.hardwareMap.get(DcMotor.class, "liftMotor") ;
        //liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER) ;
        liftMotor.setTargetPosition(0) ;
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION) ;
        liftMotor.setPower(1) ;

        armMotor = opMode.hardwareMap.get(DcMotor.class, "armMotor") ;
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE) ;
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER) ;
        armMotor.setTargetPosition(0) ;
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION) ;
        armMotor.setPower(1) ;

        gripperGrip = opMode.hardwareMap.get(Servo.class, "gripperGrip") ;
        gripperRotate = opMode.hardwareMap.get(Servo.class, "gripperRotate") ;


        opMode.telemetry.addData("Robot Class", "Initialized") ;
    }

    // Execute this update one per loop in the opMode ==========================================
    // TODO: Because we calculate Gripper Rotation, we MUST run this regularly.
    public void update() {
        gripperRotatePosition = calcGripperRotationLive() ;  // Calculate rotation based on state and CURRENT arm position
        updatePayload() ;
    }

    // Support methods =========================================================================
    // Use this update method to just update basic payload: arm, lift, grip, rotation
    public void updatePayload() {
        // Send the command to the motor only if it has not yet been sent.
        if (armMotor.getTargetPosition()!=armPosition) armMotor.setTargetPosition(armPosition) ;
        if (liftMotor.getTargetPosition()!=liftPosition) liftMotor.setTargetPosition(liftPosition) ;
        if (gripperGrip.getPosition()!=gripperGripPosition) gripperGrip.setPosition(gripperGripPosition) ;
        if (gripperRotate.getPosition()!=gripperRotatePosition) gripperRotate.setPosition(gripperRotatePosition) ;
    }

    public void armRaise() {
        armRaise(5) ;
    }

    public void armRaise(int newDeliveryLevel) {
        liftPosition = LIFT_POSITIONS[newDeliveryLevel] ;
        armPosition = ARM_POSITIONS[newDeliveryLevel] ;
        gripperState = GripperState.DELIVER ;
        gripperRotatePosition = calcGripperRotation() ;  // Calculate rotation based on state and TARGET arm position
        updatePayload() ;
    }

    public void armLower() {
        liftPosition = LIFT_POSITIONS[0] ;
        armPosition = ARM_POSITIONS[0] ;
        gripperState = GripperState.STORE ;
        gripperRotatePosition = calcGripperRotation() ;  // Calculate rotation based on state and TARGET arm position
        updatePayload() ;
    }

    public void goToIntake(int newIntakeLevel){
        liftPosition = LIFT_POSITIONS[0] + newIntakeLevel*INTAKE_OFFSET ;
        armPosition = ARM_POSITIONS[0] ;
        gripOpen() ;
        gripperState = GripperState.INTAKE ;
        gripperRotatePosition = calcGripperRotation() ;  // Calculate rotation based on state and TARGET arm position
        updatePayload() ;
    }

    public void gripClose() {
        gripperGripPosition = GRIPPER_CLOSE ;
        updatePayload() ;
    }

    public void gripOpen() {
        gripperGripPosition = GRIPPER_OPEN ;
        updatePayload() ;
    }

    public void gripAndGo() {
        gripClose() ;
        waitForTime(0.5) ;
        armRaise(deliveryLevel) ;
        updatePayload() ;
    }

    public void gripAndStore() {
        gripClose() ;
        waitForTime(0.5) ;
        gripperState = GripperState.STORE ;
        gripperRotatePosition = calcGripperRotation() ;  // Calculate rotation based on state and TARGET arm position
        updatePayload() ;
    }

    public void releaseAndDrop () {
        gripOpen() ;
        waitForTime(0.5) ;
        armLower() ;
        updatePayload() ;
    }

    //Wait for a period of time (seconds)
    public void waitForTime(double time) {
        double startTime = opMode.getRuntime() ;
        while (IsTimeUp(startTime,time)){
            update() ;
        }
    }

    public void updateRaw( double gripRaw, double rotateRaw, int armRaw, int liftRaw) {
        // For opmodes that are used to find the min/max and positional values for the payload.
        gripperGripPosition = gripRaw ;
        gripperRotatePosition = rotateRaw ;
        liftPosition = liftRaw ;
        armPosition = armRaw ;
        update() ;
    }

    public void updateInterlock() {
        // For opmodes that are used to find the min/max and positional values for the payload.
        gripperGrip.setPosition(gripperGripPosition) ;
        liftMotor.setTargetPosition(liftPosition) ;
        armMotor.setTargetPosition(armPosition) ;
        gripperRotate.setPosition(calcGripperRotation()) ;
    }

    public double calcGripperRotation(  ) {
        double[] rotationArray ;
        double newPosition = 0.5 ;
        switch (gripperState) {
            case STORE:
                rotationArray = GRIPPER_ROTATE_STORE_POS ;
                break ;
            case DELIVER:
                rotationArray = GRIPPER_ROTATE_DELIVER_POS ;
                break ;
            default: // INTAKE
                rotationArray = GRIPPER_ROTATE_INTAKE_POS ;
                break ;
        }
        newPosition = rotationArray[0] + ((double) armPosition / (double) ARM_POSITIONS[5]) * (rotationArray[1] - rotationArray[0]) ;
        opMode.telemetry.addData("Gripper Rotate Position:   ", newPosition) ;
        return newPosition ;
    }

    public double calcGripperRotationLive() {
        double[] rotationArray ;
        double newPosition = 0.5 ;
        switch (gripperState) {
            case STORE:
                rotationArray = GRIPPER_ROTATE_STORE_POS ;
                break;
            case DELIVER:
                rotationArray = GRIPPER_ROTATE_DELIVER_POS ;
                break;
            default: // INTAKE
                rotationArray = GRIPPER_ROTATE_INTAKE_POS ;
                break;
        }
        newPosition = rotationArray[0] + ((double) armMotor.getCurrentPosition() / (double) ARM_POSITIONS[5]) * (rotationArray[1] - rotationArray[0]) ;
        opMode.telemetry.addData("Gripper Rotate Position:   ", newPosition) ;
        return newPosition ;
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

    public boolean IsTimeUp(double startTime, double runTime) { return opMode.getRuntime()<startTime+runTime ; } // From Owen
}
