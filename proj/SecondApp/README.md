前一个进程的End call provider还未执行，另一个进程的Entry call provider已到来，说明完全是并发的，而且最的的binder线程名称也不一样。

12-10 11:06:06.516 4448-4462/com.cw.secondapp D/jcw: Entry call provider: Thread 2 call index: 0, Thread: Binder_2
12-10 11:06:06.517 4448-4461/com.cw.secondapp D/jcw: Entry call provider: Thread 1 call index: 0, Thread: Binder_1
12-10 11:06:06.567 4448-4462/com.cw.secondapp D/jcw: End call provider: Thread 2 call index: 0, Thread: Binder_2
12-10 11:06:06.567 4448-4590/com.cw.secondapp D/jcw: Entry call provider: Thread 2 call index: 1, Thread: Binder_3
12-10 11:06:06.568 4448-4461/com.cw.secondapp D/jcw: End call provider: Thread 1 call index: 0, Thread: Binder_1
12-10 11:06:06.568 4448-4591/com.cw.secondapp D/jcw: Entry call provider: Thread 1 call index: 1, Thread: Binder_4
12-10 11:06:06.618 4448-4590/com.cw.secondapp D/jcw: End call provider: Thread 2 call index: 1, Thread: Binder_3
12-10 11:06:06.618 4448-4462/com.cw.secondapp D/jcw: Entry call provider: Thread 2 call index: 2, Thread: Binder_2
12-10 11:06:06.619 4448-4591/com.cw.secondapp D/jcw: End call provider: Thread 1 call index: 1, Thread: Binder_4
12-10 11:06:06.620 4448-4461/com.cw.secondapp D/jcw: Entry call provider: Thread 1 call index: 2, Thread: Binder_1
12-10 11:06:06.668 4448-4462/com.cw.secondapp D/jcw: End call provider: Thread 2 call index: 2, Thread: Binder_2
12-10 11:06:06.669 4448-4590/com.cw.secondapp D/jcw: Entry call provider: Thread 2 call index: 3, Thread: Binder_3
12-10 11:06:06.670 4448-4461/com.cw.secondapp D/jcw: End call provider: Thread 1 call index: 2, Thread: Binder_1
12-10 11:06:06.670 4448-4591/com.cw.secondapp D/jcw: Entry call provider: Thread 1 call index: 3, Thread: Binder_4
12-10 11:06:06.719 4448-4590/com.cw.secondapp D/jcw: End call provider: Thread 2 call index: 3, Thread: Binder_3
12-10 11:06:06.719 4448-4462/com.cw.secondapp D/jcw: Entry call provider: Thread 2 call index: 4, Thread: Binder_2
12-10 11:06:06.721 4448-4591/com.cw.secondapp D/jcw: End call provider: Thread 1 call index: 3, Thread: Binder_4
12-10 11:06:06.721 4448-4461/com.cw.secondapp D/jcw: Entry call provider: Thread 1 call index: 4, Thread: Binder_1
12-10 11:06:06.769 4448-4462/com.cw.secondapp D/jcw: End call provider: Thread 2 call index: 4, Thread: Binder_2
12-10 11:06:06.771 4448-4461/com.cw.secondapp D/jcw: End call provider: Thread 1 call index: 4, Thread: Binder_1


MainActivity启动SecondActivity，如果MainActivity android:launchMode是"singleTask"，各版本表现不一
在Android4.4上，即使Intent不使用new task，startActivityForResult也接收不到onActivityResult回调
在Android5.1及以上，Intent不使用new task，startActivityForResult则可以接收到onActivityResult回调

MainActivity android:launchMode如果没有指定，默认为"standard"，则可以接收到onActivityResult回调