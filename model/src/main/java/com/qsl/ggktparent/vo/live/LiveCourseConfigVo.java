package com.qsl.ggktparent.vo.live;

import com.qsl.ggktparent.model.live.LiveCourseConfig;
import com.qsl.ggktparent.model.live.LiveCourseGoods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "LiveCourseConfig")
public class LiveCourseConfigVo extends LiveCourseConfig {

	@ApiModelProperty(value = "商品列表")
	private List<LiveCourseGoods> liveCourseGoodsList;
}