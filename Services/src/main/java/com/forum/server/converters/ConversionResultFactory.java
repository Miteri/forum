package com.forum.server.converters;

import com.forum.server.converters.message.MessageTextToMessageConverter;
import com.forum.server.converters.staticInfo.InfoToInfoDtoConverter;
import com.forum.server.converters.theme.ThemeToThemeDtoConverter;
import com.forum.server.converters.user.AuthDtoToUserConverter;
import com.forum.server.converters.theme.ThemeCreateDtoToThemeConverter;
import com.forum.server.converters.user.ShortUserToShortUserDtoConverter;
import com.forum.server.dto.auth.AuthDto;
import com.forum.server.dto.staticInfo.InfoDto;
import com.forum.server.dto.theme.ThemeCreateDto;
import com.forum.server.dto.theme.ThemeDto;
import com.forum.server.dto.user.ShortUserDto;
import com.forum.server.models.message.Message;
import com.forum.server.models.staticInfo.Info;
import com.forum.server.models.theme.Theme;
import com.forum.server.models.user.ShortUser;
import com.forum.server.models.user.User;
import org.springframework.stereotype.Component;

/**
 * 15.08.16
 *
 * @author Dinar Rafikov (First Software Engineering Platform)
 * @version 1.0
 */
@Component
public class ConversionResultFactory {

    public User convert(AuthDto authDto) {
        AuthDtoToUserConverter authDtoToUserConverter = new AuthDtoToUserConverter();
        return authDtoToUserConverter.convert(authDto);
    }

    public Theme convert(ThemeCreateDto themeCreateDto) {
        ThemeCreateDtoToThemeConverter converter = new ThemeCreateDtoToThemeConverter();
        return converter.convert(themeCreateDto);
    }

    public Message convert(String message) {
        MessageTextToMessageConverter converter = new MessageTextToMessageConverter();
        return converter.convert(message);
    }

    public ShortUserDto convert(ShortUser shortUser) {
        ShortUserToShortUserDtoConverter converter = new ShortUserToShortUserDtoConverter();
        return converter.convert(shortUser);
    }

    public ThemeDto convert(Theme theme) {
        ThemeToThemeDtoConverter converter = new ThemeToThemeDtoConverter();
        return converter.convert(theme);
    }

    public InfoDto convert(Info info) {
        InfoToInfoDtoConverter converter = new InfoToInfoDtoConverter();
        return converter.convert(info);
    }
}
