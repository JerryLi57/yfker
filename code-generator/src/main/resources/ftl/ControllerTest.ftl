package com.yfker.${ModuleName}.controller;

import com.yfker.controller.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @description: ${tableComment}模块ControllerTest 接口
 * @author: ${author}
 * @date: ${crateDate}
 **/
@Slf4j
public class ${ModelName}ControllerTest extends BaseControllerTest {

    @Test
    public void listByPageTest() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/${ModelNameLower}/list")
                .param("currentPage", "1")
                .param("pageSize", "5")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        ResultActions resultActions = this.mockMvc.perform(builder);
        String body = resultActions.andReturn().getResponse().getContentAsString();
        log.info("===========${ModelName}=listByPageTest=body:{}", body);
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

}