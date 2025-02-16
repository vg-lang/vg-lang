// Generated from C:/Users/hodif/Desktop/usn2024/vg lang/grammar/vg_lang.g4 by ANTLR 4.13.2
package components;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link vg_langParser}.
 */
public interface vg_langListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link vg_langParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(vg_langParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(vg_langParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(vg_langParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(vg_langParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(vg_langParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(vg_langParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(vg_langParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(vg_langParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(vg_langParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(vg_langParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(vg_langParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(vg_langParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(vg_langParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(vg_langParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void enterForCondition(vg_langParser.ForConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void exitForCondition(vg_langParser.ForConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(vg_langParser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(vg_langParser.ForUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(vg_langParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(vg_langParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileStatement(vg_langParser.DoWhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileStatement(vg_langParser.DoWhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#arrayLiteral}.
	 * @param ctx the parse tree
	 */
	void enterArrayLiteral(vg_langParser.ArrayLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#arrayLiteral}.
	 * @param ctx the parse tree
	 */
	void exitArrayLiteral(vg_langParser.ArrayLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(vg_langParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(vg_langParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#variableDeclarationNoSemi}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarationNoSemi(vg_langParser.VariableDeclarationNoSemiContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#variableDeclarationNoSemi}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarationNoSemi(vg_langParser.VariableDeclarationNoSemiContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstDeclaration(vg_langParser.ConstDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstDeclaration(vg_langParser.ConstDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(vg_langParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(vg_langParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#assignmentNoSemi}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentNoSemi(vg_langParser.AssignmentNoSemiContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#assignmentNoSemi}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentNoSemi(vg_langParser.AssignmentNoSemiContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#leftHandSide}.
	 * @param ctx the parse tree
	 */
	void enterLeftHandSide(vg_langParser.LeftHandSideContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#leftHandSide}.
	 * @param ctx the parse tree
	 */
	void exitLeftHandSide(vg_langParser.LeftHandSideContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrintStatement(vg_langParser.PrintStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrintStatement(vg_langParser.PrintStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(vg_langParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(vg_langParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOrExpression(vg_langParser.LogicalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOrExpression(vg_langParser.LogicalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAndExpression(vg_langParser.LogicalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAndExpression(vg_langParser.LogicalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(vg_langParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(vg_langParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(vg_langParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(vg_langParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(vg_langParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(vg_langParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(vg_langParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(vg_langParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(vg_langParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(vg_langParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(vg_langParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(vg_langParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#elseIfStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatement(vg_langParser.ElseIfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#elseIfStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatement(vg_langParser.ElseIfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseStatement(vg_langParser.ElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseStatement(vg_langParser.ElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(vg_langParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(vg_langParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpression(vg_langParser.PostfixExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpression(vg_langParser.PostfixExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(vg_langParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(vg_langParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(vg_langParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(vg_langParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#functionReference}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReference(vg_langParser.FunctionReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#functionReference}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReference(vg_langParser.FunctionReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(vg_langParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(vg_langParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(vg_langParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(vg_langParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(vg_langParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(vg_langParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link vg_langParser#comments}.
	 * @param ctx the parse tree
	 */
	void enterComments(vg_langParser.CommentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link vg_langParser#comments}.
	 * @param ctx the parse tree
	 */
	void exitComments(vg_langParser.CommentsContext ctx);
}