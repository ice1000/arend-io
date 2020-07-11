package org.arend.io;

import org.arend.ext.typechecking.ExpressionTypechecker;
import org.arend.ext.typechecking.TypedExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public interface StringUtil {
  static @Nullable TypedExpression convert(
      @NotNull ExpressionTypechecker typechecker,
      @NotNull IOExtension ext,
      @NotNull String text
  ) {
    return convertNoReverse(typechecker, ext, new StringBuilder(text).reverse().chars());
  }

  static @Nullable TypedExpression convertNoReverse(
      @NotNull ExpressionTypechecker typechecker,
      @NotNull IOExtension ext,
      @NotNull IntStream alreadyReversedString
  ) {
    var string = Objects.requireNonNull(typechecker.typecheck(ext.factory.ref(ext.string.getRef()), null)).getExpression();
    return typechecker.typecheck(alreadyReversedString
        .mapToObj(ext.factory::number)
        .reduce(ext.factory.ref(ext.nil.getRef()), (l, r) -> ext.factory.app(
            ext.factory.ref(ext.cons.getRef()),
            List.of(ext.factory.arg(r, true), ext.factory.arg(l, true))
        )), string);
  }
}
