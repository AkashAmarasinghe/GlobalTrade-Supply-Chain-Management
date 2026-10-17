package com.globaltrade.rest;

import com.globaltrade.bean.AlertBean;
import com.globaltrade.dto.AlertView;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/alerts")
@Produces(MediaType.APPLICATION_JSON)
public class AlertResource {
    @EJB private AlertBean alertBean;

    @GET
    public List<AlertView> open(@QueryParam("limit") @DefaultValue("5") int limit) {
        return alertBean.openAlerts(limit).stream().map(AlertView::new).toList();
    }
}
