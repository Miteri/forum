package com.forum.server.controller;

import com.forum.server.dto.response.QueryResultDto;
import com.forum.server.dto.staticInfo.*;
import com.forum.server.services.interfaces.StaticInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.forum.server.utils.ResponseBuilder.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 06.08.16
 *
 * @author Dinar Rafikov (First Software Engineering Platform)
 * @version 1.0
 */
@Controller
public class StaticInfoController {

    @Autowired
    private StaticInfoService staticInfoService;

    @RequestMapping(value = "/sections", method = GET)
    public ResponseEntity<QueryResultDto> getSections() {
        SectionsDto sections = staticInfoService.getSections();
        return buildResponseGet(sections);
    }

    @RequestMapping(value = "/sections", method = POST)
    public ResponseEntity<QueryResultDto> createSection(@RequestBody SectionCreateDto createDto,
                                                      @RequestHeader(name = "Auth-Token") String token) {
        staticInfoService.createSection(token, createDto);
        return buildResponseGet(null);
    }

    @RequestMapping(value = "/sections", method = DELETE)
    public ResponseEntity<QueryResultDto> deleteSection(@RequestHeader(name = "Auth-Token") String token,
                                                        @RequestParam String section_url) {
        staticInfoService.deleteSection(token, section_url);
        return buildResponseDelete();
    }

    @RequestMapping(value = "/info", method = POST)
    public ResponseEntity<QueryResultDto> createStaticInfo(@RequestHeader(name = "Auth-Token") String token,
                                                           @RequestBody InfoCreateDto infoCreateDto) {
        staticInfoService.createInfo(token, infoCreateDto);
        return buildResponseGet(null);
    }

    @RequestMapping(value = "/info", method = GET)
    public ResponseEntity<QueryResultDto> getStaticInfo(@RequestParam("identifier") String identifier) {
        InfoDto infoDto = staticInfoService.getInfo(identifier);
        return buildResponseGet(infoDto);
    }

    @RequestMapping(value = "/info", method = DELETE)
    public ResponseEntity<QueryResultDto> deleteStaticInfo(@RequestHeader(name = "Auth-Token") String token,
                                                           @RequestParam("identifier") String identifier) {
        staticInfoService.deleteInfo(token, identifier);
        return buildResponseDelete();
    }
}
