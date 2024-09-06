package cutscene.actions;

import cutscene.CutsceneAction;
import main.GameManager;

import java.awt.*;

public class BattleAction extends CutsceneAction {
    private boolean finished = false;
    private boolean isBoss = false;
    private int index;

    public BattleAction(boolean isBoss, int index){
        this.isBoss = isBoss;
        this.index = index;
    }

    @Override
    public void start() {
        // Iniciar a batalha aqui
        GameManager.startBattle(isBoss, index);
        finished = true;
    }

    @Override
    public void update() {
        // A batalha é iniciada instantaneamente, não há necessidade de lógica adicional aqui
    }

    @Override
    public void draw(Graphics2D g2) {
        // Pode ser deixado vazio ou usado para exibir algum efeito visual
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
