package fr.axa.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import fr.axa.application.Utilities;

@Path("/service")
public class ServiceMessage {

	@POST()
	public Response postMotsCles(@Context HttpServletRequest request,@Context HttpServletResponse response,@FormParam("message") String message) {
		return addMessage(request, response, message);
	}

	@GET()
	public Response getMotsCles(@Context HttpServletRequest request,@Context HttpServletResponse response,@QueryParam("message") String message) {
		return addMessage(request, response, message);
	}

	private Response addMessage (HttpServletRequest request,HttpServletResponse response,String message){
		System.out.println("addMessage @"+request.getRemoteAddr()+ " message: "+message);
		//Derrière l'archi d'openShift la remoteaddress retourne l'ip de leur proxy
		
		String error = Utilities.callProcedureUpdateWithErrorOut("addMessage", Utilities.getClientIpAddr(request),message);
		if (error==null)
			try {
				request.getRequestDispatcher("index.html").forward(request, response);
				return null;
			} catch (Exception e) {
				return Utilities.createInternalServerError("Erreur lors de la redirection vers la page d'accueil");
			}
		else
			return Utilities.createBadRequest(error);
	}

}
