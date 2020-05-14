#### 1. 谈谈对Zygot的理解
    源码路径
    system/core/rootdir/
                        init.rc
                        init.zygote32.rc
                        init.zygote32_64.rc
                        init.zygote64.rc
                        init.zygote64_32.rc
    init.zygote64.rc脚本为例，它由init进程解析
    根据该脚本init将fork一个进程，新进程将运行/system/bin/app_process64
    app_process64的入口函数是/framework/base/cmds/app_process/app_main.cpp::main()      
    
    frameworks/base/cmds/app_process/app_main.cpp
    frameworks/base/core/jni/AndroidRuntime.cpp
    frameworks/base/core/java/com/android/internal/os/ZygoteInit.java
    
    

#### 2. 聊一聊Android系统的启动
    https://ayusch.com/android-internals-the-android-os-boot-process/

![avatar](https://github.com/rigortek/AndroidInterview/blob/master/image/cycle_of_the_bootup_process_in_android.png
)

#### 3. 怎么添加一个系统服务


#### 4. 系统服务和bind的应用服务有什么区别


#### 5. ServiceManager启动和工作原理是怎样的


#### 6. 你知道应用进程是怎么启动的吗
