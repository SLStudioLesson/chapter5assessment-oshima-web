package com.taskapp.logic;

import java.util.List;

import com.taskapp.dataaccess.TaskDataAccess;
import com.taskapp.model.Task;
import com.taskapp.model.User;



public class TaskLogic {
    private final TaskDataAccess taskDataAccess;
    private User loginUser;

    public TaskLogic() {
        taskDataAccess = new TaskDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param taskDataAccess
     * @param logDataAccess
     * @param userDataAccess
     */

    /**
     * 全てのタスクを表示します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findAll()
     * @param loginUser ログインユーザー
     */

    public TaskLogic setLoginUser(User loginUser) {
        this.loginUser = loginUser;
        return this; // メソッドチェーン用に自身を返す
    }

    public void showAll() {
        List<Task> tasks = taskDataAccess.findAll();
        if (tasks == null || tasks.isEmpty()) {
            return;
        }

        int index = 1;
        for (Task task : tasks) {
            String statusText = switch (task.getStatus()) {
                case 0 -> "未着手";
                case 1 -> "着手中";
                case 2 -> "完了";
                default -> "不明";
            };

        String assigneeText;
        if (task.getRepUser().getCode() == loginUser.getCode()) {
            assigneeText = "あなたが担当しています";
        } else {
            assigneeText = task.getRepUser().getName() + "が担当しています";
        }

        System.out.println(index + ". タスク名：" + task.getName() + ", 担当者名：" + assigneeText + ", ステータス：" + statusText);
        index++;
        }
    }

    /**
     * 新しいタスクを保存します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#save(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code タスクコード
     * @param name タスク名
     * @param repUserCode 担当ユーザーコード
     * @param loginUser ログインユーザー
     * @throws AppException ユーザーコードが存在しない場合にスローされます
     */
    // public void save(int code, String name, int repUserCode,
    //                 User loginUser) throws AppException {
    // }

    /**
     * タスクのステータスを変更します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#update(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code タスクコード
     * @param status 新しいステータス
     * @param loginUser ログインユーザー
     * @throws AppException タスクコードが存在しない、またはステータスが前のステータスより1つ先でない場合にスローされます
     */
    // public void changeStatus(int code, int status,
    //                         User loginUser) throws AppException {
    // }

    /**
     * タスクを削除します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#delete(int)
     * @see com.taskapp.dataaccess.LogDataAccess#deleteByTaskCode(int)
     * @param code タスクコード
     * @throws AppException タスクコードが存在しない、またはタスクのステータスが完了でない場合にスローされます
     */
    // public void delete(int code) throws AppException {
    // }
}