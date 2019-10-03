package server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

import tale.JsonSyntaxMistakeException;
import tale.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/**
 * Обеспечивает доступ к коллекции.
 */

public class CollectionManager {
    private Hashtable<String, Room> collection;
    private Gson gson;
    private Date createdDate;
    private File servCollection;
    private Type collectionType;

    {
        collection = new Hashtable<String, Room>();
        gson = new Gson();
        collectionType = new TypeToken<Hashtable<String, Room>>() {}.getType();
    }

    /**
     * Предоставляет доступ к коллекции и связанному с ней файлу.
     * @param file - заданный файл
     */
    CollectionManager(File file) {
       servCollection = file;
            try (BufferedReader collectionReader = new BufferedReader(new FileReader(servCollection))) {
                System.out.println("Загрузка коллекции " + servCollection.getAbsolutePath());
                String nextLine;
                StringBuilder result = new StringBuilder();
                while ((nextLine = collectionReader.readLine()) != null) result.append(nextLine);
                String jsonString = new String(result);
                try {
                    //collection = gson.fromJson(result.toString(), collectionType);
                    collection.putAll(Room.jsonToRoomHashtable(jsonString));
                    System.out.println("Коллекция успешно загружена. Добавлено " + collection.size() + " элементов.");
                } catch (JsonSyntaxException | JsonSyntaxMistakeException ex) {
                    System.err.println("Ошибка синтаксиса JSON. " + ex.getMessage());
                    System.exit(1);
                }
            }catch (IOException e) {
                System.err.println("Что-то не так с файлом.");
                System.exit(1);
            }
        createdDate = new Date();
    }


    /**
     * Записывает элементы коллекции в файл. Так как необходим нескольким командам, реализован в этом классе.
     */
    public void save() {
        ArrayList<String> counter = new ArrayList<>();
        try (BufferedWriter writerToFile = new BufferedWriter(new FileWriter(servCollection))) {
            writerToFile.write("[");
            collection.forEach((k, v) -> {
                try {
                    counter.add(k);
                    if (counter.size() != collection.size()) writerToFile.write(gson.toJson(collection.get(k)) + ",");
                    else writerToFile.write(gson.toJson(collection.get(k)) + "]");
                } catch (IOException ex) {
                    System.out.println("Сохранение коллекции не удалось.");
                }
            });
        } catch (Exception ex) {
            System.out.println("Возникла непредвиденная ошибка. Коллекция не может быть сохранена.");
        }
    }

    public Hashtable<String, Room> getCollection() {
        return collection;
    }

    public File getServCollection() {
        return servCollection;
    }

    public Gson getGson() {
        return gson;
    }

    public Type getCollectionType() {
        return collectionType;
    }

    @Override
    public String toString() {
        return "Тип коллекции: " + collection.getClass() +
                "\nТип элементов: " + Room.class +
                "\nДата инициализации: " + createdDate +
                "\nКоличество элементов: " + collection.size();
    }
}
