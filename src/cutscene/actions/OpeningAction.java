// OpeningCutscene.java
package cutscene.actions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import cutscene.CutsceneAction;
import main.GameManager;
import ui.UI;

import static main.GameManager.screenHeight;

public class OpeningAction extends CutsceneAction {
    private String[] storyText;
    private float yPosition;
    private float scrollSpeed;
    private boolean isCutsceneActive;

    private boolean isTitleDisplayed;
    private int titleDisplayTime;
    private int titleDisplayCounter;

    @Override
    public void start() {
        storyText = new String[]{
                "In a distant and prosperous kingdom, peace was",
                "shattered when the royal crown, a symbol of ",
                "power and unity, was mysteriously stolen during",
                "a night of festivities.",
                " ",
                "Without the crown, the realm plunges into ",
                "uncertainty and fear, for it is believed that ",
                "whoever possesses it holds the right to rule." ,
                "Desperate, the king summons the greatest",
                "warriors but none prove worthy of the mission. ",
                " ",
                "Then, in an unexpected turn, the royal",
                "council chooses a humble yet brave young man",
                "pure of heart, to embark on the quest to ",
                "recover the lost crown.",
                " ",
               "Armed only with his determination and a small",
                "sword inherited from his father, he must face",
                "unimaginable challenges, cross unknown lands",
                "and confront powerful enemies to restore",
                "order and return hope to his people."
        };

        yPosition = screenHeight; // Altura da tela
        scrollSpeed = 4.5F; // Velocidade de rolagem do texto
        isCutsceneActive = true;
        isTitleDisplayed = false;
        titleDisplayTime = 300; // Tempo para exibir o título em frames
        titleDisplayCounter = 0;

        GameManager.playMusic(25);
    }

    public void update() {
        if (isCutsceneActive) {
            if (!isTitleDisplayed) {
                yPosition -= scrollSpeed;
                if (yPosition + storyText.length * 20 < 0) { // Ajuste a altura da linha conforme necessário
                    isTitleDisplayed = true;
                    yPosition = screenHeight / 2; // Posição para o título ficar no centro da tela
                }
            } else {
                titleDisplayCounter++;
                if (titleDisplayCounter >= titleDisplayTime) {
                    endCutscene();
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (isCutsceneActive) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, 512, screenHeight); // Dimensões da tela 512x384
            UI.getInstance().setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12); // Ajuste o tamanho da fonte conforme necessário

            if (!isTitleDisplayed) {
                int y = (int) yPosition;
                for (String line : storyText) {
                    g2.drawString(line, 15, y); // Ajuste a posição horizontal conforme necessário
                    y += 20; // Ajuste a altura da linha conforme necessário
                }
            } else {
                UI.getInstance().setColorAndFont(g2, Color.WHITE, Font.PLAIN, 22); // Tamanho da fonte do título
                g2.drawString("The Chronicles of Bahamut", 20, yPosition); // Posição do título no centro da tela
            }
        }
    }

    @Override
    public boolean isFinished() {
        return !isCutsceneActive;
    }

    private void endCutscene() {
        isCutsceneActive = false;
        // Aqui você pode iniciar o jogo ou a próxima parte
    }

    public boolean isCutsceneActive() {
        return isCutsceneActive;
    }
}

