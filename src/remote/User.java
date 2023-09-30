package remote;

import client.VoteInterface;
import helper.OTPGenerator;

import java.util.List;

public class User {
    private String studentNumber;
    private String password;
    private List<VoteInterface> votes;

    public User(String studentNumber, int passwordLength) {
        this.studentNumber = studentNumber;
        this.password = OTPGenerator.generate(passwordLength);
        this.votes = null;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<VoteInterface> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteInterface> votes) {
        this.votes = votes;
    }
}
