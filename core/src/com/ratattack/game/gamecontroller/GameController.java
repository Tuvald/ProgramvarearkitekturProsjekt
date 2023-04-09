package com.ratattack.game.gamecontroller;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ratattack.game.RatAttack;
import com.ratattack.game.model.Field;
import com.ratattack.game.model.GameWorld;
import com.ratattack.game.model.system.MovementSystem;
import com.ratattack.game.model.system.RenderSystem;
import com.ratattack.game.model.system.UserSystem;
import com.ratattack.game.screens.OptionScreen;
import com.ratattack.game.screens.ScreenFactory;
import com.ratattack.game.screens.TutorialScreen;

public class GameController {

    private static final GameController instance = new GameController();
    public Field field;
    private Boolean paused = true;

    SpriteBatch batch;

    // Ashley
    private RatAttack game;

    public static GameWorld ashleyWorld;
    public static PooledEngine engine;

    private GameController() {
        batch = new SpriteBatch();
        setUpAshley();
    }

    public static GameController getInstance() {
        return instance;
    }

    public void setStartScreen() {
        setGameScreen(); // Endre når man skal merge, skal være MenuScreen
    }

    private void setGameScreen() {
        try {
            game.setScreen(ScreenFactory.getScreen("GAME"));
        } catch (Exception e) {
            System.out.println("No game instance set for the game controller"); //Denne slår ut, og jeg skjønner ikke helt hvorfor. Men alt funker allikevel
        }
    }

    private void setMenuScreen() {
        try {
            game.setScreen(ScreenFactory.getScreen("MENU"));
        } catch (Exception e) {
            System.out.println("No game instance set for the game controller");
        }
    }

    private void setOptionsScreen() {
        OptionScreen optionScreen = new OptionScreen();
        game.setScreen(optionScreen);
    }

    private void setTutorialScreen() {
        TutorialScreen tutorialScreen = new TutorialScreen(this);
        game.setScreen(tutorialScreen);
    }

    private void setUpAshley() {
        engine = new PooledEngine();
        ashleyWorld = new GameWorld(engine);

        //Add systems to engine
        addSystems(engine);

        //Add entities
        addEntities();

    }

    public void addSystems(PooledEngine engine) {
        engine.addSystem(new UserSystem());
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new MovementSystem());
    }

    public void addEntities() {
        //Create Rat
        ashleyWorld.createRat();

    }

    public void update() {
        if (!paused) {
            engine.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void setUpGame() {
        try {
            field = new Field();
        }
        catch (Exception e) {
            System.out.println("Error with field creation");
        }
    }

    public void play() {
        paused = false;
    }

    public void pause() {
        paused = true;
    }

    public PooledEngine getEngine() {
        return engine;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setGame(RatAttack game) {
        this.game = game;
    }
}
