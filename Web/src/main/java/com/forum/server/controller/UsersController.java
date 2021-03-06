package com.forum.server.controller;

import com.forum.server.dto.response.QueryResultDto;
import com.forum.server.dto.user.ShortUserDto;
import com.forum.server.dto.user.UserUpdateDto;
import com.forum.server.dto.user.UserVerifyResultDto;
import com.forum.server.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.forum.server.utils.ResponseBuilder.buildResponseGet;
import static com.forum.server.utils.ResponseBuilder.buildResponsePostAndPut;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * 06.08.16
 *
 * @author Dinar Rafikov (First Software Engineering Platform)
 * @version 1.0
 */
@Controller
public class UsersController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users/{nickname}", method = GET)
    public ResponseEntity<QueryResultDto> getUser(@PathVariable("nickname") String nickname,
                                                  @RequestHeader(name = "Auth-Token", required = false) String token) {
        ShortUserDto user = userService.getUser(token, nickname);
        return buildResponseGet(user);
    }

    @RequestMapping(value = "/users/{nickname}", method = PUT)
    public ResponseEntity<QueryResultDto> updateUser(@PathVariable("nickname") String nickname,
                                                     @RequestHeader(name = "Auth-Token") String token,
                                                     @RequestBody UserUpdateDto userInfo) {
        ShortUserDto user = userService.updateUser(token, nickname, userInfo);
        return buildResponsePostAndPut(user);
    }

    @RequestMapping(value = "/", method = GET)
    public ResponseEntity<QueryResultDto> verifyUser(@RequestHeader(name = "Auth-Token", required = false) String token) {
        UserVerifyResultDto userVerifyResultDto =  userService.verify(token);
        return buildResponseGet(userVerifyResultDto);
    }
}
