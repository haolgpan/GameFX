package com.example.gamefx.Components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Enemies extends Component {
    private final Point2D direction = new Point2D(FXGLMath.random(-1D, 1D), FXGLMath.random(-1D, 1D));

    @Override
    public void onUpdate(double tpf) {
        entity.translate(direction.multiply(4));
        checkForBounds();
    }

    private void checkForBounds() {
        if (entity.getX() < 0) {
            remove();
        }
        if (entity.getX() >= getAppWidth()) {
            remove();
        }
        if (entity.getY() < 0) {
            remove();
        }
        if (entity.getY() >= getAppHeight()) {
            remove();
        }
    }

    public void shoot() {
        spawn("bullet", new SpawnData(
                getEntity().getPosition().getX() + 0,
                getEntity().getPosition().getY() + 0)
                .put("direction", direction));
    }

    public void remove() {
        entity.removeFromWorld();
    }
}
