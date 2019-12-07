package com.titan.jnly.patrolv1.ctl;


import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.mock.annotation.GetMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestParam;
import com.lib.bandaid.data.remote.mock.annotation.RestController;
import com.titan.jnly.Config;
import com.titan.jnly.patrolv1.bean.PatrolTask;

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
    @GetMapping(value = "api/v1/patrol/list")
    public TTResult<List<PatrolTask>> getPatrolList(@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageNumber") Integer size) {

        return TTResult.Ok(null);
    }

}
