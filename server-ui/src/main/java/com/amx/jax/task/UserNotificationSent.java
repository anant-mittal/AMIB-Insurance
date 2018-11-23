package com.amx.jax.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.postman.model.UserMessageEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;

@TunnelEventMapping(byEvent = UserMessageEvent.class, scheme = TunnelEventXchange.TASK_WORKER)
public class UserNotificationSent implements ITunnelSubscriber<UserMessageEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, UserMessageEvent task) {

	}

}