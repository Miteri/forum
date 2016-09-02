package com.forum.server.services.implementations;

import com.forum.server.converters.ConversionListResultFactory;
import com.forum.server.converters.ConversionResultFactory;
import com.forum.server.dao.interfaces.MessagesDao;
import com.forum.server.dao.interfaces.ThemesDao;
import com.forum.server.dao.interfaces.TokensDao;
import com.forum.server.dao.interfaces.UsersDao;
import com.forum.server.dto.message.FixMessageDto;
import com.forum.server.dto.message.MessageDto;
import com.forum.server.dto.message.MessagesDto;
import com.forum.server.dto.theme.ThemeCreateDto;
import com.forum.server.dto.theme.ThemeDto;
import com.forum.server.dto.user.ShortUserDto;
import com.forum.server.models.message.Message;
import com.forum.server.models.theme.Theme;
import com.forum.server.models.theme.ThemeUpdate;
import com.forum.server.models.user.ShortUser;
import com.forum.server.security.exceptions.AuthException;
import com.forum.server.security.exceptions.NotFoundException;
import com.forum.server.services.interfaces.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
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
    private TokensDao tokensDao;

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

    //TODO Реализовать методы
    public ThemeDto createTheme(String token, ThemeCreateDto themeCreateDto) {
        if (!tokensDao.isExistsToken(token)) {
            throw new AuthException("Incorrect token");
        } else if (themeCreateDto.getTitle().equals("") || themeCreateDto.getMessage().equals("")) {
            throw new AuthException("Title or message not found");
        }
        ShortUser user = usersDao.findShortUserByToken(token);
        Theme theme = conversionResultFactory.convert(themeCreateDto);
        long userId = user.getUserId();
        theme.setUser(user);
        themesDao.save(theme);
        long themeId = themesDao.getIdByDateAndUserId(userId, theme.getDate());
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
                .MessagesCount(1l)
                .Title(theme.getTitle())
                .Messages(new MessagesDto(messageDtos))
                .build();
    }

    public ThemeDto getTheme(long themeId, Integer offset, int count) {
        if (!themesDao.themeIsExists(themeId)) {
            throw new NotFoundException("The theme isn't exists");
        }else if (offset == null) {
            offset = 0;
        }
        ThemeDto themeDto = conversionResultFactory.convert(themesDao.getThemeByThemeId(themeId));
        themeDto.setMessages(conversionListResultFactory
                .convertMessages(messagesDao
                        .getMessagesWithLimitOffset(themeId, count, offset)));
        return themeDto;
    }

    public ThemeDto updateTheme(String token, long themeId, String title, long offset, long count) {
        if (!tokensDao.isExistsToken(token)) {
            throw new AuthException("Incorrect token");
        } else if (title.equals("") || title == null) {
            throw new AuthException("Title not found");
        } else if (!themesDao.themeIsExists(themeId)) {
            throw new NotFoundException("The theme isn't exists");
        }
        if (themesDao.getAuthorIdByThemeId(themeId) != usersDao.findIdByToken(token)){
            throw new AuthException("Forbidden");
        }

        themesDao.saveUpdate(new ThemeUpdate.Builder()
                .Title(title)
                .build(),
                themeId);

        ThemeDto themeDto = conversionResultFactory.convert(themesDao.getThemeByThemeId(themeId));
        themeDto.setMessages(conversionListResultFactory
                .convertMessages(messagesDao
                        .getMessagesWithLimitOffset(themeId, count, offset)));
        return themeDto;
    }

    public void deleteTheme(String token, long themeId) {
        if (!themesDao.themeIsExists(themeId)) {
            throw new NotFoundException("The theme not exists");
        } else if (themesDao.getAuthorIdByThemeId(themeId) != usersDao.findIdByToken(token)){
            throw new AuthException("Forbidden");
        }
        themesDao.deleteTheme(themeId);
    }
}
