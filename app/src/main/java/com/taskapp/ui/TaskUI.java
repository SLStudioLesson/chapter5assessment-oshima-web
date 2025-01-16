package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.taskapp.exception.AppException;
import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.User;

public class TaskUI {
    private  final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        private final UserLogic userLogic = new UserLogic();
        private final TaskLogic taskLogic = new TaskLogic();
        private User loginUser;

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
    }

    public TaskUI() {

    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");
        inputLogin();

        // メインメニュー
        while (true) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                    taskLogic.setLoginUser(loginUser).showAll();
                    selectSubMenu();
                        break;
                    case "2":
                        inputNewInformation();
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        return;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    public void inputLogin() {
            while (true) {
                try {
                    System.out.print("メールアドレスを入力してください：");
                    String email = reader.readLine();

                    System.out.print("パスワードを入力してください：");
                    String password = reader.readLine();

                    loginUser = userLogic.login(email, password);

                    if (loginUser == null) {
                        System.out.println("既に登録されているメールアドレス、パスワードを入力してください");
                        continue; // 再度ログインを要求
                    }

                    System.out.print("ユーザー名：" + loginUser.getName() + "でログインしました。\n");
                    System.out.println();
                    break;
                } catch (IOException e) {
                    e.printStackTrace(); // 入力エラーを処理
                } catch (AppException e) {
                    // AppException を処理
                    System.out.println(e.getMessage());
                }
            }
    }

    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    public void inputNewInformation() {
        while (true) {
            try {
                // タスクコードの入力とバリデーション
                System.out.print("タスクコードを入力してください：");
                String taskCodeInput = reader.readLine();
                if (!isNumeric(taskCodeInput)) {
                    System.out.println("コードは半角の数字で入力してください\n");
                    continue;
                }
                int taskCode = Integer.parseInt(taskCodeInput);
    
                // タスク名の入力とバリデーション
                System.out.print("タスク名を入力してください：");
                String taskName = reader.readLine();
                if (taskName.length() > 10) {
                    System.out.println("タスク名は10文字以内で入力してください\n");
                    continue;
                }
    
                // 担当者コードの入力とバリデーション
                System.out.print("担当するユーザーのコードを選択してください：");
                String userCodeInput = reader.readLine();
                if (!isNumeric(userCodeInput)) {
                    System.out.println("ユーザーのコードは半角の数字で入力してください\n");
                    continue;
                }
                int userCode = Integer.parseInt(userCodeInput);
    
                // 担当者コードが存在するか確認
                User assignedUser = userLogic.findByCode(userCode);
                if (assignedUser == null) {
                    System.out.println("存在するユーザーコードを入力してください\n");
                    continue;
                }
    
                // タスクを登録
                taskLogic.save(taskCode, taskName, userCode, loginUser);
                System.out.println(taskName + "の登録が完了しました。\n");
                break;
    
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AppException e) {
                    System.out.println(e.getMessage() + "\n");
                }
        }
    }

    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    public void selectSubMenu() {
        while (true) {
            try {
                System.out.println("以下1~2から好きな選択肢を選んでください。");
                System.out.println("1. タスクのステータス変更, 2. メインメニューに戻る");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();
    
                switch (selectMenu) {
                    case "1":
                        inputChangeInformation(); // ステータス変更機能呼び出し
                        break;
                    case "2":
                        return; // メインメニューに戻る
                    default:
                        System.out.println("選択肢が誤っています。1~2の中から選択してください。\n");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
     */
    public void inputChangeInformation() {
        while (true) {
            try {
                System.out.print("\nステータスを変更するタスクコードを入力してください:");
                String taskCodeInput = reader.readLine();
                if (!isNumeric(taskCodeInput)) {
                    System.out.println("コードは半角の数字で入力してください\n");
                    continue;
                }
                int taskCode = Integer.parseInt(taskCodeInput);
    
                System.out.println("どのステータスに変更するか選択してください。");
                System.out.println("1. 着手中, 2. 完了");
                System.out.print("選択肢：");
                String statusInput = reader.readLine();
                if (!isNumeric(statusInput)) {
                    System.out.println("ステータスは半角の数字で入力してください\n");
                    continue;
                }
                int status = Integer.parseInt(statusInput);
                if (status != 1 && status != 2) {
                    System.out.println("ステータスは1・2の中から選択してください\n");
                    continue;
                }
    
                // ステータス変更を処理
                taskLogic.changeStatus(taskCode, status, loginUser);
                System.out.println("ステータスの変更が完了しました。\n");
                break;
    
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    /**
     * ユーザーからのタスク削除情報を受け取り、タスクを削除します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#delete(int)
     */
    // public void inputDeleteInformation() {
    // }

    /**
     * 指定された文字列が数値であるかどうかを判定します。
     * 負の数は判定対象外とする。
     *
     * @param inputText 判定する文字列
     * @return 数値であればtrue、そうでなければfalse
     */
    public boolean isNumeric(String inputText) {
        try {
            Integer.parseInt(inputText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}