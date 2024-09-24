package com.inspur.assets.monitor.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteInspectionServiceImpl {
    public String pythonScriptPathForTargetTask = "src/main/resources/main.py";
    public String pythonScriptPathForAllTask = "src/main/resources/main.py";
    public void executeScriptTargetTask(int i) {
        try {
            // 创建ProcessBuilder实例
            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPathForTargetTask, Integer.toString(i));
            pb.redirectErrorStream(true);

            Process p = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程结束
            int exitCode = p.waitFor();
            System.out.println("Exited with code " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeAllScript() {
        try {
            // 创建ProcessBuilder实例
            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPathForAllTask);
            pb.redirectErrorStream(true);

            Process p = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程结束
            int exitCode = p.waitFor();
            System.out.println("Exited with code " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void executePythonScript() {
        try {
            // 修改以下命令以适应你的操作系统和路径
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "py -3.11 main.py");
            pb.redirectErrorStream(true);

            Process p = pb.start();

            // 读取输出
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程结束
            int exitCode = p.waitFor();
            System.out.println("Exited with code " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

