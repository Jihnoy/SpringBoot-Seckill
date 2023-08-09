package cn.jihnoy.service;

import cn.jihnoy.dao.UserDao;
import cn.jihnoy.domain.User;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getByid(int id){
        return userDao.getById(id);
    }

    @Transactional
    public boolean insert(){
        User u1 = new User();
        u1.setId(2);
        u1.setName("Noooo");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(3);
        u2.setName("123");
        userDao.insert(u2);

        return true;
    }
}
