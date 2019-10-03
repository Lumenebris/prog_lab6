package commands;

import server.CollectionManager;
import tale.Room;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Класс @code RemoveLowerCommand переопределяет метод @code execute (), чтобы удалить все элементы из коллекции, ключ которых меньше заданного.
 */
public class RemoveLowerCommand extends AbstractCommand {
    public RemoveLowerCommand(CollectionManager manager) {
        super(manager);
        setDescription("Удаляет все элементы из коллекции, ключ которых меньше заданного.\nremove_lower {String key}");
    }

    @Override
    public synchronized String execute(String skey) {
        String key = skey.replaceAll("[{}\"]","").trim();
        Hashtable<String, Room> collection = getManager().getCollection();
        ArrayList<String> s = new ArrayList<>();
        if (collection.size() != 0) {
                collection.forEach((k, v) -> {
                    if (k.compareTo(key) < 0) {
                        s.add(k);
                    }
                });
            if (s.size() == 0)
                return ("Совпадений, удовлетворяющих условию, не найдено");
            else {
                for (int i = 0; i < s.size(); i++) collection.remove(s.get(i));
                getManager().save();
                return "Из коллекции удалено " + s.size() + " элементов.";
            }
        } else return "Ключ не с чем сравнивать. Коллекция пуста.";
    }
}
