package org.dromara.hmily.admin.controller;

import org.dromara.hmily.admin.annotation.Permission;
import org.dromara.hmily.admin.result.AjaxResponse;
import org.dromara.hmily.admin.service.ApplicationNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HmilyApplicationController
 *
 * @author zhangwanjie3
 */
@RestController
@RequestMapping("/application")
public class HmilyApplicationController {
    
   
    private final ApplicationNameService applicationNameService;
    
    @Autowired
    public HmilyApplicationController(final ApplicationNameService applicationNameService) {
        this.applicationNameService = applicationNameService;
    }
    
    @Permission
    @PostMapping(value = "/listAppName")
    public AjaxResponse listAppName() {
        final List<String> list = applicationNameService.list();
        return AjaxResponse.success(list);
    }
}
