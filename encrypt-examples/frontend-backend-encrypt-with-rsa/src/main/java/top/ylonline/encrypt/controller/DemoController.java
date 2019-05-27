package top.ylonline.encrypt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.ylonline.encrypt.common.Const;
import top.ylonline.encrypt.util.AesUtils;
import top.ylonline.encrypt.util.RsaUtils;

/**
 * @author YL
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/aes")
    public String aes(Model model) {
        model.addAttribute("str", Const.STR);
        String key = AesUtils.randomKey();
        model.addAttribute("key", key);
        // 加密后的明文
        model.addAttribute("encrypt", AesUtils.encrypt(key, "UTF-8", Const.STR));
        return "aes";
    }

    @GetMapping("/rsa")
    public String rsa(Model model) {
        model.addAttribute("str", Const.STR);
        model.addAttribute("modulus", Const.MODULUS);
        // todo 私钥禁止传到前端（这里是为了测试），公钥可以传到前端
        model.addAttribute("privateExponent", Const.PRIVATE_EXPONENT);
        model.addAttribute("publicExponent", Const.PUBLIC_EXPONENT);

        // 私钥加密后的明文
        model.addAttribute("encrypt", RsaUtils.encryptPrivate(Const.MODULUS, Const.PRIVATE_EXPONENT, Const.STR));
        return "rsa";
    }

    @GetMapping("/rsa-and-aes")
    public String rsaAndAes(Model model) {
        String aesKey = "";
        model.addAttribute("str", Const.STR);
        model.addAttribute("modulus", Const.MODULUS);
        // todo 私钥禁止传到前端（这里是为了测试），公钥可以传到前端
        model.addAttribute("privateExponent", Const.PRIVATE_EXPONENT);
        model.addAttribute("publicExponent", Const.PUBLIC_EXPONENT);

        // aes 加密数据
        model.addAttribute("encryptData", AesUtils.encrypt(aesKey, "UTF-8", Const.STR));

        // rsa 加密 aes 密钥
        model.addAttribute("encryptAesKey", RsaUtils.encryptPrivate(Const.MODULUS, Const.PRIVATE_EXPONENT, aesKey));
        return "rsa-and-aes";
    }
}
