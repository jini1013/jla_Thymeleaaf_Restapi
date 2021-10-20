package jpastudy.jpashop.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {
    @RequestMapping("/flag")
    public String home() {
        log.info("home controller");
        return "home";  // template에 home.html 만든다
    }
    @GetMapping("/")
    public String index(){
        return "index";
    }
}