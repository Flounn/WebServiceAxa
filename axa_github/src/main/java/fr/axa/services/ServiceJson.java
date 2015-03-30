package fr.axa.services;

import java.sql.ResultSet;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.axa.application.Utilities;

@Path("/service_json")
public class ServiceJson {

	@GET()
	@Path("/top_ip")
	@Produces(MediaType.APPLICATION_JSON)
	public Response topIP(@QueryParam("limit") int n) {
		ResultSet rs = Utilities.callProcedureQuery("topIp", n);
		if (rs==null)
			return Utilities.createInternalServerError("Erreur lors de l'acc�s bdd");
		return Response.ok(Utilities.resulQueryInJson(rs),MediaType.APPLICATION_JSON).build();
	}

	@GET()
	@Path("/top_mots")
	@Produces(MediaType.APPLICATION_JSON)
	public Response topMotsCles(@QueryParam("limit") int n,@QueryParam("order") int o) {
		ResultSet rs ;
		if(o==0)
			rs = Utilities.callProcedureQuery("topMotsCles", n);
		else
			rs = Utilities.callProcedureQuery("topMotsClesOrderBy", n,o);
		if (rs==null)
			return Utilities.createInternalServerError("Erreur lors de l'acc�s bdd");
		return Response.ok(Utilities.resulQueryInJson(rs),MediaType.APPLICATION_JSON).build();
	}


}
