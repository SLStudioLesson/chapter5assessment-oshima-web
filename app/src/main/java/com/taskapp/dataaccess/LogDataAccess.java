package com.taskapp.dataaccess;
import com.taskapp.model.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class LogDataAccess {
    private final String filePath;


    public LogDataAccess() {
        filePath = "app/src/main/resources/logs.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     */
    public LogDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * ログをCSVファイルに保存します。
     *
     * @param log 保存するログ
     */
    public void save(Log log) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // ログをCSV形式で書き込む
            String line = createLine(log);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * すべてのログを取得します。
     *
     * @return すべてのログのリスト
     */
    public List<Log> findAll() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            List<Log> logs = new ArrayList<>();
            String line;
            br.readLine(); // ヘッダー行をスキップ

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int taskCode = Integer.parseInt(parts[0].trim());
                    int changeUserCode = Integer.parseInt(parts[1].trim());
                    int status = Integer.parseInt(parts[2].trim());
                    
                    String[] dateParts = parts[3].trim().split("-");
                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    int day = Integer.parseInt(dateParts[2]);
                    LocalDate changeDate = LocalDate.of(year, month, day);

                    Log log = new Log(taskCode, changeUserCode, status, changeDate);
                    logs.add(log);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // 必ず null を返す
    }

    /**
     * 指定したタスクコードに該当するログを削除します。
     *
     * @see #findAll()
     * @param taskCode 削除するログのタスクコード
     */
    // public void deleteByTaskCode(int taskCode) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * ログをCSVファイルに書き込むためのフォーマットを作成します。
     *
     * @param log フォーマットを作成するログ
     * @return CSVファイルに書き込むためのフォーマット
     */
    private String createLine(Log log) {
        return log.getTaskCode() + "," +
            log.getChangeUserCode() + "," +
            log.getStatus() + "," +
            log.getChangeDate();
    }

}