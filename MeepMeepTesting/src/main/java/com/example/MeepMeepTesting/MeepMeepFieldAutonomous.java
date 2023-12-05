package com.example.MeepMeepTesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepFieldAutonomous {
    public enum autoStart {RED_STACK1, RED_STACK2, RED_STACK3, RED_BACK, BLUE_STACK, BLUE_BACK }
    public static autoStart start = autoStart.RED_BACK;

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 13)
                .build();
        switch (start) {
            case RED_STACK1:
                myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-34, 62, Math.toRadians(90)))
                        .setReversed(true)
                        .splineTo(new Vector2d(-44, 32), Math.toRadians(-120))  // To the spike mark
                        .setReversed(false)
                        .splineTo(new Vector2d(-50, 50), Math.toRadians(90))  // Back away from spike mark
                        .setReversed(true)
                        .splineTo(new Vector2d(-57, 30), Math.toRadians(-90))  // Approach bridge
                        .splineTo(new Vector2d(42, 18), Math.toRadians(0))  // Go under bridge
                        .turnTo(-180)
                        .setReversed(false)
                        .splineTo(new Vector2d(48, 36), Math.toRadians(0))  // Go to backdrop
                        .waitSeconds(0.5)
                        .setReversed(true)
                        .splineTo(new Vector2d(0, 58), Math.toRadians(-180))  // Go back along wall
                        .splineTo(new Vector2d(-32, 58), Math.toRadians(-180))  // Go under bridge
                        .splineTo(new Vector2d(-54, 36), Math.toRadians(-90))
                        .turnTo(0)
                        .setReversed(false)
                        .turn(Math.toRadians(180))
                        .splineTo(new Vector2d(-57, 36), Math.toRadians(-180))
                        .build());
                break ;
            case RED_STACK2:
                myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-34, 62, Math.toRadians(90)))
                        .setReversed(true)
                        .splineTo(new Vector2d(-44, 32), Math.toRadians(-120))  // To the spike mark
                        .setReversed(false)
                        .splineTo(new Vector2d(-50, 42), Math.toRadians(180))  // Back away from spike mark
                        .splineTo(new Vector2d(-57, 30), Math.toRadians(-90))  // Approach bridge
                        .splineTo(new Vector2d(26, 12), Math.toRadians(0))  // Go under bridge
                        .splineTo(new Vector2d(48, 36), Math.toRadians(0))  // Go to backdrop
                        .waitSeconds(0.5)
                        .setReversed(true)
                        .splineTo(new Vector2d(0, 58), Math.toRadians(-180))  // Go back along wall
                        .splineTo(new Vector2d(-34, 58), Math.toRadians(-180))  // Go under bridge
                        .splineTo(new Vector2d(-54, 36), Math.toRadians(-120))
                        .setReversed(false)
                        .splineTo(new Vector2d(-57, 36), Math.toRadians(-180))
                        .build());
            case RED_STACK3:
                myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-34, 62, Math.toRadians(90)))
                        .setReversed(true)
                        .splineTo(new Vector2d(-44, 32), Math.toRadians(-120))  // To the spike mark
                        .setReversed(false)
                        .splineTo(new Vector2d(-50, 42), Math.toRadians(180))  // Back away from spike mark
                        .splineTo(new Vector2d(-57, 30), Math.toRadians(-90))  // Approach bridge
                        .splineTo(new Vector2d(26, 12), Math.toRadians(0))  // Go under bridge
                        .splineTo(new Vector2d(48, 36), Math.toRadians(0))  // Go to backdrop
                        .setReversed(true)
                        .splineTo(new Vector2d(40, 30), Math.toRadians(-90))  // Go back along wall
                        .splineTo(new Vector2d(52, 18), Math.toRadians(-45))  // Go under bridge
                        .build());
                break ;
            case RED_BACK:
                myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, 62, Math.toRadians(90)))
                        .setReversed(true)
                        .splineTo(new Vector2d(4, 32), Math.toRadians(-120))  // To the spike mark
                        .setReversed(false)
                        .splineTo(new Vector2d(18, 42), Math.toRadians(0))  // Back away from spike mark
                        .splineTo(new Vector2d(48, 36), Math.toRadians(0))  // Go to backdrop
                        .setReversed(true)
                        .splineTo(new Vector2d(40, 42), Math.toRadians(90))  // Go back along wall
                        .splineTo(new Vector2d(52, 58), Math.toRadians(0))  // Go under bridge
                        .build());
                break ;
        }



        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK )
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}