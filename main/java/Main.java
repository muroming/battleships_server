public class Main {
    static Thread mainThread;

    public static void main(String[] args) {
        mainThread = Thread.currentThread();
        System.out.println("Starting");
        Server s;
        try {
            s = new Server();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
