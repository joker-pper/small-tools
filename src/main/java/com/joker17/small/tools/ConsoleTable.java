package com.joker17.small.tools;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleTable {

    /**
     * 空格
     */
    private static final char SPACE_CHAR = ' ';

    /**
     * 空元素的默认字符
     */
    private static final String NULL_ELEMENT_STR = "NULL";

    /**
     * 换行符
     */
    private static final String LINE_BREAK_STR = "\r\n";

    /**
     * 列数
     */
    private int columns;

    /**
     * 标题数组
     */
    private String[] titles;

    /**
     * 内容列表
     */
    private List<String[]> elementsList;

    /**
     * 列宽数组
     */
    private int[] tableColumnWidths;

    /**
     * 文本对齐数组
     */
    private TextAlign[] textAligns;

    /**
     * 左边距
     */
    private int leftMargin;

    /**
     * 右边距
     */
    private int rightMargin;

    public enum TextAlign {
        LEFT,
        CENTER,
        RIGHT
    }

    public ConsoleTable(String... titles) {
        this.titles = titles;
        this.columns = this.titles.length;
        this.tableColumnWidths = new int[this.columns];
        this.elementsList = new ArrayList<>(64);
        updateTableWidths(this.titles);
        //设置默认文本对齐值
        textAligns(TextAlign.CENTER);
    }

    public static ConsoleTable of(String... titles) {
        return new ConsoleTable(titles);
    }

    /**
     * 设置对齐方式
     *
     * @param textAligns
     * @return
     */
    public ConsoleTable textAligns(TextAlign... textAligns) {
        if (textAligns.length == 1 || textAligns.length == this.columns) {
            this.textAligns = textAligns;
        } else {
            throw new IllegalArgumentException(String.format("textAligns length should one or eq titles length"));
        }
        return this;
    }

    /**
     * 设置左边距
     *
     * @param leftMargin
     * @return
     */
    public ConsoleTable leftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
        return this;
    }

    /**
     * 设置右边距
     *
     * @param rightMargin
     * @return
     */
    public ConsoleTable rightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
        return this;
    }

    /**
     * 添加行元素数组
     *
     * @param elements
     * @return
     */
    public ConsoleTable addRowElements(String... elements) {
        if (elements.length != this.columns) {
            throw new IllegalArgumentException(String.format("elements length must be eq titles length"));
        }
        elements = convertElements(elements);
        this.elementsList.add(elements);
        updateTableWidths(elements);
        return this;
    }

    /**
     * 转换元素数组
     *
     * @param elements
     * @return
     */
    private String[] convertElements(String... elements) {
        String[] results = new String[this.columns];
        for (int i = 0; i < this.columns; i++) {
            String element = elements[i];
            if (element == null) {
                results[i] = NULL_ELEMENT_STR;
            } else {
                results[i] = element;
            }
        }
        return results;
    }

    /**
     * 更新宽度
     *
     * @param contents
     */
    private void updateTableWidths(String... contents) {
        for (int i = 0; i < this.columns; i++) {
            int tableWidth = tableColumnWidths[i];
            int contentWidth = StringFormatUtil.strLength(contents[i]);
            if (tableWidth < contentWidth) {
                tableColumnWidths[i] = contentWidth;
            }
        }
    }

    /**
     * 输出结果
     */
    public void print() {

        //获取修饰符内容
        String tableModifierText = getTablePrintModifierText('-', "+");

        //修饰符内容输出
        toPrintConsole(tableModifierText);

        //输出table标题内容
        toPrintConsole(getTablePrintContents(this.titles, "|"));

        //修饰符内容输出
        toPrintConsole(tableModifierText);

        //输出table行元素内容
        int elementsSize = elementsList.size();
        for (int i = 0; i < elementsSize; i++) {
            String[] elements = elementsList.get(i);
            toPrintConsole(getTablePrintContents(elements, "|"));
        }

        if (elementsSize > 0) {
            //修饰符内容输出
            toPrintConsole(tableModifierText);
        }
    }

    /**
     * 获取输出内容
     *
     * @return
     */
    public String getAsString() {
        StringBuilder sb = new StringBuilder();

        //修饰符内容
        String tableModifierText = getTablePrintModifierText('-', "+");
        sb.append(tableModifierText);

        //table标题内容
        sb.append(getTablePrintContents(this.titles, "|"));

        //修饰符内容
        sb.append(tableModifierText);

        //输出table行元素内容
        int elementsSize = elementsList.size();
        for (int i = 0; i < elementsSize; i++) {
            String[] elements = elementsList.get(i);
            sb.append(getTablePrintContents(elements, "|"));
        }

        if (elementsSize > 0) {
            //修饰符内容
            sb.append(tableModifierText);
        }
        return sb.toString();
    }


    /**
     * 获取table要输出的内容
     *
     * @param contents
     * @param separator
     * @return
     */
    private String getTablePrintContents(String[] contents, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.columns; i++) {
            String content = contents[i];
            if (i == 0) {
                sb.append(separator);
            }
            sb.append(getFormatContent(content, SPACE_CHAR, i));
            sb.append(separator);
        }
        sb.append(LINE_BREAK_STR);
        return sb.toString();
    }


    /**
     * 输出字符内容到控制台
     *
     * @param text
     */
    private void toPrintConsole(String text) {
        System.out.print(text);
    }


    /**
     * 获取要输出的修饰符内容
     *
     * @param connectorChar
     * @param separator
     */
    private String getTablePrintModifierText(char connectorChar, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.columns; i++) {
            if (i == 0) {
                sb.append(separator);
            }
            String charString = StringFormatUtil.getFillString(connectorChar, tableColumnWidths[i]);
            sb.append(getFormatContent(charString, connectorChar, i));
            sb.append(separator);
        }
        sb.append(LINE_BREAK_STR);
        return sb.toString();
    }


    /**
     * 获取格式化后的内容
     *
     * @param content  字符内容
     * @param fillChar 填充char
     * @param index    索引位置
     * @return
     */
    private String getFormatContent(String content, char fillChar, int index) {
        TextAlign textAlign;
        if (textAligns.length == 1) {
            textAlign = textAligns[0];
        } else {
            textAlign = textAligns[index];
        }
        switch (textAlign) {
            case LEFT:
                return StringFormatUtil.left(content, fillChar, tableColumnWidths[index], leftMargin, rightMargin);
            case CENTER:
                return StringFormatUtil.center(content, fillChar, tableColumnWidths[index], leftMargin, rightMargin);
            case RIGHT:
                return StringFormatUtil.right(content, fillChar, tableColumnWidths[index], leftMargin, rightMargin);
            default:
                throw new UnsupportedOperationException("NOT SUPPORT textAlign: " + textAlign.name());
        }
    }


    static class StringFormatUtil {

        final static char SPACE_CHAR = ' ';

        /**
         * 获取填充新字符
         *
         * @param c
         * @param size
         * @return
         */
        public static String getFillString(char c, int size) {
            char[] chars = new char[size];
            Arrays.fill(chars, c);
            return new String(chars);
        }

        public static int strLength(String str) {
            return StringPadUtil.strLength(str, "UTF-8");
        }

        public static String center(String str, int size, int leftSize, int rightSize) {
            return center(str, SPACE_CHAR, size, leftSize, rightSize);
        }

        public static String center(String str, char c, int size, int leftSize, int rightSize) {
            if (str == null) {
                return null;
            }

            size = size + leftSize + rightSize;

            int strLength = StringPadUtil.strLength(str);
       /*     if (size <= 0 || size <= strLength) {
                return str;
            }*/

            str = StringPadUtil.leftPad(str, strLength + (size - strLength) / 2, c);
            str = StringPadUtil.rightPad(str, size, c);

            return str;
        }

        public static String left(String str, int size, int leftSize, int rightSize) {
            return left(str, SPACE_CHAR, size, leftSize, rightSize);
        }

        public static String left(String str, char c, int size, int leftSize, int rightSize) {
            if (str == null) {
                return null;
            }
            size += rightSize;
            str = StringPadUtil.rightPad(str, size, c);
            if (leftSize != 0) {
                str = StringPadUtil.leftPad(str, size + leftSize, c);
            }
            return str;
        }


        public static String right(String str, int size, int leftSize, int rightSize) {
            return right(str, SPACE_CHAR, size, leftSize, rightSize);
        }

        public static String right(String str, char c, int size, int leftSize, int rightSize) {
            if (str == null) {
                return null;
            }
            size += leftSize;
            str = StringPadUtil.leftPad(str, size, c);
            if (rightSize != 0) {
                str = StringPadUtil.rightPad(str, size + rightSize, c);
            }
            return str;
        }


    }

    /**
     * Created by IntelliJ IDEA
     * <p>
     * <p>
     * https://blog.csdn.net/CL_YD/article/details/85006808
     * <p>
     * 源地址:
     * https://github.com/clyoudu/clyoudu-util/blob/master/src/main/java/github/clyoudu/consoletable/util/StringPadUtil.java
     *
     * @author chenlei
     * @date 2019/4/16
     * @time 21:04
     * @desc StringPadUtil
     */
    static class StringPadUtil {

        public static String leftPad(String str, int size, char c) {
            if (str == null) {
                return null;
            }

            int strLength = strLength(str);
            if (size <= 0 || size <= strLength) {
                return str;
            }
            return repeat(size - strLength, c).concat(str);
        }

        public static String rightPad(String str, int size, char c) {
            if (str == null) {
                return null;
            }

            int strLength = strLength(str);
            if (size <= 0 || size <= strLength) {
                return str;
            }
            return str.concat(repeat(size - strLength, c));
        }

        public static String center(String str, int size, char c) {
            if (str == null) {
                return null;
            }

            int strLength = strLength(str);
            if (size <= 0 || size <= strLength) {
                return str;
            }
            str = leftPad(str, strLength + (size - strLength) / 2, c);
            str = rightPad(str, size, c);
            return str;
        }

        public static String leftPad(String str, int size) {
            return leftPad(str, size, ' ');
        }

        public static String rightPad(String str, int size) {
            return rightPad(str, size, ' ');
        }

        public static String center(String str, int size) {
            return center(str, size, ' ');
        }

        private static String repeat(int size, char c) {
            StringBuilder s = new StringBuilder();
            for (int index = 0; index < size; index++) {
                s.append(c);
            }
            return s.toString();
        }

        public static int strLength(String str) {
            return strLength(str, "UTF-8");
        }


        public static int strLength(String str, String charset) {
            int len = 0;
            int j = 0;
            byte[] bytes = str.getBytes(Charset.forName(charset));
            while (bytes.length > 0) {
                short tmpst = (short) (bytes[j] & 0xF0);
                if (tmpst >= 0xB0) {
                    if (tmpst < 0xC0) {
                        j += 2;
                        len += 2;
                    } else if ((tmpst == 0xC0) || (tmpst == 0xD0)) {
                        j += 2;
                        len += 2;
                    } else if (tmpst == 0xE0) {
                        j += 3;
                        len += 2;
                    } else if (tmpst == 0xF0) {
                        short tmpst0 = (short) (((short) bytes[j]) & 0x0F);
                        if (tmpst0 == 0) {
                            j += 4;
                            len += 2;
                        } else if ((tmpst0 > 0) && (tmpst0 < 12)) {
                            j += 5;
                            len += 2;
                        } else if (tmpst0 > 11) {
                            j += 6;
                            len += 2;
                        }
                    }
                } else {
                    j += 1;
                    len += 1;
                }
                if (j > bytes.length - 1) {
                    break;
                }
            }
            return len;
        }

    }
}
