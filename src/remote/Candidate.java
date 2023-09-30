package remote;

public class Candidate {
    private String firstName;
    private String lastName;
    private int rank;
    private int score;

    public Candidate(String firstName, String lastName, int rank) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.rank = rank;
        this.score = 0;
    }

    public String toString() {
        return firstName + " " + lastName + "(rank " + rank + ")";
    }

    public void addVote(int number) {
        this.score += number;
    }

    public void deleteVote(int number) {
        this.score -= number;
    }

    public int getScore() {
        return this.score;
    }

    public int getRank() {
        return this.rank;
    }
}
