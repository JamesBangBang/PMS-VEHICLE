package com.starnetsecurity.parkClientServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by JAMESBANG on 2018/2/7.
 */
@Controller
@RequestMapping("public/file")
public class PublicFileController {

    @RequestMapping(value = "/download")
    public String download(@RequestParam(value = "path") String path, HttpServletRequest request, HttpServletResponse response) throws IOException {

        path = URLDecoder.decode(path,"gbk");

        return "";
    }
}
