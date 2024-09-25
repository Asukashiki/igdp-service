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
            "            and lyCategory =#{lyCategory} ",
            "        </if>",
            "        <if test='zyCateGory != null and zyCateGory != \"\" '>\n",
            "            and zyCateGory =#{zyCateGory}  ",
            "        </if>",
            " ORDER BY created_time desc",
            "</script>"
    })
    IPage<ScenarioSquareLLmEntity> pageList(String category, String lyCategory, String zyCateGory, Integer pageNum, Integer pageSize);
}
