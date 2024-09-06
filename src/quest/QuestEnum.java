package quest;

public enum QuestEnum {
    HELP_BLACKSMITH("Ajudar o Ferreiro", "Ajude o ferreiro a encontrar seu martelo perdido."),
    FIND_KITTEN("Encontrar o Gatinho", "Ajude Helena a encontrar seu gatinho perdido."),
    DEFEAT_DRAGON("Derrotar o Dragão", "Enfrente e derrote o Bahamut para salvar o reino.");

    private final String name;
    private final String description;

    QuestEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

