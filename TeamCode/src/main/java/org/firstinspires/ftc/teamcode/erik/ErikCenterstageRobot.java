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


    public int liftPosition = 0 ;
    public static int LIFT_POSITION  = 0 ;  // Max = 1850
    public static int LIFT_MAX  = 1850 ;  // Max = 1850
    public int armPosition = 0 ;
    public static int ARM_POSITION  = 0 ;  // Max = 1660
    public static int ARM_MAX  = 1660 ;  // Max = 1660
    public static double GRIPPER_GRIP_POSITION  = 0.55 ;
    public static double GRIPPER_ROTATE_POSITION  = 0.5 ;



    // Execute this constructor during the init phase of the opMode ============================
    public ErikCenterstageRobot(OpMode opMode) {
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
        liftMotor.setTargetPosition(liftPosition);
        armMotor.setTargetPosition(armPosition);

        gripperGrip.setPosition(GRIPPER_GRIP_POSITION);
        gripperRotate.setPosition(GRIPPER_ROTATE_POSITION);

    }

    public void armRaise() {
        liftPosition = LIFT_MAX ;
        armPosition = ARM_MAX ;
    }

    public void armLower() {
        liftPosition = 0 ;
        armPosition = 0 ;
    }

    public void armDash() {
        liftPosition = LIFT_POSITION ;
        armPosition = ARM_POSITION ;
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



}
