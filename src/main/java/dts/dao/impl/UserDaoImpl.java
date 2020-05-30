package dts.dao.impl;

import dts.dao.UserDao;
import dts.domain.User;
import dts.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao{
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findUserByUsername(String username) {
        User user = new User();
        try {
            String sql = "select * from tab_user where username = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
        } catch (Exception e) {
            System.out.println("null");
            return null;
        }
        System.out.println(user);
        System.out.println("==========================");
        return user;
    }

    @Override
    public void saveUser(User user) {
        String sql = "insert into tab_user (username,password,name,birthday,sex,telephone,email,status,code) values (?,?,?,?,?,?,?,?,?)";
        template.update(sql,user.getUsername(),user.getPassword(),user.getName(),user.getBirthday(),user.getSex(),user.getTelephone(),user.getEmail(),user.getStatus(),user.getCode());
    }

    /**
     * Find user by active code in mail sent
     * @param code
     * @return
     */
    @Override
    public User findUserByCode(String code) {
        User user = null;
        try {
            String sql = "select * from tab_user where code = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), code);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("user by code" + user);
        return user;
    }

    /**
     * active user by set status to 'Y'
     * @param u
     */
    @Override
    public void updateStatus(User u) {
        String sql = "update tab_user set status = 'Y' where code = ?";
        template.update(sql,u.getCode());
    }

    /**
     * find User By Username and Password
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findUserByUsernamePassword(String username, String password) {
        User user = null;
        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username, password);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(user);
        return user;
    }
}
