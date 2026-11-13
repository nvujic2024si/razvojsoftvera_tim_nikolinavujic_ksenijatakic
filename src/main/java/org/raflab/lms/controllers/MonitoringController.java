package org.raflab.lms.controllers;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.response.TerminMonitoringResponse;
import org.raflab.lms.service.MonitoringService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitoring")
@AllArgsConstructor
public class MonitoringController {
    final MonitoringService monitoringService;

    // web aplikacija ovo poziva na svakih nekoliko sekundi (polling)
    @GetMapping("/termin/{terminId}")
    public TerminMonitoringResponse zaTermin(@PathVariable Long terminId) {
        return monitoringService.zaTermin(terminId);
    }
}