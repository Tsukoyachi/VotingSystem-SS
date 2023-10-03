package remote;

import client.ClientInterface;
import client.VoteInterface;
import exception.BadCredentialsException;
import exception.HaveAlreadyAskedOTP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class VotingSystem extends UnicastRemoteObject implements VotingSystemInterface {
    private List<CandidateInterface> candidates;
    private List<User> users;
    public static String RESSOURCE_FOLDER = "ressource/";

    public VotingSystem() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(RESSOURCE_FOLDER + "user.csv"))) {
            String line;
            users = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                users.add(new User(line, 10));
            }
        } catch (IOException ignored) {
            throw new IOException("Failed to read users file");
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(RESSOURCE_FOLDER + "candidate.csv"))) {
            String line;
            candidates = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                if(values[2].equals("video"))
                    candidates.add(new Candidate(values[0], values[1],candidates.size() + 1, "video",RESSOURCE_FOLDER + values[3]));
                else if(values[2].equals("text"))
                    candidates.add(new Candidate(values[0], values[1],candidates.size() + 1, "text",values[3]));
                else
                    candidates.add(new Candidate(values[0], values[1], candidates.size()+1, "", null));
            }
        } catch (IOException ignored) {
            throw new IOException("Failed to read candidates file"+ignored.getMessage());
        }


        System.out.println("Server successfully started with " + users.size() + " users and " + candidates.size() + " candidates");
    }

    public List<CandidateInterface> askListOfCandidate() throws RemoteException {
        return candidates;
    }
    public PitchInterface getPitchForCandidate(CandidateInterface candidate) throws RemoteException {
        return candidate.getPitch();
    }

    public synchronized String askUserOTP(ClientInterface client) throws RemoteException, BadCredentialsException, HaveAlreadyAskedOTP {
        String studentNumber = client.fetchStudentNumber();
        User user = users.stream().filter(e -> e.getStudentNumber().equals(studentNumber)).findFirst().orElse(null);
        if(user == null) {
            throw new BadCredentialsException("The student number you provided doesn't exist");
        }
        return user.askForOTP();
    }

    public synchronized Boolean emitVote(String studentNumber, String otp, List<VoteInterface> votes) throws RemoteException, BadCredentialsException {
        if(this.users.stream().filter(e -> e.getVotes() == null).toList().isEmpty()) {
            return false;
        }

        // Check student number and OTP
        User user = users.stream().filter(e -> e.getStudentNumber().equals(studentNumber)).findFirst().orElse(null);
        if(user == null) {
            throw new BadCredentialsException("The student number you provided doesn't exist");
        }
        user.checkOTP(otp);

        if(!this.checkNewVotes(votes)) {
            return false;
        }

        System.out.println("Vote of the student "+studentNumber+" : ");

        // Undo previous vote
        if(user.getVotes() != null) {
            for(Pair<Integer,Integer> vote : user.getVotes()) {
                CandidateInterface candidate = null;
                for (CandidateInterface e : candidates) {
                    if (e.getRank() == vote.getFirst()) {
                        candidate = e;
                        break;
                    }
                }
                if(candidate != null) {
                    candidate.deleteVote(vote.getSecond());
                }
            }
        }

        // Apply new vote
        user.setVotes(votes.stream().map(e -> {
            try {
                return new Pair<Integer,Integer>(e.getRank(),e.getValue());
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }).toList());

        for(VoteInterface vote : votes) {
            int rank = vote.getRank();
            CandidateInterface candidate = candidates.stream().filter(e -> {
                try {
                    return e.getRank() == rank;
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }).findFirst().orElse(null);
            if (candidate != null) {
                candidate.addVote(vote.getValue());
            }
        }

        if(this.users.stream().filter(e -> e.getVotes() == null).toList().isEmpty()) {
            System.out.print(this.checkResultOfElection());
        }

        return true;
    }

    public String checkResultOfElection() throws RemoteException {
        if(!this.users.stream().filter(e -> e.getVotes() == null).toList().isEmpty()) {
            return "The election is not yet finished, please check the result later.\n";
        }
        StringBuilder res = new StringBuilder();
        List<CandidateInterface> tmp = new ArrayList<>();
        for (CandidateInterface candidateInterface : candidates) {
            tmp.add(candidateInterface);
        }
        tmp.sort(Comparator.comparing((CandidateInterface candidateInterface) -> {
            try {
                return candidateInterface.getScore();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).reversed());
        assert !tmp.isEmpty();
        res.append("The winner of the election is : ").append(tmp.get(0).toString()).append(" with ").append(tmp.get(0).getScore()).append(" votes\n");
        res.append("For the general results :\n");
        for(CandidateInterface candidate : tmp) {
            res.append(" - ").append(candidate.toString()).append(" with ").append(candidate.getScore()).append(" votes\n");
        }
        return res.toString();
    }

    private Boolean checkNewVotes(List<VoteInterface> votes) throws RemoteException {
        if(votes.size() != candidates.size()) {
            return false;
        }

        List<Integer> ranks = new ArrayList<>();
        for (VoteInterface vote : votes) {
            Integer rank = vote.getRank();
            ranks.add(rank);
        }

        for(CandidateInterface candidate : candidates) {
            if(!ranks.contains(candidate.getRank())) {
                return false;
            }
        }

        return true;
    }
}
