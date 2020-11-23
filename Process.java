import java.util.Random;

public class Process {
    private static final Random RANDOM = new Random();
    //time boundaries
    private static final int MIN_EXEC = 50;
    private static final int MAX_EXEC = 250;
    private static final int MIN_IO = 25;
    private static final int MAX_IO = 300;

    //non-final integers
    private int processID;
    private int executionTime;
    private int ioTime;

    private Process(int processID, int executionTime, int ioTime) {
        this.processID = processID;
        this.executionTime = executionTime;
        this.ioTime = ioTime;
    }

    public Process(int processID, boolean ioThread) {
        this.processID = processID;
        this.executionTime = RANDOM.nextInt(MAX_EXEC - MIN_EXEC);
        if (ioThread) {
            this.ioTime = RANDOM.nextInt(MAX_IO - MIN_IO);
        } else {
            this.ioTime = 0;
        }
    }

    public Process clone() {
        return new Process(this.processID, this.executionTime, this.ioTime);
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int pid) {
        this.processID = pid;
    }

    public int getIoTime() {
        return ioTime;
    }

    public void setIoTime(int ioTime) {
        this.ioTime = ioTime;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public boolean isCompleted() {
        return executionTime <= 0;
    }

    public boolean isAwaitingIO() {
        return ioTime > 0;
    }

    public int awaitIO() {
        if (isAwaitingIO()) {
            System.out.print(this.toString());
            System.out.println(" awaiting for input/output");
            return ioTime;
        }
        return 0;
    }

    public int awaitIO(int amount) {
        if (isAwaitingIO()) {
            System.out.print(this.toString());
            System.out.println(" awaiting for input/output");
            if (ioTime <= amount) {
                int res = amount - ioTime;
                ioTime = 0;
                return res;
            }
            ioTime -= amount;
            return amount;
        }
        return 0;
    }

    public int run(int quantTime) {
        System.out.print(this.toString());
        System.out.print(" execution time: ");
        if (executionTime >= quantTime) {
            System.out.println(quantTime);
            executionTime -= quantTime;
            return quantTime;
        }
        int res = executionTime;
        System.out.println(executionTime);
        executionTime = 0;
        System.out.println("Process has ended");
        return res;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Process ID=" + processID);
        return sb.toString();
    }

}
