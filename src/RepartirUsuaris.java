import java.util.*;

public class RepartirUsuaris {

    private final Server[] servers;
    private final User[] users;

    public RepartirUsuaris(Server[] servers, User[] users) {
        this.servers = servers;
        this.users = users.clone();
        for (User user : this.users) {
            Post[] posts = user.getPosts();
            Arrays.sort(posts, (o1, o2) -> Long.compare(o2.getPublished(), o1.getPublished()));
            user.setPosts(posts);
        }
    }

    UserSolution branchAndBound() {
        PriorityQueue<UserSolution> liveNodes = new PriorityQueue<>(11, (o1, o2) -> Double.compare(o1.getEquity(), o2.getEquity()));
        PriorityQueue<UserSolution> solutions = new PriorityQueue<>(11, (o1, o2) -> Double.compare(o1.getDistTotal(), o2.getDistTotal()));
        double minEquity = -1;
        UserSolution x = new UserSolution(servers);

        liveNodes.add(x);

        while (liveNodes.size() > 0) {
            x = liveNodes.poll();
            ArrayList<UserSolution> options = expand(x);

            for (UserSolution option : options) {
                if (isSolution(option)) {
                    if (minEquity == -1) {
                        minEquity = option.getEquity();
                    } else if (minEquity > option.getEquity()) {
                            minEquity = option.getEquity();
                    }

                    solutions.add(option);
                } else if (isPromising(option, minEquity)) {
                    liveNodes.add(option);
                }
            }
        }

        for (UserSolution us:solutions) {
            if (us.getEquity() <= minEquity * 1.1) {
                return us;
            }
        }

        return null;
    }

    private boolean isSolution(UserSolution option) {
        return option.getTotalUsers() == users.length;
    }

    private boolean isPromising(UserSolution option, double minEquity) {
        if (minEquity == -1) {
            return true;
        } else {
            return option.getEquity() <= minEquity * 1.1;
        }
    }

    ArrayList<UserSolution> expand(UserSolution us) {
        ArrayList<UserSolution> solutions = new ArrayList<>();
        UserSolution s;

        for (Server sv:us.getServers()) {
            s = new UserSolution(us);
            s.add(users[s.getTotalUsers()], sv);
            solutions.add(s);
        }

        return solutions;
    }

    UserSolution greedy() {
        User[] users = this.users.clone();
        Server[] servers = this.servers;
        UserSolution s = new UserSolution(servers);

        Arrays.sort(users, (o1, o2) -> Double.compare(o2.getActivity(), o1.getActivity()));

        ArrayList<User> candidats = new ArrayList<>(Arrays.asList(users));

        while (candidats.size() > 0) {
            s.add(candidats.get(0), servers[0]);
            candidats.remove(0);
            Server[] solSer = s.getServers();
            Arrays.sort(solSer, new Comparator<Server>() {
                @Override
                public int compare(Server o1, Server o2) {
                    return Double.compare(o1.getLoad(), o2.getLoad());
                }
            });
            servers = solSer;
        }

        return s;
    }
}
