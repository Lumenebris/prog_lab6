package commands;

import server.CollectionManager;
import tale.Room;

import java.util.Hashtable;

/**
 * Класс переопределяет метод execute () для добавления элемента в коллекцию по заданному ключу.
 */
public class InsertCommand extends AbstractCommand {
    public InsertCommand(CollectionManager manager) {
        super(manager);
        setDescription("Добавляет элемент в коллекцию по заданному ключу.\ninsert {String key} {elementInJSON}");
    }

    @Override
    public synchronized String execute(String key, Room room) {
        Hashtable<String, Room> collection = getManager().getCollection();
        if ((room != null) && (room.getFloor() != 0) && (room.getNumber() != 0) && (room.getWall() != null) && (room.getWall().getMaterial() != null)) {
            if (!collection.containsKey(key)) {
                if (key.equals(room.getShape())) {
                    collection.put(room.getShape(), room);
                    getManager().save();
                    return "Элемент успешно добавлен.";
                } else return "Ключ должен совпадать со значением shape.";
            } else return "Такой ключ уже существует.";
        } else return "Объект неинициализирован.";
    }
}
