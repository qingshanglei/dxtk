package com.qsl.ggktparent.wechat.controller;

import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.utils.AuthContextHolder;
import com.qsl.ggktparent.utils.Base64Util;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.wechat.WxJsapiSignatureVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "微信分享")
@RestController
@RequestMapping("/api/wechat/share")
public class ShareController {

    @Autowired
    private WxMpService wxMpService;

    @ApiOperation("微信分享功能")
    @GetMapping("getSignature")
    public Result getSignature(@RequestParam("url") String url) {
//        String currentUrl = url.replace("guiguketan", "#");
        System.out.println("url:" + url + "    id： " + AuthContextHolder.getUserId());
        log.error("url:", url, "id", AuthContextHolder.getUserId());
        try {
            WxJsapiSignature jsapiSignature = wxMpService.createJsapiSignature(url);
            WxJsapiSignatureVo wxJsapiSignatureVo = new WxJsapiSignatureVo();

            // jsapiSignature 数据复制到 wxJsapiSignatureVo
            BeanUtils.copyProperties(jsapiSignature, wxJsapiSignatureVo);
            wxJsapiSignatureVo.setUserEedId(Base64Util.base64Decode(AuthContextHolder.getUserId() + ""));

            return Result.ok(wxJsapiSignatureVo);
        } catch (WxErrorException e) {
            throw new BusinessException(20001, "微信分享失败");
        }
    }


}
