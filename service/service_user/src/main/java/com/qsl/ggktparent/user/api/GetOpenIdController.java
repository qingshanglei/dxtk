package com.qsl.ggktparent.user.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

@Api(tags = "获取微信用户openid") // 因为老师微信公众号只开放了获取openid的接口，
@Controller
@RequestMapping("/api/user/openid")
public class GetOpenIdController {
    @Autowired
    private WxMpService wxMpService;

    @ApiOperation("微信公众号授权跳转路径，路径重定向") // 注意：此处的授权登录是登录尚硅谷那边公众号获取openid的。
    @GetMapping("/authorize")
    public String authorize(@ApiParam("returnUrl") @RequestParam("returnUrl") String returnUrl
            , HttpServletRequest request) {
//        String userInfoUrl =
//                "http://4034ae16.r12.cpolar.top/api/user/openid/userInfo";
        String userInfoUrl =
                "http://ggkt.vipgz1.91tunnel.com/api/user/openid/userInfo";
        String redirectURL = wxMpService
                .oauth2buildAuthorizationUrl(userInfoUrl,
                        WxConsts.OAUTH2_SCOPE_USER_INFO,
                        URLEncoder.encode(returnUrl));
        return "redirect:" + redirectURL;
    }

    @ApiOperation("公众号获取openid")
    @GetMapping("/userInfo")
    @ResponseBody
    public String userInfo(@ApiParam("code") @RequestParam("code") String code,
                           @ApiParam("state") @RequestParam("state") String returnUrl) throws Exception {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = this.wxMpService.oauth2getAccessToken(code);
        String openId = wxMpOAuth2AccessToken.getOpenId();
        System.out.println("【微信网页授权】openId={}" + openId);
        return openId;
    }
}
