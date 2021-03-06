package com.forum.server.services.implementations;

import com.forum.server.converters.ConversionListResultFactory;
import com.forum.server.converters.ConversionResultFactory;
import com.forum.server.dao.interfaces.MessagesDao;
import com.forum.server.dao.interfaces.TagsDao;
import com.forum.server.dao.interfaces.ThemesDao;
import com.forum.server.dao.interfaces.UsersDao;
import com.forum.server.dto.message.FixMessageDto;
import com.forum.server.dto.message.MessageDto;
import com.forum.server.dto.message.MessagesDto;
import com.forum.server.dto.tag.TagsDto;
import com.forum.server.dto.theme.ThemeCreateDto;
import com.forum.server.dto.theme.ThemeDto;
import com.forum.server.dto.theme.ThemeUpdateDto;
import com.forum.server.dto.user.ShortUserDto;
import com.forum.server.models.message.Message;
import com.forum.server.models.theme.Theme;
import com.forum.server.models.theme.ThemeUpdate;
import com.forum.server.models.user.ShortUser;
import com.forum.server.services.interfaces.ThemeService;
import com.forum.server.validation.RightsValidator;
import com.forum.server.validation.StaticInfoValidator;
import com.forum.server.validation.ThemeValidator;
import com.forum.server.validation.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 08.08.16
 *
 * @author Dinar Rafikov (First Software Engineering Platform)
 * @version 1.0
 */
@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private ThemesDao themesDao;

    @Autowired
    private MessagesDao messagesDao;

    @Autowired
    private ConversionResultFactory conversionResultFactory;

    @Autowired
    private ConversionListResultFactory conversionListResultFactory;

    @Autowired
    private TokenValidator tokenValidator;

    @Autowired
    private ThemeValidator themeValidator;

    @Autowired
    private RightsValidator rightsValidator;

    @Autowired
    private StaticInfoValidator staticInfoValidator;

    @Autowired
    private TagsDao tagsDao;

    public ThemeDto createTheme(String token, ThemeCreateDto themeCreateDto) {
        tokenValidator.verifyOnExistence(token);
        rightsValidator.createTheme(usersDao.getRightsByToken(token));
        themeValidator.verifyTitleOnNotNull(themeCreateDto.getTitle());
        themeValidator.verifyMessageOnNotNull(themeCreateDto.getMessage());
        staticInfoValidator.verifySectionOnExistence(themeCreateDto.getSectionUrl());
        ShortUser user = usersDao.findShortUserByToken(token);
        Theme theme = conversionResultFactory.convert(themeCreateDto);
        long userId = user.getUserId();
        theme.setUser(user);
        long themeId = themesDao.saveReturnId(theme);
        tagsDao.addTags(themeId, themeCreateDto.getTags());
        Message message = conversionResultFactory.convert(themeCreateDto.getMessage());
        message.setUser(user);
        message.setThemeId(themeId);
        messagesDao.save(message);
        message.setMessageId(messagesDao.getIdByUserIdAndDate(userId, message.getDate()));
        ShortUserDto userDto = conversionResultFactory.convert(user);
        List<MessageDto> messageDtos = new ArrayList<>();
        messageDtos.add(new MessageDto.Builder()
                .MessageId(message.getMessageId())
                .Author(userDto)
                .Date(message.getDate())
                .Message(message.getBody())
                .Rating(message.getRating())
                .Updated(new FixMessageDto.Builder()
                        .build())
                .build());
        return new ThemeDto.Builder()
                .ThemeId(themeId)
                .Date(theme.getDate())
                .AuthorId(userId)
                .Status(theme.isStatus())
                .MessagesCount(1L)
                .Title(theme.getTitle())
                .Messages(new MessagesDto(messageDtos))
                .Tags(conversionListResultFactory.convertTags(tagsDao.getTagsByThemeId(themeId)))
                .build();
    }

    public ThemeDto getTheme(String token, long themeId, Integer offset, int count) {
        themeValidator.verifyOnExistence(themeId);
        if (offset == null || offset < 0) {
            offset = 0;
        }
        ThemeDto themeDto = conversionResultFactory.convert(themesDao.getThemeByThemeId(themeId));
        themeDto.setTags(conversionListResultFactory.convertTags(tagsDao.getTagsByThemeId(themeId)));
        if (token == null || token.equals("")) {
            themeDto.setMessages(conversionListResultFactory
                    .convertMessages(messagesDao
                            .getMessagesWithLimitOffset(themeId, count, offset)));
            return themeDto;
        }
        tokenValidator.verifyOnExistence(token);
        themeDto.setMessages(conversionListResultFactory
                .convertMessages(messagesDao
                        .getMessagesWithLikedLimitOffset(usersDao.findIdByToken(token), themeId, count, offset)));
        return themeDto;
    }

    public ThemeDto updateTheme(String token, long themeId, ThemeUpdateDto theme, long count) {
        tokenValidator.verifyOnExistence(token);
        if (themesDao.getAuthorIdByThemeId(themeId) != usersDao.findIdByToken(token)) {
            String rights = usersDao.getRightsByToken(token);
            rightsValidator.updateTheme(rights);
        }
        themeValidator.verifyTitleOnNotNull(theme.getTitle());
        themeValidator.verifyOnExistence(themeId);
        staticInfoValidator.verifySectionOnExistence(theme.getSectionUrl());
        themesDao.saveUpdate(new ThemeUpdate.Builder()
                        .Title(theme.getTitle())
                        .SectionUrl(theme.getSectionUrl())
                        .build(),
                themeId);
        tagsDao.deleteTagsFromThemeByThemeId(themeId);
        tagsDao.addTags(themeId, theme.getTags());
        long offset = 0;
        ThemeDto themeDto = conversionResultFactory.convert(themesDao.getThemeByThemeId(themeId));
        themeDto.setMessages(conversionListResultFactory
                .convertMessages(messagesDao
                        .getMessagesWithLimitOffset(themeId, count, offset)));
        themeDto.setTags(new TagsDto(theme.getTags()));
        return themeDto;
    }

    public void deleteTheme(String token, long themeId) {
        //проверяем токен
        tokenValidator.verifyOnExistence(token);
        //смотрим есть ли такая тема
        themeValidator.verifyOnExistence(themeId);
        //является ли пользователь автором данной темы
        if (usersDao.findIdByToken(token) != themesDao.getAuthorIdByThemeId(themeId)) {
            //если не явлется проверяем его права
            String rights = usersDao.getRightsByToken(token);
            rightsValidator.deleteTheme(rights);
        }
        //удаляем тему
        tagsDao.deleteTagsFromThemeByThemeId(themeId);
        themesDao.deleteTheme(themeId);
    }

    public static void main(String[] args) {
        String lolo = "ЛолХЪ";
        System.out.println(lolo.matches("[A-Za-z0-9А-Яа-яёЁ]+"));
    }
}
