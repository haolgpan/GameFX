package com.example.gamefx;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.example.gamefx.Components.Enterprise;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class MainGame extends GameApplication {
    /**
     * Reference to the factory which will defines how all the types must be created.
     */
    private final Factory factory = new Factory();

    /**
     * Player object we are going to use to provide to the factory so it can start a bullet from the player center
     * or multiple bullets.
     */
    private Entity player;
    private int power;

    private static int screenWidth;
    private static int screenHeight;

    /**
     * Main entry point where the application starts.
     *
     * @param args Start-up arguments
     */
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screenSize.getWidth();
        screenHeight = (int) screenSize.getHeight();
        launch(args);
    }

    /**
     * General game settings.
     *
     * @param settings The settings of the game which can be further extended here.
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(700);
        settings.setWidth(1200);
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(false);
        settings.setTitle("Dead Space but Family Friendly");
        settings.setVersion("0.1");
    }

    /**
     * Input configuration, here you configure all the input events like key presses, mouse clicks, etc.
     */
    @Override
    protected void initInput() {
        onKey(KeyCode.LEFT, "left", () -> this.player.getComponent(Enterprise.class).left());
        onKey(KeyCode.RIGHT, "right", () -> this.player.getComponent(Enterprise.class).right());
        onKey(KeyCode.UP, "up", () -> this.player.getComponent(Enterprise.class).up());
        onKey(KeyCode.DOWN, "down", () -> this.player.getComponent(Enterprise.class).down());
        onKeyDown(KeyCode.SPACE, "Bullet", () -> {
            this.player.getComponent(Enterprise.class).shoot(power);
            if(power < 10) play("sfx_laser1.wav");
            else if(power >= 10 && power <20) {
                play("sfx_laser1.wav");
                play("sfx_laser2.wav");
            } else if (power >= 20) {
                play("sfx_laser1.wav");
                play("sfx_laser2.wav");
                play("sfx_laser1.wav");
                play("sfx_laser2.wav");
            }
        });
    }

    /**
     * General game variables. Show score, lives and powerup.
     *
     * @param vars The variables of the game which can be further extended here.
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("lives", 5);
        vars.put("killcount", 0);
    }

    /**
     * Initialization of the game by providing the {@link EntityFactory}.
     */
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(this.factory);
        spawn("background", new SpawnData(0, 0).put("width", getAppWidth())
                .put("height", getAppHeight()));
        spawn("center", new SpawnData(getAppCenter()));
        getAudioPlayer().loopMusic(getAssetLoader().loadMusic("background.mp3"));


        // Add the player
        this.player = spawn("enterprise", 0, 0);
    }

    /**
     * Initialization of the collision handlers.
     */
    @Override
    protected void initPhysics() {
        onCollisionBegin(Factory.EntityType.ENTERPRISE, Factory.EntityType.CENTER, (enterprise, center) -> {
            this.player.getComponent(Enterprise.class).die();
            set("killcount",0);
            play("sfx_lose.wav");
        });
        onCollisionBegin(Factory.EntityType.ENTERPRISE, Factory.EntityType.ENEMIES, (enemy, enemies) -> {
            this.player.getComponent(Enterprise.class).die();
            set("killcount",0);
            play("sfx_lose.wav");
        });
        onCollisionBegin(Factory.EntityType.BULLET, Factory.EntityType.ENEMIES, (bullet, enemies) -> {
            inc("killcount",1);
            inc("score", 1);
            bullet.removeFromWorld();
            enemies.removeFromWorld();
            spawn("explosion",enemies.getX(),enemies.getY());
            if (FXGLMath.randomBoolean()) {
                spawn("life", getAppWidth() / 2, getAppHeight() / 2);
            }
            play("explosion.wav");
        });
        onCollision(Factory.EntityType.ENTERPRISE, Factory.EntityType.LIFE,(enterprise, life) ->{
            inc("lives",1);
            life.removeFromWorld();
        });
    }

    /**
     * Configuration of the user interface.
     */
    @Override
    protected void initUI() {
        Text scoreLabel = getUIFactoryService().newText("Score", Color.WHITE, 22);
        Text scoreValue = getUIFactoryService().newText("", Color.GOLD, 22);
        Text livesLabel = getUIFactoryService().newText("Lives", Color.WHITE, 22);
        Text livesValue = getUIFactoryService().newText("", Color.RED, 22);
        Text killcountLabel = getUIFactoryService().newText("PowerUp", Color.WHITE, 22);
        Text killcountValue = getUIFactoryService().newText("", Color.PURPLE, 22);

        killcountLabel.setTranslateX(getAppWidth()/2);
        killcountLabel.setTranslateY(20);

        killcountValue.setTranslateX((getAppWidth()/2) + 110);
        killcountValue.setTranslateY(20);

        scoreLabel.setTranslateX(getAppWidth() - 150);
        scoreLabel.setTranslateY(20);

        scoreValue.setTranslateX(getAppWidth() - 80);
        scoreValue.setTranslateY(20);

        livesLabel.setTranslateX(20);
        livesLabel.setTranslateY(20);

        livesValue.setTranslateX(90);
        livesValue.setTranslateY(20);

        scoreValue.textProperty().bind(getWorldProperties().intProperty("score").asString());
        livesValue.textProperty().bind(getWorldProperties().intProperty("lives").asString());
        killcountValue.textProperty().bind(getWorldProperties().intProperty("killcount").asString());

        getGameScene().addUINodes(scoreLabel, scoreValue, livesLabel, livesValue, killcountLabel, killcountValue);
    }

    /**
     * Constant update.
     */
    @Override
    protected void onUpdate(double tpf) {
        power = geti("killcount");
        if(geti("score") < 10) {
            if (getGameWorld().getEntitiesByType(Factory.EntityType.ENEMIES).size() < 5) {
                spawn("enemies", getAppWidth() / 2, getAppHeight() / 2);
            }
        } else if (geti("score") >= 10 && geti("score")  <= 30) {
            if (getGameWorld().getEntitiesByType(Factory.EntityType.ENEMIES).size() < 10) {
                spawn("enemies", getAppWidth() / 2, getAppHeight() / 2);
            }
        } else if (geti("score")  > 30) {
            if (getGameWorld().getEntitiesByType(Factory.EntityType.ENEMIES).size() < 40) {
                spawn("enemies", getAppWidth() / 2, getAppHeight() / 2);
            }
        }
    }
}
