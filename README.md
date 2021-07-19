# small-tools

## features

+ ConsoleTable
  - 输出到控制台的简单表格,示例:
  
  ``` 
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
  ``` 
  
  ``` 
  
  +--------+--------+--------+--------+--------+--------+--------+--------+--------+--------+
  |   AA   |   BB   |   CC   |   DD   |   EE   |   FF   |   GG   |   HH   |   II   |   JJ   |
  +--------+--------+--------+--------+--------+--------+--------+--------+--------+--------+
  |   0    |   0    |   0    |   0    |   0    |   0    |   0    |   0    |   0    |   0    |
  |   0    |   1    |   2    |   3    |   4    |   5    |   6    |   7    |   8    |   9    |
  |   0    |   2    |   4    |   6    |   8    |   10   |   12   |   14   |   16   |   18   |
  |   0    |   3    |   6    |   9    |   12   |   15   |   18   |   21   |   24   |   27   |
  |   0    |   4    |   8    |   12   |   16   |   20   |   24   |   28   |   32   |   36   |
  |   0    |   5    |   10   |   15   |   20   |   25   |   30   |   35   |   40   |   45   |
  |   0    |   6    |   12   |   18   |   24   |   30   |   36   |   42   |   48   |   54   |
  |   0    |   7    |   14   |   21   |   28   |   35   |   42   |   49   |   56   |   63   |
  +--------+--------+--------+--------+--------+--------+--------+--------+--------+--------+
  ``` 