package commands;

import com.google.gson.JsonSyntaxException;
import server.CollectionManager;
import tale.Room;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Класс переопределяет метод @code execute () для удаления элемента из коллекции.
 */
public class RemoveAllCommand extends AbstractCommand {

    public RemoveAllCommand(CollectionManager manager) {
        super(manager);
        setDescription("Удалить из коллекции все элементы, эквивалентные данному.\nremove_all {elementInJSON}");
    }

    @Override
    public synchronized String execute(Room room) {
        Hashtable<String, Room> collection = getManager().getCollection();
        if (collection.size() != 0) {
            try {
                ArrayList<String> s = new ArrayList<>();
                collection.forEach((k, v) -> {
                    if (v.getFloor() == room.getFloor() &&
                        v.getNumber() == room.getNumber() &&
                        v.getShape().equals(room.getShape()) &&
                        v.getWall().getMaterial().equals(room.getWall().getMaterial()) &&
                        v.getX() == room.getX() &&
                        v.getY() == room.getY() &&
                        v.getAge() == room.getAge()) {
                        s.add(room.getShape()); }
                });
                if (s.size() == 0) return ("Совпадений с заданным обьектом не найдено");
                else {
                for (int i = 0;i < s.size(); i++) collection.remove(s.get(i));
                getManager().save();
                return "Из коллекции удалено " + s.size() + " элементов."; }
            } catch (JsonSyntaxException ex) {
                return "Синтаксическая ошибка JSON. Не удалось удалить элемент.";
            }
        }
        else return "Элемент не с чем сравнивать. Коллекция пуста.";
    }
}
