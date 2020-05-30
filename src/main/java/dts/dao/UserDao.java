package dts.dao;

import dts.domain.User;

public interface UserDao {
    public User findUserByUsername(String username);
    public void saveUser(User user);
    public User findUserByCode(String code);
    public void updateStatus(User u);
    public User findUserByUsernamePassword(String username, String password);
}
