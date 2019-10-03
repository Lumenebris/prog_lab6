package commands;

import server.CollectionManager;
import tale.Room;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.*;

/**
 * Класс переопределяет метод @code execute() для отображения всех элементов из коллекции.
 */
public class ShowCommand extends AbstractCommand {

    public ShowCommand(CollectionManager manager) {
        super(manager);
        setDescription("Выводит все элементы коллекции.");
    }

    @Override
    public synchronized String execute() {
        Hashtable<String, Room> collection = getManager().getCollection();
        StringBuilder result = new StringBuilder();
        if (!collection.isEmpty()) {
            return collection.entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(Map.Entry::getValue)
                    .map(Room::toString)
                    .collect(Collectors.joining("\n"));
        } else return "Коллекция пуста.";
    }
}
