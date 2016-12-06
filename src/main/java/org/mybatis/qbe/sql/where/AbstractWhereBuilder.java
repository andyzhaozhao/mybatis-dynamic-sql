/**
 *    Copyright 2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.qbe.sql.where;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.mybatis.qbe.Condition;
import org.mybatis.qbe.sql.SqlCriterion;
import org.mybatis.qbe.sql.SqlField;

public abstract class AbstractWhereBuilder<T extends AbstractWhereBuilder<T>> {
    private List<SqlCriterion<?>> criteria = new ArrayList<>();
    
    protected <S> AbstractWhereBuilder(SqlField<S> field, Condition<S> condition, SqlCriterion<?>...subCriteria) {
        criteria.add(SqlCriterion.of(field, condition, subCriteria));
    }
    
    public <S> T and(SqlField<S> field, Condition<S> condition, SqlCriterion<?>...subCriteria) {
        criteria.add(SqlCriterion.of("and", field, condition, subCriteria)); //$NON-NLS-1$
        return getThis();
    }
    
    public <S> T or(SqlField<S> field, Condition<S> condition, SqlCriterion<?>...subCriteria) {
        criteria.add(SqlCriterion.of("or", field, condition, subCriteria)); //$NON-NLS-1$
        return getThis();
    }
    
    protected WhereSupport renderCriteria(Function<SqlField<?>, String> nameFunction) {
        return WhereSupport.of(nameFunction, criteria.stream());
    }
    
    public abstract T getThis();
}