package cutscene_scripts;

import cutscene.CutsceneAction;
import cutscene.CutsceneManager;
import cutscene.actions.BattleAction;
import cutscene.actions.DialogueAction;
import cutscene.actions.MoveCharacterAction;
import cutscene.actions.OpeningAction;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import ui.UI;

import java.awt.*;

public class NotPassCutscene implements IState {

    private final CutsceneManager cutsceneManager = new CutsceneManager();
    CutsceneAction currentEvent;

    public NotPassCutscene(Point position) {
        // Adicione eventos à cutscene
        cutsceneManager.startCutscene();

        cutsceneManager.addEvent(new MoveCharacterAction(GameManager.getPlayer(),
                new Point(GameManager.tileSize * position.x, GameManager.tileSize * position.y), 2));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Não pode retornar!"
        }));
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

    }

    @Override
    public String getName() {
        return "";
    }
}
