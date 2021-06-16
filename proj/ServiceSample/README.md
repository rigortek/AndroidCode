

```
android:process="businessprocess"
```

如果不是以":"为前缀，则要求名称中必须包含一个“."，否则触发安装异常，APP接收到的安装错误码是：
The application could not be installed: INSTALL_PARSE_FAILED_MANIFEST_MALFORMED

实际PMS异常是：

```
W/PackageManager(  604): Failed parse during installPackageLI
W/PackageManager(  604): android.content.pm.PackageParser$PackageParserException: /data/app/vmdl1222210390.tmp/base.apk (at Binary XML file line #21): Invalid process name businessprocess in package com.cw.servicesample: must have at least one '.' separator
W/PackageManager(  604):        at android.content.pm.PackageParser.parseBaseApk(PackageParser.java:993)
W/PackageManager(  604):        at android.content.pm.PackageParser.parseClusterPackage(PackageParser.java:898)
W/PackageManager(  604):        at android.content.pm.PackageParser.parsePackage(PackageParser.java:862)
W/PackageManager(  604):        at com.android.server.pm.PackageManagerService.installPackageLI(PackageManagerService.java:11050)
W/PackageManager(  604):        at com.android.server.pm.PackageManagerService.access$3100(PackageManagerService.java:247)
W/PackageManager(  604):        at com.android.server.pm.PackageManagerService$6.run(PackageManagerService.java:9131)
W/PackageManager(  604):        at android.os.Handler.handleCallback(Handler.java:739)
W/PackageManager(  604):        at android.os.Handler.dispatchMessage(Handler.java:95)
W/PackageManager(  604):        at android.os.Looper.loop(Looper.java:135)
W/PackageManager(  604):        at android.os.HandlerThread.run(HandlerThread.java:61)
W/PackageManager(  604):        at com.android.server.ServiceThread.run(ServiceThread.java:46)
```



---

1. bindService调用是同步返回，而onServcieConnected是异步返回，而且是在主进程中回调，所以其中不能进行耗时操作，onServiceDisconnected同理。

2. unbindService后，一定不会调用onServiceDisconnected
3. 问：如果bindService后，建立了linkToDeath关系，在unbindService后，能否收到bindDied?

​       答：可以

4. 问：如果bindService后，未调用unbindService，在Service进程销毁时才会调用onServiceDisconnected；Service如果再次被AMS拉起，会收到onServcieConnected吗？
   答：会
5. 问：如果bindService后，建立了linkToDeath关系，未调用unbindService，在Service进程销毁时才会调用onServiceDisconnected；一定会调用bindDied吗？
   答：一定会，而且bindDied要早于onServiceDisconnected
6. unbindService之后，再次调用bindService，仍会调用onServcieConnected

---

AMS中Service重启机制

1. persistent应用，立即重启
   非persistent应用，如果onStartCommand返回值是START_STICK或START_STICKY_COMPATIBILITY，表示服务要自动重启，
2. 如果是只有一个服务，如果在服务启动过程中CRASH，第一次1S，后面是4S，按4倍数递，进行重启，但最多重启2次。
   如果服务启动完毕再CRASH，第一次1S，后面是4S，按4倍数递，最多重启6次。
3. 如果有多个进程服务CRASH重启，则两个Service之间的重启间隔必须相隔10S以上

