import java.util.ArrayList;

public class ProcessBlocking {
    protected int requiredTimeWithBlocking;
    private ArrayList<Process> processesClone;
    public ProcessBlocking(ArrayList<Process> processesClone) {
        this.processesClone = processesClone;
    }

    protected void quantToString(int currentQuant) {
        if (currentQuant == 0) {
            System.out.println("Quant is processed");
        } else {
            System.out.println("Remaining quant size is " + currentQuant);
        }
        System.out.println();
    }

    public void planProcessesBlocking() {
        System.out.println("Blocking integrated");
        System.out.println(Core.QUANT_SIZE);
        ArrayList<Process> blockedProcesses = new ArrayList<Process>();

        while (processesClone.size() > 0) {
            for (int i = 0; i < processesClone.size(); i++) {
                Process currentProc = processesClone.get(i);
                if (blockedProcesses.contains(currentProc)) {
                    continue;
                }

                if (currentProc.isAwaitingIO()) {
                    System.out.print(currentProc.toString());
                    System.out.println(" blocked/waiting for input/output device");
                    System.out.println();
                    blockedProcesses.add(currentProc);
                    continue;
                }

                int currentQuant = Core.QUANT_SIZE;

                int threadExecutionTime = currentProc.run(currentQuant);
                requiredTimeWithBlocking += threadExecutionTime;
                currentQuant -= threadExecutionTime;

                for (Process proc : blockedProcesses) {
                    proc.awaitIO(Core.QUANT_SIZE);
                }

                blockedProcesses.removeIf(proc -> !proc.isAwaitingIO());
                quantToString(currentQuant);
            }

            processesClone.removeIf(proc -> proc.isCompleted());

            if (processesClone.size() > 0 && blockedProcesses.size() == processesClone.size()) {
                System.out.println("All procs waiting for input/output device, awaiting for first one");
                int waitingTime = Integer.MAX_VALUE;
                for (Process proc : blockedProcesses) {
                    waitingTime = Integer.min(waitingTime, proc.getIoTime());
                }
                for (Process proc : blockedProcesses) {
                    proc.awaitIO(waitingTime);
                }
                blockedProcesses.removeIf(proc -> !proc.isAwaitingIO());
                requiredTimeWithBlocking += waitingTime;
                System.out.println("Waiting time : " + waitingTime);
                System.out.println();
            }
        }

    }

}
