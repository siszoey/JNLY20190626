package com.titan.jnly.patrolv1.ctl;


import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.mock.annotation.GetMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestParam;
import com.lib.bandaid.data.remote.mock.annotation.RestController;
import com.titan.jnly.Config;
import com.titan.jnly.patrolv1.bean.ConserveTask;
import com.titan.jnly.patrolv1.bean.PatrolTask;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(Config.BASE_URL.BaseUrl_Mock)
public class TaskController {


    /**
     * list
     *
     * @param pageNumber
     * @param size
     * @return
     */
    @GetMapping(value = "api/v1/patrol/task/list")
    public TTResult<List<PatrolTask>> getPatrolTaskList(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageNumber") Integer size) {

        PatrolTask task = new PatrolTask();
        task.setId("1122");
        task.setCUN_N("1122");
        task.setDZBQH("1122");
        task.setGSBH("1122");
        task.setJLDW("1122");

        List<PatrolTask> list = new ArrayList<>();
        list.add(task);
        list.add(task);
        list.add(task);
        list.add(task);
        list.add(task);
        return TTResult.Ok(list);
    }


    /**
     * list
     *
     * @param pageNumber
     * @param size
     * @return
     */
    @GetMapping(value = "api/v1/conserve/task/list")
    public TTResult<List<ConserveTask>> getConserveTaskList(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageNumber") Integer size) {

        ConserveTask task = new ConserveTask();
        task.setId("1122");
        task.setCUN_N("1122");
        task.setDZBQH("1122");
        task.setGSBH("1122");
        task.setJLDW("1122");

        List<ConserveTask> list = new ArrayList<>();
        list.add(task);
        list.add(task);
        list.add(task);
        list.add(task);
        list.add(task);
        return TTResult.Ok(list);
    }

}
