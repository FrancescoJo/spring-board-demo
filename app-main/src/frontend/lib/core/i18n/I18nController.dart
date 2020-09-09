// spring-message-board-demo
// Refer to LICENCE.txt for licence details.
import 'dart:ui';

import 'package:frontend/generated/i18n/Messages.dart';
import 'package:frontend/util/i18n/LocaleUtils.dart';

/// Custom internalisation(I18n) controller for spring-board-demo
///
/// Author: Francesco Jo(<nimbusob@gmail.com>) <br/>
/// Since: 09 - Sep - 2020
class I18nController {
  // This is very tedious because there is no dart:mirror in Flutter...
  static Messages getDelegate() {
    final Locale locale = getCurrentLocale();

    // TODO: find a proper delegate instance for current locale statically
    print("Current locale: $locale");

    throw new Exception("Not implemented");
  }
}
