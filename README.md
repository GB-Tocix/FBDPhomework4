### 作业 4 要求

1. 给出矩阵乘法的 MapReduce 实现，以 M_3_4 和 N_4_2 作为输入进行测试。

2. 给出关系代数的选择、投影、并集、交集、差集及自然连接的 MapReduce 实现。测试集如下：

- 关系Ra：(id, name, age, weight)

- 关系Rb：(id, gender, height)

  	2.1 在 Ra.txt 上选择 age = 18 的记录；在 Ra.txt 上选择 age < 18 的记录

    	2.2 在 Ra.txt 上对属性name进行投影

    	2.3 求 Ra1 和 Ra2 的并集

    	2.4 求 Ra1 和 Ra2 的交集

    	2.5 求 Ra2 - Ra1

    	2.6 Ra 和 Rb 在属性 id 上进行自然连接，要求最后的输出格式为(id, name, age, gender, weight, height)

  



## 报告

#### 本次实验主要参考原书代码，但在实验过程中对其进行了<u>较深入的理解</u>，并对遇到的一些问题进行了修改调整，使之最终满足了实验要求。



- __[矩阵乘法] MatrixMultiple__
  
  - 【代码目录】FBDPhomework4/src/main/java/Matrix/MatrixMultiply.java 

  - 【说明】原代码在从矩阵文件名中获取行列长度时，并不能很好地自动去掉“.txt”部分，例如在程序执行的过程中，会误认为“4.txt”是 M 的列数，这时候我选择使用<u>截取子串</u> `substring(0,1)` 的方式将“4”留下，而将“.txt”丢弃。
    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/add_substring.png)
  
  - 【运行参数】Idea 运行时设置的参数如下：
  
    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Matrix_configuration.png)
  
  - 【输出结果】经检验，输出结果<u>正确</u>。
  
    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/output_matrix.png)





- __[关系代数-选择] Selection__
  
  - 【代码目录】FBDPhomework4/src/main/java/RelationAlgebra/Selection.java
  
  - 【说明】
    
    - 原代码的`main`函数并没有设置`static`，在手动添加上去后便可以在 idea 中直接运行调试。
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/add_static.png)
      
    - 针对实验要求中两次不同的选择，进行对 `RelationA `类的成员函数 `isCondition` 进行修改，使其分别满足能够抽取出（1）age = 18 的元组 [注释中部分]；（2）age < 18 的元组 [未被注释部分]。
    
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Selection_modification.png)
    
  - 【运行参数】Idea 运行时设置的参数如下（去掉 age，换成 2）：![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Selection_configuration.png)
  
  - 【调试】
  
    - 遇到的<u>第一个问题</u>：
  
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Selection_error_1.png)
  
      BUG 指示的位置是：
  
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Selection_error_1_position.png)
  
      采取的办法是：直接将第一个 `LongWritable` 换成 `Text`，就可以运行成功不报错了。
      
    - 然而就遇上了<u>第二个问题</u>：原来刚刚的 `LongWritable `是换不得的，否则会导致读入的内容类型不对，导致最终输出的是十六进制编码。
    
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Selection_error_3.png)
    
      采取的解决办法：把`LongWritable`恢复成初始内容，将输入格式设置语句注释掉（如无特别说明，之后的代码中都将进行该操作），最终成功运行。
    
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Selection_final_solution.png)
    
  - 【输出结果】经检验，输出结果<u>正确</u>。
  
    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/output_selection.png)


- __[关系代数-投影] Projection__
  - 【代码目录】FBDPhomework4/src/main/java/RelationAlgebra/Projection.java
  - 【说明】这份代码运行的很顺利，在阅读理解后直接运行就跑成功了。
  - 【运行参数】Idea 运行时设置的参数如下
  
    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Projection_configuration.png)
  
  - 【输出结果】经检验，输出结果<u>正确</u>。  
  
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/output_projection.png)


- __[关系代数-交集] Intersection__
  - 【代码目录】FBDPhomework4/src/main/java/RelationAlgebra/Intersection.java
  - 【说明】这份代码运行的很顺利，在阅读理解后直接运行就跑成功了。
  - 【运行参数】Idea 运行时设置的参数如下
  
    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Intersection_configuration.png)
  
  - 【输出结果】经检验，输出结果<u>正确</u>。  
  
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/output_intersection.png)

  
  
- __[关系代数-并集] Concurrency__
  
  - 【代码目录】FBDPhomework4/src/main/java/RelationAlgebra/Concurrency.java
  
  - 【说明】原书代码中并没有给出并集的程序，结合原理一想，原来是因为它的实现和交集只相差了几个字符的改动。只需要将原来的“出现次数等于 2”，换成“大于等于 1”即可。并且改一改相关的类名和函数名。
  
      【注】这里附上改动过程中因为没考虑周全，而设置“等于 1”出现的错误结果
  
    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Concurrency_error_1.png)
  
      以及错误代码位置
  
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Concurrency_error_1_position.png)
  
  - 【运行参数】Idea 运行时设置的参数与运行 Intersection 时的一致。
  
  - 【输出结果】经检验，输出结果<u>正确</u>。  
  
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/output_concurrency.png)
  
  
  
- __[关系代数-差集] Difference__
  
  - 【代码目录】FBDPhomework4/src/main/java/RelationAlgebra/Difference.java
  
  - 【说明】这份代码运行的很顺利，在阅读理解后直接运行就跑成功了。
  
  - 【运行参数】Idea 运行时设置的参数如下
  
    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/Different_configuration.png)
  
  - 【输出结果】经检验，输出结果<u>正确</u>。

    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/output_Difference.png)
  
  
  
- __[关系代数-自然连接] Naturaljoin__
  
  - 【代码目录】FBDPhomework4/src/main/java/RelationAlgebra/Natrualjoin.java
  
  - 【说明】原书代码中的这一部分需要做出较大的改动。
  
      - 首先是因为代码中并不能够区分出模式 `RelationA`和 `RelationB`，会产生读取以及后续相关的错误。所以结合理解<u>我做出了如下的改动</u>。
  
          ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/NaturalJoin_solution.png)
  
      - 然后是输出的 `gender` 属性会自动变成“true”或者“false”，和原来的属性值不一致，于是我对类 `RelationB` 中的 `gender` 数据类型进行了调整，设置其为 `int`。
  
          ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/RelationB_modification.png)
  
      - 注意到<u>实验要求</u>中强调的输出结果的字段顺序，我对代码进行了如下改动（在输出结果截图中称为“规整”），使得最后的输出格式为(id, name, age, gender, weight, height)
  
          ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/NaturalJoin_modification.png)
  
  - 【运行参数】Idea 运行时设置的参数如下
  
    ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/NaturalJoin_configuration.png)
  
  - 【输出结果】经检验，输出结果<u>正确</u>。  
  
      ![](https://raw.githubusercontent.com/GB-Tocix/FBDPhomework4/master/pic/output_Natrualjoin.png)