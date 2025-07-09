// Generated from grammar/vg_lang.g4 by ANTLR 4.13.2
package components;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class vg_langParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, DOC_COMMENT=51, SINGLE_LINE_COMMENT=52, 
		MULTI_LINE_COMMENT=53, IDENTIFIER=54, TRUE=55, FALSE=56, INT=57, DOUBLE=58, 
		STRING_LITERAL=59, WS=60;
	public static final int
		RULE_program = 0, RULE_statement = 1, RULE_importStatement = 2, RULE_importPath = 3, 
		RULE_libraryDeclaration = 4, RULE_namespaceDeclaration = 5, RULE_functionDeclaration = 6, 
		RULE_parameterList = 7, RULE_returnStatement = 8, RULE_forStatement = 9, 
		RULE_forInit = 10, RULE_forCondition = 11, RULE_forUpdate = 12, RULE_forEachStatement = 13, 
		RULE_whileStatement = 14, RULE_doWhileStatement = 15, RULE_switchStatement = 16, 
		RULE_switchCase = 17, RULE_defaultCase = 18, RULE_breakStatement = 19, 
		RULE_continueStatement = 20, RULE_arrayLiteral = 21, RULE_variableDeclaration = 22, 
		RULE_variableDeclarationNoSemi = 23, RULE_constDeclaration = 24, RULE_assignment = 25, 
		RULE_assignmentNoSemi = 26, RULE_leftHandSide = 27, RULE_printStatement = 28, 
		RULE_expression = 29, RULE_functionReference = 30, RULE_qualifiedIdentifier = 31, 
		RULE_logicalOrExpression = 32, RULE_logicalAndExpression = 33, RULE_equalityExpression = 34, 
		RULE_relationalExpression = 35, RULE_additiveExpression = 36, RULE_multiplicativeExpression = 37, 
		RULE_unaryExpression = 38, RULE_ifStatement = 39, RULE_elseIfStatement = 40, 
		RULE_elseStatement = 41, RULE_structDeclaration = 42, RULE_structField = 43, 
		RULE_enumDeclaration = 44, RULE_enumValue = 45, RULE_block = 46, RULE_postfixExpression = 47, 
		RULE_postfixOp = 48, RULE_primary = 49, RULE_functionCall = 50, RULE_argumentList = 51, 
		RULE_tryStatement = 52, RULE_catchStatement = 53, RULE_finallyStatement = 54, 
		RULE_throwStatement = 55, RULE_literal = 56, RULE_expressionStatement = 57, 
		RULE_comments = 58;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "statement", "importStatement", "importPath", "libraryDeclaration", 
			"namespaceDeclaration", "functionDeclaration", "parameterList", "returnStatement", 
			"forStatement", "forInit", "forCondition", "forUpdate", "forEachStatement", 
			"whileStatement", "doWhileStatement", "switchStatement", "switchCase", 
			"defaultCase", "breakStatement", "continueStatement", "arrayLiteral", 
			"variableDeclaration", "variableDeclarationNoSemi", "constDeclaration", 
			"assignment", "assignmentNoSemi", "leftHandSide", "printStatement", "expression", 
			"functionReference", "qualifiedIdentifier", "logicalOrExpression", "logicalAndExpression", 
			"equalityExpression", "relationalExpression", "additiveExpression", "multiplicativeExpression", 
			"unaryExpression", "ifStatement", "elseIfStatement", "elseStatement", 
			"structDeclaration", "structField", "enumDeclaration", "enumValue", "block", 
			"postfixExpression", "postfixOp", "primary", "functionCall", "argumentList", 
			"tryStatement", "catchStatement", "finallyStatement", "throwStatement", 
			"literal", "expressionStatement", "comments"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'import'", "';'", "'.'", "'*'", "'library'", "'{'", "'}'", "'namespace'", 
			"'function'", "'('", "')'", "','", "'return'", "'for'", "'var'", "':'", 
			"'while'", "'do'", "'switch'", "'case'", "'default'", "'break'", "'continue'", 
			"'['", "']'", "'='", "'const'", "'print'", "'&'", "'||'", "'&&'", "'=='", 
			"'!='", "'<'", "'<='", "'>'", "'>='", "'+'", "'-'", "'/'", "'%'", "'!'", 
			"'if'", "'else'", "'struct'", "'enum'", "'try'", "'catch'", "'finally'", 
			"'throw'", null, null, null, null, "'true'", "'false'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, "DOC_COMMENT", "SINGLE_LINE_COMMENT", "MULTI_LINE_COMMENT", 
			"IDENTIFIER", "TRUE", "FALSE", "INT", "DOUBLE", "STRING_LITERAL", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "vg_lang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public vg_langParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(vg_langParser.EOF, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1152055915047740962L) != 0)) {
				{
				{
				setState(118);
				statement();
				}
				}
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(124);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public PrintStatementContext printStatement() {
			return getRuleContext(PrintStatementContext.class,0);
		}
		public CommentsContext comments() {
			return getRuleContext(CommentsContext.class,0);
		}
		public IfStatementContext ifStatement() {
			return getRuleContext(IfStatementContext.class,0);
		}
		public ExpressionStatementContext expressionStatement() {
			return getRuleContext(ExpressionStatementContext.class,0);
		}
		public ConstDeclarationContext constDeclaration() {
			return getRuleContext(ConstDeclarationContext.class,0);
		}
		public ForStatementContext forStatement() {
			return getRuleContext(ForStatementContext.class,0);
		}
		public ForEachStatementContext forEachStatement() {
			return getRuleContext(ForEachStatementContext.class,0);
		}
		public FunctionDeclarationContext functionDeclaration() {
			return getRuleContext(FunctionDeclarationContext.class,0);
		}
		public ReturnStatementContext returnStatement() {
			return getRuleContext(ReturnStatementContext.class,0);
		}
		public TryStatementContext tryStatement() {
			return getRuleContext(TryStatementContext.class,0);
		}
		public ThrowStatementContext throwStatement() {
			return getRuleContext(ThrowStatementContext.class,0);
		}
		public WhileStatementContext whileStatement() {
			return getRuleContext(WhileStatementContext.class,0);
		}
		public DoWhileStatementContext doWhileStatement() {
			return getRuleContext(DoWhileStatementContext.class,0);
		}
		public SwitchStatementContext switchStatement() {
			return getRuleContext(SwitchStatementContext.class,0);
		}
		public BreakStatementContext breakStatement() {
			return getRuleContext(BreakStatementContext.class,0);
		}
		public ContinueStatementContext continueStatement() {
			return getRuleContext(ContinueStatementContext.class,0);
		}
		public LibraryDeclarationContext libraryDeclaration() {
			return getRuleContext(LibraryDeclarationContext.class,0);
		}
		public ImportStatementContext importStatement() {
			return getRuleContext(ImportStatementContext.class,0);
		}
		public StructDeclarationContext structDeclaration() {
			return getRuleContext(StructDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(148);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(126);
				variableDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(127);
				assignment();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(128);
				printStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(129);
				comments();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(130);
				ifStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(131);
				expressionStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(132);
				constDeclaration();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(133);
				forStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(134);
				forEachStatement();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(135);
				functionDeclaration();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(136);
				returnStatement();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(137);
				tryStatement();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(138);
				throwStatement();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(139);
				whileStatement();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(140);
				doWhileStatement();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(141);
				switchStatement();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(142);
				breakStatement();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(143);
				continueStatement();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(144);
				libraryDeclaration();
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(145);
				importStatement();
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(146);
				structDeclaration();
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(147);
				enumDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportStatementContext extends ParserRuleContext {
		public ImportPathContext importPath() {
			return getRuleContext(ImportPathContext.class,0);
		}
		public ImportStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterImportStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitImportStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitImportStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportStatementContext importStatement() throws RecognitionException {
		ImportStatementContext _localctx = new ImportStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_importStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(T__0);
			setState(151);
			importPath();
			setState(152);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportPathContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(vg_langParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(vg_langParser.IDENTIFIER, i);
		}
		public ImportPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importPath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterImportPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitImportPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitImportPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportPathContext importPath() throws RecognitionException {
		ImportPathContext _localctx = new ImportPathContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_importPath);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
			match(IDENTIFIER);
			setState(159);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(155);
					match(T__2);
					setState(156);
					match(IDENTIFIER);
					}
					} 
				}
				setState(161);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(162);
				match(T__2);
				setState(163);
				match(T__3);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LibraryDeclarationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public List<NamespaceDeclarationContext> namespaceDeclaration() {
			return getRuleContexts(NamespaceDeclarationContext.class);
		}
		public NamespaceDeclarationContext namespaceDeclaration(int i) {
			return getRuleContext(NamespaceDeclarationContext.class,i);
		}
		public LibraryDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_libraryDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterLibraryDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitLibraryDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitLibraryDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryDeclarationContext libraryDeclaration() throws RecognitionException {
		LibraryDeclarationContext _localctx = new LibraryDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_libraryDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			match(T__4);
			setState(167);
			match(IDENTIFIER);
			setState(168);
			match(T__5);
			setState(172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(169);
				namespaceDeclaration();
				}
				}
				setState(174);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(175);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceDeclarationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public List<FunctionDeclarationContext> functionDeclaration() {
			return getRuleContexts(FunctionDeclarationContext.class);
		}
		public FunctionDeclarationContext functionDeclaration(int i) {
			return getRuleContext(FunctionDeclarationContext.class,i);
		}
		public List<VariableDeclarationContext> variableDeclaration() {
			return getRuleContexts(VariableDeclarationContext.class);
		}
		public VariableDeclarationContext variableDeclaration(int i) {
			return getRuleContext(VariableDeclarationContext.class,i);
		}
		public List<ConstDeclarationContext> constDeclaration() {
			return getRuleContexts(ConstDeclarationContext.class);
		}
		public ConstDeclarationContext constDeclaration(int i) {
			return getRuleContext(ConstDeclarationContext.class,i);
		}
		public List<NamespaceDeclarationContext> namespaceDeclaration() {
			return getRuleContexts(NamespaceDeclarationContext.class);
		}
		public NamespaceDeclarationContext namespaceDeclaration(int i) {
			return getRuleContext(NamespaceDeclarationContext.class,i);
		}
		public NamespaceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterNamespaceDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitNamespaceDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitNamespaceDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDeclarationContext namespaceDeclaration() throws RecognitionException {
		NamespaceDeclarationContext _localctx = new NamespaceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_namespaceDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(T__7);
			setState(178);
			match(IDENTIFIER);
			setState(179);
			match(T__5);
			setState(186);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134251264L) != 0)) {
				{
				setState(184);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__8:
					{
					setState(180);
					functionDeclaration();
					}
					break;
				case T__14:
					{
					setState(181);
					variableDeclaration();
					}
					break;
				case T__26:
					{
					setState(182);
					constDeclaration();
					}
					break;
				case T__7:
					{
					setState(183);
					namespaceDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(189);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDeclarationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public FunctionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterFunctionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitFunctionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclarationContext functionDeclaration() throws RecognitionException {
		FunctionDeclarationContext _localctx = new FunctionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_functionDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(T__8);
			setState(192);
			match(IDENTIFIER);
			setState(193);
			match(T__9);
			setState(195);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(194);
				parameterList();
				}
			}

			setState(197);
			match(T__10);
			setState(198);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterListContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(vg_langParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(vg_langParser.IDENTIFIER, i);
		}
		public ParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitParameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(200);
			match(IDENTIFIER);
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(201);
				match(T__11);
				setState(202);
				match(IDENTIFIER);
				}
				}
				setState(207);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterReturnStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitReturnStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStatementContext returnStatement() throws RecognitionException {
		ReturnStatementContext _localctx = new ReturnStatementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_returnStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			match(T__12);
			setState(210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1134912329331246080L) != 0)) {
				{
				setState(209);
				expression();
				}
			}

			setState(212);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public ForConditionContext forCondition() {
			return getRuleContext(ForConditionContext.class,0);
		}
		public ForUpdateContext forUpdate() {
			return getRuleContext(ForUpdateContext.class,0);
		}
		public ForStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterForStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitForStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitForStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForStatementContext forStatement() throws RecognitionException {
		ForStatementContext _localctx = new ForStatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_forStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			match(T__13);
			setState(215);
			match(T__9);
			setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__14 || _la==IDENTIFIER) {
				{
				setState(216);
				forInit();
				}
			}

			setState(219);
			match(T__1);
			setState(221);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1134912329331246080L) != 0)) {
				{
				setState(220);
				forCondition();
				}
			}

			setState(223);
			match(T__1);
			setState(225);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(224);
				forUpdate();
				}
			}

			setState(227);
			match(T__10);
			setState(228);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForInitContext extends ParserRuleContext {
		public VariableDeclarationNoSemiContext variableDeclarationNoSemi() {
			return getRuleContext(VariableDeclarationNoSemiContext.class,0);
		}
		public AssignmentNoSemiContext assignmentNoSemi() {
			return getRuleContext(AssignmentNoSemiContext.class,0);
		}
		public ForInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterForInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitForInit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitForInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx = new ForInitContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_forInit);
		try {
			setState(232);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__14:
				enterOuterAlt(_localctx, 1);
				{
				setState(230);
				variableDeclarationNoSemi();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(231);
				assignmentNoSemi();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForConditionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterForCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitForCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitForCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForConditionContext forCondition() throws RecognitionException {
		ForConditionContext _localctx = new ForConditionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_forCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForUpdateContext extends ParserRuleContext {
		public AssignmentNoSemiContext assignmentNoSemi() {
			return getRuleContext(AssignmentNoSemiContext.class,0);
		}
		public ForUpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forUpdate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterForUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitForUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitForUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForUpdateContext forUpdate() throws RecognitionException {
		ForUpdateContext _localctx = new ForUpdateContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_forUpdate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			assignmentNoSemi();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForEachStatementContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ForEachStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forEachStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterForEachStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitForEachStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitForEachStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForEachStatementContext forEachStatement() throws RecognitionException {
		ForEachStatementContext _localctx = new ForEachStatementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_forEachStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			match(T__13);
			setState(239);
			match(T__9);
			setState(240);
			match(T__14);
			setState(241);
			match(IDENTIFIER);
			setState(242);
			match(T__15);
			setState(243);
			expression();
			setState(244);
			match(T__10);
			setState(245);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterWhileStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitWhileStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			match(T__16);
			setState(248);
			match(T__9);
			setState(249);
			expression();
			setState(250);
			match(T__10);
			setState(251);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DoWhileStatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DoWhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doWhileStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterDoWhileStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitDoWhileStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitDoWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoWhileStatementContext doWhileStatement() throws RecognitionException {
		DoWhileStatementContext _localctx = new DoWhileStatementContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_doWhileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			match(T__17);
			setState(254);
			block();
			setState(255);
			match(T__16);
			setState(256);
			match(T__9);
			setState(257);
			expression();
			setState(258);
			match(T__10);
			setState(259);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<SwitchCaseContext> switchCase() {
			return getRuleContexts(SwitchCaseContext.class);
		}
		public SwitchCaseContext switchCase(int i) {
			return getRuleContext(SwitchCaseContext.class,i);
		}
		public DefaultCaseContext defaultCase() {
			return getRuleContext(DefaultCaseContext.class,0);
		}
		public SwitchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterSwitchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitSwitchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitSwitchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchStatementContext switchStatement() throws RecognitionException {
		SwitchStatementContext _localctx = new SwitchStatementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_switchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
			match(T__18);
			setState(262);
			match(T__9);
			setState(263);
			expression();
			setState(264);
			match(T__10);
			setState(265);
			match(T__5);
			setState(269);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(266);
				switchCase();
				}
				}
				setState(271);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(272);
				defaultCase();
				}
			}

			setState(275);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchCaseContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public SwitchCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchCase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterSwitchCase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitSwitchCase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitSwitchCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseContext switchCase() throws RecognitionException {
		SwitchCaseContext _localctx = new SwitchCaseContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_switchCase);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			match(T__19);
			setState(278);
			expression();
			setState(279);
			match(T__15);
			setState(283);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1152055915047740962L) != 0)) {
				{
				{
				setState(280);
				statement();
				}
				}
				setState(285);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefaultCaseContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public DefaultCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultCase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterDefaultCase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitDefaultCase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitDefaultCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultCaseContext defaultCase() throws RecognitionException {
		DefaultCaseContext _localctx = new DefaultCaseContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_defaultCase);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(286);
			match(T__20);
			setState(287);
			match(T__15);
			setState(291);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1152055915047740962L) != 0)) {
				{
				{
				setState(288);
				statement();
				}
				}
				setState(293);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BreakStatementContext extends ParserRuleContext {
		public BreakStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterBreakStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitBreakStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(294);
			match(T__21);
			setState(295);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStatementContext extends ParserRuleContext {
		public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterContinueStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitContinueStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			match(T__22);
			setState(298);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayLiteralContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterArrayLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitArrayLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitArrayLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayLiteralContext arrayLiteral() throws RecognitionException {
		ArrayLiteralContext _localctx = new ArrayLiteralContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_arrayLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			match(T__23);
			setState(309);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1134912329331246080L) != 0)) {
				{
				setState(301);
				expression();
				setState(306);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__11) {
					{
					{
					setState(302);
					match(T__11);
					setState(303);
					expression();
					}
					}
					setState(308);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(311);
			match(T__24);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitVariableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
		VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_variableDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(313);
			match(T__14);
			setState(314);
			match(IDENTIFIER);
			setState(315);
			match(T__25);
			setState(316);
			expression();
			setState(317);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationNoSemiContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableDeclarationNoSemiContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarationNoSemi; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterVariableDeclarationNoSemi(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitVariableDeclarationNoSemi(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitVariableDeclarationNoSemi(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationNoSemiContext variableDeclarationNoSemi() throws RecognitionException {
		VariableDeclarationNoSemiContext _localctx = new VariableDeclarationNoSemiContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_variableDeclarationNoSemi);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
			match(T__14);
			setState(320);
			match(IDENTIFIER);
			setState(321);
			match(T__25);
			setState(322);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstDeclarationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ConstDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterConstDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitConstDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitConstDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstDeclarationContext constDeclaration() throws RecognitionException {
		ConstDeclarationContext _localctx = new ConstDeclarationContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_constDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			match(T__26);
			setState(325);
			match(IDENTIFIER);
			setState(326);
			match(T__25);
			setState(327);
			expression();
			setState(328);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignmentContext extends ParserRuleContext {
		public LeftHandSideContext leftHandSide() {
			return getRuleContext(LeftHandSideContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitAssignment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitAssignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			leftHandSide();
			setState(331);
			match(T__25);
			setState(332);
			expression();
			setState(333);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignmentNoSemiContext extends ParserRuleContext {
		public LeftHandSideContext leftHandSide() {
			return getRuleContext(LeftHandSideContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignmentNoSemiContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentNoSemi; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterAssignmentNoSemi(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitAssignmentNoSemi(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitAssignmentNoSemi(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentNoSemiContext assignmentNoSemi() throws RecognitionException {
		AssignmentNoSemiContext _localctx = new AssignmentNoSemiContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_assignmentNoSemi);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			leftHandSide();
			setState(336);
			match(T__25);
			setState(337);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LeftHandSideContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(vg_langParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(vg_langParser.IDENTIFIER, i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public LeftHandSideContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_leftHandSide; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterLeftHandSide(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitLeftHandSide(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitLeftHandSide(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LeftHandSideContext leftHandSide() throws RecognitionException {
		LeftHandSideContext _localctx = new LeftHandSideContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_leftHandSide);
		int _la;
		try {
			setState(352);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(339);
				match(IDENTIFIER);
				setState(346);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__23) {
					{
					{
					setState(340);
					match(T__23);
					setState(341);
					expression();
					setState(342);
					match(T__24);
					}
					}
					setState(348);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(349);
				match(IDENTIFIER);
				setState(350);
				match(T__2);
				setState(351);
				match(IDENTIFIER);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrintStatementContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public PrintStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_printStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterPrintStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitPrintStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitPrintStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrintStatementContext printStatement() throws RecognitionException {
		PrintStatementContext _localctx = new PrintStatementContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_printStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(354);
			match(T__27);
			setState(355);
			match(T__9);
			setState(356);
			expression();
			setState(361);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(357);
				match(T__11);
				setState(358);
				expression();
				}
				}
				setState(363);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(364);
			match(T__10);
			setState(365);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public LogicalOrExpressionContext logicalOrExpression() {
			return getRuleContext(LogicalOrExpressionContext.class,0);
		}
		public FunctionReferenceContext functionReference() {
			return getRuleContext(FunctionReferenceContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_expression);
		try {
			setState(369);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
			case T__23:
			case T__37:
			case T__38:
			case T__41:
			case IDENTIFIER:
			case TRUE:
			case FALSE:
			case INT:
			case DOUBLE:
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(367);
				logicalOrExpression();
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 2);
				{
				setState(368);
				functionReference();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionReferenceContext extends ParserRuleContext {
		public QualifiedIdentifierContext qualifiedIdentifier() {
			return getRuleContext(QualifiedIdentifierContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public FunctionReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterFunctionReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitFunctionReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitFunctionReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReferenceContext functionReference() throws RecognitionException {
		FunctionReferenceContext _localctx = new FunctionReferenceContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_functionReference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			match(T__28);
			setState(372);
			qualifiedIdentifier();
			setState(373);
			match(T__9);
			setState(375);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1134912329331246080L) != 0)) {
				{
				setState(374);
				argumentList();
				}
			}

			setState(377);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QualifiedIdentifierContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(vg_langParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(vg_langParser.IDENTIFIER, i);
		}
		public QualifiedIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterQualifiedIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitQualifiedIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitQualifiedIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedIdentifierContext qualifiedIdentifier() throws RecognitionException {
		QualifiedIdentifierContext _localctx = new QualifiedIdentifierContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_qualifiedIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379);
			match(IDENTIFIER);
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(380);
				match(T__2);
				setState(381);
				match(IDENTIFIER);
				}
				}
				setState(386);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LogicalOrExpressionContext extends ParserRuleContext {
		public List<LogicalAndExpressionContext> logicalAndExpression() {
			return getRuleContexts(LogicalAndExpressionContext.class);
		}
		public LogicalAndExpressionContext logicalAndExpression(int i) {
			return getRuleContext(LogicalAndExpressionContext.class,i);
		}
		public LogicalOrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalOrExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterLogicalOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitLogicalOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitLogicalOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalOrExpressionContext logicalOrExpression() throws RecognitionException {
		LogicalOrExpressionContext _localctx = new LogicalOrExpressionContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_logicalOrExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			logicalAndExpression();
			setState(392);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__29) {
				{
				{
				setState(388);
				match(T__29);
				setState(389);
				logicalAndExpression();
				}
				}
				setState(394);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LogicalAndExpressionContext extends ParserRuleContext {
		public List<EqualityExpressionContext> equalityExpression() {
			return getRuleContexts(EqualityExpressionContext.class);
		}
		public EqualityExpressionContext equalityExpression(int i) {
			return getRuleContext(EqualityExpressionContext.class,i);
		}
		public LogicalAndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalAndExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterLogicalAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitLogicalAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitLogicalAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalAndExpressionContext logicalAndExpression() throws RecognitionException {
		LogicalAndExpressionContext _localctx = new LogicalAndExpressionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_logicalAndExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			equalityExpression();
			setState(400);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__30) {
				{
				{
				setState(396);
				match(T__30);
				setState(397);
				equalityExpression();
				}
				}
				setState(402);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EqualityExpressionContext extends ParserRuleContext {
		public List<RelationalExpressionContext> relationalExpression() {
			return getRuleContexts(RelationalExpressionContext.class);
		}
		public RelationalExpressionContext relationalExpression(int i) {
			return getRuleContext(RelationalExpressionContext.class,i);
		}
		public EqualityExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalityExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterEqualityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitEqualityExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitEqualityExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualityExpressionContext equalityExpression() throws RecognitionException {
		EqualityExpressionContext _localctx = new EqualityExpressionContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_equalityExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			relationalExpression();
			setState(408);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__31 || _la==T__32) {
				{
				{
				setState(404);
				_la = _input.LA(1);
				if ( !(_la==T__31 || _la==T__32) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(405);
				relationalExpression();
				}
				}
				setState(410);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationalExpressionContext extends ParserRuleContext {
		public List<AdditiveExpressionContext> additiveExpression() {
			return getRuleContexts(AdditiveExpressionContext.class);
		}
		public AdditiveExpressionContext additiveExpression(int i) {
			return getRuleContext(AdditiveExpressionContext.class,i);
		}
		public RelationalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterRelationalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitRelationalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitRelationalExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationalExpressionContext relationalExpression() throws RecognitionException {
		RelationalExpressionContext _localctx = new RelationalExpressionContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_relationalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			additiveExpression();
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 257698037760L) != 0)) {
				{
				{
				setState(412);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 257698037760L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(413);
				additiveExpression();
				}
				}
				setState(418);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AdditiveExpressionContext extends ParserRuleContext {
		public List<MultiplicativeExpressionContext> multiplicativeExpression() {
			return getRuleContexts(MultiplicativeExpressionContext.class);
		}
		public MultiplicativeExpressionContext multiplicativeExpression(int i) {
			return getRuleContext(MultiplicativeExpressionContext.class,i);
		}
		public AdditiveExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterAdditiveExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitAdditiveExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitAdditiveExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveExpressionContext additiveExpression() throws RecognitionException {
		AdditiveExpressionContext _localctx = new AdditiveExpressionContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_additiveExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(419);
			multiplicativeExpression();
			setState(424);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__37 || _la==T__38) {
				{
				{
				setState(420);
				_la = _input.LA(1);
				if ( !(_la==T__37 || _la==T__38) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(421);
				multiplicativeExpression();
				}
				}
				setState(426);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicativeExpressionContext extends ParserRuleContext {
		public List<UnaryExpressionContext> unaryExpression() {
			return getRuleContexts(UnaryExpressionContext.class);
		}
		public UnaryExpressionContext unaryExpression(int i) {
			return getRuleContext(UnaryExpressionContext.class,i);
		}
		public MultiplicativeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterMultiplicativeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitMultiplicativeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitMultiplicativeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExpressionContext multiplicativeExpression() throws RecognitionException {
		MultiplicativeExpressionContext _localctx = new MultiplicativeExpressionContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_multiplicativeExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
			unaryExpression();
			setState(432);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3298534883344L) != 0)) {
				{
				{
				setState(428);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 3298534883344L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(429);
				unaryExpression();
				}
				}
				setState(434);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExpressionContext extends ParserRuleContext {
		public UnaryExpressionContext unaryExpression() {
			return getRuleContext(UnaryExpressionContext.class,0);
		}
		public PostfixExpressionContext postfixExpression() {
			return getRuleContext(PostfixExpressionContext.class,0);
		}
		public UnaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitUnaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitUnaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpressionContext unaryExpression() throws RecognitionException {
		UnaryExpressionContext _localctx = new UnaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_unaryExpression);
		int _la;
		try {
			setState(438);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__37:
			case T__38:
			case T__41:
				enterOuterAlt(_localctx, 1);
				{
				setState(435);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 5222680231936L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(436);
				unaryExpression();
				}
				break;
			case T__9:
			case T__23:
			case IDENTIFIER:
			case TRUE:
			case FALSE:
			case INT:
			case DOUBLE:
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(437);
				postfixExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementContext extends ParserRuleContext {
		public BlockContext ifBlock;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<ElseIfStatementContext> elseIfStatement() {
			return getRuleContexts(ElseIfStatementContext.class);
		}
		public ElseIfStatementContext elseIfStatement(int i) {
			return getRuleContext(ElseIfStatementContext.class,i);
		}
		public ElseStatementContext elseStatement() {
			return getRuleContext(ElseStatementContext.class,0);
		}
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_ifStatement);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(440);
			match(T__42);
			setState(441);
			match(T__9);
			setState(442);
			expression();
			setState(443);
			match(T__10);
			setState(444);
			((IfStatementContext)_localctx).ifBlock = block();
			setState(448);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(445);
					elseIfStatement();
					}
					} 
				}
				setState(450);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			}
			setState(452);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__43) {
				{
				setState(451);
				elseStatement();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseIfStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ElseIfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterElseIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitElseIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitElseIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseIfStatementContext elseIfStatement() throws RecognitionException {
		ElseIfStatementContext _localctx = new ElseIfStatementContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_elseIfStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			match(T__43);
			setState(455);
			match(T__42);
			setState(456);
			match(T__9);
			setState(457);
			expression();
			setState(458);
			match(T__10);
			setState(459);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseStatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ElseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterElseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitElseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitElseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseStatementContext elseStatement() throws RecognitionException {
		ElseStatementContext _localctx = new ElseStatementContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_elseStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			match(T__43);
			setState(462);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StructDeclarationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public List<StructFieldContext> structField() {
			return getRuleContexts(StructFieldContext.class);
		}
		public StructFieldContext structField(int i) {
			return getRuleContext(StructFieldContext.class,i);
		}
		public StructDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterStructDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitStructDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitStructDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructDeclarationContext structDeclaration() throws RecognitionException {
		StructDeclarationContext _localctx = new StructDeclarationContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_structDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(464);
			match(T__44);
			setState(465);
			match(IDENTIFIER);
			setState(466);
			match(T__5);
			setState(470);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(467);
				structField();
				}
				}
				setState(472);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(473);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StructFieldContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public StructFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterStructField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitStructField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitStructField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructFieldContext structField() throws RecognitionException {
		StructFieldContext _localctx = new StructFieldContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_structField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(475);
			match(IDENTIFIER);
			setState(476);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumDeclarationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public List<EnumValueContext> enumValue() {
			return getRuleContexts(EnumValueContext.class);
		}
		public EnumValueContext enumValue(int i) {
			return getRuleContext(EnumValueContext.class,i);
		}
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterEnumDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitEnumDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitEnumDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_enumDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			match(T__45);
			setState(479);
			match(IDENTIFIER);
			setState(480);
			match(T__5);
			setState(481);
			enumValue();
			setState(486);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(482);
				match(T__11);
				setState(483);
				enumValue();
				}
				}
				setState(488);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(489);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumValueContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public EnumValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterEnumValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitEnumValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitEnumValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueContext enumValue() throws RecognitionException {
		EnumValueContext _localctx = new EnumValueContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_enumValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(491);
			match(IDENTIFIER);
			setState(494);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__25) {
				{
				setState(492);
				match(T__25);
				setState(493);
				expression();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(496);
			match(T__5);
			setState(500);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1152055915047740962L) != 0)) {
				{
				{
				setState(497);
				statement();
				}
				}
				setState(502);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(503);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostfixExpressionContext extends ParserRuleContext {
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public List<PostfixOpContext> postfixOp() {
			return getRuleContexts(PostfixOpContext.class);
		}
		public PostfixOpContext postfixOp(int i) {
			return getRuleContext(PostfixOpContext.class,i);
		}
		public PostfixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterPostfixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitPostfixExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitPostfixExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostfixExpressionContext postfixExpression() throws RecognitionException {
		PostfixExpressionContext _localctx = new PostfixExpressionContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_postfixExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(505);
			primary();
			setState(509);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16778248L) != 0)) {
				{
				{
				setState(506);
				postfixOp();
				}
				}
				setState(511);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostfixOpContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PostfixOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfixOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterPostfixOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitPostfixOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitPostfixOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostfixOpContext postfixOp() throws RecognitionException {
		PostfixOpContext _localctx = new PostfixOpContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_postfixOp);
		int _la;
		try {
			setState(523);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				enterOuterAlt(_localctx, 1);
				{
				setState(512);
				match(T__2);
				setState(513);
				match(IDENTIFIER);
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(514);
				match(T__9);
				setState(516);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1134912329331246080L) != 0)) {
					{
					setState(515);
					argumentList();
					}
				}

				setState(518);
				match(T__10);
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(519);
				match(T__23);
				setState(520);
				expression();
				setState(521);
				match(T__24);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterPrimary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitPrimary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitPrimary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_primary);
		try {
			setState(532);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(525);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(526);
				match(IDENTIFIER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(527);
				match(T__9);
				setState(528);
				expression();
				setState(529);
				match(T__10);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(531);
				functionCall();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_functionCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(534);
			match(IDENTIFIER);
			}
			setState(535);
			match(T__9);
			setState(537);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1134912329331246080L) != 0)) {
				{
				setState(536);
				argumentList();
				}
			}

			setState(539);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(541);
			expression();
			setState(546);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(542);
				match(T__11);
				setState(543);
				expression();
				}
				}
				setState(548);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TryStatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<CatchStatementContext> catchStatement() {
			return getRuleContexts(CatchStatementContext.class);
		}
		public CatchStatementContext catchStatement(int i) {
			return getRuleContext(CatchStatementContext.class,i);
		}
		public FinallyStatementContext finallyStatement() {
			return getRuleContext(FinallyStatementContext.class,0);
		}
		public TryStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterTryStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitTryStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitTryStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryStatementContext tryStatement() throws RecognitionException {
		TryStatementContext _localctx = new TryStatementContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_tryStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(549);
			match(T__46);
			setState(550);
			block();
			setState(552); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(551);
				catchStatement();
				}
				}
				setState(554); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__47 );
			setState(557);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__48) {
				{
				setState(556);
				finallyStatement();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CatchStatementContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(vg_langParser.IDENTIFIER, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public CatchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterCatchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitCatchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitCatchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchStatementContext catchStatement() throws RecognitionException {
		CatchStatementContext _localctx = new CatchStatementContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_catchStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(559);
			match(T__47);
			setState(560);
			match(T__9);
			setState(561);
			match(IDENTIFIER);
			setState(562);
			match(T__10);
			setState(563);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FinallyStatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public FinallyStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_finallyStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterFinallyStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitFinallyStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitFinallyStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FinallyStatementContext finallyStatement() throws RecognitionException {
		FinallyStatementContext _localctx = new FinallyStatementContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_finallyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			match(T__48);
			setState(566);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ThrowStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ThrowStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_throwStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterThrowStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitThrowStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitThrowStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ThrowStatementContext throwStatement() throws RecognitionException {
		ThrowStatementContext _localctx = new ThrowStatementContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_throwStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(568);
			match(T__49);
			setState(569);
			expression();
			setState(570);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(vg_langParser.INT, 0); }
		public TerminalNode DOUBLE() { return getToken(vg_langParser.DOUBLE, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(vg_langParser.STRING_LITERAL, 0); }
		public ArrayLiteralContext arrayLiteral() {
			return getRuleContext(ArrayLiteralContext.class,0);
		}
		public TerminalNode TRUE() { return getToken(vg_langParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(vg_langParser.FALSE, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_literal);
		try {
			setState(578);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(572);
				match(INT);
				}
				break;
			case DOUBLE:
				enterOuterAlt(_localctx, 2);
				{
				setState(573);
				match(DOUBLE);
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(574);
				match(STRING_LITERAL);
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 4);
				{
				setState(575);
				arrayLiteral();
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 5);
				{
				setState(576);
				match(TRUE);
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 6);
				{
				setState(577);
				match(FALSE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterExpressionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitExpressionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitExpressionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionStatementContext expressionStatement() throws RecognitionException {
		ExpressionStatementContext _localctx = new ExpressionStatementContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_expressionStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(580);
			expression();
			setState(581);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CommentsContext extends ParserRuleContext {
		public TerminalNode SINGLE_LINE_COMMENT() { return getToken(vg_langParser.SINGLE_LINE_COMMENT, 0); }
		public TerminalNode MULTI_LINE_COMMENT() { return getToken(vg_langParser.MULTI_LINE_COMMENT, 0); }
		public TerminalNode DOC_COMMENT() { return getToken(vg_langParser.DOC_COMMENT, 0); }
		public CommentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).enterComments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof vg_langListener ) ((vg_langListener)listener).exitComments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof vg_langVisitor ) return ((vg_langVisitor<? extends T>)visitor).visitComments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentsContext comments() throws RecognitionException {
		CommentsContext _localctx = new CommentsContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_comments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(583);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 15762598695796736L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001<\u024a\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"+
		"-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u00071\u0002"+
		"2\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u00076\u0002"+
		"7\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0001\u0000\u0005\u0000"+
		"x\b\u0000\n\u0000\f\u0000{\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0095\b\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0005\u0003\u009e\b\u0003\n\u0003\f\u0003\u00a1\t\u0003\u0001\u0003\u0001"+
		"\u0003\u0003\u0003\u00a5\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0005\u0004\u00ab\b\u0004\n\u0004\f\u0004\u00ae\t\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0005\u0005\u00b9\b\u0005\n\u0005\f\u0005\u00bc"+
		"\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0003\u0006\u00c4\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00cc\b\u0007\n\u0007\f\u0007"+
		"\u00cf\t\u0007\u0001\b\u0001\b\u0003\b\u00d3\b\b\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\t\u0003\t\u00da\b\t\u0001\t\u0001\t\u0003\t\u00de\b\t"+
		"\u0001\t\u0001\t\u0003\t\u00e2\b\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0003\n\u00e9\b\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0005\u0010\u010c\b\u0010\n\u0010\f\u0010\u010f\t\u0010\u0001"+
		"\u0010\u0003\u0010\u0112\b\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u011a\b\u0011\n\u0011\f\u0011"+
		"\u011d\t\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u0122\b"+
		"\u0012\n\u0012\f\u0012\u0125\t\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0005\u0015\u0131\b\u0015\n\u0015\f\u0015\u0134\t\u0015\u0003"+
		"\u0015\u0136\b\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0005\u001b\u0159"+
		"\b\u001b\n\u001b\f\u001b\u015c\t\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0003\u001b\u0161\b\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0005\u001c\u0168\b\u001c\n\u001c\f\u001c\u016b\t\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0003\u001d\u0172"+
		"\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u0178"+
		"\b\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0005"+
		"\u001f\u017f\b\u001f\n\u001f\f\u001f\u0182\t\u001f\u0001 \u0001 \u0001"+
		" \u0005 \u0187\b \n \f \u018a\t \u0001!\u0001!\u0001!\u0005!\u018f\b!"+
		"\n!\f!\u0192\t!\u0001\"\u0001\"\u0001\"\u0005\"\u0197\b\"\n\"\f\"\u019a"+
		"\t\"\u0001#\u0001#\u0001#\u0005#\u019f\b#\n#\f#\u01a2\t#\u0001$\u0001"+
		"$\u0001$\u0005$\u01a7\b$\n$\f$\u01aa\t$\u0001%\u0001%\u0001%\u0005%\u01af"+
		"\b%\n%\f%\u01b2\t%\u0001&\u0001&\u0001&\u0003&\u01b7\b&\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0005\'\u01bf\b\'\n\'\f\'\u01c2\t\'"+
		"\u0001\'\u0003\'\u01c5\b\'\u0001(\u0001(\u0001(\u0001(\u0001(\u0001(\u0001"+
		"(\u0001)\u0001)\u0001)\u0001*\u0001*\u0001*\u0001*\u0005*\u01d5\b*\n*"+
		"\f*\u01d8\t*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001,\u0001,\u0001,\u0001"+
		",\u0001,\u0001,\u0005,\u01e5\b,\n,\f,\u01e8\t,\u0001,\u0001,\u0001-\u0001"+
		"-\u0001-\u0003-\u01ef\b-\u0001.\u0001.\u0005.\u01f3\b.\n.\f.\u01f6\t."+
		"\u0001.\u0001.\u0001/\u0001/\u0005/\u01fc\b/\n/\f/\u01ff\t/\u00010\u0001"+
		"0\u00010\u00010\u00030\u0205\b0\u00010\u00010\u00010\u00010\u00010\u0003"+
		"0\u020c\b0\u00011\u00011\u00011\u00011\u00011\u00011\u00011\u00031\u0215"+
		"\b1\u00012\u00012\u00012\u00032\u021a\b2\u00012\u00012\u00013\u00013\u0001"+
		"3\u00053\u0221\b3\n3\f3\u0224\t3\u00014\u00014\u00014\u00044\u0229\b4"+
		"\u000b4\f4\u022a\u00014\u00034\u022e\b4\u00015\u00015\u00015\u00015\u0001"+
		"5\u00015\u00016\u00016\u00016\u00017\u00017\u00017\u00017\u00018\u0001"+
		"8\u00018\u00018\u00018\u00018\u00038\u0243\b8\u00019\u00019\u00019\u0001"+
		":\u0001:\u0001:\u0000\u0000;\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPR"+
		"TVXZ\\^`bdfhjlnprt\u0000\u0006\u0001\u0000 !\u0001\u0000\"%\u0001\u0000"+
		"&\'\u0002\u0000\u0004\u0004()\u0002\u0000&\'**\u0001\u000035\u025b\u0000"+
		"y\u0001\u0000\u0000\u0000\u0002\u0094\u0001\u0000\u0000\u0000\u0004\u0096"+
		"\u0001\u0000\u0000\u0000\u0006\u009a\u0001\u0000\u0000\u0000\b\u00a6\u0001"+
		"\u0000\u0000\u0000\n\u00b1\u0001\u0000\u0000\u0000\f\u00bf\u0001\u0000"+
		"\u0000\u0000\u000e\u00c8\u0001\u0000\u0000\u0000\u0010\u00d0\u0001\u0000"+
		"\u0000\u0000\u0012\u00d6\u0001\u0000\u0000\u0000\u0014\u00e8\u0001\u0000"+
		"\u0000\u0000\u0016\u00ea\u0001\u0000\u0000\u0000\u0018\u00ec\u0001\u0000"+
		"\u0000\u0000\u001a\u00ee\u0001\u0000\u0000\u0000\u001c\u00f7\u0001\u0000"+
		"\u0000\u0000\u001e\u00fd\u0001\u0000\u0000\u0000 \u0105\u0001\u0000\u0000"+
		"\u0000\"\u0115\u0001\u0000\u0000\u0000$\u011e\u0001\u0000\u0000\u0000"+
		"&\u0126\u0001\u0000\u0000\u0000(\u0129\u0001\u0000\u0000\u0000*\u012c"+
		"\u0001\u0000\u0000\u0000,\u0139\u0001\u0000\u0000\u0000.\u013f\u0001\u0000"+
		"\u0000\u00000\u0144\u0001\u0000\u0000\u00002\u014a\u0001\u0000\u0000\u0000"+
		"4\u014f\u0001\u0000\u0000\u00006\u0160\u0001\u0000\u0000\u00008\u0162"+
		"\u0001\u0000\u0000\u0000:\u0171\u0001\u0000\u0000\u0000<\u0173\u0001\u0000"+
		"\u0000\u0000>\u017b\u0001\u0000\u0000\u0000@\u0183\u0001\u0000\u0000\u0000"+
		"B\u018b\u0001\u0000\u0000\u0000D\u0193\u0001\u0000\u0000\u0000F\u019b"+
		"\u0001\u0000\u0000\u0000H\u01a3\u0001\u0000\u0000\u0000J\u01ab\u0001\u0000"+
		"\u0000\u0000L\u01b6\u0001\u0000\u0000\u0000N\u01b8\u0001\u0000\u0000\u0000"+
		"P\u01c6\u0001\u0000\u0000\u0000R\u01cd\u0001\u0000\u0000\u0000T\u01d0"+
		"\u0001\u0000\u0000\u0000V\u01db\u0001\u0000\u0000\u0000X\u01de\u0001\u0000"+
		"\u0000\u0000Z\u01eb\u0001\u0000\u0000\u0000\\\u01f0\u0001\u0000\u0000"+
		"\u0000^\u01f9\u0001\u0000\u0000\u0000`\u020b\u0001\u0000\u0000\u0000b"+
		"\u0214\u0001\u0000\u0000\u0000d\u0216\u0001\u0000\u0000\u0000f\u021d\u0001"+
		"\u0000\u0000\u0000h\u0225\u0001\u0000\u0000\u0000j\u022f\u0001\u0000\u0000"+
		"\u0000l\u0235\u0001\u0000\u0000\u0000n\u0238\u0001\u0000\u0000\u0000p"+
		"\u0242\u0001\u0000\u0000\u0000r\u0244\u0001\u0000\u0000\u0000t\u0247\u0001"+
		"\u0000\u0000\u0000vx\u0003\u0002\u0001\u0000wv\u0001\u0000\u0000\u0000"+
		"x{\u0001\u0000\u0000\u0000yw\u0001\u0000\u0000\u0000yz\u0001\u0000\u0000"+
		"\u0000z|\u0001\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000|}\u0005\u0000"+
		"\u0000\u0001}\u0001\u0001\u0000\u0000\u0000~\u0095\u0003,\u0016\u0000"+
		"\u007f\u0095\u00032\u0019\u0000\u0080\u0095\u00038\u001c\u0000\u0081\u0095"+
		"\u0003t:\u0000\u0082\u0095\u0003N\'\u0000\u0083\u0095\u0003r9\u0000\u0084"+
		"\u0095\u00030\u0018\u0000\u0085\u0095\u0003\u0012\t\u0000\u0086\u0095"+
		"\u0003\u001a\r\u0000\u0087\u0095\u0003\f\u0006\u0000\u0088\u0095\u0003"+
		"\u0010\b\u0000\u0089\u0095\u0003h4\u0000\u008a\u0095\u0003n7\u0000\u008b"+
		"\u0095\u0003\u001c\u000e\u0000\u008c\u0095\u0003\u001e\u000f\u0000\u008d"+
		"\u0095\u0003 \u0010\u0000\u008e\u0095\u0003&\u0013\u0000\u008f\u0095\u0003"+
		"(\u0014\u0000\u0090\u0095\u0003\b\u0004\u0000\u0091\u0095\u0003\u0004"+
		"\u0002\u0000\u0092\u0095\u0003T*\u0000\u0093\u0095\u0003X,\u0000\u0094"+
		"~\u0001\u0000\u0000\u0000\u0094\u007f\u0001\u0000\u0000\u0000\u0094\u0080"+
		"\u0001\u0000\u0000\u0000\u0094\u0081\u0001\u0000\u0000\u0000\u0094\u0082"+
		"\u0001\u0000\u0000\u0000\u0094\u0083\u0001\u0000\u0000\u0000\u0094\u0084"+
		"\u0001\u0000\u0000\u0000\u0094\u0085\u0001\u0000\u0000\u0000\u0094\u0086"+
		"\u0001\u0000\u0000\u0000\u0094\u0087\u0001\u0000\u0000\u0000\u0094\u0088"+
		"\u0001\u0000\u0000\u0000\u0094\u0089\u0001\u0000\u0000\u0000\u0094\u008a"+
		"\u0001\u0000\u0000\u0000\u0094\u008b\u0001\u0000\u0000\u0000\u0094\u008c"+
		"\u0001\u0000\u0000\u0000\u0094\u008d\u0001\u0000\u0000\u0000\u0094\u008e"+
		"\u0001\u0000\u0000\u0000\u0094\u008f\u0001\u0000\u0000\u0000\u0094\u0090"+
		"\u0001\u0000\u0000\u0000\u0094\u0091\u0001\u0000\u0000\u0000\u0094\u0092"+
		"\u0001\u0000\u0000\u0000\u0094\u0093\u0001\u0000\u0000\u0000\u0095\u0003"+
		"\u0001\u0000\u0000\u0000\u0096\u0097\u0005\u0001\u0000\u0000\u0097\u0098"+
		"\u0003\u0006\u0003\u0000\u0098\u0099\u0005\u0002\u0000\u0000\u0099\u0005"+
		"\u0001\u0000\u0000\u0000\u009a\u009f\u00056\u0000\u0000\u009b\u009c\u0005"+
		"\u0003\u0000\u0000\u009c\u009e\u00056\u0000\u0000\u009d\u009b\u0001\u0000"+
		"\u0000\u0000\u009e\u00a1\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000"+
		"\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a4\u0001\u0000"+
		"\u0000\u0000\u00a1\u009f\u0001\u0000\u0000\u0000\u00a2\u00a3\u0005\u0003"+
		"\u0000\u0000\u00a3\u00a5\u0005\u0004\u0000\u0000\u00a4\u00a2\u0001\u0000"+
		"\u0000\u0000\u00a4\u00a5\u0001\u0000\u0000\u0000\u00a5\u0007\u0001\u0000"+
		"\u0000\u0000\u00a6\u00a7\u0005\u0005\u0000\u0000\u00a7\u00a8\u00056\u0000"+
		"\u0000\u00a8\u00ac\u0005\u0006\u0000\u0000\u00a9\u00ab\u0003\n\u0005\u0000"+
		"\u00aa\u00a9\u0001\u0000\u0000\u0000\u00ab\u00ae\u0001\u0000\u0000\u0000"+
		"\u00ac\u00aa\u0001\u0000\u0000\u0000\u00ac\u00ad\u0001\u0000\u0000\u0000"+
		"\u00ad\u00af\u0001\u0000\u0000\u0000\u00ae\u00ac\u0001\u0000\u0000\u0000"+
		"\u00af\u00b0\u0005\u0007\u0000\u0000\u00b0\t\u0001\u0000\u0000\u0000\u00b1"+
		"\u00b2\u0005\b\u0000\u0000\u00b2\u00b3\u00056\u0000\u0000\u00b3\u00ba"+
		"\u0005\u0006\u0000\u0000\u00b4\u00b9\u0003\f\u0006\u0000\u00b5\u00b9\u0003"+
		",\u0016\u0000\u00b6\u00b9\u00030\u0018\u0000\u00b7\u00b9\u0003\n\u0005"+
		"\u0000\u00b8\u00b4\u0001\u0000\u0000\u0000\u00b8\u00b5\u0001\u0000\u0000"+
		"\u0000\u00b8\u00b6\u0001\u0000\u0000\u0000\u00b8\u00b7\u0001\u0000\u0000"+
		"\u0000\u00b9\u00bc\u0001\u0000\u0000\u0000\u00ba\u00b8\u0001\u0000\u0000"+
		"\u0000\u00ba\u00bb\u0001\u0000\u0000\u0000\u00bb\u00bd\u0001\u0000\u0000"+
		"\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bd\u00be\u0005\u0007\u0000"+
		"\u0000\u00be\u000b\u0001\u0000\u0000\u0000\u00bf\u00c0\u0005\t\u0000\u0000"+
		"\u00c0\u00c1\u00056\u0000\u0000\u00c1\u00c3\u0005\n\u0000\u0000\u00c2"+
		"\u00c4\u0003\u000e\u0007\u0000\u00c3\u00c2\u0001\u0000\u0000\u0000\u00c3"+
		"\u00c4\u0001\u0000\u0000\u0000\u00c4\u00c5\u0001\u0000\u0000\u0000\u00c5"+
		"\u00c6\u0005\u000b\u0000\u0000\u00c6\u00c7\u0003\\.\u0000\u00c7\r\u0001"+
		"\u0000\u0000\u0000\u00c8\u00cd\u00056\u0000\u0000\u00c9\u00ca\u0005\f"+
		"\u0000\u0000\u00ca\u00cc\u00056\u0000\u0000\u00cb\u00c9\u0001\u0000\u0000"+
		"\u0000\u00cc\u00cf\u0001\u0000\u0000\u0000\u00cd\u00cb\u0001\u0000\u0000"+
		"\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u000f\u0001\u0000\u0000"+
		"\u0000\u00cf\u00cd\u0001\u0000\u0000\u0000\u00d0\u00d2\u0005\r\u0000\u0000"+
		"\u00d1\u00d3\u0003:\u001d\u0000\u00d2\u00d1\u0001\u0000\u0000\u0000\u00d2"+
		"\u00d3\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000\u0000\u00d4"+
		"\u00d5\u0005\u0002\u0000\u0000\u00d5\u0011\u0001\u0000\u0000\u0000\u00d6"+
		"\u00d7\u0005\u000e\u0000\u0000\u00d7\u00d9\u0005\n\u0000\u0000\u00d8\u00da"+
		"\u0003\u0014\n\u0000\u00d9\u00d8\u0001\u0000\u0000\u0000\u00d9\u00da\u0001"+
		"\u0000\u0000\u0000\u00da\u00db\u0001\u0000\u0000\u0000\u00db\u00dd\u0005"+
		"\u0002\u0000\u0000\u00dc\u00de\u0003\u0016\u000b\u0000\u00dd\u00dc\u0001"+
		"\u0000\u0000\u0000\u00dd\u00de\u0001\u0000\u0000\u0000\u00de\u00df\u0001"+
		"\u0000\u0000\u0000\u00df\u00e1\u0005\u0002\u0000\u0000\u00e0\u00e2\u0003"+
		"\u0018\f\u0000\u00e1\u00e0\u0001\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000"+
		"\u0000\u0000\u00e2\u00e3\u0001\u0000\u0000\u0000\u00e3\u00e4\u0005\u000b"+
		"\u0000\u0000\u00e4\u00e5\u0003\\.\u0000\u00e5\u0013\u0001\u0000\u0000"+
		"\u0000\u00e6\u00e9\u0003.\u0017\u0000\u00e7\u00e9\u00034\u001a\u0000\u00e8"+
		"\u00e6\u0001\u0000\u0000\u0000\u00e8\u00e7\u0001\u0000\u0000\u0000\u00e9"+
		"\u0015\u0001\u0000\u0000\u0000\u00ea\u00eb\u0003:\u001d\u0000\u00eb\u0017"+
		"\u0001\u0000\u0000\u0000\u00ec\u00ed\u00034\u001a\u0000\u00ed\u0019\u0001"+
		"\u0000\u0000\u0000\u00ee\u00ef\u0005\u000e\u0000\u0000\u00ef\u00f0\u0005"+
		"\n\u0000\u0000\u00f0\u00f1\u0005\u000f\u0000\u0000\u00f1\u00f2\u00056"+
		"\u0000\u0000\u00f2\u00f3\u0005\u0010\u0000\u0000\u00f3\u00f4\u0003:\u001d"+
		"\u0000\u00f4\u00f5\u0005\u000b\u0000\u0000\u00f5\u00f6\u0003\\.\u0000"+
		"\u00f6\u001b\u0001\u0000\u0000\u0000\u00f7\u00f8\u0005\u0011\u0000\u0000"+
		"\u00f8\u00f9\u0005\n\u0000\u0000\u00f9\u00fa\u0003:\u001d\u0000\u00fa"+
		"\u00fb\u0005\u000b\u0000\u0000\u00fb\u00fc\u0003\\.\u0000\u00fc\u001d"+
		"\u0001\u0000\u0000\u0000\u00fd\u00fe\u0005\u0012\u0000\u0000\u00fe\u00ff"+
		"\u0003\\.\u0000\u00ff\u0100\u0005\u0011\u0000\u0000\u0100\u0101\u0005"+
		"\n\u0000\u0000\u0101\u0102\u0003:\u001d\u0000\u0102\u0103\u0005\u000b"+
		"\u0000\u0000\u0103\u0104\u0005\u0002\u0000\u0000\u0104\u001f\u0001\u0000"+
		"\u0000\u0000\u0105\u0106\u0005\u0013\u0000\u0000\u0106\u0107\u0005\n\u0000"+
		"\u0000\u0107\u0108\u0003:\u001d\u0000\u0108\u0109\u0005\u000b\u0000\u0000"+
		"\u0109\u010d\u0005\u0006\u0000\u0000\u010a\u010c\u0003\"\u0011\u0000\u010b"+
		"\u010a\u0001\u0000\u0000\u0000\u010c\u010f\u0001\u0000\u0000\u0000\u010d"+
		"\u010b\u0001\u0000\u0000\u0000\u010d\u010e\u0001\u0000\u0000\u0000\u010e"+
		"\u0111\u0001\u0000\u0000\u0000\u010f\u010d\u0001\u0000\u0000\u0000\u0110"+
		"\u0112\u0003$\u0012\u0000\u0111\u0110\u0001\u0000\u0000\u0000\u0111\u0112"+
		"\u0001\u0000\u0000\u0000\u0112\u0113\u0001\u0000\u0000\u0000\u0113\u0114"+
		"\u0005\u0007\u0000\u0000\u0114!\u0001\u0000\u0000\u0000\u0115\u0116\u0005"+
		"\u0014\u0000\u0000\u0116\u0117\u0003:\u001d\u0000\u0117\u011b\u0005\u0010"+
		"\u0000\u0000\u0118\u011a\u0003\u0002\u0001\u0000\u0119\u0118\u0001\u0000"+
		"\u0000\u0000\u011a\u011d\u0001\u0000\u0000\u0000\u011b\u0119\u0001\u0000"+
		"\u0000\u0000\u011b\u011c\u0001\u0000\u0000\u0000\u011c#\u0001\u0000\u0000"+
		"\u0000\u011d\u011b\u0001\u0000\u0000\u0000\u011e\u011f\u0005\u0015\u0000"+
		"\u0000\u011f\u0123\u0005\u0010\u0000\u0000\u0120\u0122\u0003\u0002\u0001"+
		"\u0000\u0121\u0120\u0001\u0000\u0000\u0000\u0122\u0125\u0001\u0000\u0000"+
		"\u0000\u0123\u0121\u0001\u0000\u0000\u0000\u0123\u0124\u0001\u0000\u0000"+
		"\u0000\u0124%\u0001\u0000\u0000\u0000\u0125\u0123\u0001\u0000\u0000\u0000"+
		"\u0126\u0127\u0005\u0016\u0000\u0000\u0127\u0128\u0005\u0002\u0000\u0000"+
		"\u0128\'\u0001\u0000\u0000\u0000\u0129\u012a\u0005\u0017\u0000\u0000\u012a"+
		"\u012b\u0005\u0002\u0000\u0000\u012b)\u0001\u0000\u0000\u0000\u012c\u0135"+
		"\u0005\u0018\u0000\u0000\u012d\u0132\u0003:\u001d\u0000\u012e\u012f\u0005"+
		"\f\u0000\u0000\u012f\u0131\u0003:\u001d\u0000\u0130\u012e\u0001\u0000"+
		"\u0000\u0000\u0131\u0134\u0001\u0000\u0000\u0000\u0132\u0130\u0001\u0000"+
		"\u0000\u0000\u0132\u0133\u0001\u0000\u0000\u0000\u0133\u0136\u0001\u0000"+
		"\u0000\u0000\u0134\u0132\u0001\u0000\u0000\u0000\u0135\u012d\u0001\u0000"+
		"\u0000\u0000\u0135\u0136\u0001\u0000\u0000\u0000\u0136\u0137\u0001\u0000"+
		"\u0000\u0000\u0137\u0138\u0005\u0019\u0000\u0000\u0138+\u0001\u0000\u0000"+
		"\u0000\u0139\u013a\u0005\u000f\u0000\u0000\u013a\u013b\u00056\u0000\u0000"+
		"\u013b\u013c\u0005\u001a\u0000\u0000\u013c\u013d\u0003:\u001d\u0000\u013d"+
		"\u013e\u0005\u0002\u0000\u0000\u013e-\u0001\u0000\u0000\u0000\u013f\u0140"+
		"\u0005\u000f\u0000\u0000\u0140\u0141\u00056\u0000\u0000\u0141\u0142\u0005"+
		"\u001a\u0000\u0000\u0142\u0143\u0003:\u001d\u0000\u0143/\u0001\u0000\u0000"+
		"\u0000\u0144\u0145\u0005\u001b\u0000\u0000\u0145\u0146\u00056\u0000\u0000"+
		"\u0146\u0147\u0005\u001a\u0000\u0000\u0147\u0148\u0003:\u001d\u0000\u0148"+
		"\u0149\u0005\u0002\u0000\u0000\u01491\u0001\u0000\u0000\u0000\u014a\u014b"+
		"\u00036\u001b\u0000\u014b\u014c\u0005\u001a\u0000\u0000\u014c\u014d\u0003"+
		":\u001d\u0000\u014d\u014e\u0005\u0002\u0000\u0000\u014e3\u0001\u0000\u0000"+
		"\u0000\u014f\u0150\u00036\u001b\u0000\u0150\u0151\u0005\u001a\u0000\u0000"+
		"\u0151\u0152\u0003:\u001d\u0000\u01525\u0001\u0000\u0000\u0000\u0153\u015a"+
		"\u00056\u0000\u0000\u0154\u0155\u0005\u0018\u0000\u0000\u0155\u0156\u0003"+
		":\u001d\u0000\u0156\u0157\u0005\u0019\u0000\u0000\u0157\u0159\u0001\u0000"+
		"\u0000\u0000\u0158\u0154\u0001\u0000\u0000\u0000\u0159\u015c\u0001\u0000"+
		"\u0000\u0000\u015a\u0158\u0001\u0000\u0000\u0000\u015a\u015b\u0001\u0000"+
		"\u0000\u0000\u015b\u0161\u0001\u0000\u0000\u0000\u015c\u015a\u0001\u0000"+
		"\u0000\u0000\u015d\u015e\u00056\u0000\u0000\u015e\u015f\u0005\u0003\u0000"+
		"\u0000\u015f\u0161\u00056\u0000\u0000\u0160\u0153\u0001\u0000\u0000\u0000"+
		"\u0160\u015d\u0001\u0000\u0000\u0000\u01617\u0001\u0000\u0000\u0000\u0162"+
		"\u0163\u0005\u001c\u0000\u0000\u0163\u0164\u0005\n\u0000\u0000\u0164\u0169"+
		"\u0003:\u001d\u0000\u0165\u0166\u0005\f\u0000\u0000\u0166\u0168\u0003"+
		":\u001d\u0000\u0167\u0165\u0001\u0000\u0000\u0000\u0168\u016b\u0001\u0000"+
		"\u0000\u0000\u0169\u0167\u0001\u0000\u0000\u0000\u0169\u016a\u0001\u0000"+
		"\u0000\u0000\u016a\u016c\u0001\u0000\u0000\u0000\u016b\u0169\u0001\u0000"+
		"\u0000\u0000\u016c\u016d\u0005\u000b\u0000\u0000\u016d\u016e\u0005\u0002"+
		"\u0000\u0000\u016e9\u0001\u0000\u0000\u0000\u016f\u0172\u0003@ \u0000"+
		"\u0170\u0172\u0003<\u001e\u0000\u0171\u016f\u0001\u0000\u0000\u0000\u0171"+
		"\u0170\u0001\u0000\u0000\u0000\u0172;\u0001\u0000\u0000\u0000\u0173\u0174"+
		"\u0005\u001d\u0000\u0000\u0174\u0175\u0003>\u001f\u0000\u0175\u0177\u0005"+
		"\n\u0000\u0000\u0176\u0178\u0003f3\u0000\u0177\u0176\u0001\u0000\u0000"+
		"\u0000\u0177\u0178\u0001\u0000\u0000\u0000\u0178\u0179\u0001\u0000\u0000"+
		"\u0000\u0179\u017a\u0005\u000b\u0000\u0000\u017a=\u0001\u0000\u0000\u0000"+
		"\u017b\u0180\u00056\u0000\u0000\u017c\u017d\u0005\u0003\u0000\u0000\u017d"+
		"\u017f\u00056\u0000\u0000\u017e\u017c\u0001\u0000\u0000\u0000\u017f\u0182"+
		"\u0001\u0000\u0000\u0000\u0180\u017e\u0001\u0000\u0000\u0000\u0180\u0181"+
		"\u0001\u0000\u0000\u0000\u0181?\u0001\u0000\u0000\u0000\u0182\u0180\u0001"+
		"\u0000\u0000\u0000\u0183\u0188\u0003B!\u0000\u0184\u0185\u0005\u001e\u0000"+
		"\u0000\u0185\u0187\u0003B!\u0000\u0186\u0184\u0001\u0000\u0000\u0000\u0187"+
		"\u018a\u0001\u0000\u0000\u0000\u0188\u0186\u0001\u0000\u0000\u0000\u0188"+
		"\u0189\u0001\u0000\u0000\u0000\u0189A\u0001\u0000\u0000\u0000\u018a\u0188"+
		"\u0001\u0000\u0000\u0000\u018b\u0190\u0003D\"\u0000\u018c\u018d\u0005"+
		"\u001f\u0000\u0000\u018d\u018f\u0003D\"\u0000\u018e\u018c\u0001\u0000"+
		"\u0000\u0000\u018f\u0192\u0001\u0000\u0000\u0000\u0190\u018e\u0001\u0000"+
		"\u0000\u0000\u0190\u0191\u0001\u0000\u0000\u0000\u0191C\u0001\u0000\u0000"+
		"\u0000\u0192\u0190\u0001\u0000\u0000\u0000\u0193\u0198\u0003F#\u0000\u0194"+
		"\u0195\u0007\u0000\u0000\u0000\u0195\u0197\u0003F#\u0000\u0196\u0194\u0001"+
		"\u0000\u0000\u0000\u0197\u019a\u0001\u0000\u0000\u0000\u0198\u0196\u0001"+
		"\u0000\u0000\u0000\u0198\u0199\u0001\u0000\u0000\u0000\u0199E\u0001\u0000"+
		"\u0000\u0000\u019a\u0198\u0001\u0000\u0000\u0000\u019b\u01a0\u0003H$\u0000"+
		"\u019c\u019d\u0007\u0001\u0000\u0000\u019d\u019f\u0003H$\u0000\u019e\u019c"+
		"\u0001\u0000\u0000\u0000\u019f\u01a2\u0001\u0000\u0000\u0000\u01a0\u019e"+
		"\u0001\u0000\u0000\u0000\u01a0\u01a1\u0001\u0000\u0000\u0000\u01a1G\u0001"+
		"\u0000\u0000\u0000\u01a2\u01a0\u0001\u0000\u0000\u0000\u01a3\u01a8\u0003"+
		"J%\u0000\u01a4\u01a5\u0007\u0002\u0000\u0000\u01a5\u01a7\u0003J%\u0000"+
		"\u01a6\u01a4\u0001\u0000\u0000\u0000\u01a7\u01aa\u0001\u0000\u0000\u0000"+
		"\u01a8\u01a6\u0001\u0000\u0000\u0000\u01a8\u01a9\u0001\u0000\u0000\u0000"+
		"\u01a9I\u0001\u0000\u0000\u0000\u01aa\u01a8\u0001\u0000\u0000\u0000\u01ab"+
		"\u01b0\u0003L&\u0000\u01ac\u01ad\u0007\u0003\u0000\u0000\u01ad\u01af\u0003"+
		"L&\u0000\u01ae\u01ac\u0001\u0000\u0000\u0000\u01af\u01b2\u0001\u0000\u0000"+
		"\u0000\u01b0\u01ae\u0001\u0000\u0000\u0000\u01b0\u01b1\u0001\u0000\u0000"+
		"\u0000\u01b1K\u0001\u0000\u0000\u0000\u01b2\u01b0\u0001\u0000\u0000\u0000"+
		"\u01b3\u01b4\u0007\u0004\u0000\u0000\u01b4\u01b7\u0003L&\u0000\u01b5\u01b7"+
		"\u0003^/\u0000\u01b6\u01b3\u0001\u0000\u0000\u0000\u01b6\u01b5\u0001\u0000"+
		"\u0000\u0000\u01b7M\u0001\u0000\u0000\u0000\u01b8\u01b9\u0005+\u0000\u0000"+
		"\u01b9\u01ba\u0005\n\u0000\u0000\u01ba\u01bb\u0003:\u001d\u0000\u01bb"+
		"\u01bc\u0005\u000b\u0000\u0000\u01bc\u01c0\u0003\\.\u0000\u01bd\u01bf"+
		"\u0003P(\u0000\u01be\u01bd\u0001\u0000\u0000\u0000\u01bf\u01c2\u0001\u0000"+
		"\u0000\u0000\u01c0\u01be\u0001\u0000\u0000\u0000\u01c0\u01c1\u0001\u0000"+
		"\u0000\u0000\u01c1\u01c4\u0001\u0000\u0000\u0000\u01c2\u01c0\u0001\u0000"+
		"\u0000\u0000\u01c3\u01c5\u0003R)\u0000\u01c4\u01c3\u0001\u0000\u0000\u0000"+
		"\u01c4\u01c5\u0001\u0000\u0000\u0000\u01c5O\u0001\u0000\u0000\u0000\u01c6"+
		"\u01c7\u0005,\u0000\u0000\u01c7\u01c8\u0005+\u0000\u0000\u01c8\u01c9\u0005"+
		"\n\u0000\u0000\u01c9\u01ca\u0003:\u001d\u0000\u01ca\u01cb\u0005\u000b"+
		"\u0000\u0000\u01cb\u01cc\u0003\\.\u0000\u01ccQ\u0001\u0000\u0000\u0000"+
		"\u01cd\u01ce\u0005,\u0000\u0000\u01ce\u01cf\u0003\\.\u0000\u01cfS\u0001"+
		"\u0000\u0000\u0000\u01d0\u01d1\u0005-\u0000\u0000\u01d1\u01d2\u00056\u0000"+
		"\u0000\u01d2\u01d6\u0005\u0006\u0000\u0000\u01d3\u01d5\u0003V+\u0000\u01d4"+
		"\u01d3\u0001\u0000\u0000\u0000\u01d5\u01d8\u0001\u0000\u0000\u0000\u01d6"+
		"\u01d4\u0001\u0000\u0000\u0000\u01d6\u01d7\u0001\u0000\u0000\u0000\u01d7"+
		"\u01d9\u0001\u0000\u0000\u0000\u01d8\u01d6\u0001\u0000\u0000\u0000\u01d9"+
		"\u01da\u0005\u0007\u0000\u0000\u01daU\u0001\u0000\u0000\u0000\u01db\u01dc"+
		"\u00056\u0000\u0000\u01dc\u01dd\u0005\u0002\u0000\u0000\u01ddW\u0001\u0000"+
		"\u0000\u0000\u01de\u01df\u0005.\u0000\u0000\u01df\u01e0\u00056\u0000\u0000"+
		"\u01e0\u01e1\u0005\u0006\u0000\u0000\u01e1\u01e6\u0003Z-\u0000\u01e2\u01e3"+
		"\u0005\f\u0000\u0000\u01e3\u01e5\u0003Z-\u0000\u01e4\u01e2\u0001\u0000"+
		"\u0000\u0000\u01e5\u01e8\u0001\u0000\u0000\u0000\u01e6\u01e4\u0001\u0000"+
		"\u0000\u0000\u01e6\u01e7\u0001\u0000\u0000\u0000\u01e7\u01e9\u0001\u0000"+
		"\u0000\u0000\u01e8\u01e6\u0001\u0000\u0000\u0000\u01e9\u01ea\u0005\u0007"+
		"\u0000\u0000\u01eaY\u0001\u0000\u0000\u0000\u01eb\u01ee\u00056\u0000\u0000"+
		"\u01ec\u01ed\u0005\u001a\u0000\u0000\u01ed\u01ef\u0003:\u001d\u0000\u01ee"+
		"\u01ec\u0001\u0000\u0000\u0000\u01ee\u01ef\u0001\u0000\u0000\u0000\u01ef"+
		"[\u0001\u0000\u0000\u0000\u01f0\u01f4\u0005\u0006\u0000\u0000\u01f1\u01f3"+
		"\u0003\u0002\u0001\u0000\u01f2\u01f1\u0001\u0000\u0000\u0000\u01f3\u01f6"+
		"\u0001\u0000\u0000\u0000\u01f4\u01f2\u0001\u0000\u0000\u0000\u01f4\u01f5"+
		"\u0001\u0000\u0000\u0000\u01f5\u01f7\u0001\u0000\u0000\u0000\u01f6\u01f4"+
		"\u0001\u0000\u0000\u0000\u01f7\u01f8\u0005\u0007\u0000\u0000\u01f8]\u0001"+
		"\u0000\u0000\u0000\u01f9\u01fd\u0003b1\u0000\u01fa\u01fc\u0003`0\u0000"+
		"\u01fb\u01fa\u0001\u0000\u0000\u0000\u01fc\u01ff\u0001\u0000\u0000\u0000"+
		"\u01fd\u01fb\u0001\u0000\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000\u0000"+
		"\u01fe_\u0001\u0000\u0000\u0000\u01ff\u01fd\u0001\u0000\u0000\u0000\u0200"+
		"\u0201\u0005\u0003\u0000\u0000\u0201\u020c\u00056\u0000\u0000\u0202\u0204"+
		"\u0005\n\u0000\u0000\u0203\u0205\u0003f3\u0000\u0204\u0203\u0001\u0000"+
		"\u0000\u0000\u0204\u0205\u0001\u0000\u0000\u0000\u0205\u0206\u0001\u0000"+
		"\u0000\u0000\u0206\u020c\u0005\u000b\u0000\u0000\u0207\u0208\u0005\u0018"+
		"\u0000\u0000\u0208\u0209\u0003:\u001d\u0000\u0209\u020a\u0005\u0019\u0000"+
		"\u0000\u020a\u020c\u0001\u0000\u0000\u0000\u020b\u0200\u0001\u0000\u0000"+
		"\u0000\u020b\u0202\u0001\u0000\u0000\u0000\u020b\u0207\u0001\u0000\u0000"+
		"\u0000\u020ca\u0001\u0000\u0000\u0000\u020d\u0215\u0003p8\u0000\u020e"+
		"\u0215\u00056\u0000\u0000\u020f\u0210\u0005\n\u0000\u0000\u0210\u0211"+
		"\u0003:\u001d\u0000\u0211\u0212\u0005\u000b\u0000\u0000\u0212\u0215\u0001"+
		"\u0000\u0000\u0000\u0213\u0215\u0003d2\u0000\u0214\u020d\u0001\u0000\u0000"+
		"\u0000\u0214\u020e\u0001\u0000\u0000\u0000\u0214\u020f\u0001\u0000\u0000"+
		"\u0000\u0214\u0213\u0001\u0000\u0000\u0000\u0215c\u0001\u0000\u0000\u0000"+
		"\u0216\u0217\u00056\u0000\u0000\u0217\u0219\u0005\n\u0000\u0000\u0218"+
		"\u021a\u0003f3\u0000\u0219\u0218\u0001\u0000\u0000\u0000\u0219\u021a\u0001"+
		"\u0000\u0000\u0000\u021a\u021b\u0001\u0000\u0000\u0000\u021b\u021c\u0005"+
		"\u000b\u0000\u0000\u021ce\u0001\u0000\u0000\u0000\u021d\u0222\u0003:\u001d"+
		"\u0000\u021e\u021f\u0005\f\u0000\u0000\u021f\u0221\u0003:\u001d\u0000"+
		"\u0220\u021e\u0001\u0000\u0000\u0000\u0221\u0224\u0001\u0000\u0000\u0000"+
		"\u0222\u0220\u0001\u0000\u0000\u0000\u0222\u0223\u0001\u0000\u0000\u0000"+
		"\u0223g\u0001\u0000\u0000\u0000\u0224\u0222\u0001\u0000\u0000\u0000\u0225"+
		"\u0226\u0005/\u0000\u0000\u0226\u0228\u0003\\.\u0000\u0227\u0229\u0003"+
		"j5\u0000\u0228\u0227\u0001\u0000\u0000\u0000\u0229\u022a\u0001\u0000\u0000"+
		"\u0000\u022a\u0228\u0001\u0000\u0000\u0000\u022a\u022b\u0001\u0000\u0000"+
		"\u0000\u022b\u022d\u0001\u0000\u0000\u0000\u022c\u022e\u0003l6\u0000\u022d"+
		"\u022c\u0001\u0000\u0000\u0000\u022d\u022e\u0001\u0000\u0000\u0000\u022e"+
		"i\u0001\u0000\u0000\u0000\u022f\u0230\u00050\u0000\u0000\u0230\u0231\u0005"+
		"\n\u0000\u0000\u0231\u0232\u00056\u0000\u0000\u0232\u0233\u0005\u000b"+
		"\u0000\u0000\u0233\u0234\u0003\\.\u0000\u0234k\u0001\u0000\u0000\u0000"+
		"\u0235\u0236\u00051\u0000\u0000\u0236\u0237\u0003\\.\u0000\u0237m\u0001"+
		"\u0000\u0000\u0000\u0238\u0239\u00052\u0000\u0000\u0239\u023a\u0003:\u001d"+
		"\u0000\u023a\u023b\u0005\u0002\u0000\u0000\u023bo\u0001\u0000\u0000\u0000"+
		"\u023c\u0243\u00059\u0000\u0000\u023d\u0243\u0005:\u0000\u0000\u023e\u0243"+
		"\u0005;\u0000\u0000\u023f\u0243\u0003*\u0015\u0000\u0240\u0243\u00057"+
		"\u0000\u0000\u0241\u0243\u00058\u0000\u0000\u0242\u023c\u0001\u0000\u0000"+
		"\u0000\u0242\u023d\u0001\u0000\u0000\u0000\u0242\u023e\u0001\u0000\u0000"+
		"\u0000\u0242\u023f\u0001\u0000\u0000\u0000\u0242\u0240\u0001\u0000\u0000"+
		"\u0000\u0242\u0241\u0001\u0000\u0000\u0000\u0243q\u0001\u0000\u0000\u0000"+
		"\u0244\u0245\u0003:\u001d\u0000\u0245\u0246\u0005\u0002\u0000\u0000\u0246"+
		"s\u0001\u0000\u0000\u0000\u0247\u0248\u0007\u0005\u0000\u0000\u0248u\u0001"+
		"\u0000\u0000\u00000y\u0094\u009f\u00a4\u00ac\u00b8\u00ba\u00c3\u00cd\u00d2"+
		"\u00d9\u00dd\u00e1\u00e8\u010d\u0111\u011b\u0123\u0132\u0135\u015a\u0160"+
		"\u0169\u0171\u0177\u0180\u0188\u0190\u0198\u01a0\u01a8\u01b0\u01b6\u01c0"+
		"\u01c4\u01d6\u01e6\u01ee\u01f4\u01fd\u0204\u020b\u0214\u0219\u0222\u022a"+
		"\u022d\u0242";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}