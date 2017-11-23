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
import java.util.List;

import javax.servlet.ServletException;

import com.google.gson.GsonBuilder;

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

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    List<String> groups = getGroups(request);
    writeResponse(response);
  }

  private List<String> getGroups(SlingHttpServletRequest request) {
    List<String> list = new ArrayList<>();
    list.add("cp-static-author");
    return list;
  }

  private void writeResponse(SlingHttpServletResponse response)
      throws IOException {
    try (PrintWriter out = response.getWriter()) {
      new GsonBuilder()
          .create()
          .toJson(
              Collections.singletonMap("status", "ok"),
              out
          );
    } catch (IOException e) {
      response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
