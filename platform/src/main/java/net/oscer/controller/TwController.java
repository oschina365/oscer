package net.oscer.controller;

import net.oscer.beans.Temperature;
import net.oscer.common.ApiResult;
import net.oscer.dao.TemperatureDAO;
import net.oscer.framework.FormatTool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 记录老婆体温变化
 *
 * @create 2019-08-06 17:21
 **/
@RequestMapping("/tw")
@Controller
public class TwController extends BaseController {

    @GetMapping
    public String index() {
        return "/tw/index";
    }

    /**
     * 添加一条温度记录
     *
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ApiResult add(@RequestParam("temperature") BigDecimal temperature) {
        String create_time = request.getParameter("create_time");

        Temperature t = new Temperature();
        t.setCreate_time(Timestamp.valueOf(create_time));
        t.setTemperature(temperature);

        if (t == null || t.getTemperature() == null) {
            return ApiResult.failWithMessage("请填写温度");
        }
        if (t == null || t.getCreate_time() == null) {
            return ApiResult.failWithMessage("请选择时间");
        }
        t.setUser(2L);
        t.setType(Temperature.TYPE_TEMPERATURE);
        t.setDay(Integer.valueOf(FormatTool.formatDate(t.getCreate_time(), "yyyyMMdd")));
        t.save();
        TemperatureDAO.ME.evict(2L, Temperature.TYPE_TEMPERATURE);
        return ApiResult.success();
    }

    /**
     * 查询温度记录列表
     *
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public ApiResult list() {
        List<Temperature> list = TemperatureDAO.ME.listByType(2L, Temperature.TYPE_TEMPERATURE);
        if (CollectionUtils.isEmpty(list)) {
            return ApiResult.success();
        }
        List<String> dates = list.stream().map(Temperature::formatCreateTime).collect(Collectors.toList());
        List<BigDecimal> nums = list.stream().map(Temperature::getTemperature).collect(Collectors.toList());
        Map<String, List> map = new HashMap<>(2);
        Collections.reverse(dates);
        Collections.reverse(nums);
        map.put("dates", dates);
        map.put("nums", nums);
        return ApiResult.successWithObject(map);
    }
}
