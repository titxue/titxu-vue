package com.titxu.cloud.sys.api;

import com.titxu.cloud.sys.application.CaptchaApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码Controller
 **/
@Tag(name = "验证码管理")
@RestController
public class CaptchaController {


    private CaptchaApplicationService captchaApplicationService;

    @Autowired
    public void setCaptchaApplicationService(CaptchaApplicationService captchaApplicationService) {
        this.captchaApplicationService = captchaApplicationService;
    }

    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //获取图片验证码
        BufferedImage image = captchaApplicationService.getCaptcha(uuid);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }
}
