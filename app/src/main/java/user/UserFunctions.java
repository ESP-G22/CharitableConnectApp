package user;

public interface UserFunctions {
    public boolean changePassword(String originalPassword, String newPassword1, String newPassword2);

    public boolean deleteUser();

}
