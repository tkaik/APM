package com.cognifide.cq.cqsm.graph;

import java.util.Arrays;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

public class CreateGroupsGraphParams {

  private final List<String> groupsIds;

  public CreateGroupsGraphParams(List<String> groupsIds) {
    this.groupsIds = groupsIds;
  }

  public static CreateGroupsGraphParams fromRequest(SlingHttpServletRequest request) {
    String groupParam = StringUtils.defaultString(request.getParameter("group"));
    String[] split = StringUtils.split(groupParam, ',');
    return new CreateGroupsGraphParams(Arrays.asList(split));
  }
}
