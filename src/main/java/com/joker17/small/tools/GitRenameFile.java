package com.joker17.small.tools;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;
import java.util.function.Function;

public class GitRenameFile {

    /**
     * 是否为windows
     */
    private static final boolean IS_WINDOWS = File.separatorChar == '\\';

    /**
     * project目录file对象,即git project dir
     */
    private final File projectDir;

    /**
     * project目录路径
     */
    private final String projectDirAbsolutePath;

    private GitRenameFile(File projectDir) {
        this.projectDir = projectDir;
        this.projectDirAbsolutePath = projectDir.getAbsolutePath();
    }

    /**
     * 获取实例对象
     *
     * @param projectDir
     * @return
     */
    public static GitRenameFile of(File projectDir) {
        return new GitRenameFile(projectDir);
    }

    /**
     * 重命名文件
     *
     * @param resolveNameFunction 处理名称的函数,返回null时将不进行处理
     */
    public void renameFiles(Function<File, File> resolveNameFunction) {
        renameFiles(projectDir, resolveNameFunction);
    }

    /**
     * 重命名文件
     *
     * @param srcFile
     * @param resolveNameFunction
     */
    void renameFiles(File srcFile, Function<File, File> resolveNameFunction) {
        if (srcFile.isFile()) {
            File resolvedResult = resolveNameFunction.apply(srcFile);
            if (resolvedResult != null) {
                //执行重命名
                String oldFileName = srcFile.getAbsolutePath().replace(projectDirAbsolutePath, "");
                String newFileName = resolvedResult.getAbsolutePath().replace(projectDirAbsolutePath, "");

                //去除文件名的前缀'\' 或 '/'
                if (oldFileName.startsWith("\\") || oldFileName.startsWith("/")) {
                    oldFileName = oldFileName.substring(1);
                }

                if (newFileName.startsWith("\\") || newFileName.startsWith("/")) {
                    newFileName = newFileName.substring(1);
                }

                try {
                    if (IS_WINDOWS) {
                        runCmd(String.format("cmd /c cd %s & git mv %s %s", projectDirAbsolutePath, oldFileName, newFileName));
                    } else {
                        //生成临时sh文件进行执行
                        File bashFile = File.createTempFile(String.format("%s-%s", "git-rename-file", UUID.randomUUID().toString().replace("-", "")), ".sh");
                        try {
                            String bashContent = String.format("#!/bin/bash\r\ncd %s & git mv %s %s", projectDirAbsolutePath, oldFileName, newFileName);
                            Files.write(bashFile.toPath(), bashContent.getBytes());
                            runCmd(String.format("sh %s", bashFile.getAbsolutePath()));
                        } finally {
                            bashFile.delete();
                        }
                    }
                    //输出成功处理的文件信息
                    System.out.printf("file renamed success:\n \tbefore: %s\n \tafter: %s%n", oldFileName, newFileName);

                } catch (Exception ex) {
                    if (isNotUnderVersionControl(ex)) {
                        //非版本控制文件时
                        System.err.printf("file renamed fail(because it's not under version control file):\n \tbefore: %s\n \tafter: %s%n", oldFileName, newFileName);
                    } else {
                        //抛出异常
                        if (ex instanceof RuntimeException) {
                            throw (RuntimeException) ex;
                        }
                        throw new RuntimeException(ex);
                    }

                }
            }
        } else if (srcFile.isDirectory()) {
            //是目录时
            File[] dirListFiles = srcFile.listFiles();
            if (dirListFiles == null) {
                return;
            }
            for (File dirChildFile : dirListFiles) {
                renameFiles(dirChildFile, resolveNameFunction);
            }
        }
    }

    /**
     * 执行cmd
     *
     * @param cmd
     */
    void runCmd(String cmd) {
        //Runtime.getRuntime().exec(cmd);
        Process process = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(cmd);
            //获取错误结果
            String errStr = getStreamStr(process.getErrorStream());
            //当退出值0为正常,其他为异常
            int exitValue = process.waitFor();
            if (exitValue != 0) {
                throw new RuntimeException(errStr);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("执行cmd: %s error", cmd), e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    String getStreamStr(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, "GBK"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return sb.toString();
    }

    /**
     * 是否为非版本控制
     *
     * @param ex
     * @return
     */
    boolean isNotUnderVersionControl(Exception ex) {
        if (ex == null) {
            return false;
        }

        if (isNotUnderVersionControl(ex.getMessage())) {
            return true;
        }

        Throwable cause = ex.getCause();
        if (cause == null) {
            return false;
        }

        if (isNotUnderVersionControl(cause.getMessage())) {
            return true;
        }

        return false;
    }

    /**
     * 是否为非版本控制
     *
     * @param errorMsg
     * @return
     */
    boolean isNotUnderVersionControl(String errorMsg) {
        if (errorMsg == null || errorMsg.isEmpty()) {
            return false;
        }
        return errorMsg.contains("fatal: not under version control");
    }

}
