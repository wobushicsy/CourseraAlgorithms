import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;

import java.util.Queue;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class BaseballElimination {

    private final int numberOfTeams;
    private final HashMap<String, Integer> teamIndex;
    private final String[] teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remains;
    private final int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        // initialize parameters
        In in = new In(filename);
        numberOfTeams = in.readInt();
        teamIndex = new HashMap<>();
        teams = new String[numberOfTeams];
        wins = new int[numberOfTeams];
        losses = new int[numberOfTeams];
        remains = new int[numberOfTeams];
        g = new int[numberOfTeams][numberOfTeams];

        // read data
        for (int i = 0; i < numberOfTeams; i += 1) {
            String name = in.readString();
            int win = in.readInt();
            int loss = in.readInt();
            int remaining = in.readInt();

            teams[i] = name;
            wins[i] = win;
            losses[i] = loss;
            remains[i] = remaining;
            for (int j = 0; j < numberOfTeams; j += 1) {
                int numberOfRemainingGames = in.readInt();
                g[i][j] = numberOfRemainingGames;
            }

            teamIndex.put(name, i);
        }
    }

    private void checkTeam(String team) {
        if (!teamIndex.containsKey(team)) {
            throw new IllegalArgumentException("team doesn't exist");
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teamIndex.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeam(team);

        int index = teamIndex.get(team);
        return wins[index];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeam(team);

        int index = teamIndex.get(team);
        return losses[index];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeam(team);

        int index = teamIndex.get(team);
        return remains[index];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);

        int index1 = teamIndex.get(team1);
        int index2 = teamIndex.get(team2);
        return g[index1][index2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeam(team);

        // Trivial elimination
        int index = teamIndex.get(team);
        int remaining = remains[index];
        int win = wins[index];
        for (int i = 0; i < numberOfTeams; i += 1) {
            int thisWin = wins[i];
            if (thisWin > win + remaining) {
                return true;
            }
        }

        // Nontrivial elimination
        int networkVertices = numberOfTeams + numberOfTeams * (numberOfTeams - 1) / 2 + 2;
        FlowNetwork flowNetwork = new FlowNetwork(networkVertices);
        int s = 0;
        int t = networkVertices - 1;
        int cnt = 1;

        // connect an artificial source vertex s to each game vertex i-j and set its capacity to g[i][j]
        // connect each game vertex i-j with the two opposing team vertices
        int teamVerticesBeginning = numberOfTeams * (numberOfTeams - 1) / 2 + 1;
        for (int i = 0; i < numberOfTeams - 1; i += 1) {
            for (int j = i + 1; j < numberOfTeams; j += 1) {
                FlowEdge gameEdge = new FlowEdge(s, cnt, g[i][j]);
                flowNetwork.addEdge(gameEdge);

                FlowEdge teamEdge1 = new FlowEdge(cnt, teamVerticesBeginning + i,
                                                                                Double.POSITIVE_INFINITY);
                FlowEdge teamEdge2 = new FlowEdge(cnt, teamVerticesBeginning + j,
                                                                                Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(teamEdge1);
                flowNetwork.addEdge(teamEdge2);

                cnt += 1;
            }
        }

        // connect each team vertex to an artificial sink vertex t
        for (int i = 0; i < numberOfTeams; i += 1) {
            FlowEdge targetEdge = new FlowEdge(teamVerticesBeginning + i, t,
                                                    wins[index] + remains[index] - wins[i]);
            flowNetwork.addEdge(targetEdge);
        }

        new FordFulkerson(flowNetwork, s, t);
        for (FlowEdge e : flowNetwork.adj(s)) {
            if (e.flow() != e.capacity())
                return true;
        }

        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        checkTeam(team);

        int x = teamIndex.get(team);
        int numMatches = numberOfTeams * (numberOfTeams - 1) / 2;
        int nodeID = 0;

        List<String> nList = new ArrayList<String>();
        for (int i = 0; i < numberOfTeams; i++) {
            if (wins[x] + remains[x] - wins[i] < 0)
                nList.add(teams[i]);
        }
        if (!nList.isEmpty())
            return nList;

        FlowNetwork fn = new FlowNetwork(numMatches + numberOfTeams + 2);
        int s = numMatches + numberOfTeams;
        int t = s + 1;

        for (int i = 0; i < numberOfTeams; i++) {
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (i == j)
                    continue;
                fn.addEdge(new FlowEdge(s, nodeID, g[i][j])); // source to match
                // nodes
                fn.addEdge(new FlowEdge(nodeID, numMatches + i,
                        Integer.MAX_VALUE)); // match to team nodes
                fn.addEdge(new FlowEdge(nodeID, numMatches + j,
                        Integer.MAX_VALUE)); // match to team nodes
                nodeID += 1;
            }
            fn.addEdge(new FlowEdge(numMatches + i, t, Math.max(0, wins[x] + remains[x]
                    - wins[i]))); // game nodes to target
        }

        FordFulkerson FF = new FordFulkerson(fn, s, t);

        boolean flag = false;
        for (FlowEdge e : fn.adj(s)) {
            if (e.flow() != e.capacity()) {
                flag = true;
                break;
            }
        }
        if (!flag)
            return null;
        else {
            List<Integer> nodeList = this.BFSRes(fn, s);
            List<String> nl = new ArrayList<>();
            for (Integer v : nodeList) {
                if (FF.inCut(v) && v >= numMatches) {
                    nl.add(this.teams[v - numMatches]);
                }
            }
            return nl;
        }
    }

    private List<Integer> BFSRes(FlowNetwork graph, int node) {
        Queue<Integer> Q = new LinkedList<>();
        boolean[] visited = new boolean[graph.V()];
        Q.add(node);
        visited[node] = true;
        List<Integer> nodeList = new ArrayList<Integer>();
        while (!Q.isEmpty()) {
            int cn = Q.remove();
            for (FlowEdge e : graph.adj(cn)) {
                int t = -1;
                if (e.from() == cn)
                    t = e.to();
                else
                    t = e.from();
                if (e.residualCapacityTo(t) > 0) {
                    if (!visited[t]) {
                        Q.add(t);
                        visited[t] = true;
                        nodeList.add(t);
                    }
                }
            }
        }
        return nodeList;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
