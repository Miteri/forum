package com.forum.server.dao;

import com.forum.server.dao.configs.PersistenceConfig;
import com.forum.server.dao.interfaces.UsersDao;
import com.forum.server.models.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * 29.08.16
 *
 * @author Dinar Rafikov (First Software Engineering Platform)
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class UsersDaoImplTest {

    private static final String MAIL = "rafteri98@gmail.com";
    private static final String TOKEN = "token";
    private static final String NICKNAME = "Din";
    private static final String PASS_HASH = "lol";
    private static final long THEME_ID = 3;
    private static final int ID = 1;

    @Autowired
    private UsersDao usersDao;

    @Test
    public void findByToken() throws Exception {
        assertNotNull(usersDao.findByToken(TOKEN));
    }

    @Test
    public void findShortUserByToken() throws Exception {
        assertNotNull(usersDao.findShortUserByToken(TOKEN));
    }

    @Test
    public void isExistsMail() throws Exception {
        assertTrue(usersDao.isExistsMail(MAIL));
    }

    @Test
    public void getHashByMail() throws Exception {
        assertEquals(PASS_HASH, usersDao.getHashByMail(MAIL));
    }

    @Test
    public void getIdByMail() throws Exception {
        assertTrue(ID == usersDao.getIdByMail(MAIL));
    }

    @Test
    public void isExistsNickName() throws Exception {
        assertTrue(usersDao.isExistsNickName(NICKNAME));
    }

    @Test
    public void getHashByNickName() throws Exception {
        assertEquals(PASS_HASH, usersDao.getHashByNickName(NICKNAME));
    }

    @Test
    public void getIdByNickName() throws Exception {
        assertTrue(ID == usersDao.getIdByNickName(NICKNAME));
    }

    @Test
    public void save() throws Exception {
        User sasha = new User.Builder()
                .NickName("Sanya")
                .Rating(0l)
                .Avatar(null)
                .IsOnline(true)
                .Mail("sanya@gmail.com")
                .DateOfBirth(0l)
                .Info(null)
                .Rights("user")
                .RegistrationTime(0l)
                .LastSession(0l)
                .MessagesCount(0l)
                .ThemesCount(0l)
                .HashPassword("blol")
                .build();
        usersDao.save(sasha);
    }

    @Test
    public void getUserByThemeId() throws Exception {
        assertNotNull(usersDao.getUserByThemeId(THEME_ID));
    }
}