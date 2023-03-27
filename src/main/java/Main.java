import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    static BlockingQueue<String> blockingQueueA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> blockingQueueB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> blockingQueueC = new ArrayBlockingQueue<>(100);
    static List<Thread> threadList = new ArrayList<>();
    static int maxCountA;
    static int maxCountB;
    static int maxCountC;
    static String A;
    static String B;
    static String C;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                String str = generateText("abc", 10000);
                try {
                    blockingQueueA.put(str);
                    blockingQueueB.put(str);
                    blockingQueueC.put(str);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        Runnable runnableA = () -> {
            try {
                String str = blockingQueueA.take();
                long count = str.chars().filter(ch -> ch == 'a').count();
                if (count > maxCountA) {
                    A = str;
                    maxCountA = (int) count;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread threadA = new Thread(runnableA);
        threadList.add(threadA);
        threadA.start();

        Runnable runnableB = () -> {
            try {
                String str = blockingQueueB.take();
                long count = str.chars().filter(ch -> ch == 'b').count();
                if (count > maxCountB) {
                    B = str;
                    maxCountB = (int) count;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread threadB = new Thread(runnableB);
        threadList.add(threadB);
        threadB.start();

        Runnable runnableC = () -> {
            try {
                String str = blockingQueueC.take();
                long count = str.chars().filter(ch -> ch == 'c').count();
                if (count > maxCountC) {
                    C = str;
                    maxCountC = (int) count;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread threadC = new Thread(runnableC);
        threadList.add(threadC);
        threadC.start();

        for (Thread thread : threadList) {
            thread.join();
        }
        System.out.println("Количество а = " + maxCountA);
        System.out.println(A);
        System.out.println("Количество b = " + maxCountB);
        System.out.println(B);
        System.out.println("Количество c = " + maxCountC);
        System.out.println(C);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
