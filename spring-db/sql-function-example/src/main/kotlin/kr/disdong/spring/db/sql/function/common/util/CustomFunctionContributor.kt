package kr.disdong.spring.db.sql.function.common.util

import org.hibernate.boot.model.FunctionContributions
import org.hibernate.boot.model.FunctionContributor
import org.hibernate.dialect.function.StandardSQLFunction
import org.hibernate.type.StandardBasicTypes

class CustomFunctionContributor : FunctionContributor {
    override fun contributeFunctions(functionContributions: FunctionContributions) {
        functionContributions.functionRegistry.register(
            "ST_DISTANCE_SPHERE",
            StandardSQLFunction("ST_DISTANCE_SPHERE", StandardBasicTypes.BIG_DECIMAL)
        )
    }
}
