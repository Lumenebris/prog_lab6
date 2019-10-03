package commands;

import com.google.gson.JsonSyntaxException;
import server.CollectionManager;
import tale.JsonSyntaxMistakeException;
import tale.Room;

import java.util.Hashtable;

/**
 * Класс переопределяет метод execute () для добавления коллекции из файла, полученного от клиента.
 */
public class ImportCommand extends AbstractCommand {

    public ImportCommand(CollectionManager manager) {
        super(manager);
        setDescription("Добавить объекты из файла на вашем компьютере в коллекцию и сохранить их на сервере.\nimport filename");
    }

    @Override
    public synchronized String execute(String filename) {
        int beginSize = getManager().getCollection().size();
        try {
            getManager().getCollection().putAll(Room.jsonToRoomHashtable(filename));
            getManager().save();
            return "Из вашего файла было добавлено " + (getManager().getCollection().size() - beginSize) + " элементов.";
        } catch (JsonSyntaxException ex) {
            return "Синтаксическая ошибка JSON в импортированных данных.";
        } catch (JsonSyntaxMistakeException ex) {
            return "Синтаксическая ошибка JSON в импортированных данных.";
        }
    }
}