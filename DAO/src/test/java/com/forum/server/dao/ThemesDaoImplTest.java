package com.forum.server.dao;

import com.forum.server.dao.configs.PersistenceConfig;
import com.forum.server.dao.interfaces.ThemesDao;
import com.forum.server.models.theme.Theme;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * 30.08.16
 *
 * @author Dinar Rafikov (First Software Engineering Platform)
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class ThemesDaoImplTest {

    private static final long THEME_ID = 9;
    private static final long USER_ID = 11;
    private static final long SECTION_ID = 1;
    private static final long SUBSECTION_ID = 1;
    private static final long THEME_DATE= 1472576342674L;
    private static final long MESSAGES_COUNT= 0;
    private static final String TITLE = "Spring JPA";
    private static final boolean STATUS = true;

    @Autowired
    private ThemesDao themesDao;

    @Test
    public void save() throws Exception {
        Theme theme = new Theme.Builder()
                .UserId(USER_ID)
                .SectionId(SECTION_ID)
                .SubsectionId(SUBSECTION_ID)
                .Title(TITLE)
                .Date(System.currentTimeMillis())
                .MessagesCount(MESSAGES_COUNT)
                .Status(STATUS)
                .build();
        themesDao.save(theme);
    }

    @Test
    public void getIdByDateAndUserId() throws Exception {
        assertEquals(THEME_ID, themesDao.getIdByDateAndUserId(USER_ID, THEME_DATE));
    }

}