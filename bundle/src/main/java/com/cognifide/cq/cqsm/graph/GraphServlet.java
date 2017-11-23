/*-
 * ========================LICENSE_START=================================
 * AEM Permission Management
 * %%
 * Copyright (C) 2013 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package com.cognifide.cq.cqsm.graph;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import com.cognifide.cq.cqsm.graph.data.Graph;
import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;

@SlingServlet(
    methods = {HttpConstants.METHOD_GET},
    paths = {"/bin/createGroupGraph"}
)
public class GraphServlet extends SlingAllMethodsServlet {

  @Reference
  private GraphService graphService;

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    CreateGroupsGraphParams groupsParams = CreateGroupsGraphParams.fromRequest(request);
    Graph graph = getGraph(groupsParams);
    writeResponse(response, graph, groupsParams);
  }

  private void writeResponse(SlingHttpServletResponse response, Graph graph, CreateGroupsGraphParams params)
      throws IOException {
    try (PrintWriter out = response.getWriter()) {
      new GsonBuilder()
          .create()
          .toJson(
              ImmutableMap.builder()
              .put("data", graph)
              .put("params", params)
              .build(),
              out
          );
    } catch (IOException e) {
      response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private Graph getGraph(CreateGroupsGraphParams params) {
    String everyone = params.getGroupsIds().stream().findFirst().orElse("everyone");
    return graphService.createGraph(everyone);
  }
}
