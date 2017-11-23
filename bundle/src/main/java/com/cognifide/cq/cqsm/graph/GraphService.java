package com.cognifide.cq.cqsm.graph;

import java.util.Iterator;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import com.google.common.collect.Lists;

@Component
@Service(GraphService.class)
public class GraphService {

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	public void createGraph(List<String> groupIds) {
		try {
			ResourceResolver resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
			UserManager userManager = resourceResolver.adaptTo(UserManager.class);

			Group group = (Group)userManager.getAuthorizable(groupIds.get(0));
			//println authorizable.getClass()

			Iterator<Authorizable> children = group.getDeclaredMembers(); //children
			Iterator<Group> parents = group.memberOf(); //children

			//println "Children"
			for (Authorizable a : Lists.newArrayList(children)) {
				//println a.getID()
			}

			//println "Parents"
			for (Authorizable a : Lists.newArrayList(parents)) {
			//	println a.getID()
			}
		} catch(LoginException | RepositoryException exc ) {

		}
	}
}
