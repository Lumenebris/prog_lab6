package commands;

import server.CollectionManager;

/**
 * Класс переопределяет метод @code execute () для сохранения коллекции в файл сервера.
 */
public class SaveCommand extends AbstractCommand {

    public SaveCommand(CollectionManager manager) {
        super(manager);
        setDescription("Сохраняет коллекцию в файл сервера.");
    }

    @Override
    public synchronized String execute() {
        try {
            getManager().save();
            return "Изменения сохранены.";
        } catch (Exception ex) {
            return "Произошла непредвиденная ошибка на сервере. Коллекция не может быть сохранена. Попробуйте ещё раз позже.";
        }
    }
}
