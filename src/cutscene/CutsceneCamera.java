package cutscene;

import entity.Player;
import main.Camera;

import java.awt.*;

import static java.lang.Math.clamp;

public class CutsceneCamera {
    private int x, y;

    public CutsceneCamera(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        // Atualizar lógica da câmera se necessário
    }

    public boolean moveTo(Point target, int speed) {
        int dx = target.x - x;
        int dy = target.y - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            x = target.x;
            y = target.y;
            return false;
        }

        x += (int) (speed * dx / distance);
        y += (int) (speed * dy / distance);

        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
