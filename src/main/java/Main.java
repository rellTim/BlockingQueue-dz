import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    static BlockingQueue<String> blockingQueueA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> blockingQueueB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> blockingQueueC = new ArrayBlockingQueue<>(100);
    static int maxCountA;
    static int maxCountB;
    static int maxCountC;
    static String A;
    static String B;
    static String C;

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
            int finalI = i;
            new Thread(() -> {
                try {
                    blockingQueueA.put(texts[finalI]);
                    blockingQueueB.put(texts[finalI]);
                    blockingQueueC.put(texts[finalI]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }).start();
            new Thread(() -> {
                try {
                    String str = blockingQueueA.take();
                    long count = str.chars().filter(ch -> ch == 'a').count();
                    if (count>maxCountA) {
                        A = str;
                        maxCountA = (int) count;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            new Thread(() -> {
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
            }).start();
            new Thread(() -> {
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
            }).start();
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
