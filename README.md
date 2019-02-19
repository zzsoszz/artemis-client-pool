# artemis-client-pool
artemis-client-pool
```java

public class Example {


    
    public static void main(String[] args) throws Exception {
        ConnectionOptions options=new ConnectionOptions();
        options.setUrl("tcp://artemis:8040");
        MessageTemplate messageTemplate=new MessageTemplate(options);
        new Thread(new SendThread(messageTemplate)).start();
        new Thread(new ReciveThread(messageTemplate)).start();
        Thread.currentThread().join();
    }


   static class SendThread implements Runnable {

        MessageTemplate messageTemplate;

        public SendThread(MessageTemplate messageTemplate) throws Exception {
            this.messageTemplate=messageTemplate;
        }

        public void run() {
            try {
                while(true){
                    String content="test";
                    System.out.println("send:"+content);
                    messageTemplate.send("bus.test.exampleQueue",content);
                    Thread.sleep(2000);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    static class ReciveThread implements Runnable {
        MessageTemplate messageTemplate;
        public ReciveThread(MessageTemplate messageTemplate) throws Exception {
            this.messageTemplate=messageTemplate;
        }
        public void run() {
            try {
                while(true){
                    String content= messageTemplate.receive("bus.test.exampleQueue");
                    System.out.println("recive:"+content);
                    Thread.sleep(5000);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}

```