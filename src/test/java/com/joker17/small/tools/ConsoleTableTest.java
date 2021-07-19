package com.joker17.small.tools;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ConsoleTableTest {

    @Test
    public void test() {
        List<String> titleList = new ArrayList<>(16);
        int size = 101;
        for (int i = 0; i < size; i++) {
            titleList.add(String.valueOf(i));
        }

        ConsoleTable table = ConsoleTable.of(titleList.toArray(new String[size]));

        table.leftMargin(3);
        table.rightMargin(3);

        for (int i = 0; i < 20; i++) {
            String[] contents = new String[size];
            for (int j = 0; j < size; j++) {
                contents[j] = String.valueOf(i * j);
            }
            table.addRowElements(contents);
        }
        table.textAligns(ConsoleTable.TextAlign.CENTER).print();
        table.textAligns(ConsoleTable.TextAlign.LEFT).print();
        table.textAligns(ConsoleTable.TextAlign.RIGHT).print();

    }

    @Test
    public void testSimple() {
        List<String> titleList = new ArrayList<>(16);
        int size = 10;
        for (int i = 0; i < size; i++) {
            titleList.add(String.format("%s%s", (char) (65 + i), (char) (65 + i)));
        }

        ConsoleTable table = ConsoleTable.of(titleList.toArray(new String[size]));

        table.leftMargin(3);
        table.rightMargin(3);

        for (int i = 0; i < 8; i++) {
            String[] contents = new String[size];
            for (int j = 0; j < size; j++) {
                contents[j] = String.valueOf(i * j);
            }
            table.addRowElements(contents);
        }
        table.textAligns(ConsoleTable.TextAlign.CENTER).print();
        table.textAligns(ConsoleTable.TextAlign.LEFT).print();
        table.textAligns(ConsoleTable.TextAlign.RIGHT).print();

    }

    @Test
    public void testChinese() {
        //测试输出中文 -- 控制台样式排版整齐字体需是等宽字体, e.g: 雅黑 幼圆等
        List<String> titleList = Arrays.asList("数学", "语文", "英语", "历史", "地理", "思想政治", "总分");
        int size = titleList.size();
        ConsoleTable table = ConsoleTable.of(titleList.toArray(new String[size]));

        for (int i = 0; i < 20; i++) {
            String[] contents = new String[size];
            int total = 0;
            for (int j = 0; j < size; j++) {
                if (j < 3) {
                    int current = new Random().nextInt(100) + 50;
                    total += current;
                    contents[j] = String.valueOf(current);
                } else {
                    if (j != size - 1) {
                        int current = new Random().nextInt(50) + 50;
                        total += current;
                        contents[j] = String.valueOf(current);
                    } else {
                        contents[j] = String.valueOf(total);
                    }
                }
            }
            table.addRowElements(contents);
        }
        table.textAligns(ConsoleTable.TextAlign.CENTER).print();
        table.textAligns(ConsoleTable.TextAlign.LEFT).print();
        table.textAligns(ConsoleTable.TextAlign.RIGHT).print();

        //测试输出获取的字符串
        System.out.println("<<<<<<<<<<<<---------------->>>>>>>>>>>>");
        System.out.print(table.textAligns(ConsoleTable.TextAlign.CENTER).getAsString());
        System.out.println("<<<<<<<<<<<<---------------->>>>>>>>>>>>");

    }

}