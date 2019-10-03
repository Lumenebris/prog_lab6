package server;

import commands.*;
import tale.Room;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Класс представляет объект сервера, который манипулирует CollectionManager.
 */
public class ServerConnection implements Runnable {

    private CollectionManager serverCollection;
    private Socket incoming;
    private HashMap<String, AbstractCommand> availableCommands;

    /**
     * Команды, доступные клиенту.
     * @param serverCollection обеспечивает доступ к коллекции.
     * @param incoming активное соединение с клиентской программой.
     */
    ServerConnection(CollectionManager serverCollection, Socket incoming) {
        this.serverCollection = serverCollection;
        this.incoming = incoming;
        availableCommands = new HashMap<>();
        availableCommands.put("show", new ShowCommand(serverCollection));
        availableCommands.put("info", new InfoCommand(serverCollection));
        availableCommands.put("save", new SaveCommand(serverCollection));
        availableCommands.put("clear", new ClearCommand(serverCollection));
        availableCommands.put("remove_all", new RemoveAllCommand(serverCollection));
        availableCommands.put("remove", new RemoveCommand(serverCollection));
        availableCommands.put("remove_lower", new RemoveLowerCommand(serverCollection));
        availableCommands.put("load", new LoadCommand(serverCollection));
        availableCommands.put("import", new ImportCommand(serverCollection));
        availableCommands.put("insert", new InsertCommand(serverCollection));
        availableCommands.put("help", new HelpCommand(serverCollection, availableCommands));
        availableCommands.put("man", new ManCommand(serverCollection, availableCommands));
    }

    /**
     * Запускает активное соединение с клиентом.
     */
    @Override
    public void run() {
        try (ObjectInputStream getFromClient = new ObjectInputStream(incoming.getInputStream());
             ObjectOutputStream sendToClient = new ObjectOutputStream(incoming.getOutputStream())) {
            sendToClient.writeObject("\nСоединение установлено.\nВы можете вводить команды.\nВведите 'help' для получения списка команд.");
            AbstractCommand errorCommand = new AbstractCommand(null) {
                @Override
                public String execute() {
                    return "Неизвестная команда. Введите 'help' для получения списка команд.";
                }
            };
            while (true) {
                try {
                    String requestFromClient = getFromClient.readUTF();
                    //String requestFromClient = (String) getFromClient.readObject();
                    System.out.print("Получено [" + requestFromClient + "] от " + incoming + ". ");
                    System.out.println();
                    String[] parsedCommand = requestFromClient.trim().split(" ",2);
                    if (parsedCommand.length == 1)
                        sendToClient.writeObject(availableCommands.getOrDefault(parsedCommand[0], errorCommand).execute());
                    else if (parsedCommand.length == 2) {
                        if (parsedCommand[0].equals("remove_all")) {
                            Room room = (Room) getFromClient.readObject();
                            sendToClient.writeObject(availableCommands.getOrDefault(parsedCommand[0], errorCommand).execute(room));
                        }
                        if (parsedCommand[0].equals("insert")) {
                            Room room = (Room) getFromClient.readObject();
                            String[] str = parsedCommand[1].trim().split(" ",2);
                            String key = str[0].replaceAll("[{}\"]","").trim();
                            sendToClient.writeObject(availableCommands.getOrDefault(parsedCommand[0], errorCommand).execute(key, room));
                        }
                        sendToClient.writeObject(availableCommands.getOrDefault(parsedCommand[0], errorCommand).execute(parsedCommand[1]));
                    }
                    System.out.println("Ответ успешно отправлен.");
                } catch (SocketException e) {
                    System.out.println(incoming + " отключился от сервера."); //Windows
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(incoming + " отключился от сервера."); //Unix
        }
    }

    @Override
    public String toString() {
        return "ServerConnection{" +
                "serverCollection=" + serverCollection +
                ", incoming=" + incoming +
                ", availableCommands=" + availableCommands +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerConnection)) return false;
        ServerConnection that = (ServerConnection) o;
        return Objects.equals(serverCollection, that.serverCollection) &&
                Objects.equals(availableCommands, that.availableCommands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverCollection, availableCommands);
    }
}