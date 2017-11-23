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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.cognifide.cq.cqsm.graph.data.Graph;
import com.cognifide.cq.cqsm.graph.data.Node;
import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

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

  private List<String> getGroups(SlingHttpServletRequest request) {
    List<String> list = new ArrayList<>();
    list.add("cp-static-author");
    return list;
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
    Graph g = new Graph();
    Node v1 = new Node("1", "Node 1");
    Node v2 = new Node("2", "Node 1");
    Node v3 = new Node("3", "Node 1");
    Node v4 = new Node("4", "Node 1");
    Node v5 = new Node("5", "Node 1");

    g.addEdge(v1, v3 );
    g.addEdge(v1, v2 );
    g.addEdge(v2, v4 );
    g.addEdge(v2, v5 );
    return graphService.createGraph("everyone");
  }
}
