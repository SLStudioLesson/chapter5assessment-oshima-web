package com.taskapp;

import com.taskapp.model.User;
import com.taskapp.ui.TaskUI;
import com.taskapp.dataaccess.UserDataAccess;

public class App {

    public static void main(String[] args) {
        TaskUI ui = new TaskUI();
        ui.displayMenu();
    }
}
