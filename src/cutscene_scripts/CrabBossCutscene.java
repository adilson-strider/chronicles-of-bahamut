package cutscene_scripts;

import cutscene.*;
import cutscene.actions.*;
import entity.Enemy;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import ui.UI;

import java.awt.*;

public class CrabBossCutscene implements IState {

    private final CutsceneManager cutsceneManager = new CutsceneManager();
    CutsceneAction currentEvent;

    public CrabBossCutscene() {
        // Adicione eventos à cutscene
        cutsceneManager.startCutscene();

        String[] dialogue = {
                "Você ousa desafiar o guardião do templo?",
                "Prepare-se para enfrentar o meu poder!"
        };

        cutsceneManager.startCutscene();
        cutsceneManager.addEvent(new DialogueAction(dialogue));
        cutsceneManager.addEvent(new BattleAction(true, 0));
    }

    @Override
    public void update() {
        if (cutsceneManager.isCutsceneActive()) {
            cutsceneManager.update();

        } else {
            StateMachine.getInstance().change("InGame");
        }

        // Se o evento atual é um diálogo, atualize o diálogo exibido
        currentEvent = cutsceneManager.getCurrentEvent();
        if (currentEvent instanceof DialogueAction) {
            UI.getInstance().currentDialogue = ((DialogueAction) currentEvent).getCurrentDialogue();
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        StateMachine.getInstance().getmStates().get("InGame").draw(g2);

        if (currentEvent != null) {
            currentEvent.draw(g2);
        }
    }

    @Override
    public void onEnter() {
        // Inicializar a cutscene quando entrar no estado
        cutsceneManager.startCutscene();
    }

    @Override
    public void onExit() {
        // Limpar o estado da cutscene quando sair do estado
        GameManager.canPass = true;
    }

    @Override
    public String getName() {
        return "CrabBoss";
    }
}
