package com.ratattack.game.model.system;

import static com.ratattack.game.model.ComponentMappers.positionMapper;
import static com.ratattack.game.model.ComponentMappers.spriteMapper;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.ratattack.game.GameSettings;
import com.ratattack.game.gamecontroller.GameController;
import com.ratattack.game.model.components.BalanceComponent;
import com.ratattack.game.model.components.BoundsComponent;
import com.ratattack.game.model.components.CircleBoundsComponent;
import com.ratattack.game.model.components.PositionComponent;
import com.ratattack.game.model.components.RectangleBoundsComponent;
import com.ratattack.game.model.components.SpriteComponent;

public class RenderSystem extends IteratingSystem {

    private static final Family renderFamily = Family.all(SpriteComponent.class, PositionComponent.class).get();
    private final SpriteBatch batch;
    private final ShapeRenderer renderer;
    int windowWidth = Gdx.graphics.getWidth();
    int windowHeight = Gdx.graphics.getHeight();
    private final GameController gameController = GameController.getInstance();


    public RenderSystem(SpriteBatch batch, ShapeRenderer renderer) {
        super(renderFamily);
        this.batch = batch;
        this.renderer = renderer;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //System.out.println(getEngine().getEntitiesFor(renderFamily));
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpriteComponent spriteComponent = spriteMapper.get(entity);
        PositionComponent positionComponent = positionMapper.get(entity);
        BoundsComponent bounds = entity.getComponent(CircleBoundsComponent.class);
        if (bounds == null) {
            bounds = entity.getComponent(RectangleBoundsComponent.class);
        }

        Texture texture = spriteComponent.sprite.getTexture();

        batch.begin();
        batch.draw(texture, positionComponent.x, positionComponent.y);
        batch.end();

        if (bounds == null) return;//If the entity does not have bounds, don´t render the bound or remove entity

        //Check if the entity has moved out of the screen
        //300 må endres til texture.getheigth() - men då kreves det at begge
        //sprites er av samme størrelse - altså begge bildene!
        Rectangle windowBounds = new Rectangle(0, -300, windowWidth, (windowHeight + (spriteComponent.sprite.getTexture().getHeight())*2));

        Circle circle = null;
        Rectangle rectangle = null;
        boolean isBoundsOutsideWindow = false;
        if (bounds.getBounds() instanceof Circle) {
            // The shape is a Circle
            circle = (Circle) bounds.getBounds();
            isBoundsOutsideWindow = !windowBounds.contains(circle);

        } else if (bounds.getBounds() instanceof Rectangle) {
            // The shape is a Rectangle
            rectangle = (Rectangle) bounds.getBounds();
            isBoundsOutsideWindow = !windowBounds.contains(rectangle);
        }

        if (isBoundsOutsideWindow) {
            // Gets the BalanceComponent for grandchildren and updates the balance
            // of the player when grandchild has crossed the whole field.
            if(entity.getComponent(BalanceComponent.class) != null) {
               int balance = entity.getComponent(BalanceComponent.class).getBalance();
               int oldBalance = gameController.getPlayer().getBalance();
               int newBalance = oldBalance + balance;
               gameController.getPlayer().setBalance(newBalance);
            }
            getEngine().removeEntity(entity);
        }

        //If debug is true, the bounds should be rendered
        if (GameSettings.debug) {
            renderer.begin(ShapeRenderer.ShapeType.Filled); // specify the shape type (Filled, Line, or Point)
            renderer.setColor(Color.RED); // set the color of the shape
            Shape2D shape = bounds.getBounds();
            if (shape instanceof Circle && circle != null) {
                renderer.circle(circle.x, circle.y, circle.radius); // draw the circle
            } else if (shape instanceof Rectangle && rectangle != null){
                renderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height); // draw the rectangle
            }
            renderer.end(); // end the ShapeRenderer
        }
    }
}
