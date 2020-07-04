package org.arend.io;

import org.arend.ext.typechecking.BaseMetaDefinition;
import org.arend.ext.typechecking.ContextData;
import org.arend.ext.typechecking.ExpressionTypechecker;
import org.arend.ext.typechecking.TypedExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class StringMeta extends BaseMetaDefinition {
  private final IOExtension ext;
  public final String text;

  public StringMeta(IOExtension ext, String text) {
    this.ext = ext;
    this.text = new StringBuilder(text).reverse().toString();
  }

  @Override
  public @Nullable boolean[] argumentExplicitness() {
    return new boolean[0];
  }

  @Override
  public @Nullable TypedExpression invokeMeta(@NotNull ExpressionTypechecker typechecker, @NotNull ContextData contextData) {
    return invokeMeta(typechecker);
  }

  public @Nullable TypedExpression invokeMeta(@NotNull ExpressionTypechecker typechecker) {
    var string = Objects.requireNonNull(typechecker.typecheck(ext.factory.ref(ext.string.getRef()), null)).getExpression();
    return typechecker.typecheck(text.chars()
        .mapToObj(ext.factory::number)
        .reduce(ext.factory.ref(ext.nil.getRef()), (l, r) -> ext.factory.app(
            ext.factory.ref(ext.cons.getRef()),
            List.of(ext.factory.arg(r, true), ext.factory.arg(l, true))
        )), string);
  }
}
