public class Main {
    public static void main(String[] args) {
        Thread threadB = new Thread(() -> {
            System.out.println("Thread B is starting ... ");
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("B. " + i*2);
                } catch (InterruptedException e) {
                    System.out.println("B thread interrupted");
                    break;
                }
            }
        });

        Thread threadA = new SubClass();
        long start = System.currentTimeMillis();
        threadA.start();
        threadB.start();

        while (threadA.isAlive()) {
            try {
                Thread.sleep(1000);
                if (System.currentTimeMillis() - start > 2000) {
                    threadA.interrupt();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class SubClass extends Thread {
        @Override
        public void run() {
            System.out.println("Thread A is starting ... ");
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("A. " + (i*2+1));
                } catch (InterruptedException e) {
                    System.out.println("A thread interrupted");
//                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}