package com.cognifide.cq.cqsm.graph;

import java.util.Arrays;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

public class CreateGroupsGraphParams {

  private final List<String> groupsIds;

  private final boolean showParents;

  private final boolean showChildren;

  public CreateGroupsGraphParams(List<String> groupsIds, boolean showParents, boolean showChildren) {
    this.groupsIds = groupsIds;
    this.showParents = showParents;
    this.showChildren = showChildren;
  }

  public static CreateGroupsGraphParams fromRequest(SlingHttpServletRequest request) {
    String groupParam = StringUtils.defaultString(request.getParameter("group"));
    String[] split = StringUtils.split(groupParam, ',');

    boolean showParents = Boolean.valueOf(request.getParameter("showParents"));
    boolean showChildren = Boolean.valueOf(request.getParameter("showChildren"));

    return new CreateGroupsGraphParams(Arrays.asList(split), showParents, showChildren);
  }

  public List<String> getGroupsIds() {
    return groupsIds;
  }

  public boolean isShowParents() {
    return showParents;
  }

  public boolean isShowChildren() {
    return showChildren;
  }
}
