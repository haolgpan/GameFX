package com.example.gamefx.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.ui.ProgressBar;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class PowerUp extends Component {

    private ProgressBar progressBar;

    public PowerUp() {
        progressBar = new ProgressBar();
        progressBar.setWidth(200);
        progressBar.setHeight(25);
        progressBar.setMinValue(0);
        progressBar.setMaxValue(100);
        progressBar.setCurrentValue(0);
    }

    public void activatePowerUp() {
        // code to activate the power-up
        progressBar.setCurrentValue(100);
    }

    public void update(double tpf) {
        if (progressBar.getCurrentValue() > 0) {
            progressBar.setCurrentValue(progressBar.getCurrentValue() - 1);
        }
    }

    @Override
    public void onAdded() {
        progressBar.setTranslateX((getAppWidth()/2) + 110);
        progressBar.setTranslateY(20);
        FXGL.getGameScene().addUINode(progressBar);
    }

    @Override
    public void onRemoved() {
        FXGL.getGameScene().removeUINode(progressBar);
    }
}

