package com.titan.jnly.patrolv1.ctl;


import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.mock.annotation.GetMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestParam;
import com.lib.bandaid.data.remote.mock.annotation.RestController;
import com.titan.jnly.Config;
import com.titan.jnly.patrolv1.bean.ConserveTask;
import com.titan.jnly.patrolv1.bean.PatrolLog;
import com.titan.jnly.patrolv1.bean.PatrolMsg;
import com.titan.jnly.patrolv1.bean.PatrolTask;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.GET;

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


    @GetMapping(value = "api/v1/patrol/log/list")
    public TTResult<List<PatrolLog>> getPatrolLogList(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageNumber") Integer size) {
        PatrolLog log = new PatrolLog();
        List<PatrolLog> list = new ArrayList<>();
        list.add(log);
        list.add(log);
        list.add(log);
        list.add(log);
        list.add(log);
        return TTResult.Ok(list);
    }

    @GetMapping(value = "api/v1/patrol/msg/list")
    public TTResult<List<PatrolMsg>> getPatrolMsgList(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageNumber") Integer size) {
        PatrolMsg msg = new PatrolMsg();
        List<PatrolMsg> list = new ArrayList<>();
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);
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
