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

package org.dromara.hmily.admin.service.login;

import org.dromara.hmily.admin.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * LoginServiceImpl.
 * @author xiaoyu(Myth)
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    /**
     * login success.
     * */
    private static Boolean loginSuccess = false;

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Value("${hmily.admin.userName}")
    private String userName;

    @Value("${hmily.admin.password}")
    private String password;

    @Override
    public Boolean login(final String userName, final String password) {
        LOGGER.info("输入的用户名密码为:{}", userName + "," + password);
        if (userName.equals(this.userName) && password.equals(this.password)) {
            loginSuccess = true;
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean logout() {
        loginSuccess = false;
        return Boolean.TRUE;
    }
    
    /**
     * Get is login or not.
     *
     * @return boolean
     * */
    public static Boolean isLoginSuccess() {
        return loginSuccess;
    }
}
