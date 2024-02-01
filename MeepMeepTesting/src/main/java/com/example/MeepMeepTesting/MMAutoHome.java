package com.example.MeepMeepTesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MMAutoHome {

    public static boolean BLUE = false ;
    public static boolean BACKSTAGE = true ;
    public static double FRONTSTAGE_X = 12 ; // Subject 24 for home
    public static double BACKSTAGE_X = 12 ;
    public static double START_Y = 62 ;
    public static double PURPLE_RELEASE_X_DELTA = 5 ;
    public static double YELLOW_RELEASE_Y_DELTA = 6 ;
    public static int colorModifier = 1 ;  // 1 = Blue, -1 = Red


    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        double startX = (BACKSTAGE ? BACKSTAGE_X : FRONTSTAGE_X) ;  // Set the starting X coordinate based on back/front stage
        double purpleReleaseX = startX ;
        double purpleReleaseY = 34 ;
        double purpleReleaseAngle = -90 ;
        double yellowReleaseY = 36 ;
        colorModifier = (BLUE ? 1 : -1) ;

        int myZone = 2 ;

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

        myBot.runAction(myBot.getDrive().actionBuilder( new Pose2d(startX, START_Y * colorModifier, Math.toRadians(90) * colorModifier))
                // Purple
                .setReversed(true)
                .splineTo(new Vector2d(startX, 48  * colorModifier), Math.toRadians(-90) * colorModifier)
                .splineTo(new Vector2d(purpleReleaseX, purpleReleaseY ), Math.toRadians(purpleReleaseAngle))
                .setReversed(false)
                //.splineTo(new Vector2d(startX, 42 * colorModifier), Math.toRadians(90) * colorModifier)
                .splineTo(new Vector2d(startX + (BACKSTAGE ? 12 : -12), 48 * colorModifier), Math.toRadians((BACKSTAGE ? 0 : 180)))
                // Frontstage
                //.splineTo(new Vector2d(startX-22, 34 * colorModifier), Math.toRadians(-90) * colorModifier)
                //.splineTo(new Vector2d(startX, 12 * colorModifier), Math.toRadians(0) )
                //.splineTo(new Vector2d(24, 13 * colorModifier), Math.toRadians(0) )
                // Yellow
                .splineTo(new Vector2d(45, yellowReleaseY ), Math.toRadians(0))
                // Park
                .setReversed(true)
                .splineTo(new Vector2d(36, (BACKSTAGE ? 48 : 24) * colorModifier), Math.toRadians(BACKSTAGE ? 90 : -90) * colorModifier)
                .splineTo(new Vector2d(50, (BACKSTAGE ? 60 : 13) * colorModifier), Math.toRadians(0))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}