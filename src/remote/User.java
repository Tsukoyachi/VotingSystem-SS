package remote;

import client.VoteInterface;
import exception.HaveAlreadyAskedOTP;
import helper.OTPGenerator;

import java.util.List;

public class User {
    private String studentNumber;
    private String password;
    private List<VoteInterface> votes;
    private Boolean haveAskedForOTP;

    public User(String studentNumber, int passwordLength) {
        this.studentNumber = studentNumber;
        this.password = OTPGenerator.generate(passwordLength);
        this.votes = null;
        this.haveAskedForOTP = false;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public List<VoteInterface> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteInterface> votes) {
        this.votes = votes;
    }

    /**
     * This method allow user to fetch his one time password (OTP)
     * Here the method is synchronized to prevent multiple individual to fetch this password.
     * @return The string containing the OTP
     * @throws HaveAlreadyAskedOTP This exception is thrown if the user already fetched his OTP.
     */
    public synchronized String askForOTP() throws HaveAlreadyAskedOTP {
        if(this.didUserAlreadyAskForOTP()) {
            throw new HaveAlreadyAskedOTP("This user already has for his one time password, please contact the administrator if you lost it.");
        }
        this.haveAskedForOTP = true;
        return this.password;
    }

    public Boolean didUserAlreadyAskForOTP() {
        return this.haveAskedForOTP;
    }
}
