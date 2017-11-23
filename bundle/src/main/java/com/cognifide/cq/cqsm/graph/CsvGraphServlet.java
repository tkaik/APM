package com.cognifide.cq.cqsm.graph;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

@SlingServlet(
		methods = { HttpConstants.METHOD_GET},
		paths = {"/bin/createCsvGroupGraph"}
)
public class CsvGraphServlet extends SlingAllMethodsServlet {

	@Reference
	private CsvGraphService csvGraphService;

	@Reference
	private GraphService graphService;


	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		CreateGroupsGraphParams groupsParams = CreateGroupsGraphParams.fromRequest(request);
		String csvGraph = null;
		try {
			csvGraph = getCsvGraph(groupsParams);
		} catch (CreateGraphException e) {
			writeErrorResponse(response, e);
		}
		writeResponse(response, csvGraph);
	}

	private void writeResponse(SlingHttpServletResponse response, String csvGraph)
			throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"graph.csv\"");
		try(OutputStream outputStream = response.getOutputStream())
		{
			String outputResult = csvGraph;
			outputStream.write(outputResult.getBytes());
			outputStream.flush();
		} catch (IOException e) {
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	//FIXME:Copy paste here
	private void writeErrorResponse(SlingHttpServletResponse response, CreateGraphException exception)
			throws IOException {
		response.setStatus(400);
		try (PrintWriter out = response.getWriter()) {
			new GsonBuilder()
					.create()
					.toJson(
							ImmutableMap.builder()
									.put("status", 400)
									.put("message", exception.getMessage())
									.put("exception", ExceptionUtils.getStackTrace(exception))
									.build(),
							out
					);
		} catch (IOException e) {
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private String getCsvGraph(CreateGroupsGraphParams params) throws CreateGraphException {
		String everyone = params.getGroupsIds().stream().findFirst().orElse("everyone");
		return csvGraphService.createCsvGraphStructure(graphService.createGraph(everyone, params));
	}
}
