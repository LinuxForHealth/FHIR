/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.tools;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CodeBuilder {
    private static final int MAX_LINE_LENGTH = 120;
    private static final int MAX_SPACES = 10;
    private StringBuilder sb = new StringBuilder();
    private int indentLevel = 0;

    public CodeBuilder _package(String name) {
        return append("package ").append(name).append(";").newLine();
    }

    public CodeBuilder _import(String typeName) {
        return append("import ").append(typeName).append(";").newLine();
    }

    public CodeBuilder _importstatic(String typeName, String identifier) {
        return append("import static ").append(typeName).append(".").append(identifier).append(";").newLine();
    }

    private CodeBuilder indent() {
        for (int i = 0; i < indentLevel; i++) {
            sb.append("    ");
        }
        return this;
    }

    private CodeBuilder prepend(String str) {
        sb.insert(0, str);
        return this;
    }

    public CodeBuilder prepend(CodeBuilder other) {
        return prepend(other.toString());
    }

    public CodeBuilder append(CodeBuilder other) {
        return append(other.toString());
    }

    public CodeBuilder lines(List<String> lines) {
        for (String line : lines) {
            append(line).newLine();
        }
        return this;
    }

    private CodeBuilder join(String delimiter, List<String> elements) {
        return append(String.join(delimiter, elements));
    }

    private CodeBuilder incIndentLevel() {
        indentLevel++;
        return this;
    }

    private CodeBuilder decIndentLevel() {
        indentLevel--;
        return this;
    }

    private CodeBuilder format(String format, Object... args) {
        return append(String.format(format, args));
    }

    private CodeBuilder append(String str) {
        sb.append(str);
        return this;
    }

    public static String param(String typeName, String identifier) {
        return String.format("%s %s", typeName, identifier);
    }

    public static List<String> params(String... params) {
        return Arrays.asList(params);
    }

    public static List<String> args(String... args) {
        return Arrays.asList(args);
    }

    public static List<String> mods(String... mods) {
        return Arrays.asList(mods);
    }

    public static String quote(String str) {
        return String.format("\"%s\"", str/* .replace("\"", "\\\"") */);
    }

    public CodeBuilder newLine() {
        return append(System.lineSeparator());
    }

    public CodeBuilder javadoc(String... lines) {
        return javadoc(Arrays.asList(lines));
    }

    public static List<String> _throws(String... exceptions) {
        return Arrays.asList(exceptions);
    }

    public static List<String> _implements(String... interfaces) {
        return Arrays.asList(interfaces);
    }

    public CodeBuilder javadocStart() {
        return indent().append("/**").newLine();
    }

    public CodeBuilder javadoc(String line) {
        return javadoc(line, true);
    }

    public CodeBuilder javadoc(String line, boolean escape) {
        return indent().append(" * ").append(escape ? escape(line) : line).newLine();
    }

    public CodeBuilder javadocParam(String name, String description) {
        indent().append(" * @param").append(" ").append(name).newLine();
        for (String line : wrap(escape(normalizeSpace(description)))) {
            indent().append(" *     ").append(line).newLine();
        }
        return this;
    }

    public CodeBuilder javadocReturn(String description) {
        indent().append(" * @return").newLine();
        for (String line : wrap(escape(normalizeSpace(description)))) {
            indent().append(" *     ").append(line).newLine();
        }
        return this;
    }

    public CodeBuilder javadocThrows(String exception, String description) {
        indent().append(" * @throws").append(" ").append(exception).newLine();
        for (String line : wrap(escape(normalizeSpace(description)))) {
            indent().append(" *     ").append(line).newLine();
        }
        return this;
    }

    public CodeBuilder javadocSee(String reference) {
        return indent()
                .append(" * @see")
                .append(" ")
                .append(reference).newLine();
    }

    public static String javadocLink(String link) {
        return String.format("{@link %s}", link);
    }

    public CodeBuilder javadocEnd() {
        return indent().append(" */").newLine();
    }

    public CodeBuilder javadoc(List<String> lines) {
        return javadoc(lines, true, true, true);
    }

    public CodeBuilder javadoc(List<String> lines, boolean start, boolean end, boolean includeParagraphTag) {
        boolean p = false;
        if (!lines.isEmpty()) {
            if (start) {
                javadocStart();
            }
            for (String line : lines) {
                if (line.isEmpty()) {
                    if (lines.indexOf(line) != lines.size() - 1) {
                        // there are more lines
                        javadoc("");
                        if (includeParagraphTag) {
                            p = true;
                        }
                    }
                } else {
                    for (String l : wrap((p ? "<p>" : "") + escape(normalizeSpace(line)))) {
                        javadoc(l, false);
                    }
                }
            }
            if (end) {
                javadocEnd();
            }
        }
        return this;
    }

    private String normalizeSpace(String line) {
        for (int i = 0; i < MAX_SPACES / 2; i++) {
            line = line.replace("  ", " ");
        }
        return line;
    }

    public static String _this(String ref) {
        return String.format("this.%s", ref);
    }

    public CodeBuilder comment(String comment) {
        return indent().append("// ").append(comment).newLine();
    }

    public CodeBuilder field(List<String> modifiers, String type, String identifier) {
        return field(modifiers, type, identifier, null);
    }

    public CodeBuilder field(List<String> modifiers, String type, String identifier, String init) {
        indent();
        if (!modifiers.isEmpty()) {
            join(" ", modifiers).append(" ");
        }
        append(type).append(" ").append(identifier);
        if (init != null) {
            append(" = ").append(init);
        }
        append(";").newLine();
        return this;
    }

    public CodeBuilder _class(List<String> modifiers, String identifier) {
        return _class(modifiers, identifier, null, Collections.emptyList());
    }

    public CodeBuilder _class(List<String> modifiers, String identifier, String _super) {
        return _class(modifiers, identifier, _super, Collections.emptyList());
    }

    public CodeBuilder _class(List<String> modifiers, String identifier, String _super, List<String> implementsInterfaces) {
        indent();
        if (!modifiers.isEmpty()) {
            join(" ", modifiers).append(" ");
        }
        append("class ").append(identifier).append(" ");
        if (_super != null) {
            append("extends ").append(_super).append(" ");
        }
        if (!implementsInterfaces.isEmpty()) {
            append("implements ").join(", ", implementsInterfaces).append(" ");
        }
        append("{").newLine().incIndentLevel();
        return this;
    }

    public CodeBuilder end() {
        return decIndentLevel()
                .indent()
                .append("}")
                .newLine();
    }

    public CodeBuilder _end() {
        return end();
    }

    public CodeBuilder constructor(List<String> modifiers, String simpleTypeName) {
        return constructor(modifiers, simpleTypeName, Collections.emptyList(), Collections.emptyList());
    }

    public CodeBuilder constructor(List<String> modifiers, String simpleTypeName, List<String> parameters) {
        return constructor(modifiers, simpleTypeName, parameters, Collections.emptyList());
    }

    public CodeBuilder constructor(List<String> modifiers, String simpleTypeName, List<String> parameters, List<String> throwsExceptions) {
        indent();
        if (!modifiers.isEmpty()) {
            join(" ", modifiers).append(" ");
        }
        append(simpleTypeName).append("(");
        if (!parameters.isEmpty()) {
            join(", ", parameters);
        }
        append(") ");
        if (!throwsExceptions.isEmpty()) {
            append("throws ").join(", ", throwsExceptions).append(" ");
        }
        append("{").newLine().incIndentLevel();
        return this;
    }

    public CodeBuilder method(List<String> modifiers, String result, String identifier) {
        return method(modifiers, result, identifier, Collections.emptyList(), Collections.emptyList());
    }

    public CodeBuilder method(List<String> modifiers, String result, String identifier, List<String> parameters) {
        return method(modifiers, result, identifier, parameters, Collections.emptyList());
    }

    public static String eq(String lhs, String rhs) {
        return String.format("%s == %s", lhs, rhs);
    }

    public CodeBuilder method(List<String> modifiers, String result, String identifier, List<String> parameters, List<String> throwsExceptions) {
        indent();
        if (!modifiers.isEmpty()) {
            join(" ", modifiers).append(" ");
        }
        append(result).append(" ").append(identifier).append("(");
        if (!parameters.isEmpty()) {
            join(", ", parameters);
        }
        append(") ");
        if (!throwsExceptions.isEmpty()) {
            append("throws ").join(", ", throwsExceptions).append(" ");
        }
        append("{").newLine().incIndentLevel();
        return this;
    }

    public CodeBuilder abstractMethod(List<String> modifiers, String result, String identifier) {
        return abstractMethod(modifiers, result, identifier, Collections.emptyList(), Collections.emptyList());
    }

    public CodeBuilder abstractMethod(List<String> modifiers, String result, String identifier, List<String> parameters) {
        return abstractMethod(modifiers, result, identifier, parameters, Collections.emptyList());
    }

    public CodeBuilder abstractMethod(List<String> modifiers, String result, String identifier, List<String> parameters, List<String> throwsExceptions) {
        indent();
        if (!modifiers.isEmpty()) {
            join(" ", modifiers).append(" ");
        }
        append(result).append(" ").append(identifier).append("(");
        if (!parameters.isEmpty()) {
            join(", ", parameters);
        }
        append(")");
        if (!throwsExceptions.isEmpty()) {
            append(" throws ").join(", ", throwsExceptions);
        }
        append(";").newLine();
        return this;
    }

    public CodeBuilder _super() {
        return _super(Collections.emptyList());
    }

    public CodeBuilder _super(List<String> arguments) {
        return invoke("super", arguments);
    }

    public CodeBuilder invoke(String methodName, List<String> arguments) {
        return statement("%s(%s)", methodName, String.join(", ", arguments));
    }

    public CodeBuilder invoke(String ref, String methodName, List<String> arguments) {
        return statement("%s.%s(%s)", ref, methodName, String.join(", ", arguments));
    }

    public CodeBuilder decl(List<String> modifiers, String typeName, String identifier) {
        String mods = "";
        if (!modifiers.isEmpty()) {
            mods = String.join(" ", modifiers);
        }
        return statement("%s %s %s", mods, typeName, identifier);
    }

    public CodeBuilder decl(List<String> modifiers, String typeName, String identifier, String init) {
        String mods = "";
        if (!modifiers.isEmpty()) {
            mods = String.join(" ", modifiers);
        }
        return statement("%s %s %s = %s", mods, typeName, identifier, init);
    }

    public CodeBuilder decl(String typeName, String identifier) {
        return statement("%s %s", typeName, identifier);
    }

    public CodeBuilder decl(String typeName, String identifier, String init) {
        return statement("%s %s = %s", typeName, identifier, init);
    }

    public CodeBuilder assign(String lhs, String rhs) {
        return statement("%s = %s", lhs, rhs);
    }

    public CodeBuilder _throw(String expr) {
        return statement("throw %s", expr);
    }

    public static String _new(String type) {
        return _new(type, Collections.emptyList());
    }

    public static String _new(String type, List<String> arguments) {
        return String.format("new %s(%s)", type, String.join(", ", arguments));
    }

    public CodeBuilder _return() {
        return statement("return");
    }

    public CodeBuilder _return(String expr) {
        return statement("return %s", expr);
    }

    public CodeBuilder _break() {
        return statement("break");
    }

    public CodeBuilder _continue() {
        return statement("continue");
    }

    public CodeBuilder _for(String init, String test, String update) {
        return indent()
                .format("for (%s; %s; %s) {", init, test, update)
                .newLine()
                .incIndentLevel();
    }

    public static String var(String type, String name) {
        return String.format("%s %s", type, name);
    }

    public CodeBuilder _foreach(String var, String collection) {
        return indent()
                .format("for (%s : %s) {", var, collection)
                .newLine()
                .incIndentLevel();
    }

    public CodeBuilder _switch(String expr) {
        return indent()
                .format("switch (%s) {", expr)
                .newLine()
                .incIndentLevel();
    }

    public CodeBuilder _case(String constExpr) {
        return decIndentLevel().indent()
                .format("case %s:", constExpr).newLine()
                .incIndentLevel();
    }

    public CodeBuilder _default() {
        return decIndentLevel().indent()
                .append("default:").newLine()
                .incIndentLevel();
    }

    public CodeBuilder _while(String condition) {
        return _while(condition, false);
    }

    public CodeBuilder _try() {
        return indent().append("try {").newLine().incIndentLevel();
    }

    public CodeBuilder _try(String resourceSpec) {
        return indent().format("try (%s) {", resourceSpec).newLine().incIndentLevel();
    }

    public CodeBuilder _catch(String expr) {
        return decIndentLevel().indent().format("} catch (%s) {", expr).newLine().incIndentLevel();
    }

    public CodeBuilder _finally() {
        return decIndentLevel().indent().append("} finally {").newLine().incIndentLevel();
    }

    public CodeBuilder _while(String condition, boolean doWhile) {
        if (doWhile) {
            return decIndentLevel()
                    .indent()
                    .format("} while (%s)", condition)
                    .newLine();
        }
        return indent()
                .format("while (%s) {", condition)
                .newLine()
                .incIndentLevel();
    }

    public CodeBuilder _do() {
        return indent()
                .append("do {")
                .newLine()
                .incIndentLevel();
    }

    public CodeBuilder statement(String statement, Object... args) {
        return indent().format(statement, args).append(";").newLine();
    }

    public CodeBuilder _if(String expression) {
        return indent().format("if (%s) {", expression).newLine().incIndentLevel();
    }

    public CodeBuilder _else() {
        return decIndentLevel().indent().append("} else {").newLine().incIndentLevel();
    }

    public CodeBuilder _elseif(String expression) {
        return decIndentLevel().indent().format("} else if (%s) {", expression).newLine().incIndentLevel();
    }

    public CodeBuilder _enum(List<String> modifiers, String identifier) {
        return _enum(modifiers, identifier, Collections.emptyList());
    }

    public CodeBuilder _enum(List<String> modifiers, String identifier, List<String> implementsInterfaces) {
        indent();
        if (!modifiers.isEmpty()) {
            join(" ", modifiers).append(" ");
        }
        append("enum ").append(identifier).append(" ");
        if (!implementsInterfaces.isEmpty()) {
            append("implements ").join(", ", implementsInterfaces).append(" ");
        }
        append("{").newLine().incIndentLevel();
        return this;
    }

    public CodeBuilder _interface(List<String> modifiers, String identifier) {
        return _interface(modifiers, identifier, Collections.emptyList());
    }

    public CodeBuilder _interface(List<String> modifiers, String identifier, List<String> extendsInterfaces) {
        indent();
        if (!modifiers.isEmpty()) {
            join(" ", modifiers).append(" ");
        }
        append("interface ").append(identifier).append(" ");
        if (!extendsInterfaces.isEmpty()) {
            append("implements ").join(", ", extendsInterfaces).append(" ");
        }
        append("{").newLine().incIndentLevel();
        return this;
    }

    public CodeBuilder enumConstant(String identifier, List<String> args, boolean last) {
        indent();
        append(identifier);
        if (!args.isEmpty()) {
            append("(").join(", ", args).append(")");
        }
        append(last ? ";" : ",").newLine();
        return this;
    }

    public static List<String> implementsInterfaces(String... interfaces) {
        return Arrays.asList(interfaces);
    }

    public static List<String> throwsExceptions(String... exceptions) {
        return Arrays.asList(exceptions);
    }

    public CodeBuilder annotation(String name) {
        return annotation(name, (String) null);
    }

    public CodeBuilder override() {
        return annotation("Override");
    }

    public CodeBuilder annotation(String name, String value) {
        indent().append("@").append(name);
        if (value != null) {
            append("(").append(value).append(")");
        }
        newLine();
        return this;
    }

    public CodeBuilder annotation(String name, Map<String, String> valueMap) {
        indent().append("@").append(name).append("(").newLine();
        List<String> keys = new ArrayList<>(valueMap.keySet());
        for (String key : keys) {
            String value = valueMap.get(key);
            indent().append("    ").append(key).append(" = ").append(value).append(!isLast(keys, key) ? "," : "").newLine();
        }
        indent().append(")").newLine();
        return this;
    }

    private boolean isLast(List<?> list, Object object) {
        return list.indexOf(object) == list.size() - 1;
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    private String escape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '&') {
                sb.append("&amp;");
            } else if (ch == '<') {
                sb.append("&lt;");
            } else if (ch == '>') {
                sb.append("&gt;");
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private List<String> wrap(String text) {
        List<String> lines = new ArrayList<>();
        BreakIterator boundary = BreakIterator.getLineInstance();
        boundary.setText(text);

        int start = boundary.first();
        int end = boundary.next();
        int lineLength = 0;

        StringBuilder sb = new StringBuilder();
        while (end != BreakIterator.DONE) {
            String word = text.substring(start, end);
            lineLength = lineLength + word.length();
            if (lineLength >= MAX_LINE_LENGTH) {
                lines.add(sb.toString());
                sb.setLength(0);
                lineLength = word.length();
            }
            sb.append(word);
            start = end;
            end = boundary.next();
        }

        if (sb.length() > 0) {
            lines.add(sb.toString());
        }

        return lines;
    }

    public static void main(String[] args) {
        CodeBuilder cb = new CodeBuilder();
        cb
        ._class(mods("public"), "MyClass")
            .method(mods("public", "static"), "void", "main", params("String[] args"), _throws("Exception"))
                .invoke("System.out", "println", args(quote("Hello, World!")))
                ._return()
            .end()
        ._end();
        System.out.println(cb);
    }
}
