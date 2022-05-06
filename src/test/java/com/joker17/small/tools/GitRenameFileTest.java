package com.joker17.small.tools;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class GitRenameFileTest {

    /**
     * 可以在目录中增加未被版本控制的jpeg文件进行查看修改失败的提示
     */
    @Test
    public void test() {
        //git项目根目录
        File projectDir = new File(System.getProperty("user.dir"));
        File targetDir = new File(projectDir, "target");

        GitRenameFile.of(projectDir).renameFiles((file -> {
            if (file.getAbsolutePath().contains(targetDir.getAbsolutePath())) {
                //target目录时不处理
                return null;
            }

            if (file.getName().endsWith(".jpeg")) {
                //处理相关的文件进行重命名
                return new File(file.getAbsolutePath().replace(".jpeg", "_new.jpeg"));
            }
            return null;
        }));
    }


}