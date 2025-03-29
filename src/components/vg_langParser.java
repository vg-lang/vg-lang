// Generated from C:/Users/hodif/Desktop/usn2024/vg lang/grammar/vg_lang.g4 by ANTLR 4.13.2
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
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, SINGLE_LINE_COMMENT=45, 
		MULTI_LINE_COMMENT=46, DOC_COMMENT=47, IDENTIFIER=48, TRUE=49, FALSE=50, 
		INT=51, DOUBLE=52, STRING_LITERAL=53, WS=54;
	public static final int
		RULE_program = 0, RULE_statement = 1, RULE_importStatement = 2, RULE_importPath = 3, 
		RULE_libraryDeclaration = 4, RULE_namespaceDeclaration = 5, RULE_functionDeclaration = 6, 
		RULE_parameterList = 7, RULE_returnStatement = 8, RULE_forStatement = 9, 
		RULE_forInit = 10, RULE_forCondition = 11, RULE_forUpdate = 12, RULE_whileStatement = 13, 
		RULE_doWhileStatement = 14, RULE_arrayLiteral = 15, RULE_variableDeclaration = 16, 
		RULE_variableDeclarationNoSemi = 17, RULE_constDeclaration = 18, RULE_assignment = 19, 
		RULE_assignmentNoSemi = 20, RULE_leftHandSide = 21, RULE_printStatement = 22, 
		RULE_expression = 23, RULE_functionReference = 24, RULE_qualifiedIdentifier = 25, 
		RULE_logicalOrExpression = 26, RULE_logicalAndExpression = 27, RULE_equalityExpression = 28, 
		RULE_relationalExpression = 29, RULE_additiveExpression = 30, RULE_multiplicativeExpression = 31, 
		RULE_unaryExpression = 32, RULE_ifStatement = 33, RULE_elseIfStatement = 34, 
		RULE_elseStatement = 35, RULE_structDeclaration = 36, RULE_structField = 37, 
		RULE_enumDeclaration = 38, RULE_enumValue = 39, RULE_block = 40, RULE_postfixExpression = 41, 
		RULE_postfixOp = 42, RULE_primary = 43, RULE_functionCall = 44, RULE_argumentList = 45, 
		RULE_tryStatement = 46, RULE_catchStatement = 47, RULE_finallyStatement = 48, 
		RULE_throwStatement = 49, RULE_literal = 50, RULE_expressionStatement = 51, 
		RULE_comments = 52;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "statement", "importStatement", "importPath", "libraryDeclaration", 
			"namespaceDeclaration", "functionDeclaration", "parameterList", "returnStatement", 
			"forStatement", "forInit", "forCondition", "forUpdate", "whileStatement", 
			"doWhileStatement", "arrayLiteral", "variableDeclaration", "variableDeclarationNoSemi", 
			"constDeclaration", "assignment", "assignmentNoSemi", "leftHandSide", 
			"printStatement", "expression", "functionReference", "qualifiedIdentifier", 
			"logicalOrExpression", "logicalAndExpression", "equalityExpression", 
			"relationalExpression", "additiveExpression", "multiplicativeExpression", 
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
			"'function'", "'('", "')'", "','", "'return'", "'for'", "'while'", "'do'", 
			"'['", "']'", "'var'", "'='", "'const'", "'print'", "'&'", "'||'", "'&&'", 
			"'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'+'", "'-'", "'/'", "'%'", 
			"'!'", "'if'", "'else'", "'struct'", "'enum'", "'try'", "'catch'", "'finally'", 
			"'throw'", null, null, null, null, "'true'", "'false'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "SINGLE_LINE_COMMENT", 
			"MULTI_LINE_COMMENT", "DOC_COMMENT", "IDENTIFIER", "TRUE", "FALSE", "INT", 
			"DOUBLE", "STRING_LITERAL", "WS"
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
			setState(109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 18000873672926754L) != 0)) {
				{
				{
				setState(106);
				statement();
				}
				}
				setState(111);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(112);
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
			setState(132);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(114);
				variableDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(115);
				assignment();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(116);
				printStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(117);
				comments();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(118);
				ifStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(119);
				expressionStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(120);
				constDeclaration();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(121);
				forStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(122);
				functionDeclaration();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(123);
				returnStatement();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(124);
				tryStatement();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(125);
				throwStatement();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(126);
				whileStatement();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(127);
				doWhileStatement();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(128);
				libraryDeclaration();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(129);
				importStatement();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(130);
				structDeclaration();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(131);
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
			setState(134);
			match(T__0);
			setState(135);
			importPath();
			setState(136);
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
			setState(138);
			match(IDENTIFIER);
			setState(143);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(139);
					match(T__2);
					setState(140);
					match(IDENTIFIER);
					}
					} 
				}
				setState(145);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(146);
				match(T__2);
				setState(147);
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
			setState(150);
			match(T__4);
			setState(151);
			match(IDENTIFIER);
			setState(152);
			match(T__5);
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(153);
				namespaceDeclaration();
				}
				}
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(159);
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
			setState(161);
			match(T__7);
			setState(162);
			match(IDENTIFIER);
			setState(163);
			match(T__5);
			setState(170);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2622208L) != 0)) {
				{
				setState(168);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__8:
					{
					setState(164);
					functionDeclaration();
					}
					break;
				case T__18:
					{
					setState(165);
					variableDeclaration();
					}
					break;
				case T__20:
					{
					setState(166);
					constDeclaration();
					}
					break;
				case T__7:
					{
					setState(167);
					namespaceDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(172);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(173);
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
			setState(175);
			match(T__8);
			setState(176);
			match(IDENTIFIER);
			setState(177);
			match(T__9);
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(178);
				parameterList();
				}
			}

			setState(181);
			match(T__10);
			setState(182);
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
			setState(184);
			match(IDENTIFIER);
			setState(189);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(185);
				match(T__11);
				setState(186);
				match(IDENTIFIER);
				}
				}
				setState(191);
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
			setState(192);
			match(T__12);
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17733005145670656L) != 0)) {
				{
				setState(193);
				expression();
				}
			}

			setState(196);
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
			setState(198);
			match(T__13);
			setState(199);
			match(T__9);
			setState(201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__18 || _la==IDENTIFIER) {
				{
				setState(200);
				forInit();
				}
			}

			setState(203);
			match(T__1);
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17733005145670656L) != 0)) {
				{
				setState(204);
				forCondition();
				}
			}

			setState(207);
			match(T__1);
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(208);
				forUpdate();
				}
			}

			setState(211);
			match(T__10);
			setState(212);
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
			setState(216);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__18:
				enterOuterAlt(_localctx, 1);
				{
				setState(214);
				variableDeclarationNoSemi();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(215);
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
			setState(218);
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
			setState(220);
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
		enterRule(_localctx, 26, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			match(T__14);
			setState(223);
			match(T__9);
			setState(224);
			expression();
			setState(225);
			match(T__10);
			setState(226);
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
		enterRule(_localctx, 28, RULE_doWhileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			match(T__15);
			setState(229);
			block();
			setState(230);
			match(T__14);
			setState(231);
			match(T__9);
			setState(232);
			expression();
			setState(233);
			match(T__10);
			setState(234);
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
		enterRule(_localctx, 30, RULE_arrayLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(T__16);
			setState(245);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17733005145670656L) != 0)) {
				{
				setState(237);
				expression();
				setState(242);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__11) {
					{
					{
					setState(238);
					match(T__11);
					setState(239);
					expression();
					}
					}
					setState(244);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(247);
			match(T__17);
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
		enterRule(_localctx, 32, RULE_variableDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(T__18);
			setState(250);
			match(IDENTIFIER);
			setState(251);
			match(T__19);
			setState(252);
			expression();
			setState(253);
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
		enterRule(_localctx, 34, RULE_variableDeclarationNoSemi);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			match(T__18);
			setState(256);
			match(IDENTIFIER);
			setState(257);
			match(T__19);
			setState(258);
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
		enterRule(_localctx, 36, RULE_constDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			match(T__20);
			setState(261);
			match(IDENTIFIER);
			setState(262);
			match(T__19);
			setState(263);
			expression();
			setState(264);
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
		enterRule(_localctx, 38, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
			leftHandSide();
			setState(267);
			match(T__19);
			setState(268);
			expression();
			setState(269);
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
		enterRule(_localctx, 40, RULE_assignmentNoSemi);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			leftHandSide();
			setState(272);
			match(T__19);
			setState(273);
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
		enterRule(_localctx, 42, RULE_leftHandSide);
		int _la;
		try {
			setState(288);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(275);
				match(IDENTIFIER);
				setState(282);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__16) {
					{
					{
					setState(276);
					match(T__16);
					setState(277);
					expression();
					setState(278);
					match(T__17);
					}
					}
					setState(284);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(285);
				match(IDENTIFIER);
				setState(286);
				match(T__2);
				setState(287);
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
		enterRule(_localctx, 44, RULE_printStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			match(T__21);
			setState(291);
			match(T__9);
			setState(292);
			expression();
			setState(297);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(293);
				match(T__11);
				setState(294);
				expression();
				}
				}
				setState(299);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(300);
			match(T__10);
			setState(301);
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
		enterRule(_localctx, 46, RULE_expression);
		try {
			setState(305);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
			case T__16:
			case T__31:
			case T__32:
			case T__35:
			case IDENTIFIER:
			case TRUE:
			case FALSE:
			case INT:
			case DOUBLE:
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(303);
				logicalOrExpression();
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 2);
				{
				setState(304);
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
		enterRule(_localctx, 48, RULE_functionReference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			match(T__22);
			setState(308);
			qualifiedIdentifier();
			setState(309);
			match(T__9);
			setState(311);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17733005145670656L) != 0)) {
				{
				setState(310);
				argumentList();
				}
			}

			setState(313);
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
		enterRule(_localctx, 50, RULE_qualifiedIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			match(IDENTIFIER);
			setState(320);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(316);
				match(T__2);
				setState(317);
				match(IDENTIFIER);
				}
				}
				setState(322);
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
		enterRule(_localctx, 52, RULE_logicalOrExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323);
			logicalAndExpression();
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23) {
				{
				{
				setState(324);
				match(T__23);
				setState(325);
				logicalAndExpression();
				}
				}
				setState(330);
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
		enterRule(_localctx, 54, RULE_logicalAndExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(331);
			equalityExpression();
			setState(336);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__24) {
				{
				{
				setState(332);
				match(T__24);
				setState(333);
				equalityExpression();
				}
				}
				setState(338);
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
		enterRule(_localctx, 56, RULE_equalityExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
			relationalExpression();
			setState(344);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__25 || _la==T__26) {
				{
				{
				setState(340);
				_la = _input.LA(1);
				if ( !(_la==T__25 || _la==T__26) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(341);
				relationalExpression();
				}
				}
				setState(346);
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
		enterRule(_localctx, 58, RULE_relationalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			additiveExpression();
			setState(352);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4026531840L) != 0)) {
				{
				{
				setState(348);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4026531840L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(349);
				additiveExpression();
				}
				}
				setState(354);
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
		enterRule(_localctx, 60, RULE_additiveExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
			multiplicativeExpression();
			setState(360);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__31 || _la==T__32) {
				{
				{
				setState(356);
				_la = _input.LA(1);
				if ( !(_la==T__31 || _la==T__32) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(357);
				multiplicativeExpression();
				}
				}
				setState(362);
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
		enterRule(_localctx, 62, RULE_multiplicativeExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			unaryExpression();
			setState(368);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 51539607568L) != 0)) {
				{
				{
				setState(364);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 51539607568L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(365);
				unaryExpression();
				}
				}
				setState(370);
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
		enterRule(_localctx, 64, RULE_unaryExpression);
		int _la;
		try {
			setState(374);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__31:
			case T__32:
			case T__35:
				enterOuterAlt(_localctx, 1);
				{
				setState(371);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 81604378624L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(372);
				unaryExpression();
				}
				break;
			case T__9:
			case T__16:
			case IDENTIFIER:
			case TRUE:
			case FALSE:
			case INT:
			case DOUBLE:
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(373);
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
		enterRule(_localctx, 66, RULE_ifStatement);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			match(T__36);
			setState(377);
			match(T__9);
			setState(378);
			expression();
			setState(379);
			match(T__10);
			setState(380);
			((IfStatementContext)_localctx).ifBlock = block();
			setState(384);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(381);
					elseIfStatement();
					}
					} 
				}
				setState(386);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			}
			setState(388);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__37) {
				{
				setState(387);
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
		enterRule(_localctx, 68, RULE_elseIfStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			match(T__37);
			setState(391);
			match(T__36);
			setState(392);
			match(T__9);
			setState(393);
			expression();
			setState(394);
			match(T__10);
			setState(395);
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
		enterRule(_localctx, 70, RULE_elseStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397);
			match(T__37);
			setState(398);
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
		enterRule(_localctx, 72, RULE_structDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(400);
			match(T__38);
			setState(401);
			match(IDENTIFIER);
			setState(402);
			match(T__5);
			setState(406);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(403);
				structField();
				}
				}
				setState(408);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(409);
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
		enterRule(_localctx, 74, RULE_structField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(IDENTIFIER);
			setState(412);
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
		enterRule(_localctx, 76, RULE_enumDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
			match(T__39);
			setState(415);
			match(IDENTIFIER);
			setState(416);
			match(T__5);
			setState(417);
			enumValue();
			setState(422);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(418);
				match(T__11);
				setState(419);
				enumValue();
				}
				}
				setState(424);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(425);
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
		enterRule(_localctx, 78, RULE_enumValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
			match(IDENTIFIER);
			setState(430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(428);
				match(T__19);
				setState(429);
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
		enterRule(_localctx, 80, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
			match(T__5);
			setState(436);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 18000873672926754L) != 0)) {
				{
				{
				setState(433);
				statement();
				}
				}
				setState(438);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(439);
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
		enterRule(_localctx, 82, RULE_postfixExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(441);
			primary();
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 132104L) != 0)) {
				{
				{
				setState(442);
				postfixOp();
				}
				}
				setState(447);
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
		enterRule(_localctx, 84, RULE_postfixOp);
		int _la;
		try {
			setState(459);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				enterOuterAlt(_localctx, 1);
				{
				setState(448);
				match(T__2);
				setState(449);
				match(IDENTIFIER);
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(450);
				match(T__9);
				setState(452);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17733005145670656L) != 0)) {
					{
					setState(451);
					argumentList();
					}
				}

				setState(454);
				match(T__10);
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 3);
				{
				setState(455);
				match(T__16);
				setState(456);
				expression();
				setState(457);
				match(T__17);
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
		enterRule(_localctx, 86, RULE_primary);
		try {
			setState(468);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(461);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(462);
				match(IDENTIFIER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(463);
				match(T__9);
				setState(464);
				expression();
				setState(465);
				match(T__10);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(467);
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
		enterRule(_localctx, 88, RULE_functionCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(470);
			match(IDENTIFIER);
			}
			setState(471);
			match(T__9);
			setState(473);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17733005145670656L) != 0)) {
				{
				setState(472);
				argumentList();
				}
			}

			setState(475);
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
		enterRule(_localctx, 90, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			expression();
			setState(482);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(478);
				match(T__11);
				setState(479);
				expression();
				}
				}
				setState(484);
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
		enterRule(_localctx, 92, RULE_tryStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
			match(T__40);
			setState(486);
			block();
			setState(488); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(487);
				catchStatement();
				}
				}
				setState(490); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__41 );
			setState(493);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__42) {
				{
				setState(492);
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
		enterRule(_localctx, 94, RULE_catchStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(495);
			match(T__41);
			setState(496);
			match(T__9);
			setState(497);
			match(IDENTIFIER);
			setState(498);
			match(T__10);
			setState(499);
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
		enterRule(_localctx, 96, RULE_finallyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501);
			match(T__42);
			setState(502);
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
		enterRule(_localctx, 98, RULE_throwStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(504);
			match(T__43);
			setState(505);
			expression();
			setState(506);
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
		enterRule(_localctx, 100, RULE_literal);
		try {
			setState(514);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(508);
				match(INT);
				}
				break;
			case DOUBLE:
				enterOuterAlt(_localctx, 2);
				{
				setState(509);
				match(DOUBLE);
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(510);
				match(STRING_LITERAL);
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 4);
				{
				setState(511);
				arrayLiteral();
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 5);
				{
				setState(512);
				match(TRUE);
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 6);
				{
				setState(513);
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
		enterRule(_localctx, 102, RULE_expressionStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(516);
			expression();
			setState(517);
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
		enterRule(_localctx, 104, RULE_comments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(519);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 246290604621824L) != 0)) ) {
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
		"\u0004\u00016\u020a\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"2\u00072\u00023\u00073\u00024\u00074\u0001\u0000\u0005\u0000l\b\u0000"+
		"\n\u0000\f\u0000o\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0085\b\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0005\u0003\u008e\b\u0003\n\u0003\f\u0003\u0091\t\u0003\u0001"+
		"\u0003\u0001\u0003\u0003\u0003\u0095\b\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0005\u0004\u009b\b\u0004\n\u0004\f\u0004\u009e\t\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u00a9\b\u0005\n\u0005"+
		"\f\u0005\u00ac\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0003\u0006\u00b4\b\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00bc\b\u0007"+
		"\n\u0007\f\u0007\u00bf\t\u0007\u0001\b\u0001\b\u0003\b\u00c3\b\b\u0001"+
		"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0003\t\u00ca\b\t\u0001\t\u0001\t\u0003"+
		"\t\u00ce\b\t\u0001\t\u0001\t\u0003\t\u00d2\b\t\u0001\t\u0001\t\u0001\t"+
		"\u0001\n\u0001\n\u0003\n\u00d9\b\n\u0001\u000b\u0001\u000b\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u00f1\b\u000f"+
		"\n\u000f\f\u000f\u00f4\t\u000f\u0003\u000f\u00f6\b\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0005\u0015\u0119\b\u0015\n\u0015\f\u0015\u011c\t\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u0121\b\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u0128\b\u0016"+
		"\n\u0016\f\u0016\u012b\t\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0017\u0001\u0017\u0003\u0017\u0132\b\u0017\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0003\u0018\u0138\b\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0005\u0019\u013f\b\u0019\n\u0019\f\u0019"+
		"\u0142\t\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0005\u001a\u0147\b"+
		"\u001a\n\u001a\f\u001a\u014a\t\u001a\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0005\u001b\u014f\b\u001b\n\u001b\f\u001b\u0152\t\u001b\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0005\u001c\u0157\b\u001c\n\u001c\f\u001c\u015a\t\u001c"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0005\u001d\u015f\b\u001d\n\u001d"+
		"\f\u001d\u0162\t\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e"+
		"\u0167\b\u001e\n\u001e\f\u001e\u016a\t\u001e\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0005\u001f\u016f\b\u001f\n\u001f\f\u001f\u0172\t\u001f\u0001 "+
		"\u0001 \u0001 \u0003 \u0177\b \u0001!\u0001!\u0001!\u0001!\u0001!\u0001"+
		"!\u0005!\u017f\b!\n!\f!\u0182\t!\u0001!\u0003!\u0185\b!\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001#\u0001#\u0001#\u0001"+
		"$\u0001$\u0001$\u0001$\u0005$\u0195\b$\n$\f$\u0198\t$\u0001$\u0001$\u0001"+
		"%\u0001%\u0001%\u0001&\u0001&\u0001&\u0001&\u0001&\u0001&\u0005&\u01a5"+
		"\b&\n&\f&\u01a8\t&\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0003\'\u01af"+
		"\b\'\u0001(\u0001(\u0005(\u01b3\b(\n(\f(\u01b6\t(\u0001(\u0001(\u0001"+
		")\u0001)\u0005)\u01bc\b)\n)\f)\u01bf\t)\u0001*\u0001*\u0001*\u0001*\u0003"+
		"*\u01c5\b*\u0001*\u0001*\u0001*\u0001*\u0001*\u0003*\u01cc\b*\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003+\u01d5\b+\u0001,\u0001,\u0001"+
		",\u0003,\u01da\b,\u0001,\u0001,\u0001-\u0001-\u0001-\u0005-\u01e1\b-\n"+
		"-\f-\u01e4\t-\u0001.\u0001.\u0001.\u0004.\u01e9\b.\u000b.\f.\u01ea\u0001"+
		".\u0003.\u01ee\b.\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u00010\u0001"+
		"0\u00010\u00011\u00011\u00011\u00011\u00012\u00012\u00012\u00012\u0001"+
		"2\u00012\u00032\u0203\b2\u00013\u00013\u00013\u00014\u00014\u00014\u0000"+
		"\u00005\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfh\u0000\u0006"+
		"\u0001\u0000\u001a\u001b\u0001\u0000\u001c\u001f\u0001\u0000 !\u0002\u0000"+
		"\u0004\u0004\"#\u0002\u0000 !$$\u0001\u0000-/\u0219\u0000m\u0001\u0000"+
		"\u0000\u0000\u0002\u0084\u0001\u0000\u0000\u0000\u0004\u0086\u0001\u0000"+
		"\u0000\u0000\u0006\u008a\u0001\u0000\u0000\u0000\b\u0096\u0001\u0000\u0000"+
		"\u0000\n\u00a1\u0001\u0000\u0000\u0000\f\u00af\u0001\u0000\u0000\u0000"+
		"\u000e\u00b8\u0001\u0000\u0000\u0000\u0010\u00c0\u0001\u0000\u0000\u0000"+
		"\u0012\u00c6\u0001\u0000\u0000\u0000\u0014\u00d8\u0001\u0000\u0000\u0000"+
		"\u0016\u00da\u0001\u0000\u0000\u0000\u0018\u00dc\u0001\u0000\u0000\u0000"+
		"\u001a\u00de\u0001\u0000\u0000\u0000\u001c\u00e4\u0001\u0000\u0000\u0000"+
		"\u001e\u00ec\u0001\u0000\u0000\u0000 \u00f9\u0001\u0000\u0000\u0000\""+
		"\u00ff\u0001\u0000\u0000\u0000$\u0104\u0001\u0000\u0000\u0000&\u010a\u0001"+
		"\u0000\u0000\u0000(\u010f\u0001\u0000\u0000\u0000*\u0120\u0001\u0000\u0000"+
		"\u0000,\u0122\u0001\u0000\u0000\u0000.\u0131\u0001\u0000\u0000\u00000"+
		"\u0133\u0001\u0000\u0000\u00002\u013b\u0001\u0000\u0000\u00004\u0143\u0001"+
		"\u0000\u0000\u00006\u014b\u0001\u0000\u0000\u00008\u0153\u0001\u0000\u0000"+
		"\u0000:\u015b\u0001\u0000\u0000\u0000<\u0163\u0001\u0000\u0000\u0000>"+
		"\u016b\u0001\u0000\u0000\u0000@\u0176\u0001\u0000\u0000\u0000B\u0178\u0001"+
		"\u0000\u0000\u0000D\u0186\u0001\u0000\u0000\u0000F\u018d\u0001\u0000\u0000"+
		"\u0000H\u0190\u0001\u0000\u0000\u0000J\u019b\u0001\u0000\u0000\u0000L"+
		"\u019e\u0001\u0000\u0000\u0000N\u01ab\u0001\u0000\u0000\u0000P\u01b0\u0001"+
		"\u0000\u0000\u0000R\u01b9\u0001\u0000\u0000\u0000T\u01cb\u0001\u0000\u0000"+
		"\u0000V\u01d4\u0001\u0000\u0000\u0000X\u01d6\u0001\u0000\u0000\u0000Z"+
		"\u01dd\u0001\u0000\u0000\u0000\\\u01e5\u0001\u0000\u0000\u0000^\u01ef"+
		"\u0001\u0000\u0000\u0000`\u01f5\u0001\u0000\u0000\u0000b\u01f8\u0001\u0000"+
		"\u0000\u0000d\u0202\u0001\u0000\u0000\u0000f\u0204\u0001\u0000\u0000\u0000"+
		"h\u0207\u0001\u0000\u0000\u0000jl\u0003\u0002\u0001\u0000kj\u0001\u0000"+
		"\u0000\u0000lo\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000mn\u0001"+
		"\u0000\u0000\u0000np\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000"+
		"pq\u0005\u0000\u0000\u0001q\u0001\u0001\u0000\u0000\u0000r\u0085\u0003"+
		" \u0010\u0000s\u0085\u0003&\u0013\u0000t\u0085\u0003,\u0016\u0000u\u0085"+
		"\u0003h4\u0000v\u0085\u0003B!\u0000w\u0085\u0003f3\u0000x\u0085\u0003"+
		"$\u0012\u0000y\u0085\u0003\u0012\t\u0000z\u0085\u0003\f\u0006\u0000{\u0085"+
		"\u0003\u0010\b\u0000|\u0085\u0003\\.\u0000}\u0085\u0003b1\u0000~\u0085"+
		"\u0003\u001a\r\u0000\u007f\u0085\u0003\u001c\u000e\u0000\u0080\u0085\u0003"+
		"\b\u0004\u0000\u0081\u0085\u0003\u0004\u0002\u0000\u0082\u0085\u0003H"+
		"$\u0000\u0083\u0085\u0003L&\u0000\u0084r\u0001\u0000\u0000\u0000\u0084"+
		"s\u0001\u0000\u0000\u0000\u0084t\u0001\u0000\u0000\u0000\u0084u\u0001"+
		"\u0000\u0000\u0000\u0084v\u0001\u0000\u0000\u0000\u0084w\u0001\u0000\u0000"+
		"\u0000\u0084x\u0001\u0000\u0000\u0000\u0084y\u0001\u0000\u0000\u0000\u0084"+
		"z\u0001\u0000\u0000\u0000\u0084{\u0001\u0000\u0000\u0000\u0084|\u0001"+
		"\u0000\u0000\u0000\u0084}\u0001\u0000\u0000\u0000\u0084~\u0001\u0000\u0000"+
		"\u0000\u0084\u007f\u0001\u0000\u0000\u0000\u0084\u0080\u0001\u0000\u0000"+
		"\u0000\u0084\u0081\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000"+
		"\u0000\u0084\u0083\u0001\u0000\u0000\u0000\u0085\u0003\u0001\u0000\u0000"+
		"\u0000\u0086\u0087\u0005\u0001\u0000\u0000\u0087\u0088\u0003\u0006\u0003"+
		"\u0000\u0088\u0089\u0005\u0002\u0000\u0000\u0089\u0005\u0001\u0000\u0000"+
		"\u0000\u008a\u008f\u00050\u0000\u0000\u008b\u008c\u0005\u0003\u0000\u0000"+
		"\u008c\u008e\u00050\u0000\u0000\u008d\u008b\u0001\u0000\u0000\u0000\u008e"+
		"\u0091\u0001\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u008f"+
		"\u0090\u0001\u0000\u0000\u0000\u0090\u0094\u0001\u0000\u0000\u0000\u0091"+
		"\u008f\u0001\u0000\u0000\u0000\u0092\u0093\u0005\u0003\u0000\u0000\u0093"+
		"\u0095\u0005\u0004\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0094"+
		"\u0095\u0001\u0000\u0000\u0000\u0095\u0007\u0001\u0000\u0000\u0000\u0096"+
		"\u0097\u0005\u0005\u0000\u0000\u0097\u0098\u00050\u0000\u0000\u0098\u009c"+
		"\u0005\u0006\u0000\u0000\u0099\u009b\u0003\n\u0005\u0000\u009a\u0099\u0001"+
		"\u0000\u0000\u0000\u009b\u009e\u0001\u0000\u0000\u0000\u009c\u009a\u0001"+
		"\u0000\u0000\u0000\u009c\u009d\u0001\u0000\u0000\u0000\u009d\u009f\u0001"+
		"\u0000\u0000\u0000\u009e\u009c\u0001\u0000\u0000\u0000\u009f\u00a0\u0005"+
		"\u0007\u0000\u0000\u00a0\t\u0001\u0000\u0000\u0000\u00a1\u00a2\u0005\b"+
		"\u0000\u0000\u00a2\u00a3\u00050\u0000\u0000\u00a3\u00aa\u0005\u0006\u0000"+
		"\u0000\u00a4\u00a9\u0003\f\u0006\u0000\u00a5\u00a9\u0003 \u0010\u0000"+
		"\u00a6\u00a9\u0003$\u0012\u0000\u00a7\u00a9\u0003\n\u0005\u0000\u00a8"+
		"\u00a4\u0001\u0000\u0000\u0000\u00a8\u00a5\u0001\u0000\u0000\u0000\u00a8"+
		"\u00a6\u0001\u0000\u0000\u0000\u00a8\u00a7\u0001\u0000\u0000\u0000\u00a9"+
		"\u00ac\u0001\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000\u0000\u0000\u00aa"+
		"\u00ab\u0001\u0000\u0000\u0000\u00ab\u00ad\u0001\u0000\u0000\u0000\u00ac"+
		"\u00aa\u0001\u0000\u0000\u0000\u00ad\u00ae\u0005\u0007\u0000\u0000\u00ae"+
		"\u000b\u0001\u0000\u0000\u0000\u00af\u00b0\u0005\t\u0000\u0000\u00b0\u00b1"+
		"\u00050\u0000\u0000\u00b1\u00b3\u0005\n\u0000\u0000\u00b2\u00b4\u0003"+
		"\u000e\u0007\u0000\u00b3\u00b2\u0001\u0000\u0000\u0000\u00b3\u00b4\u0001"+
		"\u0000\u0000\u0000\u00b4\u00b5\u0001\u0000\u0000\u0000\u00b5\u00b6\u0005"+
		"\u000b\u0000\u0000\u00b6\u00b7\u0003P(\u0000\u00b7\r\u0001\u0000\u0000"+
		"\u0000\u00b8\u00bd\u00050\u0000\u0000\u00b9\u00ba\u0005\f\u0000\u0000"+
		"\u00ba\u00bc\u00050\u0000\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bc"+
		"\u00bf\u0001\u0000\u0000\u0000\u00bd\u00bb\u0001\u0000\u0000\u0000\u00bd"+
		"\u00be\u0001\u0000\u0000\u0000\u00be\u000f\u0001\u0000\u0000\u0000\u00bf"+
		"\u00bd\u0001\u0000\u0000\u0000\u00c0\u00c2\u0005\r\u0000\u0000\u00c1\u00c3"+
		"\u0003.\u0017\u0000\u00c2\u00c1\u0001\u0000\u0000\u0000\u00c2\u00c3\u0001"+
		"\u0000\u0000\u0000\u00c3\u00c4\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005"+
		"\u0002\u0000\u0000\u00c5\u0011\u0001\u0000\u0000\u0000\u00c6\u00c7\u0005"+
		"\u000e\u0000\u0000\u00c7\u00c9\u0005\n\u0000\u0000\u00c8\u00ca\u0003\u0014"+
		"\n\u0000\u00c9\u00c8\u0001\u0000\u0000\u0000\u00c9\u00ca\u0001\u0000\u0000"+
		"\u0000\u00ca\u00cb\u0001\u0000\u0000\u0000\u00cb\u00cd\u0005\u0002\u0000"+
		"\u0000\u00cc\u00ce\u0003\u0016\u000b\u0000\u00cd\u00cc\u0001\u0000\u0000"+
		"\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000"+
		"\u0000\u00cf\u00d1\u0005\u0002\u0000\u0000\u00d0\u00d2\u0003\u0018\f\u0000"+
		"\u00d1\u00d0\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000"+
		"\u00d2\u00d3\u0001\u0000\u0000\u0000\u00d3\u00d4\u0005\u000b\u0000\u0000"+
		"\u00d4\u00d5\u0003P(\u0000\u00d5\u0013\u0001\u0000\u0000\u0000\u00d6\u00d9"+
		"\u0003\"\u0011\u0000\u00d7\u00d9\u0003(\u0014\u0000\u00d8\u00d6\u0001"+
		"\u0000\u0000\u0000\u00d8\u00d7\u0001\u0000\u0000\u0000\u00d9\u0015\u0001"+
		"\u0000\u0000\u0000\u00da\u00db\u0003.\u0017\u0000\u00db\u0017\u0001\u0000"+
		"\u0000\u0000\u00dc\u00dd\u0003(\u0014\u0000\u00dd\u0019\u0001\u0000\u0000"+
		"\u0000\u00de\u00df\u0005\u000f\u0000\u0000\u00df\u00e0\u0005\n\u0000\u0000"+
		"\u00e0\u00e1\u0003.\u0017\u0000\u00e1\u00e2\u0005\u000b\u0000\u0000\u00e2"+
		"\u00e3\u0003P(\u0000\u00e3\u001b\u0001\u0000\u0000\u0000\u00e4\u00e5\u0005"+
		"\u0010\u0000\u0000\u00e5\u00e6\u0003P(\u0000\u00e6\u00e7\u0005\u000f\u0000"+
		"\u0000\u00e7\u00e8\u0005\n\u0000\u0000\u00e8\u00e9\u0003.\u0017\u0000"+
		"\u00e9\u00ea\u0005\u000b\u0000\u0000\u00ea\u00eb\u0005\u0002\u0000\u0000"+
		"\u00eb\u001d\u0001\u0000\u0000\u0000\u00ec\u00f5\u0005\u0011\u0000\u0000"+
		"\u00ed\u00f2\u0003.\u0017\u0000\u00ee\u00ef\u0005\f\u0000\u0000\u00ef"+
		"\u00f1\u0003.\u0017\u0000\u00f0\u00ee\u0001\u0000\u0000\u0000\u00f1\u00f4"+
		"\u0001\u0000\u0000\u0000\u00f2\u00f0\u0001\u0000\u0000\u0000\u00f2\u00f3"+
		"\u0001\u0000\u0000\u0000\u00f3\u00f6\u0001\u0000\u0000\u0000\u00f4\u00f2"+
		"\u0001\u0000\u0000\u0000\u00f5\u00ed\u0001\u0000\u0000\u0000\u00f5\u00f6"+
		"\u0001\u0000\u0000\u0000\u00f6\u00f7\u0001\u0000\u0000\u0000\u00f7\u00f8"+
		"\u0005\u0012\u0000\u0000\u00f8\u001f\u0001\u0000\u0000\u0000\u00f9\u00fa"+
		"\u0005\u0013\u0000\u0000\u00fa\u00fb\u00050\u0000\u0000\u00fb\u00fc\u0005"+
		"\u0014\u0000\u0000\u00fc\u00fd\u0003.\u0017\u0000\u00fd\u00fe\u0005\u0002"+
		"\u0000\u0000\u00fe!\u0001\u0000\u0000\u0000\u00ff\u0100\u0005\u0013\u0000"+
		"\u0000\u0100\u0101\u00050\u0000\u0000\u0101\u0102\u0005\u0014\u0000\u0000"+
		"\u0102\u0103\u0003.\u0017\u0000\u0103#\u0001\u0000\u0000\u0000\u0104\u0105"+
		"\u0005\u0015\u0000\u0000\u0105\u0106\u00050\u0000\u0000\u0106\u0107\u0005"+
		"\u0014\u0000\u0000\u0107\u0108\u0003.\u0017\u0000\u0108\u0109\u0005\u0002"+
		"\u0000\u0000\u0109%\u0001\u0000\u0000\u0000\u010a\u010b\u0003*\u0015\u0000"+
		"\u010b\u010c\u0005\u0014\u0000\u0000\u010c\u010d\u0003.\u0017\u0000\u010d"+
		"\u010e\u0005\u0002\u0000\u0000\u010e\'\u0001\u0000\u0000\u0000\u010f\u0110"+
		"\u0003*\u0015\u0000\u0110\u0111\u0005\u0014\u0000\u0000\u0111\u0112\u0003"+
		".\u0017\u0000\u0112)\u0001\u0000\u0000\u0000\u0113\u011a\u00050\u0000"+
		"\u0000\u0114\u0115\u0005\u0011\u0000\u0000\u0115\u0116\u0003.\u0017\u0000"+
		"\u0116\u0117\u0005\u0012\u0000\u0000\u0117\u0119\u0001\u0000\u0000\u0000"+
		"\u0118\u0114\u0001\u0000\u0000\u0000\u0119\u011c\u0001\u0000\u0000\u0000"+
		"\u011a\u0118\u0001\u0000\u0000\u0000\u011a\u011b\u0001\u0000\u0000\u0000"+
		"\u011b\u0121\u0001\u0000\u0000\u0000\u011c\u011a\u0001\u0000\u0000\u0000"+
		"\u011d\u011e\u00050\u0000\u0000\u011e\u011f\u0005\u0003\u0000\u0000\u011f"+
		"\u0121\u00050\u0000\u0000\u0120\u0113\u0001\u0000\u0000\u0000\u0120\u011d"+
		"\u0001\u0000\u0000\u0000\u0121+\u0001\u0000\u0000\u0000\u0122\u0123\u0005"+
		"\u0016\u0000\u0000\u0123\u0124\u0005\n\u0000\u0000\u0124\u0129\u0003."+
		"\u0017\u0000\u0125\u0126\u0005\f\u0000\u0000\u0126\u0128\u0003.\u0017"+
		"\u0000\u0127\u0125\u0001\u0000\u0000\u0000\u0128\u012b\u0001\u0000\u0000"+
		"\u0000\u0129\u0127\u0001\u0000\u0000\u0000\u0129\u012a\u0001\u0000\u0000"+
		"\u0000\u012a\u012c\u0001\u0000\u0000\u0000\u012b\u0129\u0001\u0000\u0000"+
		"\u0000\u012c\u012d\u0005\u000b\u0000\u0000\u012d\u012e\u0005\u0002\u0000"+
		"\u0000\u012e-\u0001\u0000\u0000\u0000\u012f\u0132\u00034\u001a\u0000\u0130"+
		"\u0132\u00030\u0018\u0000\u0131\u012f\u0001\u0000\u0000\u0000\u0131\u0130"+
		"\u0001\u0000\u0000\u0000\u0132/\u0001\u0000\u0000\u0000\u0133\u0134\u0005"+
		"\u0017\u0000\u0000\u0134\u0135\u00032\u0019\u0000\u0135\u0137\u0005\n"+
		"\u0000\u0000\u0136\u0138\u0003Z-\u0000\u0137\u0136\u0001\u0000\u0000\u0000"+
		"\u0137\u0138\u0001\u0000\u0000\u0000\u0138\u0139\u0001\u0000\u0000\u0000"+
		"\u0139\u013a\u0005\u000b\u0000\u0000\u013a1\u0001\u0000\u0000\u0000\u013b"+
		"\u0140\u00050\u0000\u0000\u013c\u013d\u0005\u0003\u0000\u0000\u013d\u013f"+
		"\u00050\u0000\u0000\u013e\u013c\u0001\u0000\u0000\u0000\u013f\u0142\u0001"+
		"\u0000\u0000\u0000\u0140\u013e\u0001\u0000\u0000\u0000\u0140\u0141\u0001"+
		"\u0000\u0000\u0000\u01413\u0001\u0000\u0000\u0000\u0142\u0140\u0001\u0000"+
		"\u0000\u0000\u0143\u0148\u00036\u001b\u0000\u0144\u0145\u0005\u0018\u0000"+
		"\u0000\u0145\u0147\u00036\u001b\u0000\u0146\u0144\u0001\u0000\u0000\u0000"+
		"\u0147\u014a\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000\u0000\u0000"+
		"\u0148\u0149\u0001\u0000\u0000\u0000\u01495\u0001\u0000\u0000\u0000\u014a"+
		"\u0148\u0001\u0000\u0000\u0000\u014b\u0150\u00038\u001c\u0000\u014c\u014d"+
		"\u0005\u0019\u0000\u0000\u014d\u014f\u00038\u001c\u0000\u014e\u014c\u0001"+
		"\u0000\u0000\u0000\u014f\u0152\u0001\u0000\u0000\u0000\u0150\u014e\u0001"+
		"\u0000\u0000\u0000\u0150\u0151\u0001\u0000\u0000\u0000\u01517\u0001\u0000"+
		"\u0000\u0000\u0152\u0150\u0001\u0000\u0000\u0000\u0153\u0158\u0003:\u001d"+
		"\u0000\u0154\u0155\u0007\u0000\u0000\u0000\u0155\u0157\u0003:\u001d\u0000"+
		"\u0156\u0154\u0001\u0000\u0000\u0000\u0157\u015a\u0001\u0000\u0000\u0000"+
		"\u0158\u0156\u0001\u0000\u0000\u0000\u0158\u0159\u0001\u0000\u0000\u0000"+
		"\u01599\u0001\u0000\u0000\u0000\u015a\u0158\u0001\u0000\u0000\u0000\u015b"+
		"\u0160\u0003<\u001e\u0000\u015c\u015d\u0007\u0001\u0000\u0000\u015d\u015f"+
		"\u0003<\u001e\u0000\u015e\u015c\u0001\u0000\u0000\u0000\u015f\u0162\u0001"+
		"\u0000\u0000\u0000\u0160\u015e\u0001\u0000\u0000\u0000\u0160\u0161\u0001"+
		"\u0000\u0000\u0000\u0161;\u0001\u0000\u0000\u0000\u0162\u0160\u0001\u0000"+
		"\u0000\u0000\u0163\u0168\u0003>\u001f\u0000\u0164\u0165\u0007\u0002\u0000"+
		"\u0000\u0165\u0167\u0003>\u001f\u0000\u0166\u0164\u0001\u0000\u0000\u0000"+
		"\u0167\u016a\u0001\u0000\u0000\u0000\u0168\u0166\u0001\u0000\u0000\u0000"+
		"\u0168\u0169\u0001\u0000\u0000\u0000\u0169=\u0001\u0000\u0000\u0000\u016a"+
		"\u0168\u0001\u0000\u0000\u0000\u016b\u0170\u0003@ \u0000\u016c\u016d\u0007"+
		"\u0003\u0000\u0000\u016d\u016f\u0003@ \u0000\u016e\u016c\u0001\u0000\u0000"+
		"\u0000\u016f\u0172\u0001\u0000\u0000\u0000\u0170\u016e\u0001\u0000\u0000"+
		"\u0000\u0170\u0171\u0001\u0000\u0000\u0000\u0171?\u0001\u0000\u0000\u0000"+
		"\u0172\u0170\u0001\u0000\u0000\u0000\u0173\u0174\u0007\u0004\u0000\u0000"+
		"\u0174\u0177\u0003@ \u0000\u0175\u0177\u0003R)\u0000\u0176\u0173\u0001"+
		"\u0000\u0000\u0000\u0176\u0175\u0001\u0000\u0000\u0000\u0177A\u0001\u0000"+
		"\u0000\u0000\u0178\u0179\u0005%\u0000\u0000\u0179\u017a\u0005\n\u0000"+
		"\u0000\u017a\u017b\u0003.\u0017\u0000\u017b\u017c\u0005\u000b\u0000\u0000"+
		"\u017c\u0180\u0003P(\u0000\u017d\u017f\u0003D\"\u0000\u017e\u017d\u0001"+
		"\u0000\u0000\u0000\u017f\u0182\u0001\u0000\u0000\u0000\u0180\u017e\u0001"+
		"\u0000\u0000\u0000\u0180\u0181\u0001\u0000\u0000\u0000\u0181\u0184\u0001"+
		"\u0000\u0000\u0000\u0182\u0180\u0001\u0000\u0000\u0000\u0183\u0185\u0003"+
		"F#\u0000\u0184\u0183\u0001\u0000\u0000\u0000\u0184\u0185\u0001\u0000\u0000"+
		"\u0000\u0185C\u0001\u0000\u0000\u0000\u0186\u0187\u0005&\u0000\u0000\u0187"+
		"\u0188\u0005%\u0000\u0000\u0188\u0189\u0005\n\u0000\u0000\u0189\u018a"+
		"\u0003.\u0017\u0000\u018a\u018b\u0005\u000b\u0000\u0000\u018b\u018c\u0003"+
		"P(\u0000\u018cE\u0001\u0000\u0000\u0000\u018d\u018e\u0005&\u0000\u0000"+
		"\u018e\u018f\u0003P(\u0000\u018fG\u0001\u0000\u0000\u0000\u0190\u0191"+
		"\u0005\'\u0000\u0000\u0191\u0192\u00050\u0000\u0000\u0192\u0196\u0005"+
		"\u0006\u0000\u0000\u0193\u0195\u0003J%\u0000\u0194\u0193\u0001\u0000\u0000"+
		"\u0000\u0195\u0198\u0001\u0000\u0000\u0000\u0196\u0194\u0001\u0000\u0000"+
		"\u0000\u0196\u0197\u0001\u0000\u0000\u0000\u0197\u0199\u0001\u0000\u0000"+
		"\u0000\u0198\u0196\u0001\u0000\u0000\u0000\u0199\u019a\u0005\u0007\u0000"+
		"\u0000\u019aI\u0001\u0000\u0000\u0000\u019b\u019c\u00050\u0000\u0000\u019c"+
		"\u019d\u0005\u0002\u0000\u0000\u019dK\u0001\u0000\u0000\u0000\u019e\u019f"+
		"\u0005(\u0000\u0000\u019f\u01a0\u00050\u0000\u0000\u01a0\u01a1\u0005\u0006"+
		"\u0000\u0000\u01a1\u01a6\u0003N\'\u0000\u01a2\u01a3\u0005\f\u0000\u0000"+
		"\u01a3\u01a5\u0003N\'\u0000\u01a4\u01a2\u0001\u0000\u0000\u0000\u01a5"+
		"\u01a8\u0001\u0000\u0000\u0000\u01a6\u01a4\u0001\u0000\u0000\u0000\u01a6"+
		"\u01a7\u0001\u0000\u0000\u0000\u01a7\u01a9\u0001\u0000\u0000\u0000\u01a8"+
		"\u01a6\u0001\u0000\u0000\u0000\u01a9\u01aa\u0005\u0007\u0000\u0000\u01aa"+
		"M\u0001\u0000\u0000\u0000\u01ab\u01ae\u00050\u0000\u0000\u01ac\u01ad\u0005"+
		"\u0014\u0000\u0000\u01ad\u01af\u0003.\u0017\u0000\u01ae\u01ac\u0001\u0000"+
		"\u0000\u0000\u01ae\u01af\u0001\u0000\u0000\u0000\u01afO\u0001\u0000\u0000"+
		"\u0000\u01b0\u01b4\u0005\u0006\u0000\u0000\u01b1\u01b3\u0003\u0002\u0001"+
		"\u0000\u01b2\u01b1\u0001\u0000\u0000\u0000\u01b3\u01b6\u0001\u0000\u0000"+
		"\u0000\u01b4\u01b2\u0001\u0000\u0000\u0000\u01b4\u01b5\u0001\u0000\u0000"+
		"\u0000\u01b5\u01b7\u0001\u0000\u0000\u0000\u01b6\u01b4\u0001\u0000\u0000"+
		"\u0000\u01b7\u01b8\u0005\u0007\u0000\u0000\u01b8Q\u0001\u0000\u0000\u0000"+
		"\u01b9\u01bd\u0003V+\u0000\u01ba\u01bc\u0003T*\u0000\u01bb\u01ba\u0001"+
		"\u0000\u0000\u0000\u01bc\u01bf\u0001\u0000\u0000\u0000\u01bd\u01bb\u0001"+
		"\u0000\u0000\u0000\u01bd\u01be\u0001\u0000\u0000\u0000\u01beS\u0001\u0000"+
		"\u0000\u0000\u01bf\u01bd\u0001\u0000\u0000\u0000\u01c0\u01c1\u0005\u0003"+
		"\u0000\u0000\u01c1\u01cc\u00050\u0000\u0000\u01c2\u01c4\u0005\n\u0000"+
		"\u0000\u01c3\u01c5\u0003Z-\u0000\u01c4\u01c3\u0001\u0000\u0000\u0000\u01c4"+
		"\u01c5\u0001\u0000\u0000\u0000\u01c5\u01c6\u0001\u0000\u0000\u0000\u01c6"+
		"\u01cc\u0005\u000b\u0000\u0000\u01c7\u01c8\u0005\u0011\u0000\u0000\u01c8"+
		"\u01c9\u0003.\u0017\u0000\u01c9\u01ca\u0005\u0012\u0000\u0000\u01ca\u01cc"+
		"\u0001\u0000\u0000\u0000\u01cb\u01c0\u0001\u0000\u0000\u0000\u01cb\u01c2"+
		"\u0001\u0000\u0000\u0000\u01cb\u01c7\u0001\u0000\u0000\u0000\u01ccU\u0001"+
		"\u0000\u0000\u0000\u01cd\u01d5\u0003d2\u0000\u01ce\u01d5\u00050\u0000"+
		"\u0000\u01cf\u01d0\u0005\n\u0000\u0000\u01d0\u01d1\u0003.\u0017\u0000"+
		"\u01d1\u01d2\u0005\u000b\u0000\u0000\u01d2\u01d5\u0001\u0000\u0000\u0000"+
		"\u01d3\u01d5\u0003X,\u0000\u01d4\u01cd\u0001\u0000\u0000\u0000\u01d4\u01ce"+
		"\u0001\u0000\u0000\u0000\u01d4\u01cf\u0001\u0000\u0000\u0000\u01d4\u01d3"+
		"\u0001\u0000\u0000\u0000\u01d5W\u0001\u0000\u0000\u0000\u01d6\u01d7\u0005"+
		"0\u0000\u0000\u01d7\u01d9\u0005\n\u0000\u0000\u01d8\u01da\u0003Z-\u0000"+
		"\u01d9\u01d8\u0001\u0000\u0000\u0000\u01d9\u01da\u0001\u0000\u0000\u0000"+
		"\u01da\u01db\u0001\u0000\u0000\u0000\u01db\u01dc\u0005\u000b\u0000\u0000"+
		"\u01dcY\u0001\u0000\u0000\u0000\u01dd\u01e2\u0003.\u0017\u0000\u01de\u01df"+
		"\u0005\f\u0000\u0000\u01df\u01e1\u0003.\u0017\u0000\u01e0\u01de\u0001"+
		"\u0000\u0000\u0000\u01e1\u01e4\u0001\u0000\u0000\u0000\u01e2\u01e0\u0001"+
		"\u0000\u0000\u0000\u01e2\u01e3\u0001\u0000\u0000\u0000\u01e3[\u0001\u0000"+
		"\u0000\u0000\u01e4\u01e2\u0001\u0000\u0000\u0000\u01e5\u01e6\u0005)\u0000"+
		"\u0000\u01e6\u01e8\u0003P(\u0000\u01e7\u01e9\u0003^/\u0000\u01e8\u01e7"+
		"\u0001\u0000\u0000\u0000\u01e9\u01ea\u0001\u0000\u0000\u0000\u01ea\u01e8"+
		"\u0001\u0000\u0000\u0000\u01ea\u01eb\u0001\u0000\u0000\u0000\u01eb\u01ed"+
		"\u0001\u0000\u0000\u0000\u01ec\u01ee\u0003`0\u0000\u01ed\u01ec\u0001\u0000"+
		"\u0000\u0000\u01ed\u01ee\u0001\u0000\u0000\u0000\u01ee]\u0001\u0000\u0000"+
		"\u0000\u01ef\u01f0\u0005*\u0000\u0000\u01f0\u01f1\u0005\n\u0000\u0000"+
		"\u01f1\u01f2\u00050\u0000\u0000\u01f2\u01f3\u0005\u000b\u0000\u0000\u01f3"+
		"\u01f4\u0003P(\u0000\u01f4_\u0001\u0000\u0000\u0000\u01f5\u01f6\u0005"+
		"+\u0000\u0000\u01f6\u01f7\u0003P(\u0000\u01f7a\u0001\u0000\u0000\u0000"+
		"\u01f8\u01f9\u0005,\u0000\u0000\u01f9\u01fa\u0003.\u0017\u0000\u01fa\u01fb"+
		"\u0005\u0002\u0000\u0000\u01fbc\u0001\u0000\u0000\u0000\u01fc\u0203\u0005"+
		"3\u0000\u0000\u01fd\u0203\u00054\u0000\u0000\u01fe\u0203\u00055\u0000"+
		"\u0000\u01ff\u0203\u0003\u001e\u000f\u0000\u0200\u0203\u00051\u0000\u0000"+
		"\u0201\u0203\u00052\u0000\u0000\u0202\u01fc\u0001\u0000\u0000\u0000\u0202"+
		"\u01fd\u0001\u0000\u0000\u0000\u0202\u01fe\u0001\u0000\u0000\u0000\u0202"+
		"\u01ff\u0001\u0000\u0000\u0000\u0202\u0200\u0001\u0000\u0000\u0000\u0202"+
		"\u0201\u0001\u0000\u0000\u0000\u0203e\u0001\u0000\u0000\u0000\u0204\u0205"+
		"\u0003.\u0017\u0000\u0205\u0206\u0005\u0002\u0000\u0000\u0206g\u0001\u0000"+
		"\u0000\u0000\u0207\u0208\u0007\u0005\u0000\u0000\u0208i\u0001\u0000\u0000"+
		"\u0000,m\u0084\u008f\u0094\u009c\u00a8\u00aa\u00b3\u00bd\u00c2\u00c9\u00cd"+
		"\u00d1\u00d8\u00f2\u00f5\u011a\u0120\u0129\u0131\u0137\u0140\u0148\u0150"+
		"\u0158\u0160\u0168\u0170\u0176\u0180\u0184\u0196\u01a6\u01ae\u01b4\u01bd"+
		"\u01c4\u01cb\u01d4\u01d9\u01e2\u01ea\u01ed\u0202";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}