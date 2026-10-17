package com.globaltrade.rest;

import com.globaltrade.bean.VendorBean;
import com.globaltrade.entity.Vendor;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/vendors")
@Produces(MediaType.APPLICATION_JSON)
public class VendorResource {

    @EJB
    private VendorBean bean;

    @GET
    public List<Vendor> all() {
        return bean.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Vendor vendor) {
        Vendor saved = bean.create(vendor);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }
}
