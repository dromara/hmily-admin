/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hmily.admin.configuration;

import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.interceptor.AuthInterceptor;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.spi.ExtensionLoader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Hmily admin configuration.
 *
 * @author xiaoyu
 */
@Configuration
@EnableConfigurationProperties(HmilyAdminProperties.class)
public class HmilyAdminConfiguration {
    
    /**
     * Cors configurer web mvc configurer.
     *
     * @return the web mvc configurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(final InterceptorRegistry registry) {
                registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**");
            }
        };
    }
    
    /**
     * Hmily repository service hmily repository service.
     *
     * @param hmilyAdminProperties the hmily admin properties
     * @return the hmily repository service
     */
    @Bean
    public HmilyRepositoryService hmilyRepositoryService(final HmilyAdminProperties hmilyAdminProperties) {
        HmilyRepositoryService repositoryService = ExtensionLoader.getExtensionLoader(HmilyRepositoryService.class).getJoin(hmilyAdminProperties.getRepository());
        repositoryService.init(hmilyAdminProperties);
        return repositoryService;
    }
}
