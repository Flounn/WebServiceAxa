package fr.axa.application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public final class Utilities {

	private final static DataSource ds;
	private final static String jndi = "java:jboss/datasources/MySQLDS";

	static {
		DataSource datasource = null;
		try {
			Context initialContext = new InitialContext();
			datasource = (DataSource)initialContext.lookup(jndi);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		ds=datasource;
	}

	public static void callProcedureUpdate (String procedureName, Object...params){
		Connection connection = null;
		try {
			connection = ds.getConnection();
			CallableStatement cs = constructCall(connection,procedureName, params);
			cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String callProcedureUpdateWithErrorOut (String procedureName, Object...params){
		Connection connection = null;
		String result = null;
		try {
			connection = ds.getConnection();
			CallableStatement cs = constructCall(connection,procedureName,true, params);
			cs.executeUpdate();
			result = cs.getString(params.length+1);
		} catch (SQLException e) {
			e.printStackTrace();
			result = e.getMessage();
		}finally{
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static ResultSet callProcedureQuery (String procedureName, Object...params){
		ResultSet result = null;
		try {
			Connection connection = ds.getConnection();
			CallableStatement cs = constructCall(connection,procedureName, params);
			result = cs.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static CallableStatement constructCall (Connection connection, String procedureName, Object...params) throws SQLException{
		return constructCall (connection, procedureName,false,params);
	}

	private static CallableStatement constructCall (Connection connection,String procedureName, boolean errorOut, Object...params) throws SQLException{
		StringBuilder sb = new StringBuilder("{call "+procedureName+"(");
		int size = params.length;
		if (errorOut)
			size++;
		for (int i =0 ; i<size;i++)
			sb.append((i>0?",":"")+"?");
		sb.append(")}");

		CallableStatement c = connection.prepareCall(sb.toString());
		size = params.length;
		for (int i =0 ; i<size;i++)
			c.setObject(i+1, params[i]);
		if (errorOut)
			c.registerOutParameter(size+1, Types.VARCHAR);
		return c;
	}

	public static Response createBadRequest (String message){
		return createError(Status.BAD_REQUEST,message);
	}

	public static Response createInternalServerError (String message){
		return createError(Status.INTERNAL_SERVER_ERROR,message);
	}

	public static Response createError (Status status, String message){
		return Response.status(status).entity(message).build();
	}

	public static String resulQuery (ResultSet rs){
		if (rs==null)
			throw new NullPointerException();
		StringBuilder result = new StringBuilder();

		try {
			int nbColumn = rs.getMetaData().getColumnCount();
			while (rs.next()){
				for (int i =1; i<=nbColumn;i++)
					result.append(rs.getMetaData().getColumnLabel(i)+" : "+rs.getString(i)+" ");
				result.append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				rs.getStatement().getConnection().close();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	public static String resulQueryInJson (ResultSet rs){
		if (rs==null)
			throw new NullPointerException();
		StringBuilder result = new StringBuilder("list:[");
		try {
			int nbColumn = rs.getMetaData().getColumnCount();
			while (rs.next()){
				result.append((rs.isFirst()?"":",")+"{");
				for (int i =1; i<=nbColumn;i++)
					result.append("\""+rs.getMetaData().getColumnLabel(i)+"\":"+getJsonValue(rs.getObject(i))+(i<nbColumn?",":""));
				result.append("}");
			}
			result.append("]");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				rs.getStatement().getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	private static String getJsonValue(Object object){
		if (object instanceof String)
			return "\""+object.toString()+"\"";
		if (object instanceof byte[])
			return "\""+new String ((byte[])object)+"\""; 
		return object.toString();
	}

	private static final String[] HEADERS_TO_TRY = { 
	    "X-Forwarded-For",
	    "Proxy-Client-IP",
	    "WL-Proxy-Client-IP",
	    "HTTP_X_FORWARDED_FOR",
	    "HTTP_X_FORWARDED",
	    "HTTP_X_CLUSTER_CLIENT_IP",
	    "HTTP_CLIENT_IP",
	    "HTTP_FORWARDED_FOR",
	    "HTTP_FORWARDED",
	    "HTTP_VIA",
	    "REMOTE_ADDR" };

	public static String getClientIpAddr(HttpServletRequest request) {
	    for (String header : HEADERS_TO_TRY) {
	        String ip = request.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	            return ip;
	        }
	    }
	    return request.getRemoteAddr();
	}

}
