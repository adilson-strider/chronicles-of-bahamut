package tools;

import entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FixedSizeList<T> {
    private List<T> list;
    private final int maxSize;

    public FixedSizeList(int maxSize) {
        this.maxSize = maxSize;
        this.list = new ArrayList<>(Collections.nCopies(maxSize, (T) null));
    }

    // Adicionar item em uma posição específica
    public void add(T item, int index) {
        if (index >= 0 && index < list.size()) {
            list.set(index, item);
        } else {
            throw new IndexOutOfBoundsException("Índice fora do limite da lista.");
        }
    }

    // Marcar item como removido
    public void remove(int index) {
        if (index >= 0 && index < list.size()) {
            list.set(index, null); // Substitui o item por null
        } else {
            throw new IndexOutOfBoundsException("Índice fora do limite da lista.");
        }
    }

    public void sortItems() {
        ArrayList<T> nonNullItems = new ArrayList<>();
        for (T item : list) {
            if (item != null) {
                nonNullItems.add(item);
            }
        }

        Collections.sort(nonNullItems, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                // Define sua lógica de comparação aqui. Exemplo:
                return o1.toString().compareTo(o2.toString());
            }
        });

        for (int i = 0; i < maxSize; i++) {
            if (i < nonNullItems.size()) {
                list.set(i, nonNullItems.get(i));
            } else {
                list.set(i, null);
            }
        }
    }

    public int countItems() {
        int count = 0;
        for (T item : list) {
            if (item != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return list.toString();
    }

    // Readicionar item no primeiro índice disponível (null)
    public boolean readd(T item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                list.set(i, item);
                return true; // Retorna true indicando que o item foi adicionado com sucesso
            }
        }
        return false; // Retorna false indicando que não havia espaço disponível para adicionar o item
    }

    // Obter item
    public Entity get(int index) {
        if (index >= 0 && index < list.size()) {
            return (Entity) list.get(index);
        } else {
            throw new IndexOutOfBoundsException("Índice fora do limite da lista.");
        }
    }

    public void clear() {
        for (int i = 0; i < maxSize; i++) {
            list.set(i, null);
        }
    }


    // Tamanho da lista (inclui itens removidos)
    public int size() {
        return list.size();
    }

    // Exibir todos os itens
    public void display() {
        for (T item : list) {
            System.out.println(item);
        }
    }

    // Buscar item pelo nome

}
