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
  
+ GitRenameFile
  - git文件重命名,可以指定相关文件的名称,示例:

  ``` 
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
  ``` 

  ``` 
    file renamed success:
      before: src\test\resources\git-rename-file\f3c619599d45f5ae0ab3a165d842c8f7.jpeg
      after: src\test\resources\git-rename-file\f3c619599d45f5ae0ab3a165d842c8f7_new.jpeg
  ``` 

