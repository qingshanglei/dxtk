package com.qsl.ggktparent.user.api;


import com.alibaba.fastjson.JSON;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.model.user.UserInfo;
import com.qsl.ggktparent.user.service.UserInfoService;
import com.qsl.ggktparent.utils.JwtHelper;
import com.qsl.ggktparent.vo.vod.CourseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

@Slf4j
@Api(tags = "微信公众号授权登录")
@RestController // 注意：路由要重定向不能使用@RestController注解，因为注解自带@ResponseBody注解。 重定向必须使用@Controller注解，或ModelAndView返回值。。。
@RequestMapping("/api/user/wechat")
public class WechatController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private WxMpService wxMpService;

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    // ModelAndView：返回值可以让路径跳转功能。。。
    // 接收前台路径：http://qqkt2.vipqz1.91tunnel.com/quiquketan/course/14    此处用guiguketan替代#号是因为编码问题。。。。
    @ApiOperation("微信公众号授权跳转路径，路径重定向")
    @GetMapping("/authorize")
    public ModelAndView authorize(@ApiParam("returnUrl") @RequestParam("returnUrl")
                                          String returnUrl, HttpServletRequest req) {
        //      注意：oauth2buildAuthorizationUrl必须使用weixin-java-mp的2.7版本，4.1版本没有。
        String redirectURL = wxMpService.oauth2buildAuthorizationUrl(userInfoUrl,
                WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl)); // 路径不带 "#"
        //  URLEncoder.encode(returnUrl.replace("guiguketan", "#")) //  解决路径#"问题,路径guiguketan替换为#,
        log.error("====>WxConsts.OAUTH2_SCOPE_USER_INFO:", WxConsts.OAUTH2_SCOPE_USER_INFO);
        log.error("====> redirectURL: " + redirectURL);
        return new ModelAndView("redirect:" + redirectURL);
    }


    @ApiOperation("微信公众号授权登录")
    @GetMapping("/userInfo")
    public ModelAndView userInfo(@ApiParam(value = "code", required = true) @RequestParam("code") String code
            , @ApiParam(value = "returnUrl", required = true) @RequestParam("state") String returnUrl) {
        try {
            // 获取code
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = this.wxMpService.oauth2getAccessToken(code);

            String openId = wxMpOAuth2AccessToken.getOpenId();// oQK5P5uTS5BxR2dH3xkcagIfidnA
            log.error("微信网页授权openId：", openId);
            // 获取微信信息
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            log.error("微信网页授权wxMpUser：", JSON.toJSON(wxMpUser));
            // 根据OpenId查询
            UserInfo userInfo = userInfoService.getbyOpenId(openId);
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setOpenId(openId); // 小程序open id
                userInfo.setUnionId(wxMpUser.getUnionId()); // 微信开放平台unionID
                userInfo.setNickName(wxMpUser.getNickname()); //用户昵称
                userInfo.setAvatar(wxMpUser.getHeadImgUrl());//
                userInfo.setSex(wxMpUser.getSexId());
                userInfo.setProvince(wxMpUser.getProvince()); // 省份

                // 添加用户信息
                userInfoService.save(userInfo);
            }
            // 生成token
            String token = JwtHelper.createToken(userInfo.getId(), userInfo.getNickName());
            log.error("======>>>>>>>> token:", token, userInfo.getNickName());
            if (returnUrl.indexOf("?") == -1) { // 路径带？参数为-1
//                return "redirect:" + returnUrl + "?token=" + token;
                return new ModelAndView("redirect:" + returnUrl + "?token=" + token);
            } else {
                return new ModelAndView("redirect:" + returnUrl + "&token=" + token);
            }
        } catch (
                WxErrorException e) {
            throw new BusinessException(20001, "微信授权登录失败");
        }
    }
}
