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
package com.cognifide.cq.cqsm.foundation.actions.forauthorizable;

import com.cognifide.cq.cqsm.api.actions.Action;
import com.cognifide.cq.cqsm.api.actions.ActionResult;
import com.cognifide.cq.cqsm.api.exceptions.ActionExecutionException;
import com.cognifide.cq.cqsm.api.executors.Context;
import com.cognifide.cq.cqsm.api.utils.AuthorizablesUtils;
import com.cognifide.cq.cqsm.core.utils.MessagingUtils;

import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;

import javax.jcr.RepositoryException;

public class ForAuthorizable implements Action {

	private final String id;

	private final Boolean shouldBeGroup;

	public ForAuthorizable(final String id, final Boolean shouldBeGroup) {
		this.id = id;
		this.shouldBeGroup = shouldBeGroup;
	}

	@Override
	public ActionResult simulate(final Context context) {
		return process(context);
	}

	@Override
	public ActionResult execute(final Context context) {
		return process(context);
	}

	public ActionResult process(final Context context) {
		ActionResult actionResult = new ActionResult();
		try {

			if (shouldBeGroup) {
				Group group = AuthorizablesUtils.getGroup(context, id);
				context.setCurrentAuthorizable(group);
				actionResult.logMessage("Group with id: " + group.getID() + " set as current authorizable");
			} else {
				User user = AuthorizablesUtils.getUser(context, id);
				context.setCurrentAuthorizable(user);
				actionResult.logMessage("User with id: " + user.getID() + " set as current authorizable");
			}

		} catch (RepositoryException | ActionExecutionException e) {
			actionResult.logError(MessagingUtils.createMessage(e));
		}
		return actionResult;
	}

	@Override
	public boolean isGeneric() {
		return true;
	}

}
