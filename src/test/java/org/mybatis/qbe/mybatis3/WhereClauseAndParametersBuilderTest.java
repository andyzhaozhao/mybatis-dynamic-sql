package org.mybatis.qbe.mybatis3;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mybatis.qbe.condition.Conditions.and;
import static org.mybatis.qbe.condition.Conditions.isEqualTo;
import static org.mybatis.qbe.condition.Conditions.isLessThan;
import static org.mybatis.qbe.condition.Conditions.or;
import static org.mybatis.qbe.mybatis3.WhereClauseAndParameters.where;

import java.sql.JDBCType;
import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.mybatis.qbe.field.Field;

public class WhereClauseAndParametersBuilderTest {
    public static final Field<Date> field1 = Field.of("field1", JDBCType.DATE);
    public static final Field<Integer> field2 = Field.of("field2", JDBCType.INTEGER);

    @Test
    public void testSimpleCriteria() {
        Date d = new Date();

        WhereClauseAndParameters whereClauseAndParameters = where(field1, isEqualTo(d))
                .or(field2, isEqualTo(4))
                .and(field2, isLessThan(3))
                .build();

        assertThat(whereClauseAndParameters.getWhereClause(), is("where field1 = #{parameters.p1,jdbcType=DATE} or field2 = #{parameters.p2,jdbcType=INTEGER} and field2 < #{parameters.p3,jdbcType=INTEGER}"));
        
        Map<String, Object> parameters = whereClauseAndParameters.getParameters();
        assertThat(parameters.get("p1"), is(d));
        assertThat(parameters.get("p2"), is(4));
        assertThat(parameters.get("p3"), is(3));
    }

    @Test
    public void testComplexCriteria() {
        Date d = new Date();

        WhereClauseAndParameters whereClauseAndParameters = where(field1, isEqualTo(d))
                .or(field2, isEqualTo(4))
                .and(field2, isLessThan(3))
                .or(field2, isEqualTo(4), and(field2, isEqualTo(6)))
                .and(field2, isLessThan(3), or(field1, isEqualTo(d)))
                .build();
        

        String expected = "where field1 = #{parameters.p1,jdbcType=DATE}" +
                " or field2 = #{parameters.p2,jdbcType=INTEGER}" +
                " and field2 < #{parameters.p3,jdbcType=INTEGER}" +
                " or (field2 = #{parameters.p4,jdbcType=INTEGER} and field2 = #{parameters.p5,jdbcType=INTEGER})" +
                " and (field2 < #{parameters.p6,jdbcType=INTEGER} or field1 = #{parameters.p7,jdbcType=DATE})";
        
        assertThat(whereClauseAndParameters.getWhereClause(), is(expected));
        
        Map<String, Object> parameters = whereClauseAndParameters.getParameters();
        assertThat(parameters.get("p1"), is(d));
        assertThat(parameters.get("p2"), is(4));
        assertThat(parameters.get("p3"), is(3));
        assertThat(parameters.get("p4"), is(4));
        assertThat(parameters.get("p5"), is(6));
        assertThat(parameters.get("p6"), is(3));
        assertThat(parameters.get("p7"), is(d));
    }
}
