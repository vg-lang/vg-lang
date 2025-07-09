// Generated from grammar/vg_lang.g4 by ANTLR 4.13.2
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
	 * Visit a parse tree produced by {@link vg_langParser#importStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportStatement(vg_langParser.ImportStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#importPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportPath(vg_langParser.ImportPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#libraryDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryDeclaration(vg_langParser.LibraryDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#namespaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceDeclaration(vg_langParser.NamespaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#functionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(vg_langParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(vg_langParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(vg_langParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(vg_langParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(vg_langParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#forCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForCondition(vg_langParser.ForConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#forUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForUpdate(vg_langParser.ForUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#forEachStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForEachStatement(vg_langParser.ForEachStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(vg_langParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#doWhileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoWhileStatement(vg_langParser.DoWhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#switchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchStatement(vg_langParser.SwitchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#switchCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCase(vg_langParser.SwitchCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#defaultCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultCase(vg_langParser.DefaultCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(vg_langParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(vg_langParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#arrayLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiteral(vg_langParser.ArrayLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(vg_langParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#variableDeclarationNoSemi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarationNoSemi(vg_langParser.VariableDeclarationNoSemiContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#constDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDeclaration(vg_langParser.ConstDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(vg_langParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#assignmentNoSemi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentNoSemi(vg_langParser.AssignmentNoSemiContext ctx);
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
	 * Visit a parse tree produced by {@link vg_langParser#functionReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReference(vg_langParser.FunctionReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdentifier(vg_langParser.QualifiedIdentifierContext ctx);
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
	 * Visit a parse tree produced by {@link vg_langParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(vg_langParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#elseIfStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStatement(vg_langParser.ElseIfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#elseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStatement(vg_langParser.ElseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#structDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDeclaration(vg_langParser.StructDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#structField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructField(vg_langParser.StructFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#enumDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDeclaration(vg_langParser.EnumDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#enumValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumValue(vg_langParser.EnumValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(vg_langParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#postfixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixExpression(vg_langParser.PostfixExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#postfixOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixOp(vg_langParser.PostfixOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(vg_langParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(vg_langParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(vg_langParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#tryStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryStatement(vg_langParser.TryStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#catchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchStatement(vg_langParser.CatchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#finallyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFinallyStatement(vg_langParser.FinallyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link vg_langParser#throwStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowStatement(vg_langParser.ThrowStatementContext ctx);
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