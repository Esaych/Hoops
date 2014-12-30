package com.esaych.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esaych.world.aspects.HighScores;

public class AssetLoader {
	public static Texture texture, explosion, background, city, company, help;

    public static Animation ballAnim;
    public static TextureRegion ball1, ball2, ball3, ball4, ball5;
    public static TextureRegion ballRed;

    public static Animation bombAnim;
    public static TextureRegion bomb1, bomb2, bomb3, bomb4;
    
    public static Animation bexplosion;
    public static TextureRegion bexp1, bexp2, bexp3, bexp4, bexp5, bexp6, bexp7, bexp8;
    
    public static Animation hoopAnim;
    public static TextureRegion hoop1, hoop2, hoop3, hoop4;
    
    public static Animation hoopGenerator;
    public static TextureRegion hoopGen1, hoopGen2, hoopGen3, hoopGen4, hoopGen5, hoopGen6, hoopGen7, hoopGen8;
    
    public static TextureRegion life, heart, heartHoop;
    
    public static TextureRegion bg, bgcity, cmpny, logo, gameover, winner, loser, tie, forfeit;
    
    public static TextureRegion buttonL, buttonR, buttonMid;
    public static TextureRegion p_buttonL, p_buttonR, p_buttonMid;
    public static TextureRegion labelL, labelR, labelMid;
    public static TextureRegion hslabelL, hslabelR, hslabelMid;
    public static TextureRegion hsIcon, achIcon;
    public static TextureRegion pause, music, sound, cancel;
    
    public static TextureRegion controlHelpScreen, controlHelpBall, controlHelpTilt, controlHelpJoystick;
    public static TextureRegion helpWelcome, helpBall, helpBomb, helpHoop, helpLife, helpPower, helpMoreHoops, helpHardcore, helpOneLife, helpControls, helpControlNote, helpInfinity, helpInfinityInstruct;
    
    public static TextureRegion bcBlue, bcPurple, bcOrange, bcYellow, bcGreen, bcAqua;
    public static TextureRegion hcBlue, hcPurple, hcOrange, hcYellow, hcGreen, hcAqua, hcLife;

    public static BitmapFont font, hsfont, bgfont, fontSm, bgfontSm, rdfont, bigfont;
    
    public static Preferences prefs;

    public static Sound explode, point, button_click, powerup, newhoop, start_effect, loop_effect;
    public static Music intro_music, start_music, loop_music, outro_music;

	public static void load() {
    	texture = new Texture(Gdx.files.internal("data/texture.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        explosion = new Texture(Gdx.files.internal("data/explosion.png"));
        explosion.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    	background = new Texture(Gdx.files.internal("data/bg.png"));
        background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    	city = new Texture(Gdx.files.internal("data/bgcity.png"));
        city.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    	company = new Texture(Gdx.files.internal("data/s8_studios.png"));
        company.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        help = new Texture(Gdx.files.internal("data/help.png"));
        help.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        ball1 = new TextureRegion(texture, 0, 0, 28, 28);
        ball2 = new TextureRegion(texture, 0, 28, 28, 28);
        ball3 = new TextureRegion(texture, 0, 56, 28, 28);
        ball4 = new TextureRegion(texture, 0, 84, 28, 28);
        ball5 = new TextureRegion(texture, 0, 112, 28, 28);
        
        TextureRegion[] balls = { ball1, ball2, ball3, ball4, ball5 };
        ballAnim = new Animation(0.1f, balls);
        ballAnim.setPlayMode(PlayMode.LOOP_PINGPONG);
        

        ballRed = new TextureRegion(texture, 436, 0, 28, 28);
        ballRed.flip(false, true);
        
        bomb1 = new TextureRegion(texture, 0, 168, 44, 44);
        bomb2 = new TextureRegion(texture, 0, 212, 44, 44);
        bomb3 = new TextureRegion(texture, 44, 168, 44, 44);
        bomb4 = new TextureRegion(texture, 44, 212, 44, 44);
        
        TextureRegion[] bombs = { bomb1, bomb2, bomb3, bomb4 };
        bombAnim = new Animation(0.2f, bombs);
        bombAnim.setPlayMode(PlayMode.LOOP_PINGPONG);
        
        bexp1 = new TextureRegion(explosion, 0, 0, 240, 240);
        bexp1.flip(false, true);
        bexp2 = new TextureRegion(explosion, 240, 0, 240, 240);
        bexp2.flip(false, true);
        bexp3 = new TextureRegion(explosion, 480, 0, 240, 240);
        bexp3.flip(false, true);
        bexp4 = new TextureRegion(explosion, 720, 0, 240, 240);
        bexp4.flip(false, true);
        bexp5 = new TextureRegion(explosion, 960, 0, 240, 240);
        bexp5.flip(false, true);
        bexp6 = new TextureRegion(explosion, 1200, 0, 240, 240);
        bexp6.flip(false, true);
        bexp7 = new TextureRegion(explosion, 1440, 0, 240, 240);
        bexp7.flip(false, true);
        bexp8 = new TextureRegion(explosion, 1670, 0, 240, 240);
        bexp8.flip(false, true);
        
        TextureRegion[] bexps = { bexp1, bexp2, bexp3, bexp4, bexp5, bexp6, bexp7, bexp8 };
        bexplosion = new Animation(0.1f, bexps);
        
        hoop1 = new TextureRegion(texture, 28, 0, 48, 20);
        hoop2 = new TextureRegion(texture, 28, 20, 48, 20);
        hoop3 = new TextureRegion(texture, 28, 40, 48, 20);
        hoop4 = new TextureRegion(texture, 28, 60, 48, 20);
        
        TextureRegion[] hoopAnims = { hoop1, hoop1, hoop1, hoop1, hoop1, hoop1, hoop1, hoop1, hoop1, hoop1, hoop1, hoop1, hoop2, hoop3, hoop4 };
        hoopAnim = new Animation(0.1f, hoopAnims);
        hoopAnim.setPlayMode(PlayMode.LOOP_PINGPONG);
        
        hoopGen1 = new TextureRegion(texture, 88, 0, 52, 22);
        hoopGen2 = new TextureRegion(texture, 88, 22, 52, 22);
        hoopGen3 = new TextureRegion(texture, 88, 44, 52, 22);
        hoopGen4 = new TextureRegion(texture, 88, 66, 52, 22);
        hoopGen5 = new TextureRegion(texture, 88, 88, 52, 22);
        hoopGen6 = new TextureRegion(texture, 88, 110, 52, 22);
        hoopGen7 = new TextureRegion(texture, 88, 132, 52, 22);
        hoopGen8 = new TextureRegion(texture, 88, 154, 52, 22);
        
        TextureRegion[] hoopGenAnims = { hoopGen1, hoopGen2, hoopGen3, hoopGen4, hoopGen5, hoopGen6, hoopGen7, hoopGen8 };
        hoopGenerator = new Animation(.05f, hoopGenAnims);
        hoopGenerator.setPlayMode(PlayMode.NORMAL);
       
        
        bg = new TextureRegion(background, 0, 0, 1920, 720);
        bg.flip(false, true);
        
        bgcity = new TextureRegion(city, 0, 0, 1920, 720);
        bgcity.flip(false, true);
        
        cmpny = new TextureRegion(company, 0, 0, 512, 128);
        cmpny.flip(false, true);
        
        logo = new TextureRegion(texture, 223, 0, 165, 37);
        logo.flip(false, true);
        
        gameover = new TextureRegion(texture, 242, 37, 147, 30);
        gameover.flip(false, true);
        
        winner = new TextureRegion(texture, 242, 67, 79, 23);
        winner.flip(false, true);
        loser = new TextureRegion(texture, 325, 67, 64, 23);
        loser.flip(false, true);
        tie = new TextureRegion(texture, 331, 90, 34, 23);
        tie.flip(false, true);
        forfeit = new TextureRegion(texture, 242, 90, 85, 23);
        forfeit.flip(false, true);
        
        buttonL = new TextureRegion(texture, 210, 105, 12, 28);
        buttonL.flip(false, true);
        buttonMid = new TextureRegion(texture, 222, 105, 2, 28);
        buttonMid.flip(false, true);
        buttonR = new TextureRegion(texture, 224, 105, 12, 28);
        buttonR.flip(false, true);
        
        p_buttonL = new TextureRegion(texture, 210, 133, 12, 28);
        p_buttonL.flip(false, true);
        p_buttonMid = new TextureRegion(texture, 222, 133, 2, 28);
        p_buttonMid.flip(false, true);
        p_buttonR = new TextureRegion(texture, 224, 133, 12, 28);
        p_buttonR.flip(false, true);
        
        pause = new TextureRegion(texture, 210, 28, 13, 13);
        pause.flip(false, true);
        sound = new TextureRegion(texture, 210, 42, 19, 16);
        sound.flip(false, true);
        music = new TextureRegion(texture, 210, 58, 19, 16);
        music.flip(false, true);
        cancel = new TextureRegion(texture, 210, 74, 19, 16);
        cancel.flip(false, true);
        life = new TextureRegion(texture, 210, 90, 15, 15);
        life.flip(false, true);
        heart = new TextureRegion(texture, 212, 195, 18, 14);
        heart.flip(false, true);
        heartHoop = new TextureRegion(texture, 212, 209, 18, 9);
        heartHoop.flip(false, true);
        
        labelL = new TextureRegion(texture, 210, 0, 6, 14);
        labelL.flip(false, true);
        labelMid = new TextureRegion(texture, 216, 0, 1, 14);
        labelMid.flip(false, true);
        labelR = new TextureRegion(texture, 217, 0, 6, 14);
        labelR.flip(false, true);
        
        hslabelL = new TextureRegion(texture, 210, 14, 6, 14);
        hslabelL.flip(false, true);
        hslabelMid = new TextureRegion(texture, 216, 14, 1, 14);
        hslabelMid.flip(false, true);
        hslabelR = new TextureRegion(texture, 217, 14, 6, 14);
        hslabelR.flip(false, true);
        hsIcon = new TextureRegion(texture, 212, 180, 19, 15);
        hsIcon.flip(false, true);
        achIcon = new TextureRegion(texture, 212, 161, 18, 19);
        achIcon.flip(false, true);
        
//        controlHelpTilt = new TextureRegion(help, 0, 720, 165, 80);
//        controlHelpTilt.flip(false, true);
//        controlHelpBall = new TextureRegion(help, 165, 720, 165, 80);
//        controlHelpBall.flip(false, true);
//        controlHelpJoystick = new TextureRegion(help, 330, 720, 165, 80);
//        controlHelpJoystick.flip(false, true);
//        controlHelpScreen = new TextureRegion(help, 495, 720, 165, 80);
//        controlHelpScreen.flip(false, true);
        
        helpWelcome = new TextureRegion(help, 0, 0, 417, 58);
        helpWelcome.flip(false, true);
        helpBomb = new TextureRegion(help, 0, 59, 503, 140);
        helpBomb.flip(false, true);
        helpHoop = new TextureRegion(help, 0, 200, 457, 89);
        helpHoop.flip(false, true);
        helpBall = new TextureRegion(help, 0, 290, 411, 140);
        helpBall.flip(false, true);
        helpMoreHoops = new TextureRegion(help, 0, 431, 286, 57);
        helpMoreHoops.flip(false, true);
        helpLife = new TextureRegion(help, 0, 489, 413, 95);
        helpLife.flip(false, true);
        helpPower = new TextureRegion(help, 0, 585, 330, 140);
        helpPower.flip(false, true);
        helpHardcore = new TextureRegion(help, 0, 726, 482, 50);
        helpHardcore.flip(false, true);
        helpOneLife = new TextureRegion(help, 0, 777, 415, 57);
        helpOneLife.flip(false, true);
        helpControls = new TextureRegion(help, 0, 835, 194, 50);
        helpControls.flip(false, true);
        helpControlNote = new TextureRegion(help, 0, 886, 305, 80);
        helpControlNote.flip(false, true);
        helpInfinity = new TextureRegion(help, 0, 967, 430, 58);
        helpInfinity.flip(false, true);
        helpInfinityInstruct = new TextureRegion(help, 0, 1026, 325, 95);
        helpInfinityInstruct.flip(false, true);
        
        bcBlue = new TextureRegion(texture, 436, 28, 28, 28);
        hcBlue = new TextureRegion(texture, 464, 28, 48, 20);
        bcPurple = new TextureRegion(texture, 436, 56, 28, 28);
        hcPurple = new TextureRegion(texture, 464, 56, 48, 20);
        bcOrange = new TextureRegion(texture, 436, 84, 28, 28);
        hcOrange = new TextureRegion(texture, 464, 84, 48, 20);
        bcYellow = new TextureRegion(texture, 436, 112, 28, 28);
        hcYellow = new TextureRegion(texture, 464, 112, 48, 20);
        bcGreen = new TextureRegion(texture, 436, 140, 28, 28);
        hcGreen = new TextureRegion(texture, 464, 140, 48, 20);
        bcAqua = new TextureRegion(texture, 436, 168, 28, 28);
        hcAqua = new TextureRegion(texture, 464, 168, 48, 20);
        hcLife = new TextureRegion(texture, 464, 0, 48, 20);
        hcLife.flip(false, true);
        
        font = new BitmapFont(Gdx.files.internal("data/mecha2.fnt"));
        font.setColor(Color.WHITE);
        font.setScale(.3f, -.3f);
        hsfont = new BitmapFont(Gdx.files.internal("data/mecha2.fnt"));
        hsfont.setColor(new Color(255, 215, 0, 1));
        hsfont.setScale(.3f, -.3f);
        bgfont = new BitmapFont(Gdx.files.internal("data/mecha2.fnt"));
        bgfont.setColor(Color.BLACK);
        bgfont.setScale(.3f, -.3f);
        rdfont = new BitmapFont(Gdx.files.internal("data/mecha2.fnt"));
        rdfont.setColor(Color.RED);
        rdfont.setScale(.3f, -.3f);
        fontSm = new BitmapFont(Gdx.files.internal("data/mecha2.fnt"));
        fontSm.setColor(Color.WHITE);
        fontSm.setScale(.2f, -.2f);
        bgfontSm = new BitmapFont(Gdx.files.internal("data/mecha2.fnt"));
        bgfontSm.setColor(Color.BLACK);
        bgfontSm.setScale(.2f, -.2f);
        bigfont = new BitmapFont(Gdx.files.internal("data/mecha2.fnt"));
        bigfont.setColor(new Color(255, 215, 0, 1));
        bigfont.setScale(1f, -1f);
        
        prefs = Gdx.app.getPreferences("Hoops");

        if (!prefs.contains("HighScores_NORMAL"))
            HighScores.initialize();

        if (!prefs.contains("GameMode")) 
        	prefs.putString("GameMode", "Normal");
        
        if (!prefs.contains("Music")) 
            prefs.putBoolean("Music", true);
        
        if (!prefs.contains("Sound")) 
        	prefs.putBoolean("Sound", true);
        
        if (!prefs.contains("Tilt")) 
            prefs.putBoolean("Tilt", false);
        
        if (!prefs.contains("LoginRequested")) 
        	prefs.putBoolean("LoginRequested", false);
        
        prefs.flush();
        
        intro_music = Gdx.audio.newMusic(Gdx.files.internal("data/Hoops-Intro.mp3"));
        start_music = Gdx.audio.newMusic(Gdx.files.internal("data/Hoops-Start.mp3")); // Sound for mixing
        loop_music = Gdx.audio.newMusic(Gdx.files.internal("data/Hoops-Loop.mp3"));
        outro_music = Gdx.audio.newMusic(Gdx.files.internal("data/Hoops-Outro.mp3"));
        
        start_effect = Gdx.audio.newSound(Gdx.files.internal("data/Hoops-FX1.mp3"));
        loop_effect = Gdx.audio.newSound(Gdx.files.internal("data/Hoops-FX2.mp3"));
        
        explode = Gdx.audio.newSound(Gdx.files.internal("data/explode.mp3"));
        point = Gdx.audio.newSound(Gdx.files.internal("data/coin.mp3"));
        button_click = Gdx.audio.newSound(Gdx.files.internal("data/button_click.mp3"));
        powerup = Gdx.audio.newSound(Gdx.files.internal("data/powerup.mp3"));
        newhoop = Gdx.audio.newSound(Gdx.files.internal("data/hoop.mp3"));
        
	}

	public static void dispose() {
		prefs.flush();
		HighScores.flush();
		
		texture.dispose();
		background.dispose();
		company.dispose();
		help.dispose();
		
		intro_music.dispose();
		start_music.dispose();
		loop_music.dispose();
		
		explode.dispose();
		point.dispose();
		button_click.dispose();
		powerup.dispose();
		newhoop.dispose();
		
		font.dispose();
		bgfont.dispose();
		hsfont.dispose();
	}

}
