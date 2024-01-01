package com.example.MeepMeepTesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 13)
                .build();

        /*
        //  prop only
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-34, 62, Math.PI/2))
                .setReversed(true)
                .splineTo(new Vector2d(-24, 32), Math.toRadians(-60))
                .setReversed(false)
                .splineTo(new Vector2d(-20, 58), Math.toRadians(0))
                .build());


         */






/*
        // ======== For full-size field ==================

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-34, 62, Math.PI/2))
                .setReversed(true)
                .splineTo(new Vector2d(-44, 36), Math.toRadians(-120))
                .setReversed(false)

                .splineTo(new Vector2d(-20, 58), Math.toRadians(0))
                .splineTo(new Vector2d(0, 58), Math.toRadians(0))
                .splineTo(new Vector2d(44, 42), Math.toRadians(0))
                .waitSeconds(2)

                .setReversed(true)
                .splineTo(new Vector2d(0, 58), Math.toRadians(-180))
                .splineTo(new Vector2d(-26, 58), Math.toRadians(-180))

                .splineToLinearHeading(new Pose2d(-46, 42, Math.toRadians(-160)), Math.toRadians(0))

                .setReversed(false)
                .splineTo(new Vector2d(-56, 36), Math.toRadians(-180))
                .build());

 */



        // ======== For full-size field ====== With regular turn
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-34, 62, Math.toRadians(90)))
                .setReversed(true)
                .splineTo(new Vector2d(-42, 30), Math.toRadians(-120))
                .setReversed(false)
                .splineTo(new Vector2d(-42, 58), Math.toRadians(180))
                .splineTo(new Vector2d(-56, 40), Math.toRadians(-90))
                .build());





        /*
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-34, 70, Math.PI/2))
                .setReversed(true)
                .splineTo(new Vector2d(-46, 34), Math.toRadians(-120))
                .setReversed(false)
                .splineTo(new Vector2d(-20, 58), Math.toRadians(0))
                        .splineTo(new Vector2d(13, 36), Math.toRadians(0))
                .build());

         */





        /*  ======= For the home lab
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-34, 62, Math.PI/2))
                .setReversed(true)
                .splineTo(new Vector2d(-44, 36), Math.toRadians(-120))
                .setReversed(false)
                .splineTo(new Vector2d(-20, 58), Math.toRadians(0))

                .splineTo(new Vector2d(13, 36), Math.toRadians(0))
                .waitSeconds(2)

                .setReversed(true)
                .splineTo(new Vector2d(-20, 58), Math.toRadians(-180))

                .splineToLinearHeading(new Pose2d(-46, 42, Math.toRadians(-160)), Math.toRadians(0))

                .setReversed(false)
                .splineTo(new Vector2d(-56, 36), Math.toRadians(-180))
                .build());

         */

/*
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, 0, 0))
                .lineToX(30)
                .turn(Math.toRadians(90))  // Turns are relative to the robot
                .lineToY(30)
                .turn(Math.toRadians(90))
                .lineToX(0)
                .turn(Math.toRadians(90))
                .lineToY(0)
                .turn(Math.toRadians(90))
                .build());

 */



        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK )
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}