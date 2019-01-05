package com.vishaln.controllers;

import com.vishaln.domain.Statistic;
import com.vishaln.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for providing statistics to client.
 *
 * @author vishal_nagpure
 */
@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(final StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistics")
    @ResponseBody
    public Statistic getStatistics() {

        return statisticsService.getStatistics();
    }
}
