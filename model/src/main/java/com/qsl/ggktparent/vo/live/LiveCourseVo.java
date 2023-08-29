package com.qsl.ggktparent.vo.live;

import com.qsl.ggktparent.model.live.LiveCourse;
import com.qsl.ggktparent.model.vod.Teacher;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LiveCourseVo extends LiveCourse {

	@ApiModelProperty(value = "主播老师")
	private Teacher teacher;

	@ApiModelProperty(value = "直播状态")
	private Integer liveStatus;

	@ApiModelProperty(value = "直播开始时间")
	private String startTimeString;

	@ApiModelProperty(value = "直播结束时间")
	private String endTimeString;

}

