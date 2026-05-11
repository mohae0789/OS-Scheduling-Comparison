package scheduler;


import model.Process;
import java.util.ArrayList;
import java.util.List;

public class ReadyQueueService {


    public static class QueueSnapshot {
        public final int time;
        public final String executing;
        public final List<String> waiting;

        public QueueSnapshot(int time, String executing, List<String> waiting) {
            this.time = time;
            this.executing = executing;
            this.waiting = new ArrayList<>(waiting);
        }
    }

    public List<QueueSnapshot> computeSnapshots(List<Process> originalProcesses, int quantum) {
        List<Process> sorted = sortedCopies(originalProcesses);
        int n = sorted.size();
        if (n == 0) return new ArrayList<>();

        int[] remaining = new int[n];
        for (int i = 0; i < n; i++) {
            remaining[i] = sorted.get(i).getBurstTime();
        }

        List<Integer> readyQueue = new ArrayList<>();
        List<QueueSnapshot> snapshots = new ArrayList<>();

        int time = sorted.get(0).getArrivalTime();
        int nextArrival = 0;


        while (nextArrival < n && sorted.get(nextArrival).getArrivalTime() <= time) {
            readyQueue.add(nextArrival);
            nextArrival++;
        }

        while (!readyQueue.isEmpty()) {
            int idx = readyQueue.remove(0);
            String execPid = sorted.get(idx).getPid();


            List<String> waitingPids = new ArrayList<>();
            for (int qi : readyQueue) {
                waitingPids.add(sorted.get(qi).getPid());
            }


            snapshots.add(new QueueSnapshot(time, execPid, waitingPids));


            int runTime = Math.min(quantum, remaining[idx]);
            time += runTime;
            remaining[idx] -= runTime;


            for (int i = nextArrival; i < n; i++) {
                if (sorted.get(i).getArrivalTime() <= time) {
                    readyQueue.add(i);
                    nextArrival = i + 1;
                } else break;
            }


            if (remaining[idx] > 0) {
                readyQueue.add(idx);
            }


            if (readyQueue.isEmpty() && nextArrival < n) {
                time = sorted.get(nextArrival).getArrivalTime();
                while (nextArrival < n && sorted.get(nextArrival).getArrivalTime() <= time) {
                    readyQueue.add(nextArrival);
                    nextArrival++;
                }
            }
        }
        return snapshots;
    }


    private List<Process> sortedCopies(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original) {
            copy.add(new Process(p.getPid(), p.getArrivalTime(), p.getBurstTime()));
        }

        for (int i = 1; i < copy.size(); i++) {
            Process key = copy.get(i);
            int j = i - 1;
            while (j >= 0 && copy.get(j).getArrivalTime() > key.getArrivalTime()) {
                copy.set(j + 1, copy.get(j));
                j--;
            }
            copy.set(j + 1, key);
        }
        return copy;
    }
}


