package org.arend.io;

import org.arend.ext.LiteralTypechecker;
import org.arend.ext.typechecking.ContextData;
import org.arend.ext.typechecking.ExpressionTypechecker;
import org.arend.ext.typechecking.TypedExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeStringTypechecker implements LiteralTypechecker {
  private final IOExtension ext;

  public FakeStringTypechecker(IOExtension ext) {
    this.ext = ext;
  }

  @Override
  public @Nullable TypedExpression typecheckString(@NotNull String unescapedString, @NotNull ExpressionTypechecker typechecker, @NotNull ContextData contextData) {
    return StringUtil.convert(typechecker, ext, unescapedString);
  }
}
