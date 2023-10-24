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
    public int liftPosition = 0 ;
    public static int LIFT_POSITION  = 0 ;  // Max = 1850
    public static int LIFT_MAX  = 1850 ;  // Max = 1850
    public int armPosition = 0 ;
    public static int ARM_POSITION  = 0 ;  // Max = 1660
    public static int ARM_MAX  = 1660 ;  // Max = 1660
    public double gripperGripPosition = 0.55 ;
    public static double GRIPPER_GRIP_POSITION  = 0.55 ;
    public static double GRIPPER_CLOSE  = 0.75 ;
    public static double GRIPPER_OPEN  = 0.9 ;
    public double gripperRotatePosition = 0.47 ;
    public static double GRIPPER_ROTATE_POSITION  = 0.47 ;
    public static double GRIPPER_ROTATE_EXTENDED  = 0.575 ;
    public static double GRIPPER_ROTATE_INTAKE  = 0.525 ;
    public static double GRIPPER_ROTATE_UP  = 0.47 ;


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

    public boolean IsTimeUp(double startTime, double runTime) { return opMode.getRuntime()<startTime+runTime ; } // From Owen
}
