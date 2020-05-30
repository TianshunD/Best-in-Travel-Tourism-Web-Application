package dts.service.impl;

import dts.dao.UserDao;
import dts.dao.impl.UserDaoImpl;
import dts.domain.User;
import dts.service.UserService;
import dts.util.MailUtils;
import dts.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();
    @Override
    public Boolean register(User user) {
        User userFound = dao.findUserByUsername(user.getUsername());
        if (userFound!=null) {
            return false;
        }
        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        dao.saveUser(user);
        String mailContent = "<a href='http://localhost/travel/user/activateUser?code=" + user.getCode() + "'>Click to activate your account</a>";
        MailUtils.sendMail(user.getEmail(),mailContent,"Active Mail");
        System.out.println("User saved!");
        return true;
    }

    /**
     * Active user by link sent in mail
     * @param code
     * @return
     */
    @Override
    public Boolean active(String code) {
        User u = dao.findUserByCode(code);
        if (u!=null) {
            dao.updateStatus(u);
            return true;
        }
        return false;
    }

    /**
     * Login
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return dao.findUserByUsernamePassword(user.getUsername(),user.getPassword());
    }
}
