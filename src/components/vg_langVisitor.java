// Generated from C:/Users/hodif/Desktop/usn2024/vg lang/grammar/vg_lang.g4 by ANTLR 4.13.2
package components;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link vg_langParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface vg_langVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link vg_langParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(vg_langParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(vg_langParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(vg_langParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(vg_langParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#leftHandSide}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeftHandSide(vg_langParser.LeftHandSideContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#printStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStatement(vg_langParser.PrintStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(vg_langParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOrExpression(vg_langParser.LogicalOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndExpression(vg_langParser.LogicalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(vg_langParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#relationalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpression(vg_langParser.RelationalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(vg_langParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(vg_langParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(vg_langParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#postfixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixExpression(vg_langParser.PostfixExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(vg_langParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(vg_langParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(vg_langParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#comments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComments(vg_langParser.CommentsContext ctx);
}