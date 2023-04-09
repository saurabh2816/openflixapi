package com.saurabh.netflixapi.controller;


import com.saurabh.netflixapi.service.PushBulletService;
import lombok.extern.slf4j.Slf4j;
import org.silentsoft.pushbullet.api.Device;
import org.silentsoft.pushbullet.api.PushbulletAPI;
import org.silentsoft.pushbullet.api.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/pb/")public class PushBulletController {


    private final PushBulletService pushBulletService;

    public PushBulletController(PushBulletService pushBulletService) {
        this.pushBulletService = pushBulletService;
    }


    // create a get mapping called 'get' using pushbulletservice
    @RequestMapping("/get")
    public List<Device> get() throws Exception {
        PushbulletAPI pushBulletAPI = new PushbulletAPI();
        var res = pushBulletAPI.getDevices("o.xEPSXt9hNrZweMgU5jfyM4y5VtM7eWTz");
//        var body = "Saurabh Testing";
//        var response = pushBulletAPI.sendNote("o.xEPSXt9hNrZweMgU5jfyM4y5VtM7eWTz", PushbulletAPI.TargetType.device_iden, "ujxtLcGOGIesjCOQVuvXHg", "Title", body);
        return res;


    }




}
