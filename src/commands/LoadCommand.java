package commands;

import com.google.gson.JsonSyntaxException;
import server.CollectionManager;
import tale.JsonSyntaxMistakeException;
import tale.Room;
import java.io.*;
import java.util.Hashtable;

/**
 * Класс переопределяет метод @code execute () для перечитывания файла сервера.
 */
public class LoadCommand extends AbstractCommand {

    public LoadCommand(CollectionManager manager) {
        super(manager);
        setDescription("Перечитывает коллекцию из файла сервера.");
    }

    @Override
    public synchronized String execute() {
        File collectionFile = getManager().getServCollection();
        //File collectionFile = new File("serverCollection.json");
        String notificationToClient = "Возникли проблемы с файлом на сервере. Попробуйте ещё раз позже.";
        try {
            String extension = collectionFile.getAbsolutePath().substring(collectionFile.getAbsolutePath().lastIndexOf(".") + 1);
            if (!collectionFile.exists() | collectionFile.length() == 0 | !extension.equals("json"))
                throw new FileNotFoundException();
            if (!collectionFile.canRead()) throw new SecurityException();
            int beginSize = getManager().getCollection().size();
            try (BufferedReader inputStreamReader = new BufferedReader(new FileReader(collectionFile))) {
                String nextLine;
                StringBuilder result = new StringBuilder();
                while ((nextLine = inputStreamReader.readLine()) != null) result.append(nextLine);
                String jsonString = new String(result);
                try {
                    Hashtable<String, Room> addedRoom = Room.jsonToRoomHashtable(jsonString);
                    addedRoom.forEach((k,v) -> {
                        if (!getManager().getCollection().containsKey(k)) getManager().getCollection().put(k,v);
                    });
                } catch (JsonSyntaxMistakeException | JsonSyntaxException ex) {
                    return "Синтаксическая ошибка JSON. Коллекция не может быть загружена.\n";
                }
                getManager().save();
                return "Коллекция успешно перечитана. Добавлено " + (getManager().getCollection().size() - beginSize) + " новых элементов.\n";
            }
        } catch (SecurityException e) {
            System.out.println("Файл защищён от чтения.");
            return notificationToClient;
        } catch (FileNotFoundException e) {
            System.out.println("Файл по указанному пути не найден.");
            return notificationToClient;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return notificationToClient;
        } catch (Exception e) {
            System.out.println("Файл пуст");
            return notificationToClient;
        }
    }
}
