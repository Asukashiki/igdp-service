package com.inspur.scenario.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.scenario.domain.ScenarioSquareLLmEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IScenarioSquareMapper extends BaseMapper<ScenarioSquareLLmEntity> {
    @Select({
            "<script>",
            " SELECT * FROM scenario_hub ",

            " WHERE visibility != '0' \n",
            "        <if test='category != null and category != \"\" '>\n",
            "            and category = #{category}\n",
            "        </if>",
            "        <if test='lyCategory != null and lyCategory != \"\" '>\n",
            "            and ly_category =#{lyCategory} ",
            "        </if>",
            "        <if test='zyCategory != null and zyCategory != \"\" '>\n",
            "            and zy_category =#{zyCategory}  ",
            "        </if>",
            " ORDER BY created_time desc",
            "</script>"
    })
    IPage<ScenarioSquareLLmEntity> pageList( IPage<ScenarioSquareLLmEntity> page,String category, String lyCategory, String zyCategory);
}
