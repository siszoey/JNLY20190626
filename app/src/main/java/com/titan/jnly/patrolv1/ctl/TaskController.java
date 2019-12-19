package com.titan.jnly.patrolv1.ctl;


import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.mock.annotation.GetMapping;
import com.lib.bandaid.data.remote.mock.annotation.PostMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestBody;
import com.lib.bandaid.data.remote.mock.annotation.RequestMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestParam;
import com.lib.bandaid.data.remote.mock.annotation.RestController;
import com.lib.bandaid.util.CollectUtil;
import com.titan.jnly.Config;
import com.titan.jnly.patrolv1.bean.ConserveLog;
import com.titan.jnly.patrolv1.bean.ConserveTask;
import com.titan.jnly.patrolv1.bean.PatrolLog;
import com.titan.jnly.patrolv1.bean.PatrolMsg;
import com.titan.jnly.patrolv1.bean.PatrolTask;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
        List<PatrolTask> list = CollectUtil.addRepeat(null, task, 30);
        return TTResult.Ok(list);
    }


    @GetMapping(value = "api/v1/patrol/log/list")
    public TTResult<List<PatrolLog>> getPatrolLogList(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageNumber") Integer size) {
        PatrolLog log = new PatrolLog();
        List<PatrolLog> list = CollectUtil.addRepeat(null, log, 30);
        return TTResult.Ok(list);
    }

    @PostMapping(value = "api/v1/patrol/log/del")
    public TTResult<Boolean> patrolLogDel(@RequestBody PatrolLog log) {
        return TTResult.Ok(true);
    }

    /**
     * 分页获取巡查信息列表
     *
     * @param pageNumber
     * @param size
     * @return
     */
    @GetMapping(value = "api/v1/patrol/msg/list")
    public TTResult<List<PatrolMsg>> getPatrolMsgList(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageNumber") Integer size) {
        PatrolMsg msg = new PatrolMsg();
        List<PatrolMsg> list = CollectUtil.addRepeat(null, msg, 30);
        return TTResult.Ok(list);
    }

    /**
     * 删除巡查消息
     *
     * @param msg
     * @return
     */
    @PostMapping(value = "api/v1/patrol/msg/del")
    public TTResult<Boolean> patrolMsgDel(@RequestBody PatrolMsg msg) {
        return TTResult.Ok(true);
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
        List<ConserveTask> list = CollectUtil.addRepeat(null, task, 30);
        return TTResult.Ok(list);
    }

    /**
     * list
     *
     * @param pageNumber
     * @param size
     * @return
     */
    @GetMapping(value = "api/v1/conserve/log/list")
    public TTResult<List<ConserveLog>> getConserveLogList(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageNumber") Integer size) {

        ConserveLog task = new ConserveLog();
        task.setId("1122");
        task.setDZBQH("1122");
        task.setGSBH("1122");
        List<ConserveLog> list = CollectUtil.addRepeat(null, task, 30);
        return TTResult.Ok(list);
    }


    @PostMapping(value = "api/v1/conserve/log/del")
    public TTResult<Boolean> getConserveLogList(@RequestBody ConserveLog log) {
        return TTResult.Ok(true);
    }
}
