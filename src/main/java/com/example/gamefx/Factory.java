package com.example.gamefx;

import com.example.gamefx.Components.Enemies;
import com.example.gamefx.Components.Enterprise;
import com.almasb.fxgl.dsl.components.AutoRotationComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.example.gamefx.Components.Life;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

public class Factory implements EntityFactory {

    /**
     * Types of objects we are going to use in our game.
     */
    public enum EntityType {
        BACKGROUND, CENTER, ENTERPRISE, ENEMIES, BULLET, BULLETEX, BULLETEX2, LIFE
    }

    @Spawns("background")
    public Entity spawnBackground(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.BACKGROUND)
                .view(texture("space.jpg"))
                .with(new IrremovableComponent())
                .zIndex(-100)
                .build();
    }

    @Spawns("center")
    public Entity spawnCenter(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.CENTER)
                .at(450,200)
                .collidable()
                .viewWithBBox(texture("center.png",350,350))
                .with(new IrremovableComponent())
                .zIndex(-99)
                .build();
    }

    @Spawns("enterprise")
    public Entity newEnterprise(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.ENTERPRISE)
                .viewWithBBox(texture("enterprise.png", 50, 50))
                .collidable()
                .with((new AutoRotationComponent()).withSmoothing())
                .with(new Enterprise())
                .build();
    }

    @Spawns("enemies")
    public Entity newEnemies(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.ENEMIES)
                .viewWithBBox(texture("enemies.png", 50, 50))
                .with((new AutoRotationComponent()).withSmoothing())
                .with(new Enemies())
                .collidable()
                .build();
    }
    @Spawns("life")
    public Entity newLife(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.LIFE)
                .viewWithBBox(texture("pill_green.png", 15, 15))
                .collidable()
                .with((new AutoRotationComponent()).withSmoothing())
                .with(new Life())
                .collidable()
                .build();
    }

    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.BULLET)
                .viewWithBBox(texture("laserred1.png", 20, 20))
                .collidable()
                .with(new ProjectileComponent(data.get("direction"), 340), new OffscreenCleanComponent())
                .build();
    }
    @Spawns("bulletEX")
    public Entity newBulletEX(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.BULLETEX)
                .viewWithBBox(texture("laserRed02.png", 30, 15))
                .collidable()
                .with(new ProjectileComponent(data.get("direction"), 340), new OffscreenCleanComponent())
                .build();
    }
    @Spawns("bulletEX2")
    public Entity newBulletEX2(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.BULLETEX2)
                .viewWithBBox(texture("laserRed04.png", 30, 15))
                .collidable()
                .with(new ProjectileComponent(data.get("direction"), 500), new OffscreenCleanComponent())
                .build();
    }
}
