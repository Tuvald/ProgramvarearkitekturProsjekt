package com.ratattack.game.model.ecs.system;

import static com.ratattack.game.model.ecs.ComponentMappers.healthMapper;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;
import com.ratattack.game.GameSettings;
import com.ratattack.game.gamecontroller.GameController;
import com.ratattack.game.model.ecs.components.BalanceComponent;
import com.ratattack.game.model.ecs.components.BoundsComponent;
import com.ratattack.game.model.ecs.components.HealthComponent;
import com.ratattack.game.model.ecs.components.BulletEffectComponent;
import com.ratattack.game.model.ecs.components.PositionComponent;
import com.ratattack.game.model.ecs.components.RectangleBoundsComponent;
import com.ratattack.game.model.ecs.components.SpriteComponent;
import com.ratattack.game.model.ecs.components.VelocityComponent;

public class SpawnSystem extends IteratingSystem {
    private long lastRatSpawnTime;
    private long lastGrandChildSpawnTime;
    private long ratSpawnInterval;
    private final long grandChildSpawnInterval;

    private final GameController gameController = GameController.getInstance();

    public SpawnSystem() {
        super(Family.all(SpriteComponent.class).get());
        this.ratSpawnInterval = GameSettings.ratSpawnrate;
        this.grandChildSpawnInterval = GameSettings.startGrandChildSpawnrate;
        lastRatSpawnTime = TimeUtils.millis();
        lastGrandChildSpawnTime = TimeUtils.millis();
    }

    @Override
    public void update(float deltaTime) {
        ratSpawnInterval = GameSettings.ratSpawnrate;
        long currentTime = TimeUtils.millis();
        if (currentTime - lastRatSpawnTime >= ratSpawnInterval) {
            spawnRat();
            lastRatSpawnTime = currentTime;
        }
        if (currentTime - lastGrandChildSpawnTime >= grandChildSpawnInterval) {
            spawnGrandChild();
            lastGrandChildSpawnTime = currentTime;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

    private void spawnRat() {

        Entity rat = GameController.getInstance().getAshleyWorld().createRat();

        Texture texture = new Texture("rat.png");
        rat.getComponent(SpriteComponent.class).sprite = new Sprite(texture);

        rat.getComponent(BulletEffectComponent.class).setEffect("");

        //POSITION:

        PositionComponent position = rat.getComponent(PositionComponent.class);
        position.y = 1500;

        int indexInLaneDividers = (int) (Math.random()*gameController.field.laneDividers.size());
        position.x = gameController.field.laneDividers.get(indexInLaneDividers) + gameController.field.getLaneWidth()/2 - texture.getWidth()/2;

        //VELOCITY:

        VelocityComponent velocity = rat.getComponent(VelocityComponent.class);
        velocity.x = 0;
        velocity.y = GameSettings.startSpeedRat;

        BoundsComponent bounds = rat.getComponent(RectangleBoundsComponent.class);
        bounds.setSize(2*(texture.getWidth()/3), (texture.getHeight()));
        bounds.setCenter(position.x, position.y);
        rat.getComponent(HealthComponent.class).setHealth(GameSettings.ratStartHealth);
    }


    private void spawnGrandChild() {
        Entity grandChildEntity = GameController.getInstance().getAshleyWorld().createGrandChild();

        //Add position, velocity, health, balance and sprite
        Texture texture = new Texture("grandchild.png");
        grandChildEntity.getComponent(SpriteComponent.class).sprite = new Sprite(texture);


        //POSITION
        PositionComponent position = grandChildEntity.getComponent(PositionComponent.class);

        int indexInLaneDividers = (int) (Math.random()*gameController.field.laneDividers.size());
        position.x = gameController.field.laneDividers.get(indexInLaneDividers) + gameController.field.getLaneWidth()/2 - texture.getWidth()/2;
        position.y = 1500;


        //VELOCITY
        VelocityComponent velocity = grandChildEntity.getComponent(VelocityComponent.class);
        velocity.x = 0;
        velocity.y = GameSettings.startSpeedGrandchild;

        BoundsComponent bounds = grandChildEntity.getComponent(RectangleBoundsComponent.class);
        bounds.setSize(2*(texture.getWidth()/3), (texture.getHeight()));
        bounds.setCenter(position.x, position.y);

        grandChildEntity.getComponent(BalanceComponent.class).setBalance(50);

        HealthComponent health = healthMapper.get(grandChildEntity);
        health.setHealth(GameSettings.grandChildStartHealth);
    }
}
