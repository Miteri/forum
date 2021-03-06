package com.forum.server.converters.theme;

import com.forum.server.dto.theme.ThemeCreateDto;
import com.forum.server.models.theme.Theme;
import org.springframework.core.convert.converter.Converter;

/**
 * 30.08.16
 *
 * @author Dinar Rafikov (First Software Engineering Platform)
 * @version 1.0
 */
public class ThemeCreateDtoToThemeConverter  implements Converter<ThemeCreateDto, Theme> {

    @Override
    public Theme convert(ThemeCreateDto themeCreateDto) {
        return new Theme.Builder()
                .SectionUrl(themeCreateDto.getSectionUrl())
                .Title(themeCreateDto.getTitle())
                .Date(System.currentTimeMillis())
                .MessagesCount(0l)
                .Status(true)
                .build();
    }
}
