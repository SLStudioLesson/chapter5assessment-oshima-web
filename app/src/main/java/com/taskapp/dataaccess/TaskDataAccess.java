package com.taskapp.dataaccess;

import com.taskapp.model.Task;
import com.taskapp.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskDataAccess {

    private final String filePath;
    private final UserDataAccess userDataAccess;

    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv";
        this.userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     * @param userDataAccess
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath;
        this.userDataAccess = userDataAccess;
    }

    /**
     * CSVから全てのタスクデータを取得します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            List<Task> tasks = new ArrayList<>();
            String line;
            br.readLine();
    
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int code = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    int status = Integer.parseInt(parts[2].trim());
                    int repUserCode = Integer.parseInt(parts[3].trim());
                    User repUser = userDataAccess.findByCode(repUserCode);

                    if (repUser != null) {
                        tasks.add(new Task(code, name, status, repUser));
                    }
                }
            }
            /**
             * コメント
             * tasksを返却しましょう！
             */
            return tasks;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * タスクをCSVに保存します。
     * @param task 保存するタスク
     */
    public void save(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // タスクデータをCSV形式で追記
            String line = createLine(task);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを1件取得します。
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
    
    public Task findByCode(int code) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // ヘッダー行をスキップ
    
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int csvCode = Integer.parseInt(parts[0].trim());
                    if (csvCode == code) {
                        String name = parts[1].trim();
                        int status = Integer.parseInt(parts[2].trim());
                        int repUserCode = Integer.parseInt(parts[3].trim());
                        User repUser = userDataAccess.findByCode(repUserCode);
                        return new Task(csvCode, name, status, repUser);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * タスクデータを更新します。
     * @param updateTask 更新するタスク
     */
    public void update(Task updateTask) {
        List<Task> tasks = findAll();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("code,name,status,repUserCode\n");
            for (Task t : tasks) {
                if (t.getCode() == updateTask.getCode()) {
                    writer.write(createLine(updateTask) + "\n");
                } else {
                    writer.write(createLine(t) + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * コードを基にタスクデータを削除します。
     * @param code 削除するタスクのコード
     */
    // public void delete(int code) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    private String createLine(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getCode()).append(",")
            .append(task.getName()).append(",")
            .append(task.getStatus()).append(",")
            .append(task.getRepUser().getCode());
        return sb.toString();
    }
}