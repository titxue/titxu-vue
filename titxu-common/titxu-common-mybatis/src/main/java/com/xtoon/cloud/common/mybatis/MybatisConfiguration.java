package com.xtoon.cloud.common.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.xtoon.cloud.common.core.constant.CommonConstant;
import com.xtoon.cloud.common.core.util.TenantContext;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * mybatis-plus配置
 *
 * @author haoxin
 * @date 2021-06-07
 **/
@Slf4j
@Configuration
public class MybatisConfiguration {


    /**
     * 需要排除的多租户的表
     */
    private List<String> ignoreTables = Arrays.asList("sys_captcha", "sys_tenant", "sys_account", "sys_permission");

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(
                new TenantLineHandler() {

                    // 获取租户 ID 值表达式，只支持单个 ID 值
                    @Override
                    public Expression getTenantId() {
                        String tenant = TenantContext.getTenantId();
                        if (tenant != null) {
                            return new StringValue(TenantContext.getTenantId());
                        }
                        return new NullValue();
                    }

                    // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件,
                    // 这里设置 role表不需要该条件
                    @Override
                    public boolean ignoreTable(String tableName) {
                        return ignoreTables.stream().anyMatch(
                                (t) -> t.equalsIgnoreCase(tableName)
                        );
                    }

                    @Override
                    public String getTenantIdColumn() {
                        return CommonConstant.TENANT_ID;
                    }
                }));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
