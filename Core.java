import java.util.ArrayList;
import java.util.Random;

public class Core {
    public static final int quantSize = 50;
    private static Random rnd = new Random();
    ProcessBlocking blocking;
    private ArrayList<Process> processes;
    private ArrayList<Process> processesForBlocking;
    private int requiredTimeWithoutBlocking;

    public Core() {
        initProc();
        planProcesses();
        blocking = new ProcessBlocking(processesForBlocking);
        blocking.planProcessesBlocking();
        summarizeTime();
    }

    private void summarizeTime() {
        System.out.println("Without Blocking : " + requiredTimeWithoutBlocking);
        System.out.println("Blocking integrated : " + blocking.requiredTimeWithBlocking);
    }

    private void quantToString(int currentQuant) {
        if (currentQuant == 0) {
            System.out.println("Quant is processed");
        } else {
            System.out.println("Remaining quant size is " + currentQuant);
        }
        System.out.println();
    }

    public void initProc() {
        processes = new ArrayList<Process>();
        processesForBlocking = new ArrayList<Process>();

        int count = 8 + rnd.nextInt(4);
        int IOSample1 = rnd.nextInt(count);
        int IOSample2 = rnd.nextInt(count) + count / 2;
        for (int i = 0; i < count; i++) {
            Process proc;
            if (i == IOSample1 || i == IOSample2) {
                proc = new Process(i, true);
            } else {
                proc = new Process(i, false);
            }
            processes.add(proc);
            processesForBlocking.add(proc.clone());
        }
    }

    public void planProcesses() {
        System.out.println("No blocking integrated ");
        System.out.println(quantSize);

        while (processes.size() > 0) {
            for (int i = 0; i < processes.size(); i++) {
                int currentQuant = quantSize;

                Process currentProc = processes.get(i);

                if (currentProc.isAwaitingIO()) {
                    requiredTimeWithoutBlocking += currentProc.awaitIO();
                }

                int threadExecutionTime = currentProc.run(currentQuant);
                currentQuant -= threadExecutionTime;
                requiredTimeWithoutBlocking += threadExecutionTime;
                quantToString(currentQuant);
            }
            processes.removeIf(Process::isCompleted);
        }
    }
}
