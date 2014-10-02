Azure Queue Plugin for Play Framework
=========

<br>
[Official Documentation for Java](http://azure.microsoft.com/pt-br/documentation/articles/storage-java-how-to-use-queue-storage/) using Microsoft Azure Queue.

 <br />


Installation
--------------
 <br />
Create an storage in your azure dashboard.

 <br />
Download ```AzureCloudQueuePlugin.java``` to your *plugins* package

 <br />

Add microsoft azure dependencies to your ```build.sbt```

```sh
"com.microsoft.azure" % "azure-storage" % "1.2.0"
```

 <br />

Add plugin this line into ```/conf/play.plugins```

```java
1502:plugins.AzureCloudQueuePlugin
```

 <br />

Add to your ```application.conf```

```java
azure.queue.name="queuename"
azure.storage.name="storagename"
azure.storage.primary.key="key"
```

<br>
