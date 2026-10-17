package com.globaltrade.rest;
import com.globaltrade.bean.SystemStatusBean;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
@Path("/status") public class SystemResource {
    @EJB private SystemStatusBean bean;
    @GET @Produces(MediaType.APPLICATION_JSON) public Response status() {
        return Response.ok("{\"status\":\"OK\",\"message\":\""+bean.getStatus()+"\"}").build();
    }
}
