package com.service.guiTest.controller.controller;

import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.guiTest.controller.domin.ElementData;
import com.service.guiTest.controller.domin.GuiGroupData;
import com.service.guiTest.service.service.GuiGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gui/group")
public class GuiGroupController {

    @Autowired
    private GuiGroupService guiGroupService;

    @PostMapping("add")
    @ResponseBody
    public ApiBaseRe add(@RequestBody GuiGroupData data, @RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        data.setProjectId(projectId);
        String msg =  guiGroupService.add(data);
        if("success".equals(msg)){
            baseRe.setCode(1);
            baseRe.setMsg("新增成功");
        }else {
            baseRe.setCode(0);
            baseRe.setMsg(msg);
        }

        return baseRe;

    }

    @GetMapping("list")
    @ResponseBody
    public ApiBaseRe getList(@RequestHeader(name = "projectId") Integer projectId){
        ApiBaseRe baseRe = new ApiBaseRe();
        baseRe.setData(guiGroupService.list(projectId));
        baseRe.setCode(1);
        return baseRe;
    }

    @GetMapping("one")
    @ResponseBody
    public ApiBaseRe getOne(@RequestParam Integer id){
        ApiBaseRe baseRe = new ApiBaseRe();
        baseRe.setData(guiGroupService.one(id));
        baseRe.setCode(1);
        return baseRe;
    }

    @PostMapping("update")
    @ResponseBody
    public ApiBaseRe update(@RequestBody GuiGroupData data){
        ApiBaseRe baseRe = new ApiBaseRe();
        String msg = guiGroupService.update(data);
        if("success".equals(msg)){
            baseRe.setCode(1);
            baseRe.setMsg("新增成功");
        }else {
            baseRe.setCode(0);
            baseRe.setMsg(msg);
        }
        return baseRe;
    }

}
