package commands;

import server.CollectionManager;
import tale.Room;

import java.util.Hashtable;

/**
 * Класс переопределяет метод @code execute () для удаления элемента из коллекции по заданному ключу.
 */
public class RemoveCommand extends AbstractCommand {

    public RemoveCommand(CollectionManager manager) {
        super(manager);
        setDescription("Удаляет элемент из коллекции по заданному ключу.\nremove {String key}");
    }

    @Override
    public synchronized String execute(String key) {
        key = key.replaceAll("[{}\"]","").trim();
        Hashtable<String, Room> collection = getManager().getCollection();
        if (!collection.isEmpty()) {
                if (collection.containsKey(key)) {
                    collection.remove(key);
                    getManager().save();
                    return "Элемент успешно удален.";
                } else return "Такого элемента нет в коллекции.";
        } else return "Ключ не с чем сравнивать. Коллекция пуста.";
    }
}