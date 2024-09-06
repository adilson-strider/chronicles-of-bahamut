package cutscene.actions;

import cutscene.CutsceneAction;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class DialogueAction extends CutsceneAction {
    private String[] dialogue;
    private int index = 0;
    private boolean finished = false;
    private int charIndex = 0;
    private String currentDialogue = "";
    private String combinedText = "";

    public DialogueAction(String[] dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public void start() {
        index = 0;
        charIndex = 0;
        currentDialogue = "";
        combinedText = "";
    }

    @Override
    public void update() {
        if (!finished) {
            if (index < dialogue.length) {
                char[] chars = dialogue[index].toCharArray();

                if (charIndex < chars.length) {
                    String s = String.valueOf(chars[charIndex]);
                    combinedText += s;
                    currentDialogue = combinedText;
                    GameManager.playSFX(21);
                    charIndex++;
                } else {
                    if (InputManager.getInstance().isKeyDown(KeyEvent.VK_SPACE)) {
                        charIndex = 0;
                        combinedText = "";
                        index++;
                    }

                    if (InputManager.getInstance().isKeyDown(KeyEvent.VK_SPACE) && index >= dialogue.length) {
                        finished = true;
                    }
                }
            } else {
                if (InputManager.getInstance().isKeyDown(KeyEvent.VK_SPACE) && index >= dialogue.length) {
                    finished = true;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int margin = 20;
        int lineSpacing = 10; // Espaçamento adicional entre as linhas

        // Coordenadas e dimensões do painel de diálogo
        int panelX = UI.getInstance().getDialoguePanel().getFrameX();
        int panelY = UI.getInstance().getDialoguePanel().getFrameY();
        int panelWidth = UI.getInstance().getDialoguePanel().getFrameWidth();
        int panelHeight = UI.getInstance().getDialoguePanel().getFrameHeight();

        // Margem interna e área de texto
        int textX = panelX + margin;
        int textY = panelY + margin;
        int textWidth = panelWidth - 2 * margin;
        int textHeight = panelHeight - 2 * margin;

        // Desenhar o painel de diálogo
        UI.getInstance().getDialoguePanel().drawPanel(g2);

        // Definir a fonte e cor do diálogo
        UI.getInstance().setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);

        List<String> lines = wrapText(g2, currentDialogue, textWidth);

        int y = textY + 20;
        for (String line : lines) {
            g2.drawString(line, textX, y);
            y += g2.getFontMetrics().getHeight() + lineSpacing; // Adicionar espaçamento entre as linhas
            if (y > textY + textHeight) {
                break;
            }
        }
    }

    private List<String> wrapText(Graphics2D g2, String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (line.length() == 0) {
                line.append(word);
            } else {
                String testLine = line.toString() + " " + word;
                int lineWidth = g2.getFontMetrics().stringWidth(testLine);
                if (lineWidth < maxWidth) {
                    line.append(" ").append(word);
                } else {
                    lines.add(line.toString());
                    line = new StringBuilder(word);
                }
            }
        }

        if (line.length() > 0) {
            lines.add(line.toString());
        }

        return lines;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public String getCurrentDialogue() {
        return currentDialogue;
    }
}
