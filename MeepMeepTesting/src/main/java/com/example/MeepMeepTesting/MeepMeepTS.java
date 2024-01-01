package com.example.MeepMeepTesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTS {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-36, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-36, 36),Math.toRadians(270))
                .setReversed(true)
                .splineTo(new Vector2d(-36, 55), Math.toRadians(90))
                .setReversed(false)
                .splineTo(new Vector2d(-55, 36), Math.toRadians(270))
                .splineTo(new Vector2d(-36, 12), Math.toRadians(0))
                .splineTo(new Vector2d(22.7, 12), Math.toRadians(0))
                .splineTo(new Vector2d(40, 36), Math.toRadians(0))
                .setReversed(true)
                .splineTo(new Vector2d(40, 60), Math.toRadians(90))
                .setReversed(false)
                .strafeTo(new Vector2d(44, 60))
                .build());























        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}