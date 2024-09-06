package cutscene.actions;

import cutscene.CutsceneAction;
import main.GameManager;
import ui.UI;

import java.awt.*;
import java.util.List;

public class CreditsAction extends CutsceneAction {
    private List<String> credits;
    private String thankYouMessage = "Obrigado por jogar!";
    private float currentY;
    private boolean finished = false;
    private float scrollSpeed = 0.5f; // Velocidade do movimento do texto para cima (fracionária)
    private boolean thankYouDisplayed = false;
    private int thankYouDisplayTime = 300; // Tempo em frames para exibir a mensagem de agradecimento
    private int thankYouCounter = 0;
    private int creditsHeight;

    public CreditsAction(List<String> credits) {
        this.credits = credits;
        this.currentY = GameManager.screenHeight; // Começa na parte inferior da tela
        this.creditsHeight = credits.size() * 30; // Altura total dos créditos (assumindo 30 pixels por linha de texto)
    }

    @Override
    public void start() {
        currentY = GameManager.screenHeight;
        finished = false;
        thankYouDisplayed = false;
        thankYouCounter = 0;
    }

    @Override
    public void update() {
        if (!thankYouDisplayed) {
            currentY -= scrollSpeed;

            // Verifica se todos os créditos passaram pela tela
            if (currentY < -creditsHeight) {
                thankYouDisplayed = true;
            }
        } else {
            thankYouCounter++;
            if (thankYouCounter > thankYouDisplayTime) {
                finished = true;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        // Tela preta
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameManager.screenWidth, GameManager.screenHeight);

        UI.getInstance().setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);

        if (!thankYouDisplayed) {
            float y = currentY;
            for (String line : credits) {
                int x = (GameManager.screenWidth - g2.getFontMetrics().stringWidth(line)) / 2; // Centraliza o texto horizontalmente
                g2.drawString(line, x, (int) y);
                y += 30; // Incrementa a posição vertical para a próxima linha
            }
        } else {
            // Desenha a mensagem de agradecimento no centro da tela
            int x = (GameManager.screenWidth - g2.getFontMetrics().stringWidth(thankYouMessage)) / 2;
            int y = GameManager.screenHeight / 2;
            g2.drawString(thankYouMessage, x, y);
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
