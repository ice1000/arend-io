package org.arend.io;

import org.arend.ext.core.expr.CoreConCallExpression;
import org.arend.ext.core.expr.CoreExpression;
import org.arend.ext.core.expr.CoreIntegerExpression;
import org.arend.ext.core.expr.CoreLamExpression;
import org.arend.ext.core.ops.NormalizationMode;
import org.arend.ext.error.GeneralError;
import org.arend.ext.typechecking.BaseMetaDefinition;
import org.arend.ext.typechecking.ContextData;
import org.arend.ext.typechecking.ExpressionTypechecker;
import org.arend.ext.typechecking.TypedExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class PerformIOMeta extends BaseMetaDefinition {
  private IOExtension ext;

  public PerformIOMeta(IOExtension ext) {
    this.ext = ext;
  }

  @Override
  public @Nullable boolean[] argumentExplicitness() {
    return new boolean[]{true};
  }

  @Override
  public @Nullable TypedExpression invokeMeta(@NotNull ExpressionTypechecker typechecker, @NotNull ContextData contextData) {
    var arg = contextData.getArguments().get(0).getExpression();
    var io = typechecker.typecheck(ext.factory.ref(ext.io.getRef()), null);
    var ioExpr = Objects.requireNonNull(io).getExpression();
    var action = typechecker.typecheck(arg, ioExpr);
    try {
      performIO(Objects.requireNonNull(action).getExpression().normalize(NormalizationMode.WHNF), typechecker, ioExpr);
    } catch (IOException e) {
      typechecker.getErrorReporter().report(new GeneralError(GeneralError.Level.ERROR, e.getLocalizedMessage()));
    }
    return typechecker.typecheck(ext.factory.number(0), null);
  }

  private void extractString(CoreConCallExpression expr, StringBuilder builder) {
    if (expr.getDefinition().equals(ext.cons)) {
      var arguments = expr.getDefCallArguments();
      var integer = (CoreIntegerExpression) arguments.get(0).normalize(NormalizationMode.NF);
      builder.appendCodePoint(integer.getBigInteger().intValue());
      extractString((CoreConCallExpression) arguments.get(1).normalize(NormalizationMode.WHNF), builder);
    }
  }

  private @NotNull String performIO(CoreExpression arg, ExpressionTypechecker typechecker, CoreExpression io) throws IOException {
    if (!(arg instanceof CoreConCallExpression)) {
      typechecker.getErrorReporter().report(new GeneralError(GeneralError.Level.ERROR, "Expected a constructor call for IO, found `" + arg + "`."));
      return "";
    }
    var con = (CoreConCallExpression) arg;
    switch (con.getDefinition().getName()) {
      case "print": {
        var sb = new StringBuilder();
        extractString((CoreConCallExpression) con.getDefCallArguments().get(0).normalize(NormalizationMode.WHNF), sb);
        typechecker.getErrorReporter().report(new GeneralError(GeneralError.Level.INFO, sb.reverse().toString()));
        break;
      }
      case "readFile": {
        var sb = new StringBuilder();
        extractString((CoreConCallExpression) con.getDefCallArguments().get(0).normalize(NormalizationMode.WHNF), sb);
        return Files.readString(Paths.get(sb.reverse().toString()));
      }
      case ">>=": {
        var lhs = (CoreConCallExpression) con.getDefCallArguments().get(0).normalize(NormalizationMode.WHNF);
        var str = performIO(lhs, typechecker, io);
        var strExpr = new StringMeta(ext, str).invokeMeta(typechecker);
        var rhs = (CoreLamExpression) con.getDefCallArguments().get(1).normalize(NormalizationMode.WHNF);
        var checked = typechecker.typecheck(ext.factory.app(ext.factory.core(rhs.computeTyped()), List.of(ext.factory.arg(ext.factory.core(Objects.requireNonNull(strExpr)), true))), io);
        assert checked != null;
        performIO(checked.getExpression().normalize(NormalizationMode.WHNF), typechecker, io);
      }
    }
    return "";
  }
}
