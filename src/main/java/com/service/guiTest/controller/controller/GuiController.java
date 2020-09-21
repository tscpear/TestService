package com.service.guiTest.controller.controller;

import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.guiTest.controller.domin.ElementData;
import com.service.guiTest.dom.entity.GuiData;
import com.service.guiTest.service.service.GuiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/gui")
public class GuiController {

    @Autowired
    private GuiService guiService;

    @PostMapping("add")
    @ResponseBody
    public ApiBaseRe add(@RequestBody ElementData data, @RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        data.setProjectId(projectId);
        guiService.add(data);
        baseRe.setCode(1);
        baseRe.setMsg("新增成功");
        return baseRe;

    }

    @GetMapping("list")
    @ResponseBody
    public ApiBaseRe get(@RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        List<GuiData> guiDataList = guiService.getData(projectId);
        baseRe.setData(guiDataList);
        baseRe.setCode(1);
        return baseRe;
    }

    @GetMapping("getOne")
    @ResponseBody
    public ApiBaseRe getOneData(@RequestParam Integer id) {
        ApiBaseRe baseRe = new ApiBaseRe();
        GuiData data = guiService.getOneData(id);
        baseRe.setCode(1);
        baseRe.setData(data);
        return baseRe;
    }

    @PostMapping("update")
    @ResponseBody
    public ApiBaseRe update(@RequestBody GuiData data) {
        ApiBaseRe baseRe = new ApiBaseRe();
        guiService.update(data);
        baseRe.setCode(1);
        baseRe.setMsg("编辑成功");
        return baseRe;
    }
}
