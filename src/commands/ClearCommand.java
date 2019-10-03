package commands;

import server.CollectionManager;

/**
 * Класс переопределяет метод execute (), чтобы удалить все элементы из коллекции.
 */
public class ClearCommand extends AbstractCommand {

    public ClearCommand(CollectionManager manager) {
        super(manager);
        setDescription("Очистить коллекцию.");
    }

    @Override
    public synchronized String execute() {
        getManager().getCollection().clear();
        getManager().save();
        return "Коллекция очищена.";
    }
}