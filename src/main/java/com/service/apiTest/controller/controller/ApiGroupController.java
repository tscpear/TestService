package com.service.apiTest.controller.controller;

import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.controller.domin.DoGroupRequest;
import com.service.apiTest.controller.domin.RequestApiGroup;
import com.service.apiTest.dom.domin.ApiGroupParamList;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.dom.entity.DoGroupReadyData;
import com.service.apiTest.service.domian.DoGroupOfRealyData;
import com.service.apiTest.service.service.ApiGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/apigroup")
public class ApiGroupController {
    @Autowired
    private ApiGroupService apiGroupService;

    @GetMapping("/list")
    @ResponseBody
    public ApiBaseRe getList(@RequestParam int page,
                             @RequestParam int limit,
                             @RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        ApiGroupParamList param = new ApiGroupParamList();
        param.setPageBegin(page * limit - limit);
        param.setPageEnd(page * limit);
        param.setProjectId(projectId);
        baseRe.setCode(1);
        baseRe.setData(apiGroupService.getList(param));
        return baseRe;
    }

    @PostMapping("/add")
    @ResponseBody
    public ApiBaseRe addGroup(@RequestHeader(name = "projectId") Integer projectId, @RequestBody RequestApiGroup requestApiGroup) {
        ApiBaseRe baseRe = new ApiBaseRe();
        requestApiGroup.setProjectId(projectId);
        apiGroupService.addGroup(requestApiGroup);
        baseRe.setCode(1);
        baseRe.setMsg("添加成功");
        return baseRe;
    }

    @GetMapping("/one")
    @ResponseBody
    public ApiBaseRe updateData(@RequestHeader(name = "projectId") Integer projectId, @RequestParam Integer id) {
        ApiBaseRe baseRe = new ApiBaseRe();
        RequestApiGroup requestApiGroup = apiGroupService.getRequestApiGroup(id, projectId);
        baseRe.setCode(1);
        baseRe.setData(requestApiGroup);
        return baseRe;
    }

    @PostMapping("/update")
    @ResponseBody
    public ApiBaseRe updateGroup(@RequestHeader(name = "projectId") Integer projectId, @RequestBody RequestApiGroup requestApiGroup) {
        ApiBaseRe baseRe = new ApiBaseRe();
        requestApiGroup.setProjectId(projectId);
        apiGroupService.updateGroup(requestApiGroup);
        baseRe.setCode(1);
        baseRe.setMsg("编辑成功");
        return baseRe;
    }

    @GetMapping("/del")
    @ResponseBody
    public ApiBaseRe delGroup(@RequestParam Integer id) {
        ApiBaseRe baseRe = new ApiBaseRe();
        apiGroupService.delGroup(id);
        baseRe.setCode(1);
        baseRe.setMsg("删除成功");
        return baseRe;
    }

    @GetMapping("ready")
    @ResponseBody
    public ApiBaseRe ready(@RequestParam Integer id, @RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        DoGroupOfRealyData doGroupReadyData = apiGroupService.getDoGroupReadyData(id, projectId);
        baseRe.setCode(1);
        baseRe.setData(doGroupReadyData);
        return baseRe;
    }


    @PostMapping("doOne")
    @ResponseBody
    public ApiBaseRe doOne(@RequestBody DoGroupRequest doGroupRequest,@RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        doGroupRequest.setProjectId(projectId);
        DoGroupOfRealyData doGroupOfRealyData = null;
        try {
            doGroupOfRealyData = apiGroupService.doOne(doGroupRequest); baseRe.setCode(1);
            baseRe.setData(doGroupOfRealyData);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return baseRe;
    }
}
