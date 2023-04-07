import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    private static BlockingQueue<String> blockingQueueA = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> blockingQueueB = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> blockingQueueC = new ArrayBlockingQueue<>(100);
    private static final int NUMBER = 1000;

    protected static void thread(BlockingQueue<String> blockingQueue, char charName) {
        Runnable runnable = () -> {
            try {
                int maxCount = 0;
                String name = null;
                for (int i = 0; i < NUMBER; i++) {
                    String str = blockingQueue.take();
                    int count = (int) str.chars().filter(ch -> ch == charName).count();
                    if (count > maxCount) {
                        name = str;
                        maxCount = count;
                    }
                }
                System.out.println(maxCount);
                System.out.println(name);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread thread = new Thread(runnable);
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
            for (int i = 0; i < NUMBER; i++) {
                String str = generateText("abc", 1000);
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
    }
}
