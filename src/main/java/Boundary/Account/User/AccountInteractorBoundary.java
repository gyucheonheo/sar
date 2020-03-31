package Boundary.Account.User;

import Entity.Boundary.Account.CellPhoneFormat.CellPhoneFormat;
import Entity.Boundary.Account.User.RideInformation.Rate.Rate;
import Entity.Boundary.Account.User.User;

import java.util.List;

public interface AccountInteractorBoundary {

    List<User> getAllUsers();
    User createUser(String first, String last, CellPhoneFormat cellPhone , String picture);
    void registerUser(User u);
    void activateUser(String aid);

    void updateUser(String aid, String first, String last, CellPhoneFormat cellPhone, String url);

    void deleteUser(String aid);

    User getUserById(String aid);

    List<User> searchUserByKeyword(String s);

    void rateUser(String tid, Rate r);

    class UserNotFoundException extends RuntimeException{}

    class UserDoNotHavePermissionToRate extends RuntimeException{
    }
}