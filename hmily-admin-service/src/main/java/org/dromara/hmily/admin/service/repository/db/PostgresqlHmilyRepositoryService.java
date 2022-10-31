package org.dromara.hmily.admin.service.repository.db;

import org.dromara.hmily.admin.page.PageParameter;

/**
 * PostgresqlHmilyRepositoryService.
 *
 * @author zhangwanjie3
 */
public class PostgresqlHmilyRepositoryService extends AbstractHmilyRepositoryService {
    
    @Override
    protected String bulidSqlByPage(final String sql, final PageParameter pageParameter) {
        return null;
    }
    
    @Override
    protected String buildTimeQueryCondition(final String time) {
        return null;
    }
}
