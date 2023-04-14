package com.ratattack.game.model.shootingStrategy;

import static com.ratattack.game.model.ComponentMappers.circleBoundsMapper;
import static com.ratattack.game.model.ComponentMappers.positionMapper;
import static com.ratattack.game.model.ComponentMappers.spriteMapper;
import static com.ratattack.game.model.ComponentMappers.strengthMapper;
import static com.ratattack.game.model.ComponentMappers.velocityMapper;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ratattack.game.gamecontroller.GameController;
import com.ratattack.game.model.components.CircleBoundsComponent;
import com.ratattack.game.model.components.PositionComponent;
import com.ratattack.game.model.components.SpriteComponent;
import com.ratattack.game.model.components.StrengthComponent;
import com.ratattack.game.model.components.VelocityComponent;

public class BigBulletStrategy implements ShootingStrategy{

    Texture bulletTexture = new Texture("BIGbullet.png");
    @Override
    public void shoot(int x, int y) {
        Entity bullet = GameController.getInstance().getAshleyWorld().createBullet();

        PositionComponent position = positionMapper.get(bullet);
        VelocityComponent velocity = velocityMapper.get(bullet);
        SpriteComponent sprite = spriteMapper.get(bullet);
        StrengthComponent strength = strengthMapper.get(bullet);
        CircleBoundsComponent bounds = circleBoundsMapper.get(bullet);
        //PowerUpComponent powerUp = powerUpMapper.get(bullet);

        position.x = x;
        position.y = y;

        velocity.x = 0;
        velocity.y = 10;

        sprite.sprite = new Sprite(bulletTexture);

        strength.strength = 20;

        bounds.setCenter(x, y);
        bounds.setSize(bulletTexture.getWidth()/2, bulletTexture.getHeight()/2);
    }
}
