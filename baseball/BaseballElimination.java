/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private final int teamNum;
    private final int[][] g;
    private final String[] teamName;
    private HashMap<Integer, Integer> v2id;
    private HashMap<String, Team> teams;
    private int flow;
    private int matchNode;
    private int vertexNode;

    private class Team {
        private final int id;
        private final int wins;
        private final int loses;
        private final int remains;
        public Team(int _id, int _wins, int _loses, int _remains) {
            this.id = _id;
            this.wins = _wins;
            this.loses = _loses;
            this.remains = _remains;
        }
    }

    private void isValidTeam(String team) {
        if (team == null) throw new IllegalArgumentException();
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
    }

    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException();
        In in = new In(filename);
        teamNum = Integer.parseInt(in.readLine());
        teams = new HashMap<String, Team>();
        g = new int[teamNum][teamNum];
        teamName = new String[teamNum];
        v2id = new HashMap<Integer, Integer>();
        int id = 0;
        while (in.hasNextLine()) {
            String line = in.readLine().trim();
            String token[] = line.split(" +");
            String key = token[0];
            int wins = Integer.parseInt(token[1]);
            int loses = Integer.parseInt(token[2]);
            int remains = Integer.parseInt(token[3]);
            if (!teams.containsKey(key)) {
                teamName[id] = key;
                teams.put(key, new Team(id, wins, loses, remains));
                for (int i = 0; i < teamNum; i++) {
                    g[id][i] = Integer.parseInt(token[i + 4]);
                }
                id++;
            }
        }
    }

    public int numberOfTeams() {return teamNum;}
    public Iterable<String> teams() {return teams.keySet();}
    public int wins(String team) {
        isValidTeam(team);
        return teams.get(team).wins;
    }
    public int losses(String team) {
        isValidTeam(team);
        return teams.get(team).loses;
    }
    public int remaining(String team) {
        isValidTeam(team);
        return teams.get(team).remains;
    }
    public int against(String team1, String team2) {
        isValidTeam(team1);
        isValidTeam(team2);
        return g[teams.get(team1).id][teams.get(team2).id];
    }

    private FlowNetwork structureFlowNetwork(String team) {
        Team thisTeam = teams.get(team);
        int thisId = thisTeam.id;
        int thisMostWins = thisTeam.wins + thisTeam.remains;
        matchNode = (teamNum - 1) * (teamNum - 2) / 2;
        vertexNode = matchNode + (teamNum - 1) + 2;
        flow = 0;
        int indexMatch = 1;
        int indexI = matchNode;
        int indexJ = indexI;
        int s = 0;
        int t = vertexNode - 1;
        FlowNetwork flowNetwork = new FlowNetwork(vertexNode);

        for (int i = 0; i < teamNum; i++) {
            if (i == thisId) continue;
            indexI++;
            indexJ = indexI;
            if(thisMostWins < wins(teamName[i])) {
                return null;
            }
            for (int j = i + 1; j < teamNum; j++) {
                if (j == thisId) continue;
                indexJ++;
                flow += g[i][j];
                flowNetwork.addEdge(new FlowEdge(s, indexMatch, g[i][j]));
                flowNetwork.addEdge(new FlowEdge(indexMatch, indexI, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(indexMatch, indexJ, Double.POSITIVE_INFINITY));
                indexMatch++;
            }
            v2id.put(indexI, i);
            flowNetwork.addEdge(new FlowEdge(indexI, t, thisMostWins - wins(teamName[i])));
        }
        return flowNetwork;
    }

    public boolean isEliminated(String team) {
        isValidTeam(team);
        FlowNetwork flowNetwork = structureFlowNetwork(team);
        if (flowNetwork == null) {
            return true;
        }
        else {
            FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, vertexNode - 1);
            return flow > fordFulkerson.value();
        }
    }

    public Iterable<String> certificateOfElimination(String team) {
        isValidTeam(team);
        if (!isEliminated(team)) {
            return null;
        }
        else {
            Queue<String> ans = new Queue<String>();
            int thisId = teams.get(team).id;
            FlowNetwork flowNetwork = structureFlowNetwork(team);
            if (flowNetwork == null) {
                int thisMostWins = teams.get(team).wins + teams.get(team).remains;
                for (int i = 0; i < teamNum; i++) {
                    if (i == thisId) continue;
                    if (thisMostWins < wins(teamName[i])) {
                        ans.enqueue(teamName[i]);
                    }
                }
            }
            else {
                FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, vertexNode - 1);
                for (int i = 1 + matchNode; i < vertexNode; i++) {
                    if (fordFulkerson.inCut(i)) {
                        int id = v2id.get(i);
                        ans.enqueue(teamName[id]);
                    }
                }
            }
            return ans;
        }
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
