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
    
    - 原代码的`main`函数并没有设置`static`，在手动添加上去后便可以在 idea
