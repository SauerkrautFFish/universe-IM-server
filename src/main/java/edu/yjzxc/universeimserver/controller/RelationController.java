package edu.yjzxc.universeimserver.controller;

import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.response.CommonResponse;
import edu.yjzxc.universeimserver.service.RelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/relation")
@CrossOrigin("*")
@Slf4j
public class RelationController {
    @Resource
    RelationService relationService;

    @RequestMapping(method = RequestMethod.POST, value = "/addFriend")
    public CommonResponse addFriendRequest(@RequestAttribute("zxcId") Long zxcId, Long friendId) {
        log.info("[calling] addFriend(zxcId={}, friendId={})", zxcId, friendId);
        if(Objects.isNull(friendId)) {
            return CommonResponse.status(ResponseEnum.MISSING_PARAMS);
        }

        try {
            ResponseEnum responseEnum = relationService.addFriendRequest(zxcId, friendId);
            log.info("[finish] addFriend success to return. [responseEnum]={}", responseEnum);
            return CommonResponse.status(ResponseEnum.SUCCESS);
        } catch (Exception e) {
            log.error("[error] addFriend occur exception:{}", e.getMessage(), e);
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }

}
