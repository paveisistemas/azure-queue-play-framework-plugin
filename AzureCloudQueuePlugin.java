package plugins;

import play.libs.Json;
import play.Application;
import play.Logger;
import play.Plugin;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

public class AzureCloudQueuePlugin extends Plugin {


  public static final String AZURE_QUEUE = "azure.queue.name";
  public static final String AZURE_STORAGE_NAME = "azure.storage.name";
  public static final String AZURE_STORAGE_PRIMARY_KEY = "azure.storage.primary.key";
  private final Application application;

  public static String azureQueue;
  public static String accessKey;
  private static CloudQueue queue;

  public AzureCloudQueuePlugin(Application application) {
    this.application = application;
  }

  /**
   * 
   * Recive any object, transform to JSON and add to Queue
   * 
   * @param message any object
   * @throws StorageException
   */
  public static void sendMessage(Object message) throws StorageException{
    CloudQueueMessage queueMessage = new CloudQueueMessage(Json.toJson(message).toString());
    queue.addMessage(queueMessage);
  }
  
  /**
   * 
   *     
   * @return number of itens on queue
   * @throws StorageException
   */
  public static long getMessageCount() throws StorageException{
    
   queue.downloadAttributes();
   return  queue.getApproximateMessageCount();
    
  }
  
  /**
   * Retrieve the first visible message in the queue and delete then
   * 
   * @return first visible message in the queue
   * @throws StorageException
   */
  public static CloudQueueMessage getMessage() throws StorageException{
    return queue.retrieveMessage();
  }
  
  /**
   * Retrieve the first visible message in the queue but DO NOT delete
   * 
   * @return first visible message in the queue
   * @throws StorageException
   */
  public static CloudQueueMessage viewFirstMessage() throws StorageException{
    return queue.peekMessage();
  }
  
  
  public static void deleteMessage(CloudQueueMessage message) throws StorageException{
    queue.deleteMessage(message);
  }
  
  @Override
  public void onStart() {
    accessKey = application.configuration().getString(AZURE_STORAGE_NAME);
    String secretKey = application.configuration().getString(AZURE_STORAGE_PRIMARY_KEY);
    azureQueue = application.configuration().getString(AZURE_QUEUE);
    

    if ((accessKey != null) && (secretKey != null)) {

      try {
        String storageConnectionString =
            "DefaultEndpointsProtocol=http;" + "AccountName=" + accessKey + ";" + "AccountKey="
                + secretKey + ";";

        // Retrieve storage account from connection-string.
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

        // Create the blob client.
        CloudQueueClient  queueClient = storageAccount.createCloudQueueClient();
        

        // Retrieve reference to a previously created container.
         queue = queueClient.getQueueReference(azureQueue);
         queue.createIfNotExists();
        
        
         Logger.info("Using Azure Queue: " + azureQueue);


      } catch (Exception e) {
        Logger.error("Error on load Azure Queue", e);
      }
    }
  }

  @Override
  public boolean enabled() {
    return (application.configuration().keys().contains(AZURE_STORAGE_NAME)
        && application.configuration().keys().contains(AZURE_STORAGE_PRIMARY_KEY) && application
        .configuration().keys().contains(AZURE_QUEUE));
  }



}
