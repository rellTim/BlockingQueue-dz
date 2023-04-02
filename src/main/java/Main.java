import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    static BlockingQueue<String> blockingQueueA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> blockingQueueB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> blockingQueueC = new ArrayBlockingQueue<>(100);
    static List<Thread> threadList = new ArrayList<>();
    static int maxCount;
    static String name;

    protected static void thread(BlockingQueue<String> blockingQueue, char charName) {
        Runnable runnable = () -> {
            try {
                String str = blockingQueue.take();
                int count = (int) str.chars().filter(ch -> ch == charName).count();
                if (count > maxCount) {
                    name = str;
                    maxCount = count;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread thread = new Thread(runnable);
        threadList.add(thread);
        thread.start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            String str = generateText("abc", 1000);
            for (int i = 0; i < 1000; i++) {
                try {
                    blockingQueueA.put(str);
                    blockingQueueB.put(str);
                    blockingQueueC.put(str);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        thread(blockingQueueA, 'a');
        thread(blockingQueueB, 'b');
        thread(blockingQueueC, 'c');

        for (Thread thread : threadList) {
            thread.join();
        }
        System.out.println(maxCount);
        System.out.println(name);
    }
}
