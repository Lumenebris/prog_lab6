package tale;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.Serializable;
import java.util.Hashtable;

public class Room implements Comparable<Room>, Serializable {
    private int floor;
    private int number;
    private String shape;
    private Wall walls;
    private int x;
    private int y;
    private long age;

    public Room (int floor, int number, String shape, Wall walls, int x, int y) {
        this.floor = floor;
        this.number = number;
        this.shape = shape;
        this.walls = walls;
        this.x = x;
        this.y = y;
        this.age = System.currentTimeMillis();
    }

    public Room (int floor, int number, String shape, Wall walls, int x, int y, long age) {
        this.floor = floor;
        this.number = number;
        this.shape = shape;
        this.walls = walls;
        this.x = x;
        this.y = y;
        this.age = age;
    }

    public String getParameters() {
        return "этаж: " + floor +
                ",номер: " + number;
    }

    /**
     * Добавляет элементы в коллекцию
     * @param jsonRoomHashtable - коллекция
     */
    public static Hashtable<String,Room> jsonToRoomHashtable(String jsonRoomHashtable) throws tale.JsonSyntaxMistakeException {
        try {
            Gson gson = new Gson();
            Hashtable<String,Room> collection = new Hashtable<>();
            int noInitializedCount = 0;
            if (jsonRoomHashtable.length() != 0) {
                Room[] roomArray = gson.fromJson(jsonRoomHashtable, Room[].class);  //Массив объектов tale.Room
                for (Room i : roomArray) {
                    if ((i != null) && (i.getFloor() != 0) && (i.getNumber() != 0) && (i.getWall() != null) && (i.getWall().getMaterial() != null)) {
                        collection.put(i.getShape(), i);
                    }else noInitializedCount++;
                }
            }
            if (noInitializedCount > 0) System.out.println("Найдено " + noInitializedCount + " не полностью инициализированных элементов");
            return collection;
        }catch (JsonSyntaxException ex){
            throw new tale.JsonSyntaxMistakeException();
        }
    }

    /**
     * Сортировка объектов коллекции
     * @param room - объект, с которым сравниваем
     * @return результат сравнения
     */
    @Override
    public int compareTo(Room room) {
        return shape.compareTo(room.getShape());
    }

    @Override
    public String toString() {
        return "{" +
                "\"floor\": " + floor +
                ", \"number\": " + number +
                ", \"shape\": \"" + shape + "\"" +
                ", \"walls\": {\"material\": \"" + walls.getMaterial() +  "\"" + "}" +
                ", \"x\": " + x +
                ", \"y\": " + y +
                ", \"age\": " + age +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        if (floor != room.floor) return false;
        if (number != room.number) return false;
        if (walls != room.walls) return false;
        if (shape != room.shape) return false;
        boolean c = obj.hashCode()== this.hashCode() ? true : false;
        return c;
    }

    public int getFloor() {
        return floor;
    }

    public int getNumber() {
        return number;
    }

    public String getShape() {
        return shape;
    }

    public Wall getWall() {
        return walls;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getAge() {
        return age;
    }
}