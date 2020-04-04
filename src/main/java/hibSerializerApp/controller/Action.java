package hibSerializerApp.controller;

import hibSerializerApp.controller.abstraction.MainController;

@FunctionalInterface
public interface Action {
    void doAction(MainController mainController);
}
