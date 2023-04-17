package com.ratattack.game.view.screens;

import com.badlogic.gdx.Screen;
import com.ratattack.game.DataHolderClass;
import com.ratattack.game.FirebaseInterface;
import com.ratattack.game.gamecontroller.GameController;

public class ScreenFactory {

    static FirebaseInterface _FBIC =  GameController.getInstance().getFirebaseInterface();
    static DataHolderClass _dataHolderClass = GameController.getInstance().getDataHolderClass();

    public static Screen getScreen(String screenType) {
        switch (screenType) {
            case "MENU":
                return new MenuScreen(_FBIC, _dataHolderClass);
            case "NAME":
                return new NameScreen();
            case "GAME":
                return new GameScreen();
            case "TUTORIAL":
                return new TutorialScreen();
            case "HIGHSCORE":
                return new HighscoreScreen(_FBIC, _dataHolderClass);
            default:
                return null;
        }
    }
}
