package ME3;

import java.util.Random;

class Process extends Thread {
    private final int id;
    private volatile boolean hasToken;
    private boolean wantsToEnterCS;
    private Process nextProcess;
    private boolean isAlive = true;
    private static final Random random = new Random();

    public Process(int id) {
        this.id = id;
        this.hasToken = false;
        this.wantsToEnterCS = false;
    }

    public void setNextProcess(Process next) {
        this.nextProcess = next;
    }

    public void receiveToken() {
        if (isAlive) {
            hasToken = true;
        }
    }

    private void enterCriticalSection() {
        System.out.println("Process " + id + " ENTERS Critical Section.");
        try {
            Thread.sleep(3000); // Simulate work inside CS
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Process " + id + " EXITS Critical Section.");
    }

    public void failProcess() {
        isAlive = false;
        hasToken = false;
        System.out.println("Process " + id + " FAILED.");
    }

    public void recoverProcess() {
        isAlive = true;
        System.out.println("Process " + id + " RECOVERED.");
    }

    @Override
    public void run() {
        while (true) {
            if (hasToken && isAlive) {

                wantsToEnterCS = random.nextBoolean();

                // Enter CS when needed
                if (wantsToEnterCS) {
                    enterCriticalSection();
                }

                // Pass token to next active process
                hasToken = false;

                Process next = nextProcess;
                while (!next.isAlive) {
                    next = next.nextProcess; // Skip failed processes
                }

                System.out.println("Process " + id + " passes token to Process " + next.id);
                next.receiveToken();

                try {
                    Thread.sleep(2000); // Delay for realism
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class TokenRingDemo {
    public static void main(String[] args) {
        int n = 5; // Number of processes
        Process[] processes = new Process[n];

        // Create processes
        for (int i = 0; i < n; i++) {
            processes[i] = new Process(i + 1);
        }

        // Set up ring connections
        for (int i = 0; i < n; i++) {
            processes[i].setNextProcess(processes[(i + 1) % n]);
        }

        // Start process threads
        for (int i = 0; i < n; i++) {
            processes[i].start();
        }

        // Initial token to Process 1
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processes[0].receiveToken();

        // Failures and recoveries simulation
        try {
            Thread.sleep(5000);
            processes[3].failProcess(); // Process 4 fails

            Thread.sleep(8000);
            processes[3].recoverProcess(); // Process 4 recovers

            Thread.sleep(5000);
            processes[1].failProcess(); // Process 2 fails

            Thread.sleep(7000);
            processes[1].recoverProcess(); // Process 2 recovers

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
