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
    return StringUtil.convertNoReverse(typechecker, ext, text.chars());
  }
}
