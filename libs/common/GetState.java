package com.yanni.common;

public class GetState {

	public static String getState(String State) {

		String StateHZ = "未知状态";

		if (State.equals("1")) {

			StateHZ = "新订单";

		} else if (State.equals("2")) {

			StateHZ = "已安排，施工中";

		} else if (State.equals("3")) {

			StateHZ = "已完成，未发佣金";

		} else if (State.equals("4")) {

			StateHZ = "已完成，已发佣金";

		} else if (State.equals("5")) {

			StateHZ = "取消";

		}

		return StateHZ;

	}

}
