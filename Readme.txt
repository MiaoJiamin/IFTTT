1.触发器关键字：renamed Modified path_changed size_changed
  任务关键字:record_summary record_detail recover

2.输入格式如：	IF [E:\大二下\OO] renamed THEN record_summary
		IF [E:\大二下\OO\1.txt] Modified THEN record_detail
		run
  每输入一个ifttt就进行一次换行，以run结束输入
  IF之前以及每行的最后不允许出现空格
  不允许输入括号不匹配的情况
  路径中不允许出现关键字
  路径中不允许出现不存在文件或者文件夹
  路径不允许是D:\record-summary 或 D:\record-detail 或D:\

3.若具体监控一个文件，该文件被删除后退出监控

4.size_changed中，新增一个文件，输出文件名为“新建文本文档.txt”

5.每次触发都进行一次输出

6.对于非法输入，输出INVALID INPUT 提示，并且正常执行其余的线程

7.每次run结束之后，手动清除D:\record-summary 和 D:\record-detail
 






  
	