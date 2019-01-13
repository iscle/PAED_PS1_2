import java.util.*;

public class RepartirUsuaris {

    private final Server[] servers;
    private final User[] users;

    public RepartirUsuaris(Server[] servers, User[] users) {
        this.servers = servers;
        this.users = users.clone();
        for (User user : this.users) {
            Post[] posts = user.getPosts();
            Arrays.sort(posts, new Comparator<Post>() {
                @Override
                public int compare(Post o1, Post o2) {
                    return Long.compare(o2.getPublished(), o1.getPublished());
                }
            });
            user.setPosts(posts);
        }
    }

    UserSolution branchAndBound() {
        PriorityQueue<UserSolution> liveNodes = new PriorityQueue<>(11, (o1, o2) -> Double.compare(o1.getEquity(), o2.getEquity()));
        ArrayList<UserSolution> options;
        Server[] servers = this.servers.clone();
        UserSolution best = null;

        UserSolution x = new UserSolution(users[0]);

        liveNodes.add(x);

        while (liveNodes.size() > 0) {
            x = liveNodes.poll();
            options = expand(servers, x);

            for (Solution option:options) {
                if (isSolution(option)) {
                    best = min(option, best);
                } else if (isPromising(option, best)) {
                    liveNodes.add(option);
                }
            }
        }

        return best;
    }

    ArrayList<UserSolution> expand(Server[] servers, UserSolution us) {
        ArrayList<UserSolution> solutions = new ArrayList<>();
        UserSolution s;

        for (Server sv:servers) {
            s = new UserSolution(us);
            s.addNode(node, nc.getCost());
            solutions.add(s);
        }

        return solutions;

    }

    UserSolution greedy() {
        User[] users = this.users.clone();
        Server[] servers = this.servers.clone();
        UserSolution s = new UserSolution();

        Arrays.sort(users, (o1, o2) -> Double.compare(o2.getActivity(), o1.getActivity()));

        ArrayList<User> candidats = new ArrayList<>(Arrays.asList(users));

        while (candidats.size() > 0) {
            servers[0].addLoad(candidats.get(0).getActivity());
            s.add(candidats.get(0), servers[0]);
            candidats.remove(0);
            Arrays.sort(servers, new Comparator<Server>() {
                @Override
                public int compare(Server o1, Server o2) {
                    return Double.compare(o1.getLoad(), o2.getLoad());
                }
            });
        }

        return s;
    }
}
